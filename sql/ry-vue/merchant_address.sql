create table merchant_address
(
    merchant_address_id bigint                             not null comment '地址ID'
        primary key,
    merchant_base_id    bigint                             not null comment '商家ID',
    province            varchar(20)                        not null comment '省份',
    city                varchar(20)                        not null comment '城市',
    district            varchar(20)                        not null comment '区县',
    detail_address      varchar(255)                       not null comment '详细地址',
    contact_person      varchar(20)                        not null comment '联系人',
    contact_phone       varchar(20)                        not null comment '联系电话',
    create_time         datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time         datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_merchant_id
        unique (merchant_base_id)
)
    comment '商家地址表';

create index idx_district
    on merchant_address (district);

