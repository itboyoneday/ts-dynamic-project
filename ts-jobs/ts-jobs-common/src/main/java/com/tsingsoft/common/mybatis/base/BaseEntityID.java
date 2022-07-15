package com.tsingsoft.common.mybatis.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *  基础实体类
 * @author bask
 * @param <ID>
 */
@Data
public abstract class BaseEntityID<ID> implements Serializable {

    private ID id;
    private Integer page = 1;
    private Integer rows = 10;
    private Date createtime;
    private Date updatetime;

    public BaseEntityID() {
    }
}
