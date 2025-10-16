create table user_wallet_record
(
    user_wallet_record_id bigint         not null comment '流水唯一ID'
        primary key,
    user_wallet_id        bigint         not null comment '所属钱包ID',
    user_base_id          bigint         not null comment '所属用户ID',
    amount                decimal(10, 2) not null comment '金额(正数=收入，负数=支出)',
    trade_type            tinyint        not null comment '交易类型：1-充值 2-提现 3-外卖支付 4-跑腿支付 5-退款',
    related_id            bigint         null comment '关联订单ID',
    trade_status          tinyint        not null comment '交易状态：0-处理中 1-成功 2-失败',
    trade_time            datetime       not null comment '交易时间',
    remark                varchar(100)   null comment '备注'
)
    comment '用户钱包流水表';

create index idx_trade_time
    on user_wallet_record (trade_time);

create index idx_user_id
    on user_wallet_record (user_base_id);

create index idx_wallet_id
    on user_wallet_record (user_wallet_id);

