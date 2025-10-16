create table chat_message_read
(
    read_id     bigint                             not null comment '已读记录唯一ID（雪花算法生成）'
        primary key,
    message_id  bigint                             not null comment '关联消息ID（关联chat_message.message_id）',
    reader_type tinyint                            not null comment '已读用户类型：1-用户 2-骑手 3-商家',
    reader_id   bigint                             not null comment '已读用户ID（谁已读这条消息）',
    read_status tinyint  default 0                 not null comment '已读状态：0-未读 1-已读',
    read_time   datetime                           null comment '已读时间',
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint uk_message_reader
        unique (message_id, reader_type, reader_id)
)
    comment '消息已读状态表（追踪每条消息的已读情况，支撑群聊扩展）';

create index idx_message_id
    on chat_message_read (message_id);

create index idx_reader_status
    on chat_message_read (reader_type, reader_id, read_status);

