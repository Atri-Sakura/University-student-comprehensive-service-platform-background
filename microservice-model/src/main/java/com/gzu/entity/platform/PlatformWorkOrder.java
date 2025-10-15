package com.gzu.entity.platform;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlatformWorkOrder {
    private Long platformWorkorderId;
    private Long orderId;
    private Long userId;
    private Integer userType;
    private String userNickname;
    private String content;
    private String imgUrls;
    private Integer workorderType;
    private Long handlerId;
    private String handlerName;
    private Integer handleStatus;
    private String handleResult;
    private LocalDateTime createTime;
    private LocalDateTime handleTime;
    private LocalDateTime closeTime;
}