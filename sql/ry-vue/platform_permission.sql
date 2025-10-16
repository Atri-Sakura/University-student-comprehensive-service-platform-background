create table platform_permission
(
    platform_permission_id bigint                             not null comment '权限唯一ID'
        primary key,
    perm_name              varchar(50)                        not null comment '权限名称（如订单管理/商家审核）',
    perm_key               varchar(100)                       not null comment '权限标识（如order:manage/merchant:audit）',
    perm_type              tinyint                            not null comment '权限类型：1-菜单 2-按钮',
    parent_perm_id         bigint                             null comment '父权限ID（用于构建权限树，关联platform_permission.platform_permission_id）',
    menu_path              varchar(100)                       null comment '菜单路径（仅perm_type=1时有值）',
    sort                   int      default 0                 null comment '排序序号（值越小越靠前）',
    create_time            datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time            datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    constraint uk_perm_key
        unique (perm_key)
)
    comment '权限表';

create index idx_parent_id
    on platform_permission (parent_perm_id);

