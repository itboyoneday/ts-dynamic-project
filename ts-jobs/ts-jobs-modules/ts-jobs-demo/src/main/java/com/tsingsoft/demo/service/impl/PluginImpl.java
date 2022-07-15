package com.tsingsoft.demo.service.impl;

import com.tsingsoft.demo.service.PluginInterface;
import org.springframework.stereotype.Service;

/**
 * @author bask
 */
@Service("pluginInterface")
public class PluginImpl implements PluginInterface {
    @Override
    public String sayHello(String name) {
        String result = "Hello " + name;
        System.out.println(result);
        return result;
    }
}
