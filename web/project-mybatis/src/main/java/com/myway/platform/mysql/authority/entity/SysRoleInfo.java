package com.myway.platform.mysql.authority.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 系统用户角色表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sys_role_info")
public class SysRoleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "role_id", type = IdType.ID_WORKER)
    private String roleId;

    @NotBlank(message = "请输入角色编码")
    private String code;

    @NotBlank(message = "请输入角色标题")
    private String title;

    private String detail;

    @NotNull(message = "状态无输入")
    @Range(max = 1, min = -1, message = "状态异常")
    private Integer status;


}
