package com.example.Balance.Management.service;

import com.example.Balance.Management.dto.request.AuthenticationRequest;
import com.example.Balance.Management.dto.request.ChangePasswordRequest;
import com.example.Balance.Management.dto.request.RegistrationRequest;
import com.example.Balance.Management.dto.response.AuthenticationResponse;
import com.example.Balance.Management.email.EmailService;
import com.example.Balance.Management.email.EmailTemplateName;
import com.example.Balance.Management.entity.TokenEntity;
import com.example.Balance.Management.entity.UserEntity;
import com.example.Balance.Management.exception.CustomException;
import com.example.Balance.Management.repository.AuthorityRepository;
import com.example.Balance.Management.repository.TokenRepository;
import com.example.Balance.Management.repository.UserRepository;
import com.example.Balance.Management.security.JwtService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import static com.example.Balance.Management.config.ConstantsConfig.*;
import static com.example.Balance.Management.utils.UtilMethods.throwIf;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthorityRepository authorityRepository;

    @Value("${application.mailing.frontend.reset-password-url}")
    private  String resetPasswordUrl;

    @Value("${application.mailing.frontend.activation-url}")
    private  String activateAccountUrl;

    @Value("${application.mailing.frontend.resendActivation-url}")
    private String resendActivation;

    public void register(RegistrationRequest request) throws MessagingException {

        throwIf(() -> checkByEmail(request.getEmail()),
                new CustomException(null, "This email is already in use", EMAIL_ALREADY_REGISTERED, 400));

        throwIf(() -> checkByUsername(request.getUsername()),
                new CustomException(null, "This username is already in use", USERNAME_ALREADY_REGISTERED, 400));

        UserEntity user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
        log.info("User registered successfully");
    }

    @Transactional
    public void activateAccount(String email,String token) throws MessagingException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(null, "User not found with this email: " + email, USER_NOT_FOUND, 404));
        TokenEntity savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new CustomException(null, "Invalid token", INVALID_TOKEN, 400));
        if (LocalDateTime.now().isAfter(savedToken.getExpireAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new CustomException(null, "Activation token has expired. A new token has been sent to the same email address", TOKEN_EXPIRED, 400);
        }
        authorityRepository.addUserAuthorities(user.getUsername());
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidateAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
        log.info("User activated successfully");
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = (User) auth.getPrincipal();
        UserEntity userEntity = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(null, "User not found", USER_NOT_FOUND, 404));
        if (!userEntity.isEnabled()) {
            throw new CustomException("User is not activated");
        }

        var claims = new HashMap<String, Object>();
        claims.put("fullName", userEntity.getFullName());

        var jwtToken = jwtService.generateToken(claims, user);
        log.info("User authenticated successfully");
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void changePassword(String currentPassword, String newPassword) {
        UserEntity user=userRepository.findByEmail(getCurrentUser().getEmail())
                .orElseThrow(() -> new CustomException(null, "User not found", USER_NOT_FOUND, 404));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new CustomException("Current password is incorrect");
        } else if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new CustomException("New password cannot be the same as the current password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password changed successfully");
    }

    @Transactional
    public void forgotPassword(String email) throws MessagingException {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(null, "User not found with email: " + email, USER_NOT_FOUND, 404));

        String token = generateAndSaveActivationToken(email);
        String resetUrl = String.format(resetPasswordUrl, email, token);

        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                resetUrl,
                null,
                "Password reset"
        );
        log.info("Password reset email sent successfully");
    }

    @Transactional
    public void resetPassword(String email,String token, String newPassword) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(null, "User not found with email: " + email, USER_NOT_FOUND, 404));
        TokenEntity tokenEntity = tokenRepository.findByToken(token)
                .orElseThrow(() -> new CustomException(null, "Invalid token", INVALID_TOKEN, 400));
        if (!tokenEntity.getUser().getEmail().equals(email)) {
            throw new CustomException(null, "Invalid token", INVALID_TOKEN, 400);
        }
        if (LocalDateTime.now().isAfter(tokenEntity.getExpireAt())) {
            throw new CustomException(null, "Token has expired", TOKEN_EXPIRED, 400);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenEntity.setValidateAt(LocalDateTime.now());
        tokenRepository.delete(tokenEntity);
        log.info("Password reset successfully");
    }

    private void sendValidationEmail(UserEntity user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        String activationUrl = String.format(activateAccountUrl, user.getEmail(), newToken);
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
        log.info("Activation email sent successfully");
    }

    private String generateActivationCode() {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public boolean checkByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean checkByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public UserEntity getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new CustomException(null, "User not found", USER_NOT_FOUND, 404));
    }

    public void resendActivation(String email) throws MessagingException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(null, "User not found with email: " + email, USER_NOT_FOUND, 404));
        if (user.isEnabled()) {
            throw new CustomException("User is already activated");
        }
        String token = generateAndSaveActivationToken(user);
        String activationUrl = resendActivation + user.getEmail() + "&token=" + token;
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                token,
                "Account activation"
        );

        log.info("Activation email sent successfully");
    }

    private String generateAndSaveActivationToken(UserEntity user) {
        String generatedToken = generateActivationCode();
        var token = TokenEntity.builder()
                .token(generatedToken)
                .createAt(LocalDateTime.now())
                .expireAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateAndSaveActivationToken(String email) {
        String generatedToken = generateActivationCode();
        var token = TokenEntity.builder()
                .token(generatedToken)
                .createAt(LocalDateTime.now())
                .expireAt(LocalDateTime.now().plusMinutes(15))
                .user(userRepository.findByEmail(email).orElseThrow(() -> new CustomException(null, "User not found", USER_NOT_FOUND, 404)))
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

}