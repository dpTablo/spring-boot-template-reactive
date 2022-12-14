package com.dptablo.template.springboot.model.entity;

import com.dptablo.template.springboot.model.enumtype.Role;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "user_role")
@Table(name = "user_role")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@EqualsAndHashCode
public class UserRole implements Serializable {
    @Id
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String description;
}
