package com.looyt.usermanagementservice.repository.specification;

import com.looyt.usermanagementservice.model.entity.UserEntity;
import com.looyt.usermanagementservice.model.enums.Role;
import com.looyt.usermanagementservice.model.enums.Status;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<UserEntity> hasStatus(Status status) {
        return (root, query, cb)
                -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<UserEntity> hasRole(Role role) {
        return (root, query, cb)
                -> role == null ? null : cb.equal(root.get("role"), role);
    }

    public static Specification<UserEntity> containsSearch(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return null;

            String pattern = "%" + search.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern),
                    cb.like(cb.lower(root.get("phoneNumber")), pattern)
            );
        };
    }

}
