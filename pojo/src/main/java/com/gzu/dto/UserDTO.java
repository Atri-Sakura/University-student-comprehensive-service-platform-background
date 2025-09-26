package com.gzu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private Integer id;
    private Integer studentId;
    private String name;
    private Integer password;
    private Integer phoneNumber;
    private Integer age;
    private String sex;
    private String userImg;
}
