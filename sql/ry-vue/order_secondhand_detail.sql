create table order_secondhand_detail
(
    order_secondhand_detail_id bigint            not null comment '明细唯一ID'
        primary key,
    order_main_id              bigint            not null comment '关联订单ID',
    goods_id                   bigint            not null comment '二手商品ID',
    goods_name                 varchar(100)      not null comment '商品名称(冗余)',
    seller_id                  bigint            not null comment '卖家ID（关联user_db.user_base.user_base_id）',
    sell_way                   tinyint           not null comment '交易方式：1-线上 2-线下',
    seller_nickname            varchar(50)       not null comment '卖家昵称(冗余)',
    deposit_amount             decimal(10, 2)    not null comment '担保金金额',
    confirm_time               datetime          null comment '买家确认收货时间',
    evaluate_status            tinyint default 0 not null comment '评价状态：0-未评价 1-已评价'
)
    comment '二手交易订单明细表（不含地址信息）';

create index idx_goods_id
    on order_secondhand_detail (goods_id);

create index idx_order_id
    on order_secondhand_detail (order_main_id);

create index idx_seller_id
    on order_secondhand_detail (seller_id);

