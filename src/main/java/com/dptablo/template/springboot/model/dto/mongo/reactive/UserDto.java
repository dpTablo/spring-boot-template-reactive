package com.dptablo.template.springboot.model.dto.mongo.reactive;

import com.dptablo.template.springboot.model.mongo.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userId;
    private String phoneNumber;
    private String name;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public UserDto(User user) {
        this.userId = user.getUserId();
        this.phoneNumber = user.getPhoneNumber();
        this.name = user.getName();
        this.createDate = user.getCreateDate();
        this.updateDate = user.getUpdateDate();
    }
}
