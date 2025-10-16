create table user_address
(
    user_address_id bigint                             not null comment '地址唯一ID'
        primary key,
    user_base_id    bigint                             not null comment '所属用户ID（关联user_base.user_base_id）',
    receiver        varchar(20)                        not null comment '收货人姓名',
    phone           varchar(20)                        not null comment '收货人电话（AES加密）',
    province        varchar(20)                        not null comment '省份',
    city            varchar(20)                        not null comment '城市',
    district        varchar(20)                        not null comment '区县',
    detail_address  varchar(255)                       not null comment '详细地址（如XX宿舍3栋201）',
    address_tag     varchar(30)                        null comment '地址标签（如DORM-宿舍/CLASSROOM-教室）',
    longitude       decimal(10, 6)                     null comment '经度',
    latitude        decimal(10, 6)                     null comment '纬度',
    is_default      tinyint  default 0                 not null comment '是否默认地址：0-否 1-是',
    create_time     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '用户地址表';

create index idx_address_tag
    on user_address (address_tag)
    comment '地址标签索引';

create index idx_lon_lat
    on user_address (longitude, latitude)
    comment '经纬度索引';

create index idx_user_default
    on user_address (user_base_id, is_default)
    comment '用户+默认地址索引';

