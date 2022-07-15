package com.tsingsoft.logs.job;

import com.tsingsoft.logs.service.ILogService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class LogsHandle {

    @Value("${server.port}")
    private String port;

    @Autowired
    private ILogService logService;

    /**
     * 实现日志备份功能
     */
    @XxlJob("logsHandler")
    public void logBack() throws Exception{
        XxlJobHelper.log("日志开始备份"+" 端口："+port);

        String command = XxlJobHelper.getJobParam();
        XxlJobHelper.log("开始处理参数："+command+" 端口："+port);
        log.info("开始处理参数："+command+" 端口："+port);

        XxlJobHelper.log("业务处理"+" 端口："+port);
        XxlJobHelper.log("日志结束备份"+" 端口："+port);
        log.info("执行时间："+new Date() +" 端口："+port);

        log.debug("定时任务开始。。。。。。。。。。。。。。。。。。。。。。。。。",System.currentTimeMillis());
        boolean table = logService.backLog();
        log.debug("定时任务结束。。。。。。。。。。。。。。。。。。。。。。。。。",System.currentTimeMillis());
    }
}
