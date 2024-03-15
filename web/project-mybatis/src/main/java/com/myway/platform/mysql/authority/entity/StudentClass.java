package com.myway.platform.mysql.authority.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_student_class")
public class StudentClass implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *  
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private String content;


}