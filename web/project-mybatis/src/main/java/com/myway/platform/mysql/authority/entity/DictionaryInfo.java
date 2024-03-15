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
 * 字典表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_dictionary_info")
public class DictionaryInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "dictionary_id", type = IdType.ID_WORKER)
    private String dictionaryId;

    /**
     * 字典编号
     */
    private String code;

    /**
     * 字典标题
     */
    private String title;

    /**
     * 所属上级
     */
    private String parentId;

    /**
     * 字典分类
     */
    private String classify;

    private String icon;

    /**
     * 字典等级
     */
    private Integer level;

    /**
     * 状态：0、禁用，1、启用
     */
    private Integer status;

    private LocalDateTime createTime;

    private Integer createDate;


}
