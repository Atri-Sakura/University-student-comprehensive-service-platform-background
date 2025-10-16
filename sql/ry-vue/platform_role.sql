create table platform_role
(
    platform_role_id bigint                             not null comment '角色唯一ID'
        primary key,
    role_name        varchar(50)                        not null comment '角色名称（如超级管理员/运营专员）',
    role_code        varchar(50)                        not null comment '角色编码（唯一标识，如ADMIN/OPERATOR）',
    role_desc        varchar(255)                       null comment '角色描述',
    create_time      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    constraint uk_role_code
        unique (role_code),
    constraint uk_role_name
        unique (role_name)
)
    comment '角色表';

