-- USERS
INSERT INTO users (first_name, last_name, password, phone, birthday, email, role_id, logo)
VALUES
('Иван', 'Иванов', 'pass123', '+996700000001', '1990-01-01', 'ivan@example.com', 1, 'ivan.png'),
('Мария', 'Петрова', 'pass123', '+996700000002', '1995-05-15', 'maria@example.com', 2, 'maria.png'),
('Азамат', 'Садыков', 'pass123', '+996700000003', '1988-08-08', 'azamat@example.com', 3, 'azamat.png');

-- CITY
INSERT INTO city (name)
VALUES
('Бишкек'),
('Ош'),
('Каракол');

-- BUSINESS_CATEGORIES
INSERT INTO business_categories (name)
VALUES
('Красота'),
('Спорт'),
('Здоровье');

-- BUSSINES
INSERT INTO bussines (user_id, title, description, logo, city_id, business_category_id, business_address, business_phone, business_email)
VALUES
(3, 'Barbershop №1', 'Мужская парикмахерская', 'barber.png', 1, 1, 'ул. Ленина, 1', '+996555111111', 'barber@shop.kg'),
(3, 'Yoga Life', 'Йога студия для всех', 'yoga.png', 2, 2, 'пр. Мира, 15', '+996555222222', 'yoga@life.kg'),
(3, 'Zdorovye+ клиника', 'Общая медицина', 'clinic.png', 1, 3, 'ул. Советская, 10', '+996555333333', 'info@zdorovye.kg');

-- BUSINESS_UNDER_CATEGORIES
INSERT INTO business_under_categories (business_id, name)
VALUES
(1, 'Стрижки'),
(1, 'Бритьё'),
(2, 'Хатха-йога');

-- SERVICES
INSERT INTO services (service_name, bussines_id, duration_in_min, price)
VALUES
('Мужская стрижка', 1, 30, 500.00),
('Групповая йога', 2, 60, 300.00),
('Консультация терапевта', 3, 20, 1000.00);

-- DAY_OF_WEEK
INSERT INTO day_of_week (name, is_working)
VALUES
('Понедельник', true),
('Вторник', true),
('Воскресенье', false);

-- SCHEDULE
INSERT INTO schedule (bussines_service_id, day_of_week_id, start_time, end_time, is_available, max_booking_size)
VALUES
(1, 1, '09:00:00', '12:00:00', true, 5),
(2, 2, '10:00:00', '13:00:00', true, 10),
(3, 1, '08:30:00', '11:30:00', true, 3);

-- BOOKS
INSERT INTO books (book_by, strated_at, finished_at, schedule_id)
VALUES
(1, '2025-06-28 09:30:00', '2025-06-28 10:00:00', 1),
(2, '2025-06-28 10:00:00', '2025-06-28 11:00:00', 2),
(1, '2025-06-29 08:30:00', '2025-06-29 08:50:00', 3);

-- BUSINESS_REVIEW
INSERT INTO business_review (business_id, user_id, review_text, rating)
VALUES
(1, 1, 'Отличная стрижка!', 4.5),
(2, 2, 'Хорошая студия, понравился инструктор.', 4.0),
(3, 1, 'Врач был вежливый, всё понравилось.', 5.0);
