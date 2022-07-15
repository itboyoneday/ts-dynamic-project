package com.tsingsoft.demo.task;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * ========================
 *
 * @author bask
 * @Description: 测试任务
 * @date : 2022/3/16 16:49
 * Version: 1.0
 * ========================
 */
@Component
@Slf4j
public class DemoXxlJob {

    @Value("${server.port}")
    private String port;

    /**
     * 1、简单任务示例（Bean模式）
     *
     */
    @XxlJob("tsJobHandler")
    public void demoJobHandler() throws Exception {
        XxlJobHelper.log("ts开始统计"+" 端口："+port);

        String command = XxlJobHelper.getJobParam();
        XxlJobHelper.log("ts开始处理参数："+command+" 端口："+port);
        log.info("ts开始处理参数："+command+" 端口："+port);

        XxlJobHelper.log("ts业务处理"+" 端口："+port);
        XxlJobHelper.log("ts结束统计"+" 端口："+port);
        log.info("ts执行时间："+new Date() +" 端口："+port);
        XxlJobHelper.handleSuccess("测试成功了"+new Date());
    }

    /**
     *  父任务测试
     */
    @XxlJob("parentJobHandler")
    public void parentJobHandler() throws Exception {
        log.info("开始执行父任务");
        XxlJobHelper.log("父任务开始统计");
        XxlJobHelper.log("父任务业务处理");
        XxlJobHelper.log("父任务结束统计");
        log.info("执行时间："+new Date());
        XxlJobHelper.handleSuccess("父任务测试成功了"+new Date());
    }

    /**
     *  子任务测试
     */
    @XxlJob("childJobHandler")
    public void childJobHandler() throws Exception {
        log.info("开始执行子任务");
        XxlJobHelper.log("子任务开始统计");
        XxlJobHelper.log("子任务业务处理");
        XxlJobHelper.log("子任务结束统计");
        Thread.sleep(1000);
        log.info("执行时间："+new Date());
        XxlJobHelper.handleSuccess("子任务测试成功了"+new Date());
    }

    /**
     *  子任务2测试
     */
    @XxlJob("childTwoJobHandler")
    public void childTwoJobHandler() throws Exception {
        log.info("开始执行子任务2");
        XxlJobHelper.log("子任务2开始统计");
        XxlJobHelper.log("子任务2业务处理");
        XxlJobHelper.log("子任务2结束统计");
        Thread.sleep(2000);
        log.info("执行时间："+new Date());
        XxlJobHelper.handleSuccess("子任务2测试成功了"+new Date());
    }

    /**
     *  传递参数测试
     */
    @XxlJob("paramsJobHandler")
    public void paramsJobHandler() throws Exception {
        log.info("开始执行传递参数任务");
        XxlJobHelper.log("传递参数任务处理业务");
        log.info("执行时间："+new Date());
        String command = XxlJobHelper.getJobParam();
        XxlJobHelper.handleSuccess("传递参数任务成功了,参数："+command);
    }

}
