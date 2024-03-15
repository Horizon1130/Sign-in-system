package com.myway.platform.mysql.authority.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 新闻资讯表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_news_info")
public class NewsInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "news_id", type = IdType.ID_WORKER)
    private String newsId;

    /**
     * 新闻标题
     */
    private String title;

    /**
     * 新闻内容
     */
    private String content;

    /**
     * 状态：0、未启用，1、已启用
     */
    private Integer status;

    private String type;

    private LocalDateTime createTime;

    private Integer createDate;


}
