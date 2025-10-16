create table user_timetable
(
    user_timetable_id bigint                             not null comment '课表记录唯一ID'
        primary key,
    user_base_id      bigint                             not null comment '所属用户ID（关联user_base.user_base_id）',
    course_name       varchar(50)                        not null comment '课程名称',
    teacher_name      varchar(20)                        null comment '授课教师姓名',
    class_room        varchar(50)                        not null comment '上课教室（如"1号教学楼302"）',
    week_day          tinyint                            not null comment '星期(1-周一 7-周日)',
    start_period      tinyint                            not null comment '开始节次',
    end_period        tinyint                            not null comment '结束节次',
    start_time        varchar(20)                        not null comment '开始时间（如"08:00"）',
    end_time          varchar(20)                        not null comment '结束时间（如"09:40"）',
    start_date        date                               not null comment '课程开始日期',
    end_date          date                               not null comment '课程结束日期',
    import_source     varchar(20)                        null comment '导入来源',
    create_time       datetime default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '个人课表表';

create index idx_class_room
    on user_timetable (class_room)
    comment '教室索引';

create index idx_user_week_time
    on user_timetable (user_base_id, week_day, end_time)
    comment '用户+星期+下课时间索引';

