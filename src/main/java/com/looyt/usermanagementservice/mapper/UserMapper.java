package com.looyt.usermanagementservice.mapper;

import com.looyt.usermanagementservice.dto.request.UserRequest;
import com.looyt.usermanagementservice.dto.response.UserResponse;
import com.looyt.usermanagementservice.model.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity mapToEntity(UserRequest request);

    UserResponse mapToDto(UserEntity userEntity);

}
