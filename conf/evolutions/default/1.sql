# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table deadline (
  id                        bigint auto_increment not null,
  year                      integer,
  month                     integer,
  day                       integer,
  hour                      integer,
  min                       integer,
  sec                       integer,
  constraint pk_deadline primary key (id))
;

create table image (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  data                      longblob,
  project_id                bigint,
  constraint pk_image primary key (id))
;

create table project (
  id                        bigint auto_increment not null,
  project_name              varchar(255),
  project_desc              varchar(255),
  team_id                   bigint,
  constraint pk_project primary key (id))
;

create table rate (
  id                        bigint auto_increment not null,
  score                     integer,
  user_id                   bigint,
  category_id               bigint,
  project_id                bigint,
  timestamp                 datetime not null,
  constraint pk_rate primary key (id))
;

create table vote_category (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_vote_category primary key (id))
;

create table team (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  members                   varchar(255),
  constraint pk_team primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  username                  varchar(255),
  password                  varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  type                      integer,
  last_update               datetime not null,
  constraint pk_user primary key (id))
;

create table vote (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  project_id                bigint,
  timestamp                 datetime not null,
  constraint pk_vote primary key (id))
;

alter table rate add constraint fk_rate_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_rate_user_1 on rate (user_id);
alter table rate add constraint fk_rate_category_2 foreign key (category_id) references vote_category (id) on delete restrict on update restrict;
create index ix_rate_category_2 on rate (category_id);
alter table rate add constraint fk_rate_project_3 foreign key (project_id) references project (id) on delete restrict on update restrict;
create index ix_rate_project_3 on rate (project_id);
alter table vote add constraint fk_vote_user_4 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_vote_user_4 on vote (user_id);
alter table vote add constraint fk_vote_project_5 foreign key (project_id) references project (id) on delete restrict on update restrict;
create index ix_vote_project_5 on vote (project_id);



# --- !Downs

# SET FOREIGN_KEY_CHECKS=0;
#
# drop table deadline;
#
# drop table image;
#
# drop table project;
#
# drop table rate;
#
# drop table vote_category;
#
# drop table team;
#
# drop table user;
#
# drop table vote;
#
# SET FOREIGN_KEY_CHECKS=1;

