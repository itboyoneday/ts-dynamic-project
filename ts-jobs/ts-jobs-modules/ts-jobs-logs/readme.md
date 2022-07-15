# xxl-job任务怎么写。

## 1. 在pom.xml 增加依赖

```
<!-- xxl-job -->
<dependency>
    <groupId>com.xuxueli</groupId>
    <artifactId>xxl-job-core</artifactId>
    <version>2.3.1.1</version>
</dependency>
```
## 2. 实现一个组件类，如下示例代码。
``` 
package com.tsingsoft.logs.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class LogsHandle {

    @Value("${server.port}")
    private String port;

    /**
     * 实现日志备份功能
     */
    @XxlJob("testHandler")
    public void logBack() throws Exception{
        XxlJobHelper.log("日志开始备份"+" 端口："+port);

        String command = XxlJobHelper.getJobParam();
        XxlJobHelper.log("开始处理参数："+command+" 端口："+port);
        log.info("开始处理参数："+command+" 端口："+port);

        XxlJobHelper.log("业务处理"+" 端口："+port);
        XxlJobHelper.log("日志结束备份"+" 端口："+port);
        log.info("执行时间："+new Date() +" 端口："+port);
    }
}
 
```
主要要实现注释：@XxlJob("testHandler")和@Component，告诉执行器需要执行的任务是哪个接口，抛出给界面创建任务使用。