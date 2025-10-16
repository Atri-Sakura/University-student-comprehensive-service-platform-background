create table rider_location
(
    rider_location_id bigint         not null comment '位置记录唯一ID'
        primary key,
    rider_base_id     bigint         not null comment '所属骑手ID',
    longitude         decimal(10, 6) not null comment '当前经度',
    latitude          decimal(10, 6) not null comment '当前纬度',
    update_time       datetime       not null comment '位置更新时间',
    constraint uk_rider_id
        unique (rider_base_id)
)
    comment '骑手位置表';

create index idx_update_time
    on rider_location (update_time);

