create table user_base
(
    user_base_id    bigint                             not null comment '用户唯一ID（雪花算法）'
        primary key,
    username        varchar(50)                        not null comment '登录账号（唯一）',
    password        varchar(100)                       not null comment '密码（BCrypt加密）',
    nickname        varchar(50)                        not null comment '用户昵称',
    avatar          varchar(255)                       null comment '头像URL',
    student_id      varchar(20)                        not null comment '学号（唯一）',
    college         varchar(50)                        not null comment '所属学院（如计算机学院）',
    major           varchar(50)                        not null comment '所属专业（如软件工程）',
    grade           varchar(20)                        not null comment '年级（如2022级）',
    gender          tinyint                            null comment '性别：1-男 2-女 0-未知',
    phone           varchar(20)                        not null comment '联系电话（AES加密）',
    credit_score    int      default 600               not null comment '信用分（影响推荐优先级）',
    account_status  tinyint  default 1                 not null comment '账号状态：0-禁用 1-正常',
    last_login_time datetime                           null comment '最后登录时间',
    last_login_ip   varchar(50)                        null comment '最后登录IP',
    create_time     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_student_id
        unique (student_id) comment '学号唯一',
    constraint uk_username
        unique (username) comment '登录账号唯一'
)
    comment '用户基础信息表';

create index idx_account_status
    on user_base (account_status)
    comment '账号状态索引（过滤禁用用户）';

create index idx_college_grade
    on user_base (college, grade)
    comment '学院+年级索引（推荐同群体热门商品）';

