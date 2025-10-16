create table merchant_goods
(
    merchant_goods_id bigint                                  not null comment '商品唯一ID'
        primary key,
    merchant_base_id  bigint                                  not null comment '所属商家ID',
    goods_name        varchar(100)                            not null comment '商品名称',
    category          varchar(50)                             not null comment '商品分类',
    sub_category      varchar(50)                             null comment '商品子分类',
    price             decimal(10, 2)                          not null comment '单价',
    original_price    decimal(10, 2)                          null comment '原价',
    stock             int           default 0                 not null comment '库存',
    sales_count       bigint        default 0                 not null comment '销量',
    description       text                                    null comment '商品描述',
    tag_codes         varchar(500)                            null comment '商品标签编码（逗号分隔，如FOOD_SPICY,FAST_FOOD）',
    status            tinyint       default 1                 not null comment '状态：0-下架 1-上架',
    avg_rating        decimal(3, 2) default 4.00              not null comment '商品平均评分',
    rating_count      int           default 0                 not null comment '评分总次数',
    five_star_rate    decimal(5, 2) default 0.00              null comment '五星好评率(%)',
    four_star_rate    decimal(5, 2) default 0.00              null comment '四星好评率(%)',
    three_star_rate   decimal(5, 2) default 0.00              null comment '三星评价率(%)',
    two_star_rate     decimal(5, 2) default 0.00              null comment '二星评价率(%)',
    one_star_rate     decimal(5, 2) default 0.00              null comment '一星差评率(%)',
    create_time       datetime      default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time       datetime      default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间'
)
    comment '商品表';

create index idx_avg_rating
    on merchant_goods (avg_rating);

create index idx_category
    on merchant_goods (category);

create index idx_merchant_id
    on merchant_goods (merchant_base_id);

create index idx_sales_count
    on merchant_goods (sales_count);

create index idx_status
    on merchant_goods (status);

create index idx_tag_codes
    on merchant_goods (tag_codes);

