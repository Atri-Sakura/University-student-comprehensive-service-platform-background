create table chat_message
(
    message_id   bigint                             not null comment '消息唯一ID（雪花算法生成，全局唯一）'
        primary key,
    session_id   bigint                             not null comment '所属会话ID（关联chat_session.session_id，聚合同一会话的消息）',
    from_type    tinyint                            not null comment '发送方类型：1-用户 2-骑手 3-商家 4-系统',
    from_id      bigint                             not null comment '发送方ID（系统消息from_id固定为0）',
    to_type      tinyint                            not null comment '接收方类型：1-用户 2-骑手 3-商家',
    to_id        bigint                             not null comment '接收方ID',
    msg_type     tinyint                            not null comment '消息类型：1-文本 2-图片 3-语音 4-系统通知',
    msg_content  text                               null comment '消息内容：文本消息存内容；图片/语音存MinIO的URL；系统通知存模板内容',
    msg_status   tinyint  default 0                 not null comment '消息状态：0-发送中 1-已送达 2-已读 3-已撤回 4-发送失败',
    send_time    datetime default CURRENT_TIMESTAMP not null comment '消息发送时间',
    deliver_time datetime                           null comment '消息送达时间（仅用于需确认送达的场景）',
    read_time    datetime                           null comment '消息已读时间（接收方点击后更新）',
    is_deleted   tinyint  default 0                 not null comment '是否删除（软删除）：0-未删除 1-已删除（仅对删除方隐藏）',
    create_time  datetime default CURRENT_TIMESTAMP not null,
    update_time  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    comment '聊天消息表（存储单条消息的核心信息）';

create index idx_from
    on chat_message (from_type, from_id);

create index idx_send_time
    on chat_message (send_time);

create index idx_session_sendtime
    on chat_message (session_id, send_time);

create index idx_session_status
    on chat_message (session_id, msg_status);

create index idx_to
    on chat_message (to_type, to_id);

