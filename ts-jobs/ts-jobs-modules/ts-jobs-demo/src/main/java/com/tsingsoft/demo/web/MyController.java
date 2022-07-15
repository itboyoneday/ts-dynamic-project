package com.tsingsoft.demo.web;

import com.tsingsoft.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bask
 */
@RestController
public class MyController {

    @Autowired
    private DemoService demoService;

    @GetMapping("/test")
    public String test() {
        System.out.println("test plugin");
        return "test plugin";
    }

    @GetMapping("/lijian")
    public String lijian() {
        String msg = demoService.hello("lijian hahaha>>>>");
        System.out.println(msg);
        return msg;
    }

    @GetMapping("/aaa")
    public String aaa() {
        String msg = demoService.hello("lijian aaaaa>>>>");
        System.out.println(msg);
        return msg;
    }

    @GetMapping("/bbb")
    public String bbb() {
        String msg = demoService.hello("lijian bbbbb>>>>");
        System.out.println(msg);
        return msg;
    }
}
