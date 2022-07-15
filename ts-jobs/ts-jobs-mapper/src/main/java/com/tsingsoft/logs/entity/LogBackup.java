package com.tsingsoft.logs.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tsingsoft.common.mybatis.base.BaseEntity;
import lombok.Data;

/**
 * 日志信息
 * @author bask
 */
@TableName("base_log_backup")
@Data
public class LogBackup extends BaseEntity {

    private String menu;

    private String opt;

    private String uri;

    private Long crtUser;

    private String crtName;

    private String crtHost; // 服务器

    private String crtArgs; // 请求参数

    private String os; // 操作系统

    private String browser; // 浏览器

    private String method; // 当前执行的方法

    private int level ; // 日志安全等级

    private String type; // 日志类型：1-操作日志 2-异常日志

    private String eventType;

}
