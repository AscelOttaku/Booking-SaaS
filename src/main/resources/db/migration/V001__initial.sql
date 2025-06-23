DROP TYPE IF EXISTS role_enum;

-- Тип роли (ENUM)
CREATE TYPE role_enum AS ENUM (
    'CLIENT',
    'BUSINESS_OWNER',
    'ADMIN'
);

-- Таблица AUTHORITY (опционально, если нужен RBAC)
CREATE TABLE authority (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Таблица ROLE
CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    role_name role_enum NOT NULL,
    authority_id BIGINT,
    CONSTRAINT fk_role_authority FOREIGN KEY (authority_id) REFERENCES authority(id)
);

-- Таблица USER
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    middle_name VARCHAR(100),
    phone VARCHAR(20),
    birthday TIMESTAMP,
    email VARCHAR(100) NOT NULL UNIQUE,
    role_id BIGINT,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role(id)
);

-- Таблица BUSSINES
CREATE TABLE bussines (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255),
    description TEXT,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now(),
    CONSTRAINT fk_bussines_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Таблица SERVICES
CREATE TABLE services (
    id BIGSERIAL PRIMARY KEY,
    service_name VARCHAR(255) NOT NULL,
    bussines_id BIGINT NOT NULL,
    CONSTRAINT fk_services_bussines FOREIGN KEY (bussines_id) REFERENCES bussines(id)
);

-- Таблица BOOKS (бронирования)
CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    service_id BIGINT NOT NULL,
    strated_at TIMESTAMP NOT NULL,
    finished_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_books_service FOREIGN KEY (service_id) REFERENCES services(id)
);

-- Таблица user_service (многие-ко-многим между user и service)
CREATE TABLE user_service (
    id BIGSERIAL PRIMARY KEY,
    service_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_user_service_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_service_service FOREIGN KEY (service_id) REFERENCES services(id),
    CONSTRAINT unq_user_service UNIQUE (user_id, service_id)
);
