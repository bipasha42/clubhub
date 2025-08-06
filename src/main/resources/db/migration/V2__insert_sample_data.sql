-- =========================
-- SAMPLE DATA FOR CLUBHUB
-- =========================

-- Insert Countries
INSERT INTO country (country_id, country_name) VALUES
                                                   (gen_random_uuid(), 'Bangladesh'),
                                                   (gen_random_uuid(), 'USA'),
                                                   (gen_random_uuid(), 'UK');

-- Insert Universities
INSERT INTO university (university_id, university_name, country_id) VALUES
                                                                        (gen_random_uuid(), 'DIU',
                                                                         (SELECT country_id FROM country WHERE country_name = 'Bangladesh')),
                                                                        (gen_random_uuid(), 'NSU',
                                                                         (SELECT country_id FROM country WHERE country_name = 'Bangladesh')),
                                                                        (gen_random_uuid(), 'BUP',
                                                                         (SELECT country_id FROM country WHERE country_name = 'Bangladesh')),
                                                                        (gen_random_uuid(), 'Harvard',
                                                                         (SELECT country_id FROM country WHERE country_name = 'USA'));

-- Insert Clubs
INSERT INTO club (id, name, description, category, contactEmail, isApproved, createdAt, updatedAt, university_id) VALUES
                                                                                                                      (gen_random_uuid(), 'DIU Computer Club', 'Programming and tech club', 'TECHNICAL', 'computer@diu.edu.bd', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
                                                                                                                       (SELECT university_id FROM university WHERE university_name = 'DIU')),
                                                                                                                      (gen_random_uuid(), 'DIU Photo Club', 'Photography enthusiasts', 'CULTURAL', 'photo@diu.edu.bd', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
                                                                                                                       (SELECT university_id FROM university WHERE university_name = 'DIU')),
                                                                                                                      (gen_random_uuid(), 'BUP Defense Club', 'Military strategy and defense studies', 'ACADEMIC', 'defense@bup.edu.bd', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
                                                                                                                       (SELECT university_id FROM university WHERE university_name = 'BUP')),
                                                                                                                      (gen_random_uuid(), 'BUP Leadership Club', 'Leadership development', 'ACADEMIC', 'leadership@bup.edu.bd', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
                                                                                                                       (SELECT university_id FROM university WHERE university_name = 'BUP')),
                                                                                                                      (gen_random_uuid(), 'BUP Sports Club', 'Physical fitness and sports', 'SPORTS', 'sports@bup.edu.bd', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
                                                                                                                       (SELECT university_id FROM university WHERE university_name = 'BUP')),
                                                                                                                      (gen_random_uuid(), 'NSU Robotics', 'Robotics and automation', 'TECHNICAL', 'robotics@nsu.edu', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
                                                                                                                       (SELECT university_id FROM university WHERE university_name = 'NSU'));

-- Insert Users
INSERT INTO users (user_id, email, passwordHash, role, user_name, bio, profilePictureUri, isVerified, user_createdAt, user_updatedAt, university_id, club_id) VALUES
                                                                                                                                                                  (gen_random_uuid(), 'admin@diu.edu.bd', '     ', 'ADMIN', 'Dr. Rahman', 'Admin', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
                                                                                                                                                                   (SELECT university_id FROM university WHERE university_name = 'DIU'), null),
                                                                                                                                                                  (gen_random_uuid(), 'rifat@diu.edu.bd', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'STUDENT', 'Rifat Ahmed', 'CSE student', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
                                                                                                                                                                   (SELECT university_id FROM university WHERE university_name = 'DIU'),
                                                                                                                                                                   (SELECT id FROM club WHERE name = 'DIU Computer Club')),
                                                                                                                                                                  (gen_random_uuid(), 'sarah@diu.edu.bd', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'STUDENT', 'Sarah Khan', 'Photographer', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
                                                                                                                                                                   (SELECT university_id FROM university WHERE university_name = 'DIU'),
                                                                                                                                                                   (SELECT id FROM club WHERE name = 'DIU Photo Club')),
                                                                                                                                                                  (gen_random_uuid(), 'captain@bup.edu.bd', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'STUDENT', 'Captain Ali', 'Defense studies', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
                                                                                                                                                                   (SELECT university_id FROM university WHERE university_name = 'BUP'),
                                                                                                                                                                   (SELECT id FROM club WHERE name = 'BUP Defense Club')),
                                                                                                                                                                  (gen_random_uuid(), 'leader@bup.edu.bd', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'STUDENT', 'Major Khan', 'Leadership expert', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
                                                                                                                                                                   (SELECT university_id FROM university WHERE university_name = 'BUP'),
                                                                                                                                                                   (SELECT id FROM club WHERE name = 'BUP Leadership Club')),
                                                                                                                                                                  (gen_random_uuid(), 'athlete@bup.edu.bd', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'STUDENT', 'Lt. Rahman', 'Sports enthusiast', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
                                                                                                                                                                   (SELECT university_id FROM university WHERE university_name = 'BUP'),
                                                                                                                                                                   (SELECT id FROM club WHERE name = 'BUP Sports Club'));

-- Insert Follow relationships
INSERT INTO follow (user_id, club_id, createdAt) VALUES
                                                     ((SELECT user_id FROM users WHERE email = 'rifat@diu.edu.bd'),
                                                      (SELECT id FROM club WHERE name = 'DIU Photo Club'), CURRENT_TIMESTAMP),
                                                     ((SELECT user_id FROM users WHERE email = 'sarah@diu.edu.bd'),
                                                      (SELECT id FROM club WHERE name = 'DIU Computer Club'), CURRENT_TIMESTAMP),
                                                     ((SELECT user_id FROM users WHERE email = 'captain@bup.edu.bd'),
                                                      (SELECT id FROM club WHERE name = 'BUP Leadership Club'), CURRENT_TIMESTAMP);

-- Insert Club Applications
INSERT INTO clubapplication (clubapplication_id, status, message, appliedAt, reviewedAt, student_id, club_id) VALUES
                                                                                                                  (gen_random_uuid(), 'APPROVED', 'Want to join computer club', CURRENT_TIMESTAMP - INTERVAL '7 days', CURRENT_TIMESTAMP - INTERVAL '5 days',
                                                                                                                   (SELECT user_id FROM users WHERE email = 'rifat@diu.edu.bd'),
                                                                                                                   (SELECT id FROM club WHERE name = 'DIU Computer Club')),
                                                                                                                  (gen_random_uuid(), 'PENDING', 'Interested in defense studies', CURRENT_TIMESTAMP - INTERVAL '2 days', null,
                                                                                                                   (SELECT user_id FROM users WHERE email = 'captain@bup.edu.bd'),
                                                                                                                   (SELECT id FROM club WHERE name = 'BUP Defense Club'));

-- Insert Posts
INSERT INTO post (post_id, title, content, imageUri, post_createdAt, post_updatedAt, club_id) VALUES
                                                                                                  (gen_random_uuid(), 'Programming Workshop', 'Join us every Friday at 3 PM', null, CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP - INTERVAL '3 days',
                                                                                                   (SELECT id FROM club WHERE name = 'DIU Computer Club')),
                                                                                                  (gen_random_uuid(), 'Photo Contest 2024', 'Submit your best campus photos', null, CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day',
                                                                                                   (SELECT id FROM club WHERE name = 'DIU Photo Club')),
                                                                                                  (gen_random_uuid(), 'Leadership Seminar', 'Military leadership principles', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
                                                                                                   (SELECT id FROM club WHERE name = 'BUP Leadership Club'));

-- Insert RSVPs
INSERT INTO rsvp (rsvp_id, status, rsvp_createdAt, rsvp_updatedAt, post_id, user_id) VALUES
                                                                                         (gen_random_uuid(), 'GOING', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days',
                                                                                          (SELECT post_id FROM post WHERE title = 'Programming Workshop'),
                                                                                          (SELECT user_id FROM users WHERE email = 'rifat@diu.edu.bd')),
                                                                                         (gen_random_uuid(), 'MAYBE', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day',
                                                                                          (SELECT post_id FROM post WHERE title = 'Photo Contest 2024'),
                                                                                          (SELECT user_id FROM users WHERE email = 'sarah@diu.edu.bd'));

-- Insert Notifications
INSERT INTO notification (notification_id, message, isRead, notification_createdAt, user_id, related_post_id, related_event_id) VALUES
                                                                                                                                    (gen_random_uuid(), 'Application approved!', false, CURRENT_TIMESTAMP - INTERVAL '5 days',
                                                                                                                                     (SELECT user_id FROM users WHERE email = 'rifat@diu.edu.bd'), null, null),
                                                                                                                                    (gen_random_uuid(), 'New post in Photo Club', false, CURRENT_TIMESTAMP - INTERVAL '1 day',
                                                                                                                                     (SELECT user_id FROM users WHERE email = 'rifat@diu.edu.bd'),
                                                                                                                                     (SELECT post_id FROM post WHERE title = 'Photo Contest 2024'), null);