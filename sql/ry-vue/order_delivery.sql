create table order_delivery
(
    order_delivery_id        bigint            not null comment '配送记录ID'
        primary key,
    order_main_id            bigint            not null comment '订单ID',
    rider_id                 bigint            null comment '骑手ID',
    rider_nickname           varchar(50)       null comment '骑手昵称(冗余)',
    delivery_fee             decimal(10, 2)    not null comment '配送费（可基于主表取货-送货坐标计算）',
    actual_pick_longitude    decimal(11, 8)    null comment '实际取货经度',
    actual_pick_latitude     decimal(10, 8)    null comment '实际取货纬度',
    actual_deliver_longitude decimal(11, 8)    null comment '实际送达经度',
    actual_deliver_latitude  decimal(10, 8)    null comment '实际送达纬度',
    assign_time              datetime          null comment '派单时间',
    receive_time             datetime          null comment '接单时间',
    pick_time                datetime          null comment '取货时间',
    deliver_time             datetime          null comment '送达时间',
    delivery_status          tinyint default 0 not null comment '配送状态：0-待分配 1-已接单 2-已取货 3-已送达',
    constraint uk_order_id
        unique (order_main_id)
)
    comment '订单配送表（含实际配送定位）';

create index idx_actual_pick_location
    on order_delivery (actual_pick_longitude, actual_pick_latitude);

create index idx_delivery_status
    on order_delivery (delivery_status);

create index idx_rider_id
    on order_delivery (rider_id);

