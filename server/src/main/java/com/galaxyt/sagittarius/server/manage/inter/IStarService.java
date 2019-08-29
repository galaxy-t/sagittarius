package com.galaxyt.sagittarius.server.manage.inter;

import com.galaxyt.sagittarius.common.dto.StarVersionDto;
import com.galaxyt.sagittarius.server.enums.ResultCode;
import com.galaxyt.sagittarius.server.pojo.dto.StarPutDto;
import com.galaxyt.sagittarius.server.pojo.vo.StarVo;

import java.util.List;

public interface IStarService {

    List<StarVo> list(int namespaceId);

    ResultCode save(StarPutDto starPutDto);


    StarVersionDto loadStar(String application, String namespace, Integer starVersion);
}
