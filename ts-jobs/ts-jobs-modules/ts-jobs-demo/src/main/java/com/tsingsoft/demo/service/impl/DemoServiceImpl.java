package com.tsingsoft.demo.service.impl;

import com.tsingsoft.demo.service.DemoService;
import org.springframework.stereotype.Service;

/**
 * @author bask
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Override
    public String hello(String test_plugin) {
        return "say:"+test_plugin;
    }

}
