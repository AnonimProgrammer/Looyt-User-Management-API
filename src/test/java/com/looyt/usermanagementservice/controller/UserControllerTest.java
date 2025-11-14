package com.looyt.usermanagementservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.looyt.usermanagementservice.dto.request.UpdateRoleRequest;
import com.looyt.usermanagementservice.dto.request.UpdateStatusRequest;
import com.looyt.usermanagementservice.dto.request.UserRequest;
import com.looyt.usermanagementservice.dto.response.UserResponse;
import com.looyt.usermanagementservice.model.enums.Role;
import com.looyt.usermanagementservice.model.enums.Status;
import com.looyt.usermanagementservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String NAME = "Omar Ismayilov";
    private static final String EMAIL = "omar.ismayilov@icloud.com";
    private static final String PHONE = "+994555555555";
    private static final Role ROLE = Role.USER;
    private static final Status STATUS = Status.ACTIVE;
    private static final LocalDateTime CREATED_AT = LocalDateTime.now().minusDays(1);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.now();

    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest(NAME, EMAIL, PHONE);

        userResponse = new UserResponse(
                USER_ID, NAME, EMAIL, PHONE, ROLE, STATUS, CREATED_AT, UPDATED_AT
        );
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        when(userService.createUser(ArgumentMatchers.any(UserRequest.class)))
                .thenReturn(userResponse);

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(USER_ID.toString()))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.phoneNumber").value(PHONE))
                .andExpect(jsonPath("$.role").value(ROLE.toString()))
                .andExpect(jsonPath("$.status").value(STATUS.toString()))
                .andExpect(jsonPath("$.createdAt").value(CREATED_AT.toString()))
                .andExpect(jsonPath("$.updatedAt").value(UPDATED_AT.toString()));
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    void getUser_shouldReturnUser() throws Exception {
        when(userService.getUserById(USER_ID)).thenReturn(userResponse);

        mockMvc.perform(get("/v1/users/{id}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID.toString()))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.phoneNumber").value(PHONE))
                .andExpect(jsonPath("$.role").value(ROLE.toString()))
                .andExpect(jsonPath("$.status").value(STATUS.toString()))
                .andExpect(jsonPath("$.createdAt").value(CREATED_AT.toString()))
                .andExpect(jsonPath("$.updatedAt").value(UPDATED_AT.toString()));
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    void getUsers_shouldReturnPage() throws Exception {
        Page<UserResponse> page = new PageImpl<>(List.of(userResponse));
        when(userService.getUsers(ArgumentMatchers.any())).thenReturn(page);

        mockMvc.perform(get("/v1/users")
                        .param("page", "0")
                        .param("size", "10")
                        .param("search", "Omar")
                        .param("status", STATUS.toString())
                        .param("role", ROLE.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(USER_ID.toString()))
                .andExpect(jsonPath("$.content[0].name").value(NAME))
                .andExpect(jsonPath("$.content[0].email").value(EMAIL))
                .andExpect(jsonPath("$.content[0].phoneNumber").value(PHONE))
                .andExpect(jsonPath("$.content[0].role").value(ROLE.toString()))
                .andExpect(jsonPath("$.content[0].status").value(STATUS.toString()))
                .andExpect(jsonPath("$.content[0].createdAt").value(CREATED_AT.toString()))
                .andExpect(jsonPath("$.content[0].updatedAt").value(UPDATED_AT.toString()));
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        when(userService.updateUser(ArgumentMatchers.eq(USER_ID), ArgumentMatchers.any(UserRequest.class)))
                .thenReturn(userResponse);

        mockMvc.perform(put("/v1/users/{id}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID.toString()))
                .andExpect(jsonPath("$.name").value(NAME));
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    void updateRole_shouldReturnUpdatedUser() throws Exception {
        UpdateRoleRequest request = new UpdateRoleRequest(ROLE);

        when(userService.updateUserRole(USER_ID, ROLE)).thenReturn(userResponse);

        mockMvc.perform(patch("/v1/users/{id}/role", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value(ROLE.toString()));
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    void updateStatus_shouldReturnUpdatedUser() throws Exception {
        UpdateStatusRequest request = new UpdateStatusRequest(STATUS);

        when(userService.updateUserStatus(USER_ID, STATUS)).thenReturn(userResponse);

        mockMvc.perform(patch("/v1/users/{id}/status", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(STATUS.toString()));
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    void deleteUser_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/v1/users/{id}", USER_ID))
                .andExpect(status().isNoContent());
    }
}
