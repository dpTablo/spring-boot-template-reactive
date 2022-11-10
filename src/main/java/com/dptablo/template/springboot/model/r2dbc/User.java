package com.dptablo.template.springboot.model.r2dbc;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "t_user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"createDate", "updateDate"})
public class User {
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
}


