package com.tsingsoft.common.mybatis.base;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类
 *
 * @author Bask
 */
@Data
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 6441418903032368440L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 描述.
	 */
	private String description;

	/**
	 * 创建时间
	 */
	private Date crtTime;

	/**
	 * 更新时间
	 */
	private Date updTime;

	/**
	 * 创建用户编号
	 */
	private Long crtUserId;

	/**
	 * 更新用户编号
	 */
	private Long updUserId;

	/**
	 * 删除标记（0：正常；1：删除；2：审核）
	 */
	private String delFlag = "0";
}
