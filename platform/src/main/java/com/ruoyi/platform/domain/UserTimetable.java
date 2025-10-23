package com.ruoyi.platform.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 个人课对象 user_timetable
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class UserTimetable extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 课表记录唯一ID */
    private Long userTimetableId;

    /** 所属用户ID（关联user_base.user_base_id） */
    @Excel(name = "所属用户ID", readConverterExp = "关=联user_base.user_base_id")
    private Long userBaseId;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String courseName;

    /** 授课教师姓名 */
    @Excel(name = "授课教师姓名")
    private String teacherName;

    /** 上课教室（如"1号教学楼302"） */
    @Excel(name = "上课教室", readConverterExp = "如=1号教学楼302")
    private String classRoom;

    /** 星期(1-周一 7-周日) */
    @Excel(name = "星期(1-周一 7-周日)")
    private Long weekDay;

    /** 开始节次 */
    @Excel(name = "开始节次")
    private Long startPeriod;

    /** 结束节次 */
    @Excel(name = "结束节次")
    private Long endPeriod;

    /** 开始时间（如"08:00"） */
    @Excel(name = "开始时间", readConverterExp = "如=08:00")
    private String startTime;

    /** 结束时间（如"09:40"） */
    @Excel(name = "结束时间", readConverterExp = "如=09:40")
    private String endTime;

    /** 课程开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "课程开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 课程结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "课程结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 导入来源 */
    @Excel(name = "导入来源")
    private String importSource;

    public void setUserTimetableId(Long userTimetableId) 
    {
        this.userTimetableId = userTimetableId;
    }

    public Long getUserTimetableId() 
    {
        return userTimetableId;
    }

    public void setUserBaseId(Long userBaseId) 
    {
        this.userBaseId = userBaseId;
    }

    public Long getUserBaseId() 
    {
        return userBaseId;
    }

    public void setCourseName(String courseName) 
    {
        this.courseName = courseName;
    }

    public String getCourseName() 
    {
        return courseName;
    }

    public void setTeacherName(String teacherName) 
    {
        this.teacherName = teacherName;
    }

    public String getTeacherName() 
    {
        return teacherName;
    }

    public void setClassRoom(String classRoom) 
    {
        this.classRoom = classRoom;
    }

    public String getClassRoom() 
    {
        return classRoom;
    }

    public void setWeekDay(Long weekDay) 
    {
        this.weekDay = weekDay;
    }

    public Long getWeekDay() 
    {
        return weekDay;
    }

    public void setStartPeriod(Long startPeriod) 
    {
        this.startPeriod = startPeriod;
    }

    public Long getStartPeriod() 
    {
        return startPeriod;
    }

    public void setEndPeriod(Long endPeriod) 
    {
        this.endPeriod = endPeriod;
    }

    public Long getEndPeriod() 
    {
        return endPeriod;
    }

    public void setStartTime(String startTime) 
    {
        this.startTime = startTime;
    }

    public String getStartTime() 
    {
        return startTime;
    }

    public void setEndTime(String endTime) 
    {
        this.endTime = endTime;
    }

    public String getEndTime() 
    {
        return endTime;
    }

    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }

    public Date getStartDate() 
    {
        return startDate;
    }

    public void setEndDate(Date endDate) 
    {
        this.endDate = endDate;
    }

    public Date getEndDate() 
    {
        return endDate;
    }

    public void setImportSource(String importSource) 
    {
        this.importSource = importSource;
    }

    public String getImportSource() 
    {
        return importSource;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userTimetableId", getUserTimetableId())
            .append("userBaseId", getUserBaseId())
            .append("courseName", getCourseName())
            .append("teacherName", getTeacherName())
            .append("classRoom", getClassRoom())
            .append("weekDay", getWeekDay())
            .append("startPeriod", getStartPeriod())
            .append("endPeriod", getEndPeriod())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("importSource", getImportSource())
            .append("createTime", getCreateTime())
            .toString();
    }
}
