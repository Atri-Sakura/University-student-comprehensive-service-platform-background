create table secondhand_goods_image
(
    secondhand_goods_image_id bigint auto_increment comment '图片唯一ID'
        primary key,
    secondhand_goods_id       bigint                             not null comment '关联商品ID',
    image_url                 varchar(255)                       not null comment '图片URL(建议使用OSS存储)',
    is_main                   tinyint  default 0                 not null comment '是否主图:0-否 1-是(每个商品仅一张主图)',
    sort_order                int      default 0                 not null comment '排序序号(升序排列)',
    create_time               datetime default CURRENT_TIMESTAMP not null comment '上传时间',
    constraint fk_image_goods
        foreign key (secondhand_goods_id) references secondhand_goods (secondhand_goods_id)
            on delete cascade
)
    comment '二手商品图片表(支持1-9张图片)';

create index idx_goods_id
    on secondhand_goods_image (secondhand_goods_id)
    comment '商品ID索引';

create index idx_goods_main
    on secondhand_goods_image (secondhand_goods_id, is_main)
    comment '商品+主图索引(快速查询主图)';

create index idx_goods_sort
    on secondhand_goods_image (secondhand_goods_id, sort_order)
    comment '商品+排序索引(图片排序)';

