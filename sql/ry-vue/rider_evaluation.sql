create table rider_evaluation
(
    rider_evaluation_id bigint                             not null comment '评价唯一ID'
        primary key,
    rider_base_id       bigint                             not null comment '骑手ID',
    user_id             bigint                             not null comment '评价用户ID',
    order_id            bigint                             not null comment '关联订单ID',
    rating              tinyint                            not null comment '评分(1-5分)',
    speed_score         tinyint                            not null comment '速度评分(1-5分)',
    attitude_score      tinyint                            not null comment '态度评分(1-5分)',
    content             text                               null comment '评价内容',
    create_time         datetime default CURRENT_TIMESTAMP not null comment '评价时间'
)
    comment '骑手评价表';

create index idx_create_time
    on rider_evaluation (create_time);

create index idx_rating
    on rider_evaluation (rating);

create index idx_rider_id
    on rider_evaluation (rider_base_id);

