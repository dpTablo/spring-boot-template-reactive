package com.dptablo.template.springboot.model.mongo.reactive;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@Document(collection = "t_user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class User {
    @Id
    @Field(targetType = FieldType.STRING)
    private String userId;

    @Field(targetType = FieldType.STRING)
    private String password;

    @Field(targetType = FieldType.STRING)
    private String phoneNumber;

    @Field(targetType = FieldType.STRING)
    private String name;

    @Field(targetType = FieldType.TIMESTAMP)
    private LocalDateTime createDate;

    @Field(targetType = FieldType.TIMESTAMP)
    private LocalDateTime updateDate;
}
