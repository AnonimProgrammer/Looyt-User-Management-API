package com.looyt.usermanagementservice.controller;

import com.looyt.usermanagementservice.dto.request.UpdateRoleRequest;
import com.looyt.usermanagementservice.dto.request.UpdateStatusRequest;
import com.looyt.usermanagementservice.dto.request.UserFilterRequest;
import com.looyt.usermanagementservice.dto.request.UserRequest;
import com.looyt.usermanagementservice.dto.response.UserResponse;
import com.looyt.usermanagementservice.model.enums.Role;
import com.looyt.usermanagementservice.model.enums.Status;
import com.looyt.usermanagementservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
        UserResponse user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Role role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UserFilterRequest filter = UserFilterRequest.builder()
                .search(search)
                .status(status)
                .role(role)
                .page(page)
                .size(size)
                .build();

        Page<UserResponse> users = userService.getUsers(filter);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id, @RequestBody @Valid UserRequest request
    ) {
        UserResponse updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateRole(
            @PathVariable UUID id, @RequestBody @Valid UpdateRoleRequest request
    ) {
        UserResponse updatedUser = userService.updateUserRole(id, request.role());
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponse> updateStatus(
            @PathVariable UUID id, @RequestBody @Valid UpdateStatusRequest request
    ) {
        UserResponse updatedUser = userService.updateUserStatus(id, request.status());
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
