create sequence task_seq start with 1 increment by 50;

create table task (
                      closed boolean not null,
                      id bigint not null,
                      description varchar(255),
                      primary key (id)
);
