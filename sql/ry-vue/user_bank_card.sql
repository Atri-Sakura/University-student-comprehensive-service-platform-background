create table user_bank_card
(
    id               bigint auto_increment comment '主键'
        primary key,
    user_base_id     bigint                             not null comment '所属用户ID',
    bank_name        varchar(50)                        not null comment '银行名称',
    bank_card_type   tinyint                            not null comment '卡类型：1-储蓄卡 2-信用卡',
    card_number      varchar(30)                        not null comment '银行卡号（加密存储）',
    card_tail_number varchar(10)                        not null comment '卡号尾号（冗余）',
    holder_name      varchar(50)                        not null comment '持卡人姓名',
    id_number        varchar(30)                        not null comment '身份证号（加密存储）',
    reserve_phone    varchar(20)                        not null comment '预留手机号（加密存储）',
    bind_status      tinyint  default 1                 not null comment '绑定状态：1-正常 0-已解绑',
    create_time      datetime default CURRENT_TIMESTAMP not null comment '绑定时间',
    update_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '用户银行卡绑定表';

create index idx_user_id
    on user_bank_card (user_base_id);

