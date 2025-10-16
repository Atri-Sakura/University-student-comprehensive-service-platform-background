create table chat_session
(
    session_id       bigint                             not null comment '会话唯一ID（雪花算法生成，全局唯一）'
        primary key,
    from_type        tinyint                            not null comment '发送方类型：1-用户 2-骑手 3-商家',
    from_id          bigint                             not null comment '发送方ID（关联user_db.user_id/ridder_db.ridder_id/merchant_db.merchant_base_id）',
    to_type          tinyint                            not null comment '接收方类型：1-用户 2-骑手 3-商家',
    to_id            bigint                             not null comment '接收方ID（关联对应业务库的主键）',
    last_msg_id      bigint                             null comment '最后一条消息的ID（关联chat_message.message_id）',
    last_msg_content varchar(500)                       null comment '最后一条消息内容（冗余，用于会话列表快速展示）',
    last_msg_type    tinyint                            null comment '最后一条消息类型：1-文本 2-图片 3-语音 4-系统通知',
    last_msg_time    datetime                           null comment '最后一条消息发送时间',
    unread_count     int      default 0                 not null comment '未读消息数（接收方视角，如用户A的会话中未读数量）',
    session_status   tinyint  default 1                 not null comment '会话状态：0-已删除 1-正常 2-已屏蔽',
    create_time      datetime default CURRENT_TIMESTAMP not null comment '会话创建时间（首次发消息时生成）',
    update_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '会话更新时间（最后一条消息发送/状态变更时更新）',
    constraint uk_from_to
        unique (from_type, from_id, to_type, to_id)
)
    comment '聊天会话表（管理双方的聊天窗口关系）';

create index idx_from
    on chat_session (from_type, from_id);

create index idx_last_msg_time
    on chat_session (last_msg_time);

create index idx_to
    on chat_session (to_type, to_id);

