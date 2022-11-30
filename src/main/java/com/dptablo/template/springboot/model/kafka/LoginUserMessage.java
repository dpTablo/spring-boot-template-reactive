package com.dptablo.template.springboot.model.kafka;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUserMessage {
    private String userId;
    private String name;
    private LocalDateTime loginTime;
}
