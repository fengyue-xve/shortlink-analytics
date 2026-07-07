package com.example.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shortlink.entity.VisitLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VisitLogMapper extends BaseMapper<VisitLog> {
    @Select("SELECT COUNT(*) FROM visit_log WHERE DATE(visit_time) = CURDATE()")
    Long countTodayVisits();

    @Select("SELECT COUNT(*) FROM visit_log vl INNER JOIN short_link sl ON vl.link_id = sl.id WHERE sl.user_id = #{userId} AND DATE(vl.visit_time) = CURDATE()")
    Long countTodayVisitsByUser(Long userId);
}
