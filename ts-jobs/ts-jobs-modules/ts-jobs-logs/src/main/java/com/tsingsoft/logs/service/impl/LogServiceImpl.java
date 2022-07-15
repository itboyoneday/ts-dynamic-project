package com.tsingsoft.logs.service.impl;

import com.google.common.collect.Lists;
import com.tsingsoft.common.utils.BeanUtil;
import com.tsingsoft.logs.entity.LogBackup;
import com.tsingsoft.logs.entity.LogInfo;
import com.tsingsoft.logs.mapper.LogInfoMapper;
import com.tsingsoft.logs.service.ILogBackupService;
import com.tsingsoft.logs.service.ILogService;
import com.xxl.job.core.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * ========================
 *
 * @author bask
 * @Description: 日志接口实现
 * @date : 2022/7/6 17:58
 * Version: 1.0
 * ========================
 */
@Slf4j
@Service
public class LogServiceImpl /*extends BaseServiceImpl<LogInfoMapper, LogInfo>*/ implements ILogService {

    @Autowired
    private ILogBackupService logBackupService;

    @Autowired
    private LogInfoMapper logInfoMapper;

    /**
     * 备份日志
     *
     * @return 数据
     */
    @Override
    public boolean backLog() throws Exception {
        Date date = GregorianCalendar.getInstance().getTime();
        log.info("autoBackupLog>>> 备份日志》》》删除日志");
        Date yesterday = DateUtil.addDays(date, -1);
        List<LogInfo> logByUpdTime = logInfoMapper.findLogByUpdTime(yesterday);

        if(logByUpdTime != null && !logByUpdTime.isEmpty()){
            List<LogBackup> logBackupList = new ArrayList<>();
            for (LogInfo logInfo:logByUpdTime) {
                LogBackup logBackup = new LogBackup();
                BeanUtil.copy(logInfo,logBackup);
                logBackupList.add(logBackup);
            }
            if(logBackupList != null && !logBackupList.isEmpty()){
                if(logBackupList.size() > 2000){
                    List<List<LogBackup>> partition = Lists.partition(logBackupList, 2000);
                    for (List<LogBackup> list:partition) {
                        logBackupService.batchInser(list);
                    }
                }else{
                    logBackupService.batchInser(logBackupList);
                }
            }
        }

        //删除base_log表里三个月前的数据
        Date logDate = DateUtil.addMonths(date, -3);
        logInfoMapper.deleteLogByUpdTime(logDate);
        //删除base_log_backup表里六个月前的数据
        Date logBackupDate = DateUtil.addMonths(date, -6);
        logBackupService.deleteLogBackupByUpdTime(logBackupDate);
        return true;
    }
}
