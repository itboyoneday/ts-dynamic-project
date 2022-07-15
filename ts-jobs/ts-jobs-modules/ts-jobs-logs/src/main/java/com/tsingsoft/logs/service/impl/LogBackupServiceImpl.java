package com.tsingsoft.logs.service.impl;

import com.tsingsoft.common.mybatis.base.BaseServiceImpl;
import com.tsingsoft.logs.entity.LogBackup;
import com.tsingsoft.logs.mapper.LogBackupMapper;
import com.tsingsoft.logs.service.ILogBackupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>日志接口实现</p>
 *
 * @author:bask
 * @create 2021-06-23
 */
@Slf4j
@Service
public class LogBackupServiceImpl /*extends BaseServiceImpl<LogBackupMapper, LogBackup>*/ implements ILogBackupService {

    @Autowired
    private LogBackupMapper backupMapper;

    @Async
    @Override
    public void saveLog(LogBackup info) {
        backupMapper.insert(info);
    }

    @Async
    @Override
    public void batchInser(List<LogBackup> logInfos) throws Exception {
        backupMapper.batchInser(logInfos);
    }

    @Override
    public void deleteLogBackupByUpdTime(Date updTime){
        backupMapper.deleteLogBackupByUpdTime(updTime);
    }

}
