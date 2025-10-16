create table chat_attachment
(
    attachment_id   bigint                             not null comment '附件唯一ID（雪花算法生成）'
        primary key,
    message_id      bigint                             not null comment '关联消息ID（一条消息可对应多个附件，如多图发送）',
    attachment_type tinyint                            not null comment '附件类型：1-图片 2-语音',
    attachment_url  varchar(255)                       not null comment '附件存储URL（MinIO的访问地址，如http://minio:9000/chat-attach/202409/xxx.png）',
    file_name       varchar(100)                       null comment '原始文件名（如"IMG_2024.png"）',
    file_size       bigint                             not null comment '文件大小（字节，用于前端显示"2.5MB"）',
    file_ext        varchar(10)                        null comment '文件后缀（如"png""mp3"，便于筛选文件类型）',
    expire_time     datetime                           null comment '过期时间（null表示永久有效；如临时图片设为24小时后过期）',
    is_valid        tinyint  default 1                 not null comment '是否有效：0-无效（已删除/过期） 1-有效',
    create_time     datetime default CURRENT_TIMESTAMP not null,
    update_time     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    comment '消息附件表（存储图片/语音等附件的元信息）';

create index idx_attachment_type
    on chat_attachment (attachment_type);

create index idx_expire_time
    on chat_attachment (expire_time, is_valid);

create index idx_message_id
    on chat_attachment (message_id);

