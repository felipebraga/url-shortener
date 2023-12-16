CREATE EXTENSION IF NOT EXISTS pgcrypto SCHEMA "public";

CREATE SEQUENCE IF NOT EXISTS public.short_code_id_seq INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1000 NO CYCLE;

CREATE TABLE public.users
(
    id            bigserial     NOT NULL,
    public_id     uuid          NOT NULL DEFAULT gen_random_uuid(),
    full_name     varchar(120)  NOT NULL,
    email         varchar(160)  NOT NULL,
    username      varchar(80)   NOT NULL,
    password      varchar(80)   NOT NULL,
    custom_domain text          NULL,
    enabled       boolean       NOT NULL DEFAULT TRUE,
    created_at    timestamp(6)  NOT NULL DEFAULT now(),
    CONSTRAINT users_email_key UNIQUE (email),
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_username_key UNIQUE (username)
);

CREATE TABLE public.url
(
    id             bigint       NOT NULL,
    short_code     varchar(12)  NULL,
    shortened_url  text         NULL,
    original_url   text         NOT NULL,
    expires_in     timestamp(6) NULL,
    created_at     timestamp(6) NOT NULL DEFAULT now(),
    active         boolean      NOT NULL DEFAULT TRUE,
    user_id        bigint       NULL,
    CONSTRAINT url_pkey PRIMARY KEY (id),
    CONSTRAINT url_id_short_code_key UNIQUE (id, short_code),
    CONSTRAINT url_short_code_key UNIQUE (short_code),
    CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES public.users(id)
);

CREATE TABLE public.activity
(
    id         bigserial    NOT NULL,
    url_id     bigint       NOT NULL,
    visited_in timestamp(6) NOT NULL,
    headers    jsonb        NULL,
    CONSTRAINT activity_pkey PRIMARY KEY (id),
    CONSTRAINT url_id_fk FOREIGN KEY (url_id) REFERENCES public.url (id)
);
