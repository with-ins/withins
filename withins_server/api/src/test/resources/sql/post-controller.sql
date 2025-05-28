-- Welfare Centers Data
INSERT INTO welfare_centers (id, name, region, created_date, last_modified_date)
VALUES (1, '복지센터1', '서울', NOW(), NOW());
INSERT INTO welfare_centers (id, name, region, created_date, last_modified_date)
VALUES (2, '복지센터2', '부산', NOW(), NOW());

-- Posts Data - Notice Category
INSERT INTO posts (id, title, welfare_center_id, url, category, created_date, last_modified_date)
VALUES (1, '첫 번째 공지사항', 1, 'https://example.com/notice1', 'notice', NOW(), NOW());
INSERT INTO posts (id, title, welfare_center_id, url, category, created_date, last_modified_date)
VALUES (2, '두 번째 공지사항', 1, 'https://example.com/notice2', 'notice', NOW(), NOW());

-- Posts Data - Job Category
INSERT INTO posts (id, title, welfare_center_id, url, category, created_date, last_modified_date, recruitment_start_date, recruitment_end_date)
VALUES (3, '첫 번째 채용공고', 1, 'https://example.com/job1', 'job', NOW(), NOW(), NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY));
INSERT INTO posts (id, title, welfare_center_id, url, category, created_date, last_modified_date, recruitment_start_date, recruitment_end_date)
VALUES (4, '두 번째 채용공고', 2, 'https://example.com/job2', 'job', NOW(), NOW(), NOW(), DATE_ADD(NOW(), INTERVAL 15 DAY));