create table user_preference_tag
(
    user_preference_tag_id bigint                             not null comment '唯一ID'
        primary key,
    user_base_id           bigint                             not null comment '所属用户ID（关联user_base.user_base_id）',
    tag_code               varchar(30)                        not null comment '标签编码（唯一标识，如FOOD_SPICY/STATIONERY）',
    tag_name               varchar(50)                        not null comment '标签名称（如"爱吃辣""文具刚需"）',
    tag_type               varchar(30)                        not null comment '标签类型（如FOOD-美食偏好/SHOPPING-购物偏好）',
    score                  int      default 10                not null comment '偏好分数（1-100，分数越高偏好越强）',
    source                 tinyint                            not null comment '标签来源：1-用户主动设置 2-系统行为分析 3-人工标注',
    create_time            datetime default CURRENT_TIMESTAMP not null comment '标签创建时间',
    update_time            datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '分数更新时间',
    constraint uk_user_tag
        unique (user_base_id, tag_code) comment '用户+标签编码唯一（避免重复标签）'
)
    comment '用户偏好标签表';

create index idx_tag_code_score
    on user_preference_tag (tag_code, score)
    comment '标签编码+分数索引（找高偏好该标签的用户）';

create index idx_user_tag_type
    on user_preference_tag (user_base_id, tag_type)
    comment '用户+标签类型索引（按类型查偏好）';

