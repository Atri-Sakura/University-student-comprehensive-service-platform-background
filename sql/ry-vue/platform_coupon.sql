create table platform_coupon
(
    platform_coupon_id bigint                                   not null comment '优惠券ID'
        primary key,
    coupon_no          varchar(50)                              not null comment '优惠券编号',
    coupon_name        varchar(100)                             not null comment '优惠券名称',
    coupon_type        tinyint                                  not null comment '类型：1-满减券 2-折扣券 3-固定金额券',
    face_value         decimal(10, 2)                           not null comment '面值',
    min_spend          decimal(10, 2) default 0.00              null comment '最低消费金额',
    discount           decimal(3, 2)                            null comment '折扣率（如0.85=85折）',
    start_time         datetime                                 not null comment '开始时间',
    end_time           datetime                                 not null comment '结束时间',
    total_count        int                                      not null comment '总发行量',
    remain_count       int                                      not null comment '剩余数量',
    status             tinyint        default 0                 not null comment '状态：0-未发布 1-已发布 2-已过期',
    create_time        datetime       default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time        datetime       default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_coupon_no
        unique (coupon_no)
)
    comment '平台优惠券表';

create index idx_coupon_type
    on platform_coupon (coupon_type);

create index idx_status_time
    on platform_coupon (status, start_time, end_time);

