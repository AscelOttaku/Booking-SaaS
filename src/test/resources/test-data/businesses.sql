-- Вставляем город
INSERT INTO city (name)
VALUES ('Test City');

-- Вставляем категорию бизнеса
INSERT INTO business_categories (name)
VALUES ('Test Category');

-- Вставляем бизнес, ссылаясь на пользователя, город и категорию через подзапросы
INSERT INTO BUSSINES (user_id, title, description, logo, city_id,
                      business_category_id, business_address, business_phone, business_email)
VALUES ((SELECT id FROM users WHERE email = 'user1@example.com'),
        'Test Business',
        'Test Description',
        'logo.png',
        (SELECT id FROM city WHERE name = 'Test City'),
        (SELECT id FROM business_categories WHERE name = 'Test Category'),
        'Test Address',
        '+123456789',
        'test@business.com');