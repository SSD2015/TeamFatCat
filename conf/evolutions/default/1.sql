# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table project (
  id                        integer auto_increment not null,
  project_name              varchar(255),
  project_desc              varchar(255),
  constraint pk_project primary key (id))
;

create table user (
  id                        integer auto_increment not null,
  username                  varchar(255),
  password                  varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  major                     varchar(255),
  year                      integer,
  team                      varchar(255),
  last_update               datetime not null,
  constraint pk_user primary key (id))
;

create table vote (
  id                        bigint auto_increment not null,
  score                     integer,
  user_id                   integer,
  category_id               bigint,
  project_id                integer,
  constraint pk_vote primary key (id))
;

create table vote_category (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_vote_category primary key (id))
;

alter table vote add constraint fk_vote_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_vote_user_1 on vote (user_id);
alter table vote add constraint fk_vote_category_2 foreign key (category_id) references vote_category (id) on delete restrict on update restrict;
create index ix_vote_category_2 on vote (category_id);
alter table vote add constraint fk_vote_project_3 foreign key (project_id) references project (id) on delete restrict on update restrict;
create index ix_vote_project_3 on vote (project_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table project;

drop table user;

drop table vote;

drop table vote_category;

SET FOREIGN_KEY_CHECKS=1;

