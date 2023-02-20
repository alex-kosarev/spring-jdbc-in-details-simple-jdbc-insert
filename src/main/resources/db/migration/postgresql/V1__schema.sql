create schema if not exists sandbox;

create sequence sandbox.t_todo_id_seq;

create table sandbox.t_todo
(
    id           integer primary key default nextval('sandbox.t_todo_id_seq'),
    c_title      varchar(250),
    c_details    text,
    c_created_at timestamp not null  default now()
);