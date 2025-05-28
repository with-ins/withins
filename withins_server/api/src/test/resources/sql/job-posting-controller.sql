-- Welfare Centers Data
INSERT INTO welfare_centers (id, name, region, created_date, last_modified_date) 
VALUES (1, '복지센터1', '서울', NOW(), NOW());
INSERT INTO welfare_centers (id, name, region, created_date, last_modified_date) 
VALUES (2, '복지센터2', '부산', NOW(), NOW());

-- Job Postings Data
INSERT INTO job_postings (id, title, welfare_center_id, recruitment_start_date, recruitment_end_date, created_date, last_modified_date) 
VALUES (1, '첫 번째 구인공고', 1, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), NOW(), NOW());
INSERT INTO job_postings (id, title, welfare_center_id, recruitment_start_date, recruitment_end_date, created_date, last_modified_date) 
VALUES (2, '두 번째 구인공고', 1, NOW(), DATE_ADD(NOW(), INTERVAL 15 DAY), NOW(), NOW());
INSERT INTO job_postings (id, title, welfare_center_id, recruitment_start_date, recruitment_end_date, created_date, last_modified_date) 
VALUES (3, '세 번째 구인공고', 2, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), NOW(), NOW());