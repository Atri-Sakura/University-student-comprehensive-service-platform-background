create table user_recommend_setting
(
    user_recommend_setting_id bigint                             not null comment '唯一ID'
        primary key,
    user_base_id              bigint                             not null comment '所属用户ID（关联user_base.user_base_id）',
    is_recommend_enabled      tinyint  default 1                 not null comment '是否开启个性化推荐：0-关闭 1-开启',
    recommend_freq            tinyint  default 2                 not null comment '推荐频率：1-高频 2-中频 3-低频',
    shielded_tag_codes        varchar(500)                       null comment '屏蔽的标签编码（逗号分隔）',
    preferred_scene           varchar(50)                        null comment '偏好推荐场景（逗号分隔）',
    update_time               datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '设置更新时间',
    create_time               datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    constraint uk_user_id
        unique (user_base_id) comment '用户ID唯一（一对一关联）'
)
    comment '用户个性化推荐设置表';

create index idx_recommend_enabled
    on user_recommend_setting (is_recommend_enabled)
    comment '推荐开启状态索引';

