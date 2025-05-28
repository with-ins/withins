-- Users Data
INSERT INTO users (id, social_user_id, nickname, role, created_date, last_modified_date) 
VALUES (1, 'social_id_1', 'Test User 1', 'USER', NOW(), NOW());
INSERT INTO users (id, social_user_id, nickname, role, created_date, last_modified_date) 
VALUES (2, 'social_id_2', 'Test User 2', 'USER', NOW(), NOW());

-- Portfolios Data
INSERT INTO portfolios (id, title, content, user_id, created_date, last_modified_date) 
VALUES (1, 'Portfolio 1', 'Content for portfolio 1', 1, NOW(), NOW());
INSERT INTO portfolios (id, title, content, user_id, created_date, last_modified_date) 
VALUES (2, 'Portfolio 2', 'Content for portfolio 2', 1, NOW(), NOW());
INSERT INTO portfolios (id, title, content, user_id, created_date, last_modified_date) 
VALUES (3, 'Portfolio 3', 'Content for portfolio 3', 2, NOW(), NOW());
