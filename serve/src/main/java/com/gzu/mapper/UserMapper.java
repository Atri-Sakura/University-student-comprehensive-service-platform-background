package com.gzu.mapper;

import com.gzu.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {


    /**
     * 根据id查询用户信息
     * @param publisherId
     * @return
     */
    @Select("select * from User where id =#{publisherId}")
    User getById(Integer publisherId);


    //根据用户id查询用户信息
    @Select("select * from User where student_id = #{userId}")
    User getByUsertId(String userId);


    void updatePdById(User user);

    @Select("select student_id from User where name = #{Name}")
    Integer getPublisherIdByName(String Name);
}
