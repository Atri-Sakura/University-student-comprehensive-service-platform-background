create table rider_base
(
    rider_base_id  bigint                             not null comment '骑手唯一ID'
        primary key,
    username       varchar(50)                        not null comment '登录账号',
    password       varchar(100)                       not null comment '密码(BCrypt加密)',
    nickname       varchar(50)                        not null comment '骑手昵称',
    avatar         varchar(255)                       null comment '头像URL',
    real_name      varchar(20)                        not null comment '真实姓名',
    id_card        varchar(20)                        not null comment '身份证号(AES加密)',
    id_card_front  varchar(255)                       not null comment '身份证正面照URL',
    id_card_back   varchar(255)                       not null comment '身份证反面照URL',
    phone          varchar(20)                        not null comment '联系电话',
    audit_status   tinyint  default 0                 not null comment '审核状态：0-待审核 1-通过 2-拒绝',
    work_status    tinyint  default 0                 not null comment '工作状态：0-下线 1-上线 2-忙碌',
    credit_score   int      default 600               not null comment '服务信用分',
    account_status tinyint  default 1                 not null comment '账号状态：0-禁用 1-正常',
    create_time    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    constraint uk_id_card
        unique (id_card),
    constraint uk_phone
        unique (phone),
    constraint uk_username
        unique (username)
)
    comment '骑手基础信息表';

create index idx_audit_status
    on rider_base (audit_status);

create index idx_work_status
    on rider_base (work_status);

