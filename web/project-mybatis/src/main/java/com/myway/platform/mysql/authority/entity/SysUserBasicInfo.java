package com.myway.platform.mysql.authority.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统用户基本信息表
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sys_user_basic_info")
public class SysUserBasicInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "sys_user_id", type = IdType.ID_WORKER)
    private String sysUserId;

    private String code;

    private String nice;

    private String face;

    private Integer sex;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private String phone;

    private String provinceId;

    private String cityId;

    private String areaId;

    private String address;

    private String email;

    private Integer type;

    private String qq;

    private String weixin;

    private String sign;

    private Integer online;

    private Double balance;

    private String token;

    private String positionId;

    private String courseId;

    private LocalDateTime createTime;

    private Integer createDate;

    private LocalDateTime lastUpdateTime;

    private Integer lastUpdateDate;

}
