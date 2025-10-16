create table merchant_base
(
    merchant_base_id    bigint                                  not null comment '商家唯一ID'
        primary key,
    username            varchar(50)                             not null comment '登录账号',
    password            varchar(100)                            not null comment '密码(BCrypt加密)',
    merchant_name       varchar(100)                            not null comment '商家名称',
    logo                varchar(255)                            null comment '商家Logo URL',
    merchant_address_id bigint                                  not null comment '店铺地址ID',
    business_scope      varchar(50)                             not null comment '经营范围',
    business_hours      varchar(100)                            not null comment '营业时间',
    delivery_range      decimal(5, 2)                           not null comment '配送范围(公里)',
    min_order_amount    decimal(10, 2)                          not null comment '起送金额',
    delivery_fee        decimal(10, 2)                          not null comment '基础配送费',
    license_img         varchar(255)                            not null comment '营业执照URL',
    rating              decimal(3, 2) default 4.00              not null comment '商家评分',
    month_sales         int           default 0                 not null comment '月销量',
    audit_status        tinyint       default 0                 not null comment '审核状态：0-待审核 1-通过 2-拒绝',
    business_status     tinyint       default 1                 not null comment '营业状态：0-停业 1-营业',
    longitude           decimal(10, 6)                          null comment '店铺经度',
    latitude            decimal(10, 6)                          null comment '店铺纬度',
    create_time         datetime      default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time         datetime      default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    constraint uk_merchant_name
        unique (merchant_name),
    constraint uk_username
        unique (username)
)
    comment '商家基础信息表';

create index idx_audit_status
    on merchant_base (audit_status);

create index idx_business_status
    on merchant_base (business_status);

create index idx_lon_lat
    on merchant_base (longitude, latitude);

create index idx_rating_sales
    on merchant_base (rating, month_sales);

