create table order_main
(
    order_main_id      bigint                                   not null comment '订单唯一ID'
        primary key,
    order_no           varchar(32)                              not null comment '订单编号',
    user_id            bigint                                   not null comment '下单用户ID（关联user_db.user_base.user_base_id）',
    user_nickname      varchar(50)                              not null comment '用户昵称(冗余)',
    order_type         tinyint                                  not null comment '订单类型：1-外卖单 2-跑腿单 3-二手交易单',
    total_amount       decimal(10, 2)                           not null comment '订单总金额',
    pay_amount         decimal(10, 2)                           not null comment '实付金额',
    discount_amount    decimal(10, 2) default 0.00              not null comment '优惠金额',
    pay_status         tinyint        default 0                 not null comment '支付状态：0-未支付 1-已支付 2-退款中 3-已退款',
    pay_time           datetime                                 null comment '支付时间',
    pay_type           tinyint                                  null comment '支付方式：1-余额 2-微信 3-支付宝',
    order_status       tinyint        default 1                 not null comment '订单状态：1-待接单 2-待取货 3-配送中 4-已完成 5-已取消',
    cancel_reason      varchar(255)                             null comment '取消原因',
    cancel_operator    varchar(50)                              null comment '取消操作人',
    pick_address_id    bigint                                   not null comment '取货地址ID（外卖关联merchant_db.merchant_address.merchant_address_id；其他关联user_db.user_address.user_address_id）',
    pick_address       varchar(255)                             not null comment '取货地址文本（冗余，如“XX食堂3楼奶茶店”“XX宿舍2栋101”）',
    pick_contact       varchar(20)                              not null comment '取货联系人',
    pick_phone         varchar(20)                              not null comment '取货电话（AES加密，与user_db加密标准一致）',
    pick_longitude     decimal(11, 8)                           null comment '取货经度（定位功能填充，WGS84坐标系，精度1米内）',
    pick_latitude      decimal(10, 8)                           null comment '取货纬度（定位功能填充，WGS84坐标系，精度1米内）',
    deliver_address_id bigint                                   null comment '送货地址ID（关联user_db.user_address.user_address_id，线下二手单可空）',
    deliver_address    varchar(255)                             null comment '送货地址文本（冗余，如“XX教学楼503室”）',
    deliver_contact    varchar(20)                              null comment '收货联系人',
    deliver_phone      varchar(20)                              null comment '收货电话（AES加密，与user_db加密标准一致）',
    deliver_longitude  decimal(11, 8)                           null comment '送货经度（定位功能填充，WGS84坐标系）',
    deliver_latitude   decimal(10, 8)                           null comment '送货纬度（定位功能填充，WGS84坐标系）',
    create_time        datetime       default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time        datetime       default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    complete_time      datetime                                 null comment '完成时间',
    constraint uk_order_no
        unique (order_no)
)
    comment '订单主表（整合地址与定位信息）';

create index idx_create_time
    on order_main (create_time);

create index idx_deliver_address_id
    on order_main (deliver_address_id);

create index idx_deliver_location
    on order_main (deliver_longitude, deliver_latitude)
    comment '送货经纬度索引（配送范围校验）';

create index idx_order_status
    on order_main (order_status);

create index idx_order_type
    on order_main (order_type);

create index idx_pay_status
    on order_main (pay_status);

create index idx_pick_address_id
    on order_main (pick_address_id);

create index idx_pick_location
    on order_main (pick_longitude, pick_latitude)
    comment '取货经纬度索引（附近取货点查询）';

create index idx_user_id
    on order_main (user_id);

