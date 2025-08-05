-- =========================
-- 1. COUNTRY TABLE
-- =========================
CREATE TABLE country (
                         country_id UUID PRIMARY KEY,
                         country_name VARCHAR(255) NOT NULL
);

-- =========================
-- 2. UNIVERSITY TABLE
-- =========================
CREATE TABLE university (
                            university_id UUID PRIMARY KEY,
                            university_name VARCHAR(255) NOT NULL,
                            country_id UUID REFERENCES country(country_id)
);

-- =========================
-- 3. CLUB TABLE
-- =========================
CREATE TABLE club (
                      id UUID PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      description TEXT,
                      category VARCHAR(255),
                      contactEmail VARCHAR(255),
                      isApproved BOOLEAN,
                      createdAt TIMESTAMP,
                      updatedAt TIMESTAMP,
                      university_id UUID REFERENCES university(university_id)
);

-- =========================
-- 4. USER TABLE
-- =========================
CREATE TABLE users (
                       user_id UUID PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       passwordHash VARCHAR(255) NOT NULL,
                       role VARCHAR(50),
                       user_name VARCHAR(255),
                       bio TEXT,
                       profilePictureUri VARCHAR(255),
                       isVerified BOOLEAN,
                       user_createdAt TIMESTAMP,
                       user_updatedAt TIMESTAMP,
                       university_id UUID REFERENCES university(university_id),
                       club_id UUID REFERENCES club(id)
);

-- =========================
-- 5. FOLLOW TABLE
-- (Composite PK: user_id + club_id)
-- =========================
CREATE TABLE follow (
                        user_id UUID REFERENCES users(user_id),
                        club_id UUID REFERENCES club(id),
                        createdAt TIMESTAMP,
                        PRIMARY KEY (user_id, club_id)
);

-- =========================
-- 6. CLUBAPPLICATION TABLE
-- =========================
CREATE TABLE clubapplication (
                                 clubapplication_id UUID PRIMARY KEY,
                                 status VARCHAR(50),
                                 message TEXT,
                                 appliedAt TIMESTAMP,
                                 reviewedAt TIMESTAMP,
                                 student_id UUID REFERENCES users(user_id),
                                 club_id UUID REFERENCES club(id)
);

-- =========================
-- 7. POST TABLE
-- =========================
CREATE TABLE post (
                      post_id UUID PRIMARY KEY,
                      title VARCHAR(255),
                      content TEXT,
                      imageUri VARCHAR(255),
                      post_createdAt TIMESTAMP,
                      post_updatedAt TIMESTAMP,
                      club_id UUID REFERENCES club(id)
);

-- =========================
-- 8. RSVP TABLE
-- =========================
CREATE TABLE rsvp (
                      rsvp_id UUID PRIMARY KEY,
                      status VARCHAR(50),
                      rsvp_createdAt TIMESTAMP,
                      rsvp_updatedAt TIMESTAMP,
                      post_id UUID REFERENCES post(post_id),
                      user_id UUID REFERENCES users(user_id)
);

-- =========================
-- 9. NOTIFICATION TABLE
-- =========================
CREATE TABLE notification (
                              notification_id UUID PRIMARY KEY,
                              message TEXT,
                              isRead BOOLEAN,
                              notification_createdAt TIMESTAMP,
                              user_id UUID REFERENCES users(user_id),
                              related_post_id UUID REFERENCES post(post_id),
                              related_event_id UUID REFERENCES rsvp(rsvp_id)
);