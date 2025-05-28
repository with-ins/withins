package com.withins.core.welfarecenter.component;

import com.withins.core.welfarecenter.entity.Post;
import com.withins.core.welfarecenter.entity.PostJob;
import com.withins.core.welfarecenter.entity.PostNotice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Component for bulk inserting Post entities using JDBC Template.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PostBulkInsertExecutor {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Bulk inserts a list of Post entities.
     *
     * @param posts the list of Post entities to insert
     * @return the number of rows inserted
     */
    public int bulkInsert(List<Post> posts) {
        if (posts == null || posts.isEmpty()) {
            return 0;
        }

        // Separate posts by type
        List<PostNotice> notices = new ArrayList<>();
        List<PostJob> jobs = new ArrayList<>();

        for (Post post : posts) {
            if (post instanceof PostNotice) {
                notices.add((PostNotice) post);
            } else if (post instanceof PostJob) {
                jobs.add((PostJob) post);
            }
        }

        int insertedCount = 0;
        if (!notices.isEmpty()) {
            insertedCount += bulkInsertNotices(notices);
        }
        if (!jobs.isEmpty()) {
            insertedCount += bulkInsertJobs(jobs);
        }

        return insertedCount;
    }

    private int bulkInsertNotices(List<PostNotice> notices) {
        LocalDateTime now = LocalDateTime.now();
        String sql = "INSERT INTO posts (title, url, welfare_center_id, category, created_date, last_modified_date) VALUES (?, ?, ?, 'notice', ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (PostNotice notice : notices) {
            batchArgs.add(new Object[]{
                    notice.getTitle(),
                    notice.getUrl(),
                    notice.getWelfareCenter().getId(),
                    Timestamp.valueOf(now),
                    Timestamp.valueOf(now)
            });
        }

        int[] updateCounts = jdbcTemplate.batchUpdate(sql, batchArgs);
        int totalInserted = 0;
        for (int count : updateCounts) {
            totalInserted += count;
        }

        log.info("Bulk inserted {} notice posts", totalInserted);
        return totalInserted;
    }

    private int bulkInsertJobs(List<PostJob> jobs) {
        LocalDateTime now = LocalDateTime.now();
        String sql = "INSERT INTO posts (title, url, welfare_center_id, category, recruitment_start_date, recruitment_end_date, created_date, last_modified_date) VALUES (?, ?, ?, 'job', ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (PostJob job : jobs) {
            batchArgs.add(new Object[]{
                    job.getTitle(),
                    job.getUrl(),
                    job.getWelfareCenter().getId(),
                    job.getRecruitmentStartDate() != null ? Timestamp.valueOf(job.getRecruitmentStartDate()) : null,
                    job.getRecruitmentEndDate() != null ? Timestamp.valueOf(job.getRecruitmentEndDate()) : null,
                    Timestamp.valueOf(now),
                    Timestamp.valueOf(now)
            });
        }

        int[] updateCounts = jdbcTemplate.batchUpdate(sql, batchArgs);
        int totalInserted = 0;
        for (int count : updateCounts) {
            totalInserted += count;
        }

        log.info("Bulk inserted {} job posts", totalInserted);
        return totalInserted;
    }
}