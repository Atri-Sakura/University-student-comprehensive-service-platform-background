create table platform_admin
(
    platform_admin_id bigint                             not null comment '管理员唯一ID（雪花算法）'
        primary key,
    username          varchar(50)                        not null comment '登录账号（唯一）',
    password          varchar(100)                       not null comment '密码（BCrypt加密存储）',
    real_name         varchar(20)                        not null comment '真实姓名',
    phone             varchar(20)                        not null comment '联系电话',
    platform_role_id  bigint                             not null comment '关联角色ID（关联platform_role.platform_role_id）',
    account_status    tinyint  default 1                 not null comment '账号状态：0-禁用 1-正常',
    last_login_time   datetime                           null comment '最后登录时间',
    last_login_ip     varchar(50)                        null comment '最后登录IP',
    create_time       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    constraint uk_username
        unique (username)
)
    comment '平台管理员表';

create index idx_account_status
    on platform_admin (account_status);

create index idx_role_id
    on platform_admin (platform_role_id);

