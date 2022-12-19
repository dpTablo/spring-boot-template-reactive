package com.dptablo.template.springboot.model.dto.mongo.reactive;

import com.dptablo.template.springboot.model.mongo.reactive.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    private String userId;
    private String password;
    private String phoneNumber;
    private String name;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    public UpdateUserDto(User user) {
        this.userId = user.getUserId();
        this.password = user.getPassword();
        this.phoneNumber = user.getPhoneNumber();
        this.name = user.getName();
        this.createDate = user.getCreateDate();
        this.updateDate = user.getUpdateDate();
    }

    public User createUser() {
        return User.builder()
                .userId(this.userId)
                .password(this.password)
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .createDate(this.createDate)
                .updateDate(this.updateDate)
                .build();
    }
}
