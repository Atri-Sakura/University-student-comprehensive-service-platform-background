package com.gzu.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginVO {
    private Integer id;
    private String studentId;
    private String name;
    private String password;
    private String phoneNumber;
    private Integer age;
    private String sex;
    private Integer userType;
    private String userImg;

    private String token;
}
