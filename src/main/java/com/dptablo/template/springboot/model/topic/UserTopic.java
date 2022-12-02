package com.dptablo.template.springboot.model.topic;


import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UserTopic {
    private String userId;
    private String name;
}
