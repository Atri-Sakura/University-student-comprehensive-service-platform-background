create table goods_evaluation_image
(
    goods_evaluation_image_id bigint                             not null comment '图片ID'
        primary key,
    goods_evaluation_id       bigint                             not null comment '关联评价ID',
    image_url                 varchar(255)                       not null comment '图片URL',
    sort_order                int      default 0                 not null comment '排序序号',
    create_time               datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    constraint fk_eval_image_evaluation
        foreign key (goods_evaluation_id) references goods_evaluation (goods_evaluation_id)
            on delete cascade
)
    comment '商品评价图片表';

create index idx_evaluation_id
    on goods_evaluation_image (goods_evaluation_id);

