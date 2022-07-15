package com.tsingsoft.logs.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tsingsoft.logs.entity.LogBackup;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * 日志接口类
 * @author bask
 */
@Mapper
public interface LogBackupMapper extends BaseMapper<LogBackup> {

    /**
     * 批量插入日志表表
     * @param baseLogs 日志数据
     */
    void batchInser(List<LogBackup> baseLogs);

    /**
     * 通过修改时间删除日志记录
     * @param updTime
     * @return
     */
    void deleteLogBackupByUpdTime(Date updTime);
}