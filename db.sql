CREATE TABLE IF NOT EXISTS users
(
    id       serial primary key,
    name     text,
    email    text unique,
    password text not null
);

CREATE TABLE IF NOT EXISTS todos
(
    id         serial primary key,
    task       text,
    done       bool,
    created_by int references users (id)
);


