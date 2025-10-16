create table user_credit_score_record
(
    id           bigint auto_increment comment '主键'
        primary key,
    user_base_id bigint                             not null comment '用户ID',
    change_score int                                not null comment '分数变动（正负）',
    `desc`       varchar(100)                       not null comment '变动说明',
    change_time  datetime default CURRENT_TIMESTAMP not null comment '变动时间'
)
    comment '用户信用分流水表';

create index idx_user_time
    on user_credit_score_record (user_base_id, change_time);

