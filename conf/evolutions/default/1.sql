# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table user (
  username                  varchar(255) primary key,
  password                  varchar(255),
  firstname                 varchar(255),
  lastname                  varchar(255),
  major                     varchar(255),
  year                      integer,
  team                      varchar(255),
  last_update               timestamp not null)
;

create table vote (
  id                        integer primary key AUTOINCREMENT,
  user_id                   varchar(255),
  category_id               integer)
;

create table vote_category (
  id                        integer primary key AUTOINCREMENT,
  score                     integer,
  name                      varchar(255))
;

alter table vote add constraint fk_vote_category_1 foreign key (category_id) references vote_category (id);
create index ix_vote_category_1 on vote (category_id);



# --- !Downs

PRAGMA foreign_keys = OFF;

drop table user;

drop table vote;

drop table vote_category;

PRAGMA foreign_keys = ON;

