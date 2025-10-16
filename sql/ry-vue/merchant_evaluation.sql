create table merchant_evaluation
(
    merchant_evaluation_id bigint                             not null comment '评价唯一ID'
        primary key,
    merchant_base_id       bigint                             not null comment '所属商家ID',
    user_id                bigint                             not null comment '评价用户ID',
    order_id               bigint                             null comment '关联订单ID',
    rating                 tinyint                            not null comment '评分(1-5分)',
    taste_score            tinyint                            null comment '口味评分(1-5分，仅餐饮类)',
    package_score          tinyint                            null comment '包装评分(1-5分)',
    content                text                               null comment '评价内容',
    img_urls               varchar(1000)                      null comment '评价图片URL(逗号分隔)',
    merchant_reply         text                               null comment '商家回复',
    create_time            datetime default CURRENT_TIMESTAMP not null comment '评价时间',
    reply_time             datetime                           null comment '回复时间'
)
    comment '商家评价表';

create index idx_create_time
    on merchant_evaluation (create_time);

create index idx_merchant_id
    on merchant_evaluation (merchant_base_id);

create index idx_rating
    on merchant_evaluation (rating);

create index idx_user_id
    on merchant_evaluation (user_id);

