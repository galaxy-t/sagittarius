package com.galaxyt.sagittarius.server.manage.web;

import com.galaxyt.sagittarius.common.exception.SagittariusException;
import com.galaxyt.sagittarius.server.enums.ResultCode;
import com.galaxyt.sagittarius.server.manage.inter.INamespaceService;
import com.galaxyt.sagittarius.server.pojo.dto.NamespaceDto;
import com.galaxyt.sagittarius.server.pojo.vo.NamespaceVo;
import com.galaxyt.sagittarius.server.pojo.vo.ReturnValueLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/namespace")
public class NamespaceController {


    @Autowired
    private INamespaceService namespaceService;


    @PutMapping
    public ReturnValueLoader put(@Valid @RequestBody NamespaceDto namespaceDto) {

        ResultCode resultCode = this.namespaceService.put(namespaceDto);

        return new ReturnValueLoader(resultCode);

    }

    @DeleteMapping
    public ReturnValueLoader remove(Integer id) {

        if (id == null) {
            throw new SagittariusException("参数错误");
        }

        int removeCount = this.namespaceService.remove(id);

        if (removeCount == 0) {
            throw new SagittariusException(String.format("要删除的数据(ID:%s)不存在",id));
        }

        return new ReturnValueLoader(ResultCode.SUCCESS);
    }


    @PatchMapping
    public ReturnValueLoader edit(NamespaceDto namespaceDto) {

        if (namespaceDto.getId() == null) {
            throw new SagittariusException("参数错误");
        }

        int editCount = this.namespaceService.edit(namespaceDto);

        if (editCount == 0) {
            throw new SagittariusException(String.format("要修改的数据(ID:%s)不存在", namespaceDto.getId()));
        }


        return new ReturnValueLoader(ResultCode.SUCCESS);
    }


    @GetMapping
    public ReturnValueLoader detail(@RequestParam Integer id) {

        if (id == null) {
            throw new SagittariusException("参数错误");
        }

        NamespaceVo namespace = this.namespaceService.detail(id);

        if (namespace == null) {
            throw new SagittariusException(String.format("要查询的数据(ID:%s)不存在", id));
        }

        return new ReturnValueLoader(namespace);

    }


    @GetMapping("list")
    public ReturnValueLoader list(@RequestParam Integer applicationId) {

        if (applicationId == null) {
            throw new SagittariusException("参数错误");
        }

        List<NamespaceVo> list = this.namespaceService.list(applicationId);

        if (list == null || list.size() == 0) {
            return new ReturnValueLoader(ResultCode.DATA_IS_NULL);
        }

        return new ReturnValueLoader(list);

    }



}
