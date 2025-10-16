create table platform_operate_log
(
    platform_operate_log_id bigint      not null comment '日志唯一ID'
        primary key,
    admin_id                bigint      not null comment '操作管理员ID（关联platform_admin.platform_admin_id）',
    admin_name              varchar(20) not null comment '管理员姓名（冗余）',
    oper_type               varchar(20) not null comment '操作类型（create/update/delete/audit）',
    oper_module             varchar(50) not null comment '操作模块（merchant/order/user/rider）',
    oper_content            text        not null comment '操作内容（如"审核商家ID=123通过"）',
    ip_address              varchar(50) not null comment '操作IP地址',
    oper_time               datetime    not null comment '操作时间',
    user_agent              text        null comment '用户代理信息（浏览器/设备）'
)
    comment '系统操作日志表';

create index idx_admin_id
    on platform_operate_log (admin_id);

create index idx_oper_module
    on platform_operate_log (oper_module);

create index idx_oper_time
    on platform_operate_log (oper_time);

