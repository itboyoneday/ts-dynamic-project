package com.tsingsoft.logs.service;

import com.tsingsoft.common.mybatis.base.BaseService;
import com.tsingsoft.logs.entity.LogInfo;

/**
 * 日志接口类
 */
public interface ILogService /*extends BaseService<LogInfo>*/ {

    /**
     * 日志备份
     * @return
     */
    boolean backLog() throws Exception ;
}
