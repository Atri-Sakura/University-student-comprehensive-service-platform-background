create table platform_workorder
(
    platform_workorder_id bigint                             not null comment '工单唯一ID'
        primary key,
    order_id              bigint                             null comment '关联订单ID（来自order_db）',
    user_id               bigint                             not null comment '提交用户ID（来自user/rider/merchant_db）',
    user_type             tinyint                            not null comment '用户类型：1-学生 2-骑手 3-商家',
    user_nickname         varchar(50)                        not null comment '用户昵称（冗余）',
    content               text                               not null comment '工单内容（问题描述）',
    img_urls              varchar(1000)                      null comment '问题图片URL（逗号分隔）',
    workorder_type        tinyint                            not null comment '工单类型：1-订单投诉 2-服务差评 3-系统故障 4-其他',
    handler_id            bigint                             null comment '处理人ID（关联platform_admin.platform_admin_id）',
    handler_name          varchar(20)                        null comment '处理人姓名（冗余）',
    handle_status         tinyint  default 0                 not null comment '处理状态：0-待处理 1-处理中 2-已解决 3-已关闭',
    handle_result         text                               null comment '处理结果',
    create_time           datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    handle_time           datetime                           null comment '处理时间',
    close_time            datetime                           null comment '关闭时间'
)
    comment '客服工单表';

create index idx_create_time
    on platform_workorder (create_time);

create index idx_handle_status
    on platform_workorder (handle_status);

create index idx_order_id
    on platform_workorder (order_id);

create index idx_user_id_type
    on platform_workorder (user_id, user_type);

