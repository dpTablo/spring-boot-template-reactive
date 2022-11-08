package com.dptablo.template.springboot.model.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"user\"")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@EqualsAndHashCode(exclude = "userRoleMappings")
public class User implements Serializable {
    @Id
    @Column
    private String userId;

    @Column
    private String password;

    @Column
    private String phoneNumber;

    @Column
    private String name;

    @Column
    private LocalDateTime createDate;

    @Column
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserRoleMapping> userRoleMappings = new HashSet<>();

    public UserRoleMapping addUserRole(UserRole userRole) {
        var mapping = UserRoleMapping.builder()
                .user(this)
                .role(userRole)
                .build();
        userRoleMappings.add(mapping);
        return mapping;
    }

    public void addUserRoleMapping(UserRoleMapping mapping) {
        mapping.setUser(this);
        userRoleMappings.add(mapping);
    }
}
