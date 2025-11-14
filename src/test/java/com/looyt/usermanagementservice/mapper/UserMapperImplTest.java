package com.looyt.usermanagementservice.mapper;

import com.looyt.usermanagementservice.dto.request.UserRequest;
import com.looyt.usermanagementservice.dto.response.UserResponse;
import com.looyt.usermanagementservice.model.entity.UserEntity;
import com.looyt.usermanagementservice.model.enums.Role;
import com.looyt.usermanagementservice.model.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperImplTest {

    private UserMapper userMapper;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String NAME = "Omar Ismayilov";
    private static final String EMAIL = "omar.ismayilov@icloud.com";
    private static final String PHONE = "+994555555555";
    private static final Role ROLE = Role.USER;
    private static final Status STATUS = Status.ACTIVE;
    private static final LocalDateTime CREATED_AT = LocalDateTime.now().minusDays(1);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.now();

    private UserRequest userRequest;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();

        userRequest = new UserRequest(NAME, EMAIL, PHONE);

        userEntity = new UserEntity();
        userEntity.setId(USER_ID);
        userEntity.setName(NAME);
        userEntity.setEmail(EMAIL);
        userEntity.setPhoneNumber(PHONE);
        userEntity.setRole(ROLE);
        userEntity.setStatus(STATUS);
        userEntity.setCreatedAt(CREATED_AT);
        userEntity.setUpdatedAt(UPDATED_AT);
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    void mapToEntity_shouldMapAllFields() {
        UserEntity result = userMapper.mapToEntity(userRequest);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(NAME);
        assertThat(result.getEmail()).isEqualTo(EMAIL);
        assertThat(result.getPhoneNumber()).isEqualTo(PHONE);
    }

    @Test
    void mapToEntity_shouldReturnNull_whenRequestIsNull() {
        UserEntity result = userMapper.mapToEntity(null);
        assertThat(result).isNull();
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    void mapToDto_shouldMapAllFields() {
        UserResponse result = userMapper.mapToDto(userEntity);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(USER_ID);
        assertThat(result.name()).isEqualTo(NAME);
        assertThat(result.email()).isEqualTo(EMAIL);
        assertThat(result.phoneNumber()).isEqualTo(PHONE);
        assertThat(result.role()).isEqualTo(ROLE);
        assertThat(result.status()).isEqualTo(STATUS);
        assertThat(result.createdAt()).isEqualTo(CREATED_AT);
        assertThat(result.updatedAt()).isEqualTo(UPDATED_AT);
    }

    @Test
    void mapToDto_shouldReturnNull_whenEntityIsNull() {
        UserResponse result = userMapper.mapToDto(null);
        assertThat(result).isNull();
    }

}
