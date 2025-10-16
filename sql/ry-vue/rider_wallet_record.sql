create table rider_wallet_record
(
    rider_wallet_record_id bigint         not null comment '流水唯一ID'
        primary key,
    rider_wallet_id        bigint         not null comment '所属钱包ID',
    rider_base_id          bigint         not null comment '所属骑手ID',
    amount                 decimal(10, 2) not null comment '金额(正数=收入，负数=支出)',
    trade_type             tinyint        not null comment '交易类型：1-配送收入 2-提现 3-违规扣款 4-平台补贴',
    related_id             bigint         null comment '关联业务ID',
    trade_status           tinyint        not null comment '交易状态：0-处理中 1-成功 2-失败',
    trade_time             datetime       not null comment '交易时间',
    remark                 varchar(100)   null comment '备注'
)
    comment '骑手钱包流水表';

create index idx_rider_id
    on rider_wallet_record (rider_base_id);

create index idx_trade_time
    on rider_wallet_record (trade_time);

create index idx_wallet_id
    on rider_wallet_record (rider_wallet_id);

