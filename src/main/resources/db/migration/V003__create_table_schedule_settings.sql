CREATE TABLE schedule_settings
(
    id                                BIGSERIAL PRIMARY KEY,
    schedule_id                       BIGINT UNIQUE NOT NULL,
    break_between_bookings_in_minutes INTEGER       NOT NULL,
    CONSTRAINT fk_schedule
        FOREIGN KEY (schedule_id)
            REFERENCES schedule (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE break_period
(
    id                   BIGSERIAL PRIMARY KEY,
    schedule_settings_id BIGINT NOT NULL,
    start_time           TIME   NOT NULL,
    end_time             TIME   NOT NULL,
    CONSTRAINT fk_schedule_settings
        FOREIGN KEY (schedule_settings_id)
            REFERENCES schedule_settings (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);