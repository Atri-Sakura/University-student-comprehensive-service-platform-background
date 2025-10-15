package com.gzu.entity.chat;

import lombok.Data;
import java.util.Date;

@Data
public class ChatAttachment {
    private Long attachmentId;
    private Long messageId;
    private Integer attachmentType;
    private String attachmentUrl;
    private String fileName;
    private Long fileSize;
    private String fileExt;
    private Date expireTime;
    private Integer isValid;
    private Date createTime;
    private Date updateTime;
}