create table merchant_wallet
(
    merchant_wallet_id bigint                                   not null comment '钱包唯一ID'
        primary key,
    merchant_base_id   bigint                                   not null comment '所属商家ID',
    balance            decimal(10, 2) default 0.00              not null comment '可用余额',
    freeze_amount      decimal(10, 2) default 0.00              not null comment '冻结金额',
    create_time        datetime       default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time        datetime       default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    constraint uk_merchant_id
        unique (merchant_base_id)
)
    comment '商家钱包表';

