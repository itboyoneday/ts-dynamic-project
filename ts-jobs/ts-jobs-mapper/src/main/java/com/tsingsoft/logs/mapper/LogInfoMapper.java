package com.tsingsoft.logs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tsingsoft.logs.entity.LogInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 日志接口类
 * @author bask
 */
public interface LogInfoMapper extends BaseMapper<LogInfo> {

    /**
     * 批量插入日志表表
     * @param baseLogs 日志数据
     */
    void batchInser(List<LogInfo> baseLogs);

    /**
     * 根据名称查询日志集合;
     * @param title 名称
     * @return     集合
     */
    List<LogInfo> findListByName(@Param("title") String title);

    /**
     * 通过创建时间查询日志记录
     * @param crtTime
     * @return
     */
    List<LogInfo> findLogByCrtTime(Date crtTime);


    /**
     * 通过修改时间查询日志记录
     * @param updTime
     * @return
     */
    List<LogInfo> findLogByUpdTime(Date updTime);

    /**
     * 通过修改时间删除日志记录
     * @param updTime
     * @return
     */
    void deleteLogByUpdTime(Date updTime);

    /**
     * 通过时间筛选查询日志记录
     * @param ids
     * @return
     */
    List<LogInfo> findAllByTime(@Param("ids") Long[] ids);

    /**
     * 保存菜单日志
     *
     * @param logInfo
     */
    void insertLog(LogInfo logInfo);

}
