create table order_coupon
(
    order_coupon_id bigint                             not null comment 'ID'
        primary key,
    order_main_id   bigint                             not null comment '订单ID',
    coupon_id       bigint                             not null comment '优惠券ID',
    coupon_name     varchar(100)                       not null comment '优惠券名称',
    discount_amount decimal(10, 2)                     not null comment '优惠金额',
    create_time     datetime default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '订单优惠券表';

create index idx_coupon_id
    on order_coupon (coupon_id);

create index idx_order_id
    on order_coupon (order_main_id);

