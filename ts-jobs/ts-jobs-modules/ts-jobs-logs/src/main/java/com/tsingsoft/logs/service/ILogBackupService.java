package com.tsingsoft.logs.service;

import com.tsingsoft.common.mybatis.base.BaseService;
import com.tsingsoft.logs.entity.LogBackup;

import java.util.Date;
import java.util.List;

/**
 * <p> 日志业务接口</p>
 *
 * @author : bask
 * @date 2018-04-26 14:58
 */
public interface ILogBackupService /*extends BaseService<LogBackup>*/ {

    /**
     * 保存日志
     *
     * @param info 日志信息
     * @throws Exception 异常
     */
    void saveLog(LogBackup info) throws Exception;

    /**
     * 批量保存日志
     *
     * @param logInfos 日志信息
     * @throws Exception 异常
     */
    void batchInser(List<LogBackup> logInfos) throws Exception;

    /**
     * 通过修改时间删除日志
     * @param updTime
     * @return
     */
    void deleteLogBackupByUpdTime(Date updTime);

}
