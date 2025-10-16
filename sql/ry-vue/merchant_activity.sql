create table merchant_activity
(
    merchant_activity_id bigint                             not null comment '活动唯一ID'
        primary key,
    merchant_base_id     bigint                             not null comment '所属商家ID',
    activity_name        varchar(100)                       not null comment '活动名称',
    activity_type        varchar(50)                        not null comment '活动类型',
    start_time           datetime                           not null comment '开始时间',
    end_time             datetime                           not null comment '结束时间',
    content              text                               null comment '活动内容',
    status               tinyint  default 0                 not null comment '状态：0-未开始 1-进行中 2-已结束',
    create_time          datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time          datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间'
)
    comment '商家活动表';

create index idx_merchant_id
    on merchant_activity (merchant_base_id);

create index idx_status
    on merchant_activity (status);

create index idx_time_range
    on merchant_activity (start_time, end_time);

