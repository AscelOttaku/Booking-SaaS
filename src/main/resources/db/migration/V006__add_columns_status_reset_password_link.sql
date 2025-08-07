alter table books
    add column if not exists status varchar(20) not null default 'ACCEPTED';

insert into schedule_settings(schedule_id,
                              break_between_bookings_in_minutes)
values (1,
        15),
       (2, 15),
       (3, 15);

alter table USERS
    add column if not exists reset_password_link varchar(255);