create table rider_order_rel
(
    rider_order_rel_id bigint                             not null comment '关联记录唯一ID'
        primary key,
    rider_base_id      bigint                             not null comment '骑手ID',
    order_id           bigint                             not null comment '关联订单ID',
    order_type         tinyint                            not null comment '订单类型：1-外卖单 2-跑腿单',
    receive_time       datetime                           not null comment '接单时间',
    pick_up_time       datetime                           null comment '取货时间',
    deliver_time       datetime                           null comment '送达时间',
    delivery_status    tinyint                            not null comment '配送状态：1-待取货 2-配送中 3-已送达 4-异常取消',
    abnormal_reason    varchar(255)                       null comment '异常原因',
    create_time        datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time        datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    constraint uk_order_id
        unique (order_id)
)
    comment '骑手接单关联表';

create index idx_delivery_status
    on rider_order_rel (delivery_status);

create index idx_receive_time
    on rider_order_rel (receive_time);

create index idx_rider_id
    on rider_order_rel (rider_base_id);

