package com.example.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shortlink.entity.Campaign;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CampaignMapper extends BaseMapper<Campaign> {
    @Select("SELECT COALESCE(SUM(visit_count), 0) FROM short_link WHERE campaign_id = #{campaignId}")
    Long sumVisitCount(Long campaignId);

    @Select("SELECT COUNT(*) FROM short_link WHERE campaign_id = #{campaignId}")
    Long countLinks(Long campaignId);
}
