package com.myway.platform.mysql.authority.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 系统用户权限表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sys_role_authority")
public class SysRoleAuthority implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "authority_id", type = IdType.ID_WORKER)
    private String authorityId;

    private String resourceId;

    private String roleId;


}
