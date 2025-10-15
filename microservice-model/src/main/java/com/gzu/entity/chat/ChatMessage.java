package com.gzu.entity.chat;

import lombok.Data;
import java.util.Date;

@Data
public class ChatMessage {
    private Long messageId;
    private Long sessionId;
    private Integer fromType;
    private Long fromId;
    private Integer toType;
    private Long toId;
    private Integer msgType;
    private String msgContent;
    private Integer msgStatus;
    private Date sendTime;
    private Date deliverTime;
    private Date readTime;
    private Integer isDeleted;
    private Date createTime;
    private Date updateTime;
}