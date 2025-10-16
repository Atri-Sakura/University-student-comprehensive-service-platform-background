create table order_pay_record
(
    order_pay_record_id bigint         not null comment '支付记录ID'
        primary key,
    order_main_id       bigint         not null comment '订单ID',
    pay_no              varchar(64)    null comment '支付单号',
    pay_amount          decimal(10, 2) not null comment '支付金额',
    pay_type            tinyint        not null comment '支付方式：1-余额 2-微信 3-支付宝',
    pay_status          tinyint        not null comment '支付状态：0-处理中 1-成功 2-失败',
    pay_time            datetime       null comment '支付时间',
    callback_data       text           null comment '支付回调数据'
)
    comment '订单支付记录表';

create index idx_order_id
    on order_pay_record (order_main_id);

create index idx_pay_no
    on order_pay_record (pay_no);

create index idx_pay_status
    on order_pay_record (pay_status);

