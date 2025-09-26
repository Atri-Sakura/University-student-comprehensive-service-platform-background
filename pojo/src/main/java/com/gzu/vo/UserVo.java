package com.gzu.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    private Integer id;

    private String studentId;

    private String name;

    private String phoneNumber;

    private Integer age;

    private String sex;

    private String QQ;

}
