package com.myway.platform.mysql.authority.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统资源表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sys_resources")
public class SysResources implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "resource_id", type = IdType.ID_WORKER)
    private String resourceId;

    private String title;

    private String url;

    private String perms;

    private String icon;

    private Boolean visible;

    private String style;

    private String parentId;

    private Integer type;

    private String classify;

    private Integer sort;

    private Integer ascription;

    private String grouping;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime lastUpdateTime;


}
