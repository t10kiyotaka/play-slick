# Message schema
# --- !Ups

create table "messages" (
  "id" bigint generated by default as identity(start with 1) not null primary key,
  "person_id" int,
  "message" varchar(255) not null,
  "created_at" timestamp not null default current_timestamp
);

insert into messages values (default, 1, 'This is sample message.', default);
insert into messages values (default, 1, 'Hello.', default);
insert into messages values (default, 2, 'AAAAAAAA.', default);
insert into messages values (default, 3, 'Yea!.', default);
insert into messages values (default, 1, 'Umumm....', default);
insert into messages values (default, 1, 'Yey!.', default);

# --- !Downs
drop table "messages" if exists