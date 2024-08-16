package com.example.Balance.Management.controller;

import com.example.Balance.Management.dto.request.AuthenticationRequest;
import com.example.Balance.Management.dto.request.ChangePasswordRequest;
import com.example.Balance.Management.dto.request.RegistrationRequest;
import com.example.Balance.Management.dto.response.AuthenticationResponse;
import com.example.Balance.Management.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.example.Balance.Management.utils.UtilMethods.checkValidation;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request, BindingResult br) throws MessagingException {
        checkValidation(br);
        service.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/activate-account")
    public void confirm(@RequestParam String email, @RequestParam String token) throws MessagingException {
        service.activateAccount(email,token);
    }

   @PostMapping("/change-password")
    public ResponseEntity<?> changePassword( @RequestParam String oldPassword, @RequestParam String newPassword) {
    service.changePassword(oldPassword, newPassword);
    return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestParam String email) throws MessagingException {
        service.forgotPassword(email);
    }

    @GetMapping("/reset-password")
    public void resetPassword(@RequestParam String email ,@RequestParam String token, @RequestParam String newPassword) {
        service.resetPassword(email, token, newPassword);
    }

    @GetMapping("/resend-activation")
    public void resendActivation(@RequestParam String email) throws MessagingException {
        service.resendActivation(email);
    }

}