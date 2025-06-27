-- Таблица AUTHORITY
CREATE TABLE authority (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Таблица ROLE
CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL,
    authority_id BIGINT
);

-- Таблица USERS
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    password VARCHAR(100),
    last_name VARCHAR(100),
    middle_name VARCHAR(100),
    phone VARCHAR(20),
    birthday DATE,
    email VARCHAR(100) NOT NULL UNIQUE,
    role_id BIGINT,
    logo VARCHAR(155)
);

-- Таблица BUSINESS_CATEGORIES
CREATE TABLE business_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Таблица BUSSINES
CREATE TABLE bussines (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255),
    description TEXT,
    logo VARCHAR(155),
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now(),
    city_id BIGINT,
    business_category_id BIGINT,
    business_address VARCHAR(255),
    business_phone VARCHAR(20),
    business_email VARCHAR(255)
);

-- Таблица BUSINESS_UNDER_CATEGORIES
CREATE TABLE business_under_categories (
    id BIGSERIAL PRIMARY KEY,
    business_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL
);

-- Таблица SERVICES
CREATE TABLE services (
    id BIGSERIAL PRIMARY KEY,
    service_name VARCHAR(255) NOT NULL,
    bussines_id BIGINT NOT NULL,
    duration_in_min INT DEFAULT 0,
    price DECIMAL(10, 2) CHECK (price >= 0)
);

CREATE TABLE day_of_week (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_working BOOLEAN NOT NULL
);

-- Таблица SCHEDULE
CREATE TABLE schedule (
    id BIGSERIAL PRIMARY KEY,
    bussines_service_id BIGINT NOT NULL,
    day_of_week_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    is_available BOOLEAN NOT NULL,
    max_booking_size INT NOT NULL
);

-- Таблица BOOKS
CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    service_id BIGINT NOT NULL,
    book_by BIGINT NOT NULL,
    strated_at TIMESTAMP NOT NULL,
    finished_at TIMESTAMP NOT NULL
);

-- Таблица USER_SERVICE
CREATE TABLE user_service (
    id BIGSERIAL PRIMARY KEY,
    service_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT unq_user_service UNIQUE (user_id, service_id)
);

-- Таблица BUSINESS_REVIEW
CREATE TABLE business_review (
    id BIGSERIAL PRIMARY KEY,
    business_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    review_text VARCHAR(255) NOT NULL,
    rating DECIMAL(2,1) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE city (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- ✅ Добавление всех FOREIGN KEY'ев отдельно
ALTER TABLE role
    ADD CONSTRAINT fk_role_authority FOREIGN KEY (authority_id) REFERENCES authority(id);

ALTER TABLE users
    ADD CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role(id);

ALTER TABLE books
    ADD CONSTRAINT fk_books_user FOREIGN KEY (book_by) REFERENCES users(id);

ALTER TABLE schedule
    ADD CONSTRAINT fk_schedule_day_of_week FOREIGN KEY (day_of_week_id) REFERENCES day_of_week(id);

ALTER TABLE bussines
    ADD CONSTRAINT fk_bussines_user FOREIGN KEY (user_id) REFERENCES users(id),
    ADD CONSTRAINT fk_business_category_id FOREIGN KEY (business_category_id) REFERENCES business_categories(id);

ALTER TABLE bussines
    ADD CONSTRAINT fk_bussines_city FOREIGN KEY (city_id) REFERENCES city(id);

ALTER TABLE business_under_categories
    ADD CONSTRAINT fk_business_under_categories FOREIGN KEY (business_id) REFERENCES bussines(id);

ALTER TABLE services
    ADD CONSTRAINT fk_services_bussines FOREIGN KEY (bussines_id) REFERENCES bussines(id);

ALTER TABLE schedule
    ADD CONSTRAINT fk_schedule_service FOREIGN KEY (bussines_service_id) REFERENCES services(id);

ALTER TABLE books
    ADD CONSTRAINT fk_books_service FOREIGN KEY (service_id) REFERENCES services(id);

ALTER TABLE user_service
    ADD CONSTRAINT fk_user_service_user FOREIGN KEY (user_id) REFERENCES users(id),
    ADD CONSTRAINT fk_user_service_service FOREIGN KEY (service_id) REFERENCES services(id);

ALTER TABLE business_review
    ADD CONSTRAINT fk_review_business FOREIGN KEY (business_id) REFERENCES bussines(id),
    ADD CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(id);

-- 🟩 Первоначальные данные
INSERT INTO role (role_name) VALUES ('CLIENT');
INSERT INTO role (role_name) VALUES ('ADMIN');
INSERT INTO role (role_name) VALUES ('BUSINESS_OWNER');
