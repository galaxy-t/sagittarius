package com.galaxyt.sagittarius.server.manage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.galaxyt.sagittarius.server.pojo.bo.StarVersionBo;
import com.galaxyt.sagittarius.server.pojo.po.StarVersion;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StarVersionDao extends BaseMapper<StarVersion> {


    @Select(" SELECT MAX(sv.id) AS version,a.name AS application,n.name AS namespace FROM t_star_version sv INNER JOIN t_namespace n ON sv.namespace_id = n.id INNER JOIN t_application a ON n.application_id = a.id GROUP BY a.id,n.id ")
    List<StarVersionBo> selectNewestStarVersion();


    @Select(" SELECT MAX(id) FROM t_star_version WHERE namespace_id = #{namespaceId} ")
    Integer selectMaxVersionIdByNamespaceId(@Param("namespaceId") int namespaceId);

    

}
