package com.tsingsoft.executor.controller;

import com.tsingsoft.executor.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author bask
 * @version 1.0
 * @date 2022/7/5
 * <p>
 */
@RestController
public class TestController {

    @Autowired
    private MyService myService;

    @GetMapping("/hello")
    public String hello() {

        return myService.print(" local controller hello word");
    }


}
