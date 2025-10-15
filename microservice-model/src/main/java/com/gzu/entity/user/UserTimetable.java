package com.gzu.entity.user;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserTimetable {
    private Long userTimetableId;
    private Long userBaseId;
    private String courseName;
    private String teacherName;
    private String classRoom;
    private Integer weekDay;
    private Integer startPeriod;
    private Integer endPeriod;
    private String startTime;
    private String endTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private String importSource;
    private LocalDateTime createTime;
}