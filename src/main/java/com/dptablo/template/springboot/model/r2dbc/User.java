package com.dptablo.template.springboot.model.r2dbc;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("userId")
    private String userId;

    @Column
    @JsonProperty("password")
    private String password;

    @Column
    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @Column
    @JsonProperty("name")
    private String name;

    @Column
    @JsonProperty("createDate")
    private LocalDateTime createDate;

    @Column
    @JsonProperty("updateDate")
    private LocalDateTime updateDate;
}


