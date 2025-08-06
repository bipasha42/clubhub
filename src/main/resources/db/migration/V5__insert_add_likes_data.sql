INSERT INTO post_like (post_id, user_id)
VALUES (
    (SELECT post_id FROM post WHERE title = 'Programming Workshop'),
    (SELECT user_id FROM users WHERE email = 'rifat@diu.edu.bd')
);

-- Sarah likes Photo Contest 2024
INSERT INTO post_like (post_id, user_id)
VALUES (
    (SELECT post_id FROM post WHERE title = 'Photo Contest 2024'),
    (SELECT user_id FROM users WHERE email = 'sarah@diu.edu.bd')
);

-- Captain Ali likes Leadership Seminar
INSERT INTO post_like (post_id, user_id)
VALUES (
    (SELECT post_id FROM post WHERE title = 'Leadership Seminar'),
    (SELECT user_id FROM users WHERE email = 'captain@bup.edu.bd')
);

-- =========================
-- 4. SAMPLE DATA FOR comment
-- =========================

-- Rifat comments on Programming Workshop
INSERT INTO comment (post_id, user_id, content)
VALUES (
    (SELECT post_id FROM post WHERE title = 'Programming Workshop'),
    (SELECT user_id FROM users WHERE email = 'rifat@diu.edu.bd'),
    'Looking forward to this workshop!'
);

-- Sarah comments on Photo Contest 2024
INSERT INTO comment (post_id, user_id, content)
VALUES (
    (SELECT post_id FROM post WHERE title = 'Photo Contest 2024'),
    (SELECT user_id FROM users WHERE email = 'sarah@diu.edu.bd'),
    'Can I submit more than one photo?'
);

-- Captain Ali comments on Leadership Seminar
INSERT INTO comment (post_id, user_id, content)
VALUES (
    (SELECT post_id FROM post WHERE title = 'Leadership Seminar'),
    (SELECT user_id FROM users WHERE email = 'captain@bup.edu.bd'),
    'Great initiative!'
);

-- =========================
-- 5. Indexes for performance
-- =========================
CREATE INDEX IF NOT EXISTS idx_post_like_post ON post_like(post_id);
CREATE INDEX IF NOT EXISTS idx_post_like_user ON post_like(user_id);
CREATE INDEX IF NOT EXISTS idx_comment_post ON comment(post_id);
CREATE INDEX IF NOT EXISTS idx_comment_user ON comment(user_id);