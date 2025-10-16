create table user_behavior_log
(
    user_behavior_log_id bigint        not null comment '唯一ID'
        primary key,
    user_base_id         bigint        not null comment '所属用户ID（关联user_base.user_base_id）',
    behavior_type        tinyint       not null comment '行为类型：1-浏览商品 2-收藏商品 3-加入购物车 4-下单购买 5-取消订单 6-评价商品',
    target_id            bigint        not null comment '行为对象ID（如商品ID=123/商家ID=45）',
    target_type          tinyint       not null comment '对象类型：1-商品 2-商家 3-订单 4-活动',
    target_name          varchar(100)  null comment '对象名称（冗余，如"珍珠奶茶"）',
    behavior_time        datetime      not null comment '行为发生时间',
    device               varchar(30)   null comment '行为设备（如APP/小程序/H5）',
    scene                varchar(30)   null comment '行为场景（如HOME-首页/SEARCH-搜索页）',
    duration             int default 0 null comment '停留时长（秒，仅behavior_type=1时有效）',
    extra                varchar(500)  null comment '额外信息（如搜索关键词"平价奶茶"）'
)
    comment '用户行为记录表';

create index idx_behavior_time
    on user_behavior_log (behavior_time)
    comment '行为时间索引';

create index idx_target_id_type
    on user_behavior_log (target_id, target_type)
    comment '对象ID+类型索引';

create index idx_user_behavior_time
    on user_behavior_log (user_base_id, behavior_type, behavior_time)
    comment '用户+行为类型+时间索引';

