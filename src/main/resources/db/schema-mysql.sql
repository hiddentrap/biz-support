drop table if exists member;

create table member
(
    member_id             bigint auto_increment comment '회원아이디',
    member_type           varchar(10)  not null comment '회원타입',
    email                 varchar(50)  not null comment '이메일',
    password              varchar(200) null comment '패스워드',
    member_name           varchar(20)  not null comment '회원이름',
    profile               varchar(200) null comment '프로필',
    role                  varchar(10)  not null comment '역할',
    refresh_token         varchar(250) null comment '리프레시 토큰',
    token_expiration_time timestamp    null comment '토큰 만료 시간',
    created_by            varchar(255) null comment '생성자',
    create_time           timestamp    null comment '생성시간',
    modified_by           varchar(255) null comment '수정자',
    update_time           timestamp    null comment '수정시간',
    constraint member_pk
        primary key (member_id)
);