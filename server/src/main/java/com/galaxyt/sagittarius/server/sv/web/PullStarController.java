package com.galaxyt.sagittarius.server.sv.web;

import com.galaxyt.sagittarius.common.dto.StarVersionDto;
import com.galaxyt.sagittarius.server.manage.inter.IStarService;
import com.galaxyt.sagittarius.server.sv.version.StarVersionRegistry;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author zhouqi
 * @date 2019-07-15 10:28
 * @version v1.0.0
 * @Description 
 * 
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-15 10:28     zhouqi          v1.0.0           Created
 *
 */
@RestController
@RequestMapping("/pull")
public class PullStarController {


    @Autowired
    private IStarService starService;


    private Gson gson = new Gson();


    @GetMapping
    public ResponseEntity<String> pull(@RequestParam String applicationName,
                                 @RequestParam String namespace) {

        Integer serverCurrentVersion = StarVersionRegistry.INSTANCE.getVersion(applicationName, namespace);


        StarVersionDto starUpdateDto = this.starService.loadStar(applicationName, namespace, serverCurrentVersion);

        //使用 GSON 转成 JSON 字符串响应回去，客户端也以 GSON 进行转换，防止出现某些不确定的差异出现
        return new ResponseEntity<>(this.gson.toJson(starUpdateDto), HttpStatus.OK);


    }





}
