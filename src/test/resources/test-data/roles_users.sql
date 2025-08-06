-- Вставляем роль
INSERT INTO role (role_name)
VALUES ('CLIENT');

-- Вставляем пользователя, ссылаясь на роль через подзапрос
INSERT INTO users (first_name, last_name, password, phone, birthday, email, role_id, logo)
VALUES ('Test', 'User', 'pass', '+123456789', '2000-01-01', 'user1@example.com',
        (SELECT id FROM role WHERE role_name = 'CLIENT'),
        'logo.png');