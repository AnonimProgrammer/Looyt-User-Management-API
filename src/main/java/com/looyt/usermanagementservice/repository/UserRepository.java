package com.looyt.usermanagementservice.repository;

import com.looyt.usermanagementservice.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {

    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);

}
