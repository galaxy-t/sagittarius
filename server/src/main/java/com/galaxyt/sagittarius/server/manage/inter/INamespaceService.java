package com.galaxyt.sagittarius.server.manage.inter;

import com.galaxyt.sagittarius.server.enums.ResultCode;
import com.galaxyt.sagittarius.server.pojo.dto.ApplicationDto;
import com.galaxyt.sagittarius.server.pojo.dto.NamespaceDto;
import com.galaxyt.sagittarius.server.pojo.vo.NamespaceVo;

import java.util.List;

public interface INamespaceService {
    ResultCode put(NamespaceDto namespaceDto);

    int remove(Integer id);

    int edit(NamespaceDto namespaceDto);

    NamespaceVo detail(Integer id);

    List<NamespaceVo> list(Integer applicationId);
}
