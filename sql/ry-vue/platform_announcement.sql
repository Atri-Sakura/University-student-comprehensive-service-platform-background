create table platform_announcement
(
    platform_announcement_id bigint                             not null comment '公告唯一ID'
        primary key,
    title                    varchar(100)                       not null comment '公告标题',
    content                  text                               not null comment '公告内容',
    publisher_id             bigint                             not null comment '发布人ID（关联platform_admin.platform_admin_id）',
    publisher_name           varchar(20)                        not null comment '发布人姓名（冗余）',
    publish_time             datetime                           not null comment '发布时间',
    status                   tinyint  default 0                 not null comment '状态：0-草稿 1-已发布 2-已下架',
    read_count               int      default 0                 not null comment '阅读量',
    is_top                   tinyint  default 0                 not null comment '是否置顶：0-否 1-是',
    create_time              datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time              datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间'
)
    comment '系统公告表';

create index idx_publish_time
    on platform_announcement (publish_time);

create index idx_status_top
    on platform_announcement (status, is_top);

