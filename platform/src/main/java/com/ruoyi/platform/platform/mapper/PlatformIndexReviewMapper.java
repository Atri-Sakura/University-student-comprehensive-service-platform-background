package com.ruoyi.platform.platform.mapper;

import com.ruoyi.platform.domain.IndexImgUrl;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PlatformIndexReviewMapper {
    @Select("SELECT * FROM index_image_url")
    List<IndexImgUrl> getUserIndexImgs();


    @Delete("delete from index_image_url where index_image_url_id = #{indexImgUrlId}")
    int deleteUserIndexImgs(Integer indexImgUrlId);

    @Insert("insert into index_image_url (index_image_url) values (#{imageUrl})")
    int addIndexImgUrl(String imageUrl);
}
