package com.looyt.usermanagementservice.service;

import com.looyt.usermanagementservice.dto.request.UserFilterRequest;
import com.looyt.usermanagementservice.dto.request.UserRequest;
import com.looyt.usermanagementservice.dto.response.UserResponse;
import com.looyt.usermanagementservice.exception.DuplicateFieldException;
import com.looyt.usermanagementservice.exception.UserNotFoundException;
import com.looyt.usermanagementservice.mapper.UserMapper;
import com.looyt.usermanagementservice.model.entity.UserEntity;
import com.looyt.usermanagementservice.model.enums.Role;
import com.looyt.usermanagementservice.model.enums.Status;
import com.looyt.usermanagementservice.repository.UserRepository;
import com.looyt.usermanagementservice.repository.specification.UserSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // CREATE USER
    @Override
    @Transactional
    public UserResponse createUser(UserRequest request) {
        log.info("Creating user with email: {}", request.email());
        checkUniqueness(request);

        UserEntity userEntity = userMapper.mapToEntity(request);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        log.info("User created successfully. Id: {}", savedUserEntity.getId());
        return userMapper.mapToDto(savedUserEntity);
    }

    // GET USER BY ID
    @Override
    public UserResponse getUserById(UUID id) {
        log.info("Retrieving user with id: {}", id);

        UserEntity userEntity = getUserOrThrow(id);
        return userMapper.mapToDto(userEntity);
    }

    // GET USERS WITH PAGINATION AND FILTERING
    @Override
    public Page<UserResponse> getUsers(UserFilterRequest filter) {
        log.info("Retrieving users. Search: {}, status: {}, role: {}",
                filter.search(), filter.status(), filter.role());

        PageRequest pageable = PageRequest.of(filter.page(), filter.size());

        Specification<UserEntity> specification = Specification.<UserEntity>unrestricted()
                .and(UserSpecification.hasStatus(filter.status()))
                .and(UserSpecification.hasRole(filter.role()))
                .and(UserSpecification.containsSearch(filter.search()));

        Page<UserEntity> usersPage = userRepository.findAll(specification, pageable);

        return usersPage.map(userMapper::mapToDto);
    }

    // UPDATE USER DATA
    @Override
    @Transactional
    public UserResponse updateUser(UUID id, UserRequest request) {
        log.info("Updating user with id: {}", id);

        UserEntity existingUserEntity = getUserOrThrow(id);
        checkSimilarity(existingUserEntity, request);

        existingUserEntity.setName(request.name());
        existingUserEntity.setEmail(request.email());
        existingUserEntity.setPhoneNumber(request.phoneNumber());

        UserEntity updatedUserEntity = userRepository.save(existingUserEntity);

        log.info("User with id: {} updated successfully.", id);
        return userMapper.mapToDto(updatedUserEntity);
    }

    private void checkUniqueness(UserRequest request) {
        if(userRepository.existsByEmailOrPhoneNumber(request.email(), request.phoneNumber())) {
            throw new DuplicateFieldException("Email or phone number already in use.");
        }
    }

    private void checkSimilarity(UserEntity entity, UserRequest request) {
        if(!entity.getEmail().equals(request.email())
                || !entity.getPhoneNumber().equals(request.phoneNumber())) {
            checkUniqueness(request);
        }
    }

    // UPDATE USER STATUS
    @Override
    @Transactional
    public UserResponse updateUserStatus(UUID id, Status status) {
        log.info("Updating status of user {} to {}", id, status);
        UserEntity userEntity = getUserOrThrow(id);

        userEntity.setStatus(status);
        UserEntity updatedUserEntity = userRepository.save(userEntity);

        log.info("Status of user with id: {} updated successfully.", id);
        return userMapper.mapToDto(updatedUserEntity);
    }

    // UPDATE USER ROLE
    @Override
    @Transactional
    public UserResponse updateUserRole(UUID id, Role role) {
        log.info("Updating role of user {} to {}", id, role);
        UserEntity userEntity = getUserOrThrow(id);

        userEntity.setRole(role);
        UserEntity updatedUserEntity = userRepository.save(userEntity);

        log.info("Role of user with id: {} updated successfully.", id);
        return userMapper.mapToDto(updatedUserEntity);
    }

    // DELETE USER
    @Override
    @Transactional
    public void deleteUser(UUID id) {
        log.info("Deleting user with id: {}", id);

        UserEntity userEntity = getUserOrThrow(id);
        userRepository.delete(userEntity);

        log.info("User with id: {} deleted successfully.", id);
    }

    private UserEntity getUserOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

}
