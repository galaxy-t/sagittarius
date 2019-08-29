package com.galaxyt.sagittarius.server.manage.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.galaxyt.sagittarius.server.enums.ResultCode;
import com.galaxyt.sagittarius.server.manage.dao.NamespaceDao;
import com.galaxyt.sagittarius.server.manage.inter.INamespaceService;
import com.galaxyt.sagittarius.server.pojo.dto.NamespaceDto;
import com.galaxyt.sagittarius.server.pojo.po.Namespace;
import com.galaxyt.sagittarius.server.pojo.vo.NamespaceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NamespaceService implements INamespaceService {



    @Autowired
    private NamespaceDao namespaceDao;


    @Override
    public ResultCode put(NamespaceDto namespaceDto) {

        int count = this.namespaceDao.selectCount(new QueryWrapper<Namespace>().
                eq("name",namespaceDto.getName()).
                eq("application_id",namespaceDto.getApplicationId()));


        if (count > 0) {
            return ResultCode.APPLICATION_NAMESPACE_EXISTED;
        }

        Namespace namespace = new Namespace();
        namespace.setName(namespaceDto.getName());
        namespace.setApplicationId(namespaceDto.getApplicationId());

        int insertCount = this.namespaceDao.insert(namespace);

        if (insertCount <= 0) {
            return ResultCode.ERROR;
        }

        return ResultCode.SUCCESS;
    }

    @Override
    public int remove(Integer id) {
        int deleteCount = this.namespaceDao.deleteById(id);

        return deleteCount;
    }

    @Override
    public int edit(NamespaceDto namespaceDto) {


        Namespace namespace = this.namespaceDao.selectById(namespaceDto.getId());

        if (namespace == null) {
            return 0;
        }

        namespace.setName(namespaceDto.getName());

        int updateCount = this.namespaceDao.updateById(namespace);
        return updateCount;
    }

    @Override
    public NamespaceVo detail(Integer id) {


        Namespace namespace = this.namespaceDao.selectById(id);

        if (namespace == null) {
            return null;
        }

        NamespaceVo namespaceVo = new NamespaceVo();
        namespaceVo.setName(namespace.getName());

        return namespaceVo;
    }

    @Override
    public List<NamespaceVo> list(Integer applicationId) {


        List<Namespace> list = this.namespaceDao.selectList(new QueryWrapper<Namespace>()
                .eq("application_id",applicationId)
                .orderByAsc("id"));

        List<NamespaceVo> voList = null;


        if (list != null && list.size() > 0) {

            voList = new ArrayList<>();

            NamespaceVo vo = null;


            for (Namespace namespace : list) {

                vo = new NamespaceVo();

                vo.setId(namespace.getId());
                vo.setName(namespace.getName());

                voList.add(vo);

            }

            return voList;
        }

        return null;
    }
}
