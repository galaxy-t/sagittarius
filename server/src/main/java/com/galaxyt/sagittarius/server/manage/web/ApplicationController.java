package com.galaxyt.sagittarius.server.manage.web;

import com.galaxyt.sagittarius.common.exception.SagittariusException;
import com.galaxyt.sagittarius.server.enums.ResultCode;
import com.galaxyt.sagittarius.server.manage.inter.IApplicationService;
import com.galaxyt.sagittarius.server.pojo.dto.ApplicationDto;
import com.galaxyt.sagittarius.server.pojo.vo.ApplicationVo;
import com.galaxyt.sagittarius.server.pojo.vo.ReturnValueLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 应用管理
 * @author zhouqi
 * @date 2019-08-20 10:21
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-08-20 10:21     zhouqi          v1.0.0           Created
 *
 */
@RestController
@RequestMapping("/application")
public class ApplicationController {


    @Autowired
    private IApplicationService applicationService;

    /**
     * 新增方法
     * @param application
     * @return
     */
    @PutMapping
    public ReturnValueLoader put(@Valid @RequestBody ApplicationDto application) {

        //执行新增逻辑问问
        ResultCode resultCode = this.applicationService.put(application);

        return new ReturnValueLoader(resultCode);
    }

    /**
     * 根据主键ID删除一条数据
     * @param id    主键ID
     * @return
     */
    @DeleteMapping
    public ReturnValueLoader remove(@RequestParam Integer id) {

        //执行删除操作
        int removeCount = this.applicationService.remove(id.intValue());

        /*
        若无法正常删除则抛出异常
         */
        if (removeCount == 0) {
            throw new SagittariusException(String.format("要删除的数据(ID:%s)不存在",id));
        }

        return new ReturnValueLoader(ResultCode.SUCCESS);
    }

    /**
     * 修改信息
     * @param application   修改时参数中 ID 必须存在
     * @return
     */
    @PatchMapping
    public ReturnValueLoader edit(ApplicationDto application) {

        if (application.getId() == null) {
            throw new SagittariusException("参数错误");
        }

        /*
        是否修改成功，若不成功则直接抛出异常
         */
        int editCount = this.applicationService.edit(application);
        if (editCount == 0) {
            throw new SagittariusException(String.format("要修改的数据(ID:%s)不存在", application.getId()));
        }

        return new ReturnValueLoader(ResultCode.SUCCESS);

    }

    /**
     * 根据主键 ID 查询应用详情
     * @param id    应用主键 ID
     * @return
     */
    @GetMapping
    public ReturnValueLoader detail(@RequestParam Integer id) {

        /*
        查询详情，若查询不到则抛出异常
         */
        ApplicationVo application = this.applicationService.detail(id.intValue());
        if (application == null) {
            throw new SagittariusException(String.format("要查询的数据(ID:%s)不存在", id));
        }


        return new ReturnValueLoader(application);
    }

    /**
     * 根据当前登陆用户主键 ID 查询应用列表
     * @param userId
     * @return
     */
    @GetMapping("list")
    public ReturnValueLoader list(@RequestParam Integer userId) {

        List<ApplicationVo> list = this.applicationService.list(userId);
        if (list == null || list.size() == 0) {
            return new ReturnValueLoader(ResultCode.DATA_IS_NULL);
        }

        return new ReturnValueLoader(list);
    }



}
