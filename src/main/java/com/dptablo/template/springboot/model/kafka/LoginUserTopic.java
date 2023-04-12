package com.dptablo.template.springboot.model.kafka;


import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserTopic {
    public static final String TOPIC_NAME = "login-user-topic";

    private String userId;
    private String name;
    private LocalDateTime loginTime;
}
