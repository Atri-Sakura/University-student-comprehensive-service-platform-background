create table platform_role_mapping
(
    platform_role_mapping_id bigint                             not null comment '主键ID'
        primary key,
    username                 varchar(50)                        not null comment '登录账号（各角色的username字段）',
    role_type                tinyint                            not null comment '角色类型：1-学生用户 2-骑手 3-商家 4-平台管理员',
    target_db                varchar(20)                        not null comment '目标数据库：user_db/rider_db/merchant_db/platform_db',
    target_table             varchar(50)                        not null comment '目标表：user_base/rider_base/merchant_base/platform_admin',
    account_status           tinyint  default 1                 not null comment '账号全局状态：0-禁用 1-正常',
    create_time              datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time              datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_username_role
        unique (username, role_type)
)
    comment '角色-账号映射表（多角色登录路由核心）';

create index idx_username
    on platform_role_mapping (username);

