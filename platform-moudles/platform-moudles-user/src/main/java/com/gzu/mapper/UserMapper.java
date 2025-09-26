package com.gzu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.gzu.domain.UserBase;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;


@Mapper
public interface UserMapper extends BaseMapper<UserBase> {

}
