create table secondhand_goods
(
    secondhand_goods_id bigint auto_increment comment '二手商品唯一ID'
        primary key,
    user_base_id        bigint                             not null comment '发布用户ID(关联user_base.user_base_id)',
    goods_name          varchar(100)                       not null comment '商品名称',
    category            varchar(50)                        not null comment '商品分类(如数码产品/图书教材/服饰鞋包/生活用品/运动健身/美妆个护)',
    price               decimal(10, 2)                     not null comment '售价/估价',
    description         text                               null comment '商品描述(详细说明)',
    status              tinyint  default 1                 not null comment '商品状态:0-已下架 1-在售中 2-已售出 3-已预定',
    view_count          int      default 0                 not null comment '浏览次数',
    favorite_count      int      default 0                 not null comment '收藏次数',
    share_count         int      default 0                 not null comment '分享次数',
    create_time         datetime default CURRENT_TIMESTAMP not null comment '发布时间',
    update_time         datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    sold_time           datetime                           null comment '售出时间',
    constraint fk_secondhand_user
        foreign key (user_base_id) references user_base (user_base_id)
            on delete cascade
)
    comment '二手商品表(简化版)';

create index idx_category
    on secondhand_goods (category)
    comment '分类索引(支持分类筛选)';

create index idx_category_status_time
    on secondhand_goods (category asc, status asc, create_time desc)
    comment '复合索引(分类+状态+时间)';

create index idx_create_time
    on secondhand_goods (create_time desc)
    comment '发布时间索引(最新商品排序)';

create index idx_price
    on secondhand_goods (price)
    comment '价格索引(支持价格区间查询)';

create index idx_status
    on secondhand_goods (status)
    comment '状态索引(过滤已售商品)';

create index idx_user_id
    on secondhand_goods (user_base_id)
    comment '用户ID索引';

