package com.dptablo.template.springboot.model.topic;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class UserTopic {
    private String userId;
    private String name;
}
