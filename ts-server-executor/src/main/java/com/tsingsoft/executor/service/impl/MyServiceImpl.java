package com.tsingsoft.executor.service.impl;

import com.tsingsoft.executor.service.MyService;
import org.springframework.stereotype.Service;

/**
 * @author bask
 */
@Service
public class MyServiceImpl implements MyService {

    @Override
    public String print(String msg) {
        System.out.println(msg);
        return "say:"+msg;
    }
}
