create table goods_evaluation
(
    goods_evaluation_id bigint                             not null comment '评价唯一ID'
        primary key,
    merchant_goods_id   bigint                             not null comment '商品ID',
    merchant_base_id    bigint                             not null comment '商家ID',
    user_id             bigint                             not null comment '评价用户ID',
    order_id            bigint                             null comment '关联订单ID',
    order_item_id       bigint                             null comment '关联订单项ID',
    rating              tinyint                            not null comment '商品评分(1-5分)',
    content             text                               null comment '评价内容',
    is_anonymous        tinyint  default 0                 not null comment '是否匿名：0-否 1-是',
    merchant_reply      text                               null comment '商家回复',
    create_time         datetime default CURRENT_TIMESTAMP not null comment '评价时间',
    reply_time          datetime                           null comment '回复时间',
    useful_count        int      default 0                 not null comment '有用数（点赞数）',
    constraint fk_goods_evaluation_goods
        foreign key (merchant_goods_id) references merchant_goods (merchant_goods_id)
            on delete cascade,
    constraint fk_goods_evaluation_merchant
        foreign key (merchant_base_id) references merchant_base (merchant_base_id)
            on delete cascade
)
    comment '商品评价表';

create index idx_create_time
    on goods_evaluation (create_time);

create index idx_goods_id
    on goods_evaluation (merchant_goods_id);

create index idx_merchant_id
    on goods_evaluation (merchant_base_id);

create index idx_rating
    on goods_evaluation (rating);

create index idx_user_id
    on goods_evaluation (user_id);

