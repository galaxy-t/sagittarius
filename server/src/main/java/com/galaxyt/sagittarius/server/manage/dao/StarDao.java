package com.galaxyt.sagittarius.server.manage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.galaxyt.sagittarius.common.dto.StarDto;
import com.galaxyt.sagittarius.server.pojo.po.Star;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StarDao extends BaseMapper<Star> {


    @Select(" SELECT star_key AS starKey,star_value AS starValue FROM t_star WHERE star_version_id = #{version} ")
    List<StarDto> selectByVersion(@Param("version") int version);





}
