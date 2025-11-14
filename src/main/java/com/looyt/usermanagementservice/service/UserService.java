package com.looyt.usermanagementservice.service;

import com.looyt.usermanagementservice.dto.request.UserFilterRequest;
import com.looyt.usermanagementservice.dto.request.UserRequest;
import com.looyt.usermanagementservice.dto.response.UserResponse;
import com.looyt.usermanagementservice.model.enums.Role;
import com.looyt.usermanagementservice.model.enums.Status;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserService {

    UserResponse createUser(UserRequest request);

    UserResponse getUserById(UUID id);

    Page<UserResponse> getUsers(UserFilterRequest filter);

    UserResponse updateUser(UUID id, UserRequest request);

    UserResponse updateUserStatus(UUID id, Status status);

    UserResponse updateUserRole(UUID id, Role role);

    void deleteUser(UUID id);

}
