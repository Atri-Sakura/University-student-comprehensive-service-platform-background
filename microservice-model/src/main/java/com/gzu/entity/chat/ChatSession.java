package com.gzu.entity.chat;

import lombok.Data;
import java.util.Date;

@Data
public class ChatSession {
    private Long sessionId;
    private Integer fromType;
    private Long fromId;
    private Integer toType;
    private Long toId;
    private Long lastMsgId;
    private String lastMsgContent;
    private Integer lastMsgType;
    private Date lastMsgTime;
    private Integer unreadCount;
    private Integer sessionStatus;
    private Date createTime;
    private Date updateTime;
}
