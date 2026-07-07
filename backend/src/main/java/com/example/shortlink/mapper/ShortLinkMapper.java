package com.example.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shortlink.entity.ShortLink;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ShortLinkMapper extends BaseMapper<ShortLink> {
    @Select("SELECT * FROM short_link WHERE short_code = #{shortCode}")
    ShortLink selectByCode(String shortCode);

    @Update("UPDATE short_link SET visit_count = visit_count + 1 WHERE id = #{id}")
    int increaseVisitCount(Long id);
}
