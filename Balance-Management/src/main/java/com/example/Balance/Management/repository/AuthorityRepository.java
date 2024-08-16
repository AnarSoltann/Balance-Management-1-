package com.example.Balance.Management.repository;

import com.example.Balance.Management.entity.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {
    List<AuthorityEntity> findByUsername(String username);

    Optional<AuthorityEntity> findByAuthority(String authority);

    @Query(value = "insert into authorities (username,authority) select ?1,authority from authority_list where users=1", nativeQuery = true)
    @Modifying
    public void addUserAuthorities(String username);


}