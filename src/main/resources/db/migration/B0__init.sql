CREATE TABLE public.url
(
    id             bigserial    NOT NULL,
    short_code     varchar(12)  NULL,
    shortened_url  text         NULL,
    original_url   text         NOT NULL,
    expires_in     timestamp(6) NULL,
    created_at     timestamp(6) NOT NULL,
    active         boolean      NOT NULL DEFAULT TRUE,
    CONSTRAINT url_pkey PRIMARY KEY (id),
    CONSTRAINT url_short_code_key UNIQUE (short_code)
);
