CREATE TABLE holiday
(
    id           BIGSERIAL PRIMARY KEY,
    date         DATE    NOT NULL,
    local_name   VARCHAR(255),
    name         VARCHAR(255),
    country_code VARCHAR(10),
    global       BOOLEAN NOT NULL,
    type         VARCHAR(100)
);

CREATE TABLE holiday_types
(
    holiday_id BIGINT NOT NULL REFERENCES holiday (id)
        ON DELETE CASCADE,
    type       VARCHAR(255),
    PRIMARY KEY (holiday_id, type)
);