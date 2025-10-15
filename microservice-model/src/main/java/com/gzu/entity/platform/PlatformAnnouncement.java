package com.gzu.entity.platform;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlatformAnnouncement {
    private Long platformAnnouncementId;
    private String title;
    private String content;
    private Long publisherId;
    private String publisherName;
    private LocalDateTime publishTime;
    private Integer status;
    private Integer readCount;
    private Integer isTop;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}