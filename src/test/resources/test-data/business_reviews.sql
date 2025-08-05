-- Вставляем отзывы, ссылаясь на бизнес и пользователя через подзапросы
INSERT INTO BUSINESS_REVIEW (business_id, user_id, review_text, rating, created_at)
SELECT (SELECT id FROM BUSSINES WHERE title = 'Test Business'),
       (SELECT id FROM users WHERE email = 'user1@example.com'),
       'Great service!',
       5.0,
       CURRENT_TIMESTAMP()
UNION ALL
SELECT (SELECT id FROM BUSSINES WHERE title = 'Test Business'),
       (SELECT id FROM users WHERE email = 'user1@example.com'),
       'Good experience.',
       4.0,
       CURRENT_TIMESTAMP();

INSERT INTO BUSINESS_REVIEW (business_id, user_id, review_text, rating, created_at)
SELECT (SELECT ID FROM BUSSINES WHERE TITLE = 'Test Business'),
       (SELECT ID FROM users WHERE email = 'user1@example.com'),
       'Average service.',
       3.0,
       CURRENT_TIMESTAMP()


