DROP EXTENSION IF EXISTS pgcrypto;
CREATE EXTENSION pgcrypto SCHEMA "public";

DROP SEQUENCE IF EXISTS public.short_code_id_seq;
CREATE SEQUENCE public.short_code_id_seq INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE;

CREATE TABLE public.users
(
    id            bigserial     NOT NULL,
    public_id     uuid          NOT NULL DEFAULT gen_random_uuid(),
    full_name     varchar(120)  NOT NULL,
    email         varchar(160)  NOT NULL,
    username      varchar(80)   NOT NULL,
    password      varchar(80)   NOT NULL,
    enabled       boolean       NOT NULL DEFAULT TRUE,
    created_at    timestamp(6)  NOT NULL DEFAULT now(),
    CONSTRAINT users_pk PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT uk_users_public_id UNIQUE (public_id),
    CONSTRAINT uk_users_username UNIQUE (username)
);

CREATE TABLE public.url
(
    id             bigint       NOT NULL,
    short_code     varchar(12)  NOT NULL,
    shortened_url  text         NULL,
    source_url     text         NOT NULL,
    expires_in     timestamp(6) NULL,
    created_at     timestamp(6) NOT NULL DEFAULT now(),
    active         boolean      NOT NULL DEFAULT TRUE,
    user_id        bigint       NULL,
    CONSTRAINT url_pk PRIMARY KEY (id),
    CONSTRAINT uk_url_id_short_code UNIQUE (id, short_code),
    CONSTRAINT uk_url_short_code UNIQUE (short_code),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES public.users(id)
);

CREATE TABLE public.activity
(
    id         bigserial    NOT NULL,
    url_id     bigint       NOT NULL,
    visited_in timestamp(6) NOT NULL,
    headers    jsonb        NULL,
    CONSTRAINT activity_pk PRIMARY KEY (id),
    CONSTRAINT fk_url_id FOREIGN KEY (url_id) REFERENCES public.url (id)
);
