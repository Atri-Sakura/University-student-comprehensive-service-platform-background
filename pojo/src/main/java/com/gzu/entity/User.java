package com.gzu.entity;

import lombok.*;

import java.time.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String studentId;
    private String name;
    private String password;
    private String phoneNumber;
    private Integer age;
    private String sex;
    private String QQ;
    private Integer userType;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String userImg;
    private LocalDate regTime;
}
