create table merchant_goods_image
(
    merchant_goods_image_id bigint                             not null comment '图片ID'
        primary key,
    merchant_goods_id       bigint                             not null comment '关联商品ID',
    image_url               varchar(255)                       not null comment '图片URL',
    image_desc              varchar(100)                       null comment '图片描述（如"商品正面图"）',
    sort_order              int      default 0                 not null comment '排序序号（值越小越靠前）',
    is_main                 tinyint  default 0                 not null comment '是否主图：0-否 1-是',
    create_time             datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time             datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint fk_image_goods_merchant
        foreign key (merchant_goods_id) references merchant_goods (merchant_goods_id)
            on delete cascade
)
    comment '商品图片关联表（支持多图展示）';

create index idx_goods_id
    on merchant_goods_image (merchant_goods_id);

create index idx_goods_main
    on merchant_goods_image (merchant_goods_id, is_main);

