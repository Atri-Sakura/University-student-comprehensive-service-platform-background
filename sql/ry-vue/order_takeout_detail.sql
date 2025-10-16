create table order_takeout_detail
(
    order_takeout_detail_id bigint         not null comment '明细唯一ID'
        primary key,
    order_main_id           bigint         not null comment '关联订单ID',
    merchant_id             bigint         not null comment '商家ID（关联merchant_db.merchant_base.merchant_base_id）',
    merchant_name           varchar(100)   not null comment '商家名称(冗余)',
    goods_id                bigint         not null comment '商品ID（关联merchant_db.merchant_goods.merchant_goods_id）',
    goods_name              varchar(100)   not null comment '商品名称(冗余)',
    goods_price             decimal(10, 2) not null comment '商品单价',
    quantity                int            not null comment '购买数量',
    subtotal                decimal(10, 2) not null comment '小计金额',
    goods_spec              varchar(100)   null comment '商品规格（如“中杯/少糖”）',
    goods_tags              varchar(200)   null comment '商品标签(冗余，如“甜口/冰饮”，用于推荐)'
)
    comment '外卖订单明细表（不含地址信息）';

create index idx_goods_id
    on order_takeout_detail (goods_id);

create index idx_merchant_id
    on order_takeout_detail (merchant_id);

create index idx_order_id
    on order_takeout_detail (order_main_id);

