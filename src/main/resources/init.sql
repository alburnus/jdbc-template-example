create database team;
create user teamuser with encrypted password 'secret';
grant all privileges on database team to teamuser;

-- CREATE TABLES
-- Team
CREATE TABLE public.team
(
  id SERIAL PRIMARY KEY,
  name character varying(255) COLLATE pg_catalog."default"
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.team
  OWNER to teamuser;

-- Teammate
CREATE TABLE public.teammate
(
  id SERIAL PRIMARY KEY,
  name character varying(255) COLLATE pg_catalog."default",
  team_id bigint,
  CONSTRAINT fko2eamonl4lewb1jsvvslmpmj8 FOREIGN KEY (team_id)
  REFERENCES public.team (id) MATCH SIMPLE
  ON UPDATE NO ACTION
  ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.teammate
  OWNER to teamuser;