
-- ===============================================================
-- 👥 3. Вставляем 10 пользователей
-- ===============================================================
INSERT INTO users (full_name, email, password, phone_number, image_url, role) VALUES
                                                                                  ('Иван Петров',     'ivan.petrov@example.com',     '$2a$10$7MvF3sZlUfuMaQ2s9YfW3eRhaLVakYhMG.lfZPzTzUklkncqAhz2K', '+79990000001', 'https://example.com/avatar1.png', 'USER'),
                                                                                  ('Анна Смирнова',   'anna.smirnova@example.com',   '$2a$10$H0NoxmKbl/M4jPGu7LXoG.Kwfu4mvhRDrhJrhlm3gXcK23TnMCb7W', '+79990000002', 'https://example.com/avatar2.png', 'USER'),
                                                                                  ('Павел Иванов',    'pavel.ivanov@example.com',    '$2a$10$A2TkU3vPf1J95ksE21mLpuRfAfEZwIV7BGgkClKz4Rz9A4aAZImru', '+79990000003', 'https://example.com/avatar3.png', 'USER'),
                                                                                  ('Елена Кузнецова', 'elena.kuz@example.com',       '$2a$10$C8r9RMhW4LJbEXoPoQULy.sAqJBRvHkt36.8SdxTgwHUfWfmkYb1u', '+79990000004', 'https://example.com/avatar4.png', 'USER'),
                                                                                  ('Михаил Соколов',  'm.sokolov@example.com',       '$2a$10$zkT8vXXpPha1m6OcfV6C1e1sv0K5zXpsgLj7nRZMK0AxftrSXfxFC', '+79990000005', 'https://example.com/avatar5.png', 'USER'),
                                                                                  ('Дарья Орлова',    'darya.orlova@example.com',    '$2a$10$54QZGhLxoOXb8jvEZ9AQ3OJvvTx3Hb5yR6V8fgrC5zLptb7QYokTa', '+79990000006', 'https://example.com/avatar6.png', 'USER'),
                                                                                  ('Артём Волков',    'artem.volkov@example.com',    '$2a$10$O.lYZb4xS.7KMUOXMEFe3u2eP0saTHnPbPyHbo5W4HliMuKn1g37G', '+79990000007', 'https://example.com/avatar7.png', 'USER'),
                                                                                  ('Мария Попова',    'm.popova@example.com',        '$2a$10$J0GvlyOIsv/j0Zs0Lw3gPe95h8hIIfu5KycRlQDJbyOdPcvBO5mAy', '+79990000008', 'https://example.com/avatar8.png', 'USER'),
                                                                                  ('Сергей Егоров',   'sergey.egorov@example.com',   '$2a$10$BqfH8KzdlUm0loKQmEb0GuLZHVw9lD6wYfGZm4JfMTbXl/JY90P5u', '+79990000009', 'https://example.com/avatar9.png', 'USER'),
                                                                                  ('Ольга Алексеева', 'olga.alex@example.com',       '$2a$10$onf9bsltkgf3XPE7ZEGdXe14jYkP7VIZ6gqXqf4O03WkKSkbi5.tK', '+79990000010', 'https://example.com/avatar10.png', 'USER');

-- ===============================================================
-- 🔐 4. Пароли до шифрования (для справки)
-- ===============================================================
-- Иван Петров:     Petrov2024
-- Анна Смирнова:   Anna2024
-- Павел Иванов:    Pavel2024
-- Елена Кузнецова: Elena2024
-- Михаил Соколов:  Misha2024
-- Дарья Орлова:    Darya2024
-- Артём Волков:    Artyom2024
-- Мария Попова:    Maria2024
-- Сергей Егоров:   Sergey2024
-- Ольга Алексеева: Olga2024
-- Все >=8 символов, буквы и цифры.
-- ===============================================================
