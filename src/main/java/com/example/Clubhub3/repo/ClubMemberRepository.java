package com.example.Clubhub3.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ClubMemberRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Object[]> findMembersByClubId(UUID clubId) {
        System.out.println("--- Repository: Finding members for club ID: " + clubId + " ---");

        // =================================================================================
        // THE FINAL FIX IS HERE: The table name is corrected to "users" (plural, lowercase).
        // =================================================================================
        String sql = "SELECT cm.id, cm.club_id, cm.user_id, cm.member_since, " +
                "u.first_name, u.last_name, u.email, u.profile_picture_url, " +
                "c.admin_id " +
                "FROM clubmember cm " +
                "JOIN users u ON cm.user_id = u.id " +  // <-- CORRECTED THIS LINE
                "JOIN club c ON cm.club_id = c.id " +
                "WHERE cm.club_id = ? " +
                "ORDER BY cm.member_since DESC";

        try {
            System.out.println("--- Repository: Executing SQL query... ---");
            List<Object[]> results = jdbcTemplate.query(sql,
                    (rs, rowNum) -> new Object[]{
                            rs.getObject("id"),
                            rs.getObject("club_id"),
                            rs.getObject("user_id"),
                            rs.getTimestamp("member_since"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("profile_picture_url"),
                            rs.getObject("admin_id")
                    },
                    clubId);

            System.out.println("--- Repository: SQL query SUCCEEDED. Returned " + results.size() + " rows. ---");
            return results;
        } catch (Exception e) {
            System.err.println("---!!! REPOSITORY CRITICAL ERROR: FAILED to execute SQL query. !!!---");
            e.printStackTrace();
            return List.of();
        }
    }

    public boolean existsByClubIdAndUserId(UUID clubId, UUID userId) {
        String sql = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
                "FROM clubmember WHERE club_id = ? AND user_id = ?";

        try {
            Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, clubId, userId);
            return result != null ? result : false;
        } catch (Exception e) {
            System.err.println("Error in existsByClubIdAndUserId: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}