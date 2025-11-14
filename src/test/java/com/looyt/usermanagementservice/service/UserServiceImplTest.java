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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String NAME = "Omar Ismayilov";
    private static final String EMAIL = "omar.ismayilov@icloud.com";
    private static final String PHONE = "+994555555555";
    private static final Role ROLE = Role.USER;
    private static final Status STATUS = Status.ACTIVE;
    private static final LocalDateTime DATE = LocalDateTime.now();

    private UserRequest userRequest;
    private UserEntity userEntity;
    private UserResponse userResponse;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<UserEntity> userEntityCaptor;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest(NAME, EMAIL, PHONE);
        userEntity = new UserEntity();
        userEntity.setId(USER_ID);
        userEntity.setName(NAME);
        userEntity.setEmail(EMAIL);
        userEntity.setPhoneNumber(PHONE);
        userEntity.setRole(ROLE);
        userEntity.setStatus(STATUS);
        userEntity.setCreatedAt(DATE);
        userEntity.setUpdatedAt(DATE);

        userResponse = new UserResponse(USER_ID, NAME, EMAIL, PHONE, ROLE, STATUS, DATE, DATE);
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    void createUser_shouldCreateUser_whenUnique() {
        when(userRepository.existsByEmailOrPhoneNumber(EMAIL, PHONE)).thenReturn(false);
        when(userMapper.mapToEntity(userRequest)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.mapToDto(userEntity)).thenReturn(userResponse);

        UserResponse result = userService.createUser(userRequest);

        assertThat(result).isEqualTo(userResponse);
        verify(userRepository).save(userEntityCaptor.capture());
        assertThat(userEntityCaptor.getValue().getId()).isEqualTo(USER_ID);
        assertThat(userEntityCaptor.getValue().getEmail()).isEqualTo(EMAIL);
        assertThat(userEntityCaptor.getValue().getPhoneNumber()).isEqualTo(PHONE);
        assertThat(userEntityCaptor.getValue().getRole()).isEqualTo(ROLE);
        assertThat(userEntityCaptor.getValue().getStatus()).isEqualTo(STATUS);
        assertThat(userEntityCaptor.getValue().getCreatedAt()).isEqualTo(DATE);
        assertThat(userEntityCaptor.getValue().getUpdatedAt()).isEqualTo(DATE);
    }

    @Test
    void createUser_shouldThrowDuplicateFieldException_whenEmailOrPhoneExists() {
        when(userRepository.existsByEmailOrPhoneNumber(EMAIL, PHONE)).thenReturn(true);

        assertThrows(DuplicateFieldException.class, () -> userService.createUser(userRequest));
        verify(userRepository, never()).save(any());
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    void getUserById_shouldReturnUser_whenExists() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));
        when(userMapper.mapToDto(userEntity)).thenReturn(userResponse);

        UserResponse result = userService.getUserById(USER_ID);

        assertThat(result).isEqualTo(userResponse);
    }

    @Test
    void getUserById_shouldThrowNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(USER_ID));
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    void updateUser_shouldUpdate_whenFieldsChanged() {
        UserRequest updatedRequest = new UserRequest(
                "Murad Ismayilov",
                "murad.ismayilov@icloud.com",
                "+994556666666"
        );
        UserEntity updatedEntity = new UserEntity();
        updatedEntity.setId(USER_ID);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));
        when(userRepository.existsByEmailOrPhoneNumber(updatedRequest.email(), updatedRequest.phoneNumber()))
                .thenReturn(false);
        when(userRepository.save(any())).thenReturn(userEntity);
        when(userMapper.mapToDto(userEntity)).thenReturn(userResponse);

        UserResponse result = userService.updateUser(USER_ID, updatedRequest);

        assertThat(result).isEqualTo(userResponse);
        verify(userRepository).save(userEntityCaptor.capture());
        assertThat(userEntityCaptor.getValue().getEmail()).isEqualTo(updatedRequest.email());
        assertThat(userEntityCaptor.getValue().getPhoneNumber()).isEqualTo(updatedRequest.phoneNumber());
        assertThat(userEntityCaptor.getValue().getName()).isEqualTo(updatedRequest.name());
    }

    @Test
    void updateUser_shouldThrowDuplicateFieldException_whenFieldsConflict() {
        UserRequest updatedRequest = new UserRequest(
                "Murad Ismayilov",
                "murad.ismayilov@icloud.com",
                "+994556666666"
        );
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));
        when(userRepository.existsByEmailOrPhoneNumber(updatedRequest.email(), updatedRequest.phoneNumber()))
                .thenReturn(true);

        assertThrows(DuplicateFieldException.class, () -> userService.updateUser(USER_ID, updatedRequest));
    }

    @Test
    void updateUser_shouldNotCheckUniqueness_whenFieldsSame() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.mapToDto(userEntity)).thenReturn(userResponse);

        UserResponse result = userService.updateUser(USER_ID, userRequest);

        assertThat(result).isEqualTo(userResponse);
        verify(userRepository, never()).existsByEmailOrPhoneNumber(anyString(), anyString());
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    void updateUserStatus_shouldUpdateStatus() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.mapToDto(userEntity)).thenReturn(userResponse);

        UserResponse result = userService.updateUserStatus(USER_ID, Status.INACTIVE);

        assertThat(result).isEqualTo(userResponse);
        assertThat(userEntity.getStatus()).isEqualTo(Status.INACTIVE);
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    void updateUserRole_shouldUpdateRole() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.mapToDto(userEntity)).thenReturn(userResponse);

        UserResponse result = userService.updateUserRole(USER_ID, Role.ADMIN);

        assertThat(result).isEqualTo(userResponse);
        assertThat(userEntity.getRole()).isEqualTo(Role.ADMIN);
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    void deleteUser_shouldDeleteUser_whenExists() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));

        userService.deleteUser(USER_ID);

        verify(userRepository).delete(userEntity);
    }

    @Test
    void deleteUser_shouldThrowNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(USER_ID));
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    void getUsers_shouldReturnPaginatedResults() {
        UserFilterRequest filter = new UserFilterRequest(null, Status.ACTIVE, Role.USER, 0, 10);

        Page<UserEntity> page = new PageImpl<>(Collections.singletonList(userEntity));
        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(userMapper.mapToDto(userEntity)).thenReturn(userResponse);

        Page<UserResponse> result = userService.getUsers(filter);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst()).isEqualTo(userResponse);
    }

}
