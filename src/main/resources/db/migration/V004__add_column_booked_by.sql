ALTER TABLE books
    ADD COLUMN booked_by BIGINT UNIQUE;

ALTER TABLE books
    ADD CONSTRAINT fk_books_user
        FOREIGN KEY (booked_by) REFERENCES users(id);