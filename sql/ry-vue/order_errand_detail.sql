create table order_errand_detail
(
    order_errand_detail_id bigint                      not null comment '明细唯一ID'
        primary key,
    order_main_id          bigint                      not null comment '关联订单ID',
    errand_type            tinyint                     not null comment '跑腿类型：1-帮我送 2-帮我买',
    goods_desc             varchar(255)                not null comment '物品/商品描述（如“生日蛋糕/6寸”“ textbooks/高等数学”）',
    expected_time          datetime                    null comment '期望送达时间',
    advance_amount         decimal(10, 2) default 0.00 null comment '骑手垫付金额（帮我买场景专用）',
    tip_amount             decimal(10, 2) default 0.00 null comment '小费金额',
    buy_photo_url          varchar(255)                null comment '代付凭证'
)
    comment '跑腿订单明细表（不含地址信息）';

create index idx_errand_type
    on order_errand_detail (errand_type);

create index idx_order_id
    on order_errand_detail (order_main_id);

