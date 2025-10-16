create table user_privacy
(
    user_privacy_id    bigint                             not null comment '设置唯一ID'
        primary key,
    user_base_id       bigint                             not null comment '所属用户ID',
    is_recommend       tinyint  default 1                 not null comment '个性化推荐：0-关闭 1-开启',
    is_location_permit tinyint  default 1                 not null comment '位置权限：0-关闭 1-开启',
    update_time        datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    constraint uk_user_id
        unique (user_base_id)
)
    comment '用户隐私设置表';

