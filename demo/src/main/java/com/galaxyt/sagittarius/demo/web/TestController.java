package com.galaxyt.sagittarius.demo.web;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @Value("${testLocal}")
    private String testLocal;

    @Value("${testRemote}")
    private String testRemote;




    @GetMapping("test")
    public void change() {

        System.out.println(this.testLocal);
        System.out.println(this.testRemote);

    }



}
