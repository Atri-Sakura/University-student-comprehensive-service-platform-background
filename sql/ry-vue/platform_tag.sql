create table platform_tag
(
    platform_tag_id bigint                             not null comment '标签ID'
        primary key,
    tag_code        varchar(30)                        not null comment '标签编码（唯一）',
    tag_name        varchar(50)                        not null comment '标签名称',
    tag_type        varchar(30)                        not null comment '标签类型',
    tag_desc        varchar(255)                       null comment '标签描述',
    parent_code     varchar(30)                        null comment '父标签编码',
    status          tinyint  default 1                 not null comment '状态：0-禁用 1-启用',
    create_time     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_tag_code
        unique (tag_code)
)
    comment '平台标签体系表（管理用户和商品标签）';

create index idx_parent_code
    on platform_tag (parent_code);

create index idx_tag_type
    on platform_tag (tag_type);

