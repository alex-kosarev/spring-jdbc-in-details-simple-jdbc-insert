create schema if not exists sandbox;

create sequence sandbox.t_todo_id_seq;

create table sandbox.t_todo
(
    id           integer            default next value for sandbox.t_todo_id_seq primary key,
    c_title      varchar(250),
    c_details    text,
    c_created_at timestamp not null default now()
);