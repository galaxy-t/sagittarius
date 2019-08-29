package com.galaxyt.sagittarius.server.manage.web;


import com.galaxyt.sagittarius.common.exception.SagittariusException;
import com.galaxyt.sagittarius.server.enums.ResultCode;
import com.galaxyt.sagittarius.server.manage.inter.IStarService;
import com.galaxyt.sagittarius.server.pojo.dto.StarPutDto;
import com.galaxyt.sagittarius.server.pojo.vo.ApplicationVo;
import com.galaxyt.sagittarius.server.pojo.vo.ReturnValueLoader;
import com.galaxyt.sagittarius.server.pojo.vo.StarVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("star")
public class StarController {

    @Autowired
    private IStarService starService;


    @PutMapping
    public ReturnValueLoader put(@Valid @RequestBody StarPutDto starPutDto) {


        ResultCode resultCode = this.starService.save(starPutDto);

        return new ReturnValueLoader(resultCode);

    }


    @GetMapping("list")
    public ReturnValueLoader list(@RequestParam Integer namespaceId) {

        if (namespaceId == null) {
            throw new SagittariusException("参数错误");
        }

        List<StarVo> list = this.starService.list(namespaceId);

        if (list == null || list.size() == 0) {
            return new ReturnValueLoader(ResultCode.DATA_IS_NULL);
        }

        return new ReturnValueLoader(list);

    }




}
