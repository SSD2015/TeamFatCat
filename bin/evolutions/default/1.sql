# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table vote (
  id                        bigint not null,
  user_id                   varchar(255),
  category_id               bigint,
  constraint pk_vote primary key (id))
;

create table vote_category (
  id                        bigint not null,
  score                     integer,
  name                      varchar(255),
  constraint pk_vote_category primary key (id))
;

create sequence vote_seq;

create sequence vote_category_seq;

alter table vote add constraint fk_vote_category_1 foreign key (category_id) references vote_category (id) on delete restrict on update restrict;
create index ix_vote_category_1 on vote (category_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists vote;

drop table if exists vote_category;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists vote_seq;

drop sequence if exists vote_category_seq;

