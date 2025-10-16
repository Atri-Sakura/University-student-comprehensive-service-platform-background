create table platform_role_perm
(
    platform_role_perm_id  bigint                             not null comment '关联唯一ID'
        primary key,
    platform_role_id       bigint                             not null comment '角色ID（关联platform_role.platform_role_id）',
    platform_permission_id bigint                             not null comment '权限ID（关联platform_permission.platform_permission_id）',
    create_time            datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    constraint uk_role_perm
        unique (platform_role_id, platform_permission_id),
    constraint fk_role_perm_perm
        foreign key (platform_permission_id) references platform_permission (platform_permission_id)
            on delete cascade,
    constraint fk_role_perm_role
        foreign key (platform_role_id) references platform_role (platform_role_id)
            on delete cascade
)
    comment '角色权限关联表';

create index idx_perm_id
    on platform_role_perm (platform_permission_id);

