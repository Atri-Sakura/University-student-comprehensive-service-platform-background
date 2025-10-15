package com.gzu.entity.chat;

import lombok.Data;
import java.util.Date;

@Data
public class ChatMessageRead {
    private Long readId;
    private Long messageId;
    private Integer readerType;
    private Long readerId;
    private Integer readStatus;
    private Date readTime;
    private Date createTime;
    private Date updateTime;
}