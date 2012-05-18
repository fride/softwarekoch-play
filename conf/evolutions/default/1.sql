# --- !Ups

create table ingredient (
  id                        bigint not null IDENTITY,
  name                      varchar(255),
  constraint pk_ingredients primary key (id)
);

create table recipe (
  id                        bigint not null IDENTITY,
  name                      varchar(255),
  description               clob,
  picture_url               varchar(255),
  preparation               varchar(255),
  complexity                integer,
  constraint ck_recipe_complexity check (complexity in (0,1,2)),
  constraint pk_recipe primary key (id)

);

create table category (
  id                        bigint not null IDENTITY,
  name                      varchar(255),
  constraint pk_category primary key (id),
  constraint unique_category_name unique (name)
);

create table recipe_category (
  id                        bigint not null IDENTITY,
  name                      varchar(255),
  constraint pk_category primary key (id)
);

create table recipe_ingredient(
  amount                    integer,
  measure                   integer,
  recipe_id                 bigint not null,
  ingredient_id             bigint not null,
  constraint ck_ingredients_measure check (measure in (0,1,2,3,4,5,6)),
  constraint unique_recipe_ingred unique (recipe_id, ingredient_id)
);

alter table recipe_ingredient
  add constraint fk_ingredient foreign key (recipe_id)
                               references recipe (id)
                               on delete restrict on update restrict;

alter table recipe_ingredient
    add constraint fk_recipe foreign key (ingredient_id)
                             references ingredient (id)
                             on delete restrict on update restrict;


create sequence ingredients_seq;

create sequence recipe_seq;



# --- !Downs



SET REFERENTIAL_INTEGRITY FALSE;


drop table if exists ingredient;

drop table if exists recipe;

drop table if exists recipe_ingredient;

drop table if exists category;

drop table if exists RECIPE_CATEGORY

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists ingredients_seq;

drop sequence if exists recipe_seq;


