package com.galaxyt.sagittarius.server.manage.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.galaxyt.sagittarius.common.dto.StarVersionDto;
import com.galaxyt.sagittarius.server.enums.ResultCode;
import com.galaxyt.sagittarius.server.manage.dao.ApplicationDao;
import com.galaxyt.sagittarius.server.manage.dao.NamespaceDao;
import com.galaxyt.sagittarius.server.manage.dao.StarDao;
import com.galaxyt.sagittarius.server.manage.dao.StarVersionDao;
import com.galaxyt.sagittarius.server.manage.inter.IStarService;
import com.galaxyt.sagittarius.server.pojo.dto.StarDto;
import com.galaxyt.sagittarius.server.pojo.dto.StarPutDto;
import com.galaxyt.sagittarius.server.pojo.po.Application;
import com.galaxyt.sagittarius.server.pojo.po.Namespace;
import com.galaxyt.sagittarius.server.pojo.po.Star;
import com.galaxyt.sagittarius.server.pojo.po.StarVersion;
import com.galaxyt.sagittarius.server.pojo.vo.StarVo;
import com.galaxyt.sagittarius.server.sv.version.StarVersionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StarService implements IStarService {

    @Autowired
    private ApplicationDao applicationDao;

    @Autowired
    private NamespaceDao namespaceDao;


    @Autowired
    private StarDao starDao;

    @Autowired
    private StarVersionDao starVersionDao;

    @Override
    public List<StarVo> list(int namespaceId) {


        Integer versionId = this.starVersionDao.selectMaxVersionIdByNamespaceId(namespaceId);

        if (versionId == null) {
            return null;
        }

        List<Star> list = this.starDao.selectList(new QueryWrapper<Star>().eq("star_version_id", versionId));


        if (list == null || list.size() == 0) {
            return null;
        }


        List<StarVo> voList = new ArrayList<>();

        StarVo vo = null;

        for (Star star : list) {
            vo = new StarVo();

            vo.setStarKey(star.getStarKey());
            vo.setStarValue(star.getStarValue());

            voList.add(vo);
        }

        return voList;
    }

    @Override
    @Transactional
    public ResultCode save(StarPutDto starPutDto) {


        StarVersion starVersion = new StarVersion();

        starVersion.setNamespaceId(starPutDto.getNamespaceId());

        this.starVersionDao.insert(starVersion);


        Star newStar = new Star();

        for (StarDto sd : starPutDto.getStars()) {
            newStar.setStarVersionId(starVersion.getId());
            newStar.setStarKey(sd.getStarKey());
            newStar.setStarValue(sd.getStarValue());
            this.starDao.insert(newStar);
        }

        Namespace namespace = this.namespaceDao.selectById(starPutDto.getNamespaceId());

        Application application = this.applicationDao.selectById(namespace.getApplicationId());


        com.galaxyt.sagittarius.server.sv.version.StarVersion sv
                = new com.galaxyt.sagittarius.server.sv.version.StarVersion(application.getName(), namespace.getName(), starVersion.getId());

        StarVersionRegistry.INSTANCE.addStarVersion(sv);

        return ResultCode.SUCCESS;
    }



    @Override
    public StarVersionDto loadStar(String application, String namespace, Integer starVersion) {


        StarVersionDto starVersionDto = new StarVersionDto();

        starVersionDto.setApplicationName(application);
        starVersionDto.setNamespace(namespace);
        starVersionDto.setVersion(starVersion);

        List<com.galaxyt.sagittarius.common.dto.StarDto> stars = this.starDao.selectByVersion(starVersion.intValue());
        starVersionDto.setStars(stars);

        return starVersionDto;
    }


}
