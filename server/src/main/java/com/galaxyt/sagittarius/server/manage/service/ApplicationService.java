package com.galaxyt.sagittarius.server.manage.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.galaxyt.sagittarius.server.enums.ResultCode;
import com.galaxyt.sagittarius.server.manage.dao.ApplicationDao;
import com.galaxyt.sagittarius.server.manage.dao.NamespaceDao;
import com.galaxyt.sagittarius.server.manage.inter.IApplicationService;
import com.galaxyt.sagittarius.server.pojo.dto.ApplicationDto;
import com.galaxyt.sagittarius.server.pojo.po.Application;
import com.galaxyt.sagittarius.server.pojo.vo.ApplicationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationService implements IApplicationService {


    @Autowired
    private ApplicationDao applicationDao;

    @Autowired
    private NamespaceDao namespaceDao;


    @Override
    @Transactional
    public ResultCode put(ApplicationDto application) {

        int count = this.applicationDao.selectCount(new QueryWrapper<Application>().eq("name", application.getName()));

        if (count > 0) {
            return ResultCode.APPLICATION_EXISTED;
        }

        Application newApplication = new Application();

        newApplication.setName(application.getName());

        int insertCount = this.applicationDao.insert(newApplication);

        if (insertCount <= 0) {
            return ResultCode.ERROR;
        }

        return ResultCode.SUCCESS;
    }

    @Override
    public int remove(int id) {

        int deleteCount = this.applicationDao.deleteById(id);

        return deleteCount;
    }

    @Override
    public int edit(ApplicationDto newApplication) {

        Application oldApplication = this.applicationDao.selectById(newApplication.getId());

        if (oldApplication == null) {
            return 0;
        }

        oldApplication.setName(newApplication.getName());

        return this.applicationDao.updateById(oldApplication);
    }

    @Override
    public ApplicationVo detail(int id) {

        Application application = this.applicationDao.selectById(id);

        if (application == null) {
            return null;
        }

        ApplicationVo applicationVo = new ApplicationVo();

        applicationVo.setId(application.getId());
        applicationVo.setName(application.getName());

        return applicationVo;
    }

    @Override
    public List<ApplicationVo> list(Integer userId) {


        List<Application> list = this.applicationDao.selectList(new QueryWrapper<Application>().orderByAsc("id"));


        if (list == null || list.size() == 0) {
            return null;
        }


        List<ApplicationVo> voList = new ArrayList<>();

        ApplicationVo vo = null;

        for (Application application : list) {

            vo = new ApplicationVo();
            vo.setId(application.getId());
            vo.setName(application.getName());

            voList.add(vo);
        }

        return voList;
    }


}
