-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Country table for club search
CREATE TABLE Country (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- University table
CREATE TABLE University (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(200) NOT NULL,
    country_id INTEGER NOT NULL REFERENCES Country(id) ON DELETE RESTRICT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT university_unique_name_country UNIQUE (name, country_id)
);

-- User table with different roles
CREATE TABLE "User" (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('UNIVERSITY_ADMIN', 'CLUB_ADMIN', 'STUDENT')),
    university_id UUID REFERENCES University(id) ON DELETE CASCADE,
    profile_picture_url VARCHAR(255),
    bio TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Club table
CREATE TABLE Club (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(200) NOT NULL,
    description TEXT,
    university_id UUID NOT NULL REFERENCES University(id) ON DELETE CASCADE,
    admin_id UUID NOT NULL REFERENCES "User"(id) ON DELETE RESTRICT,
    logo_url VARCHAR(255),
    banner_url VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT club_unique_name_university UNIQUE (name, university_id)
);

-- Club member junction table
CREATE TABLE ClubMember (
    id SERIAL PRIMARY KEY,
    club_id UUID NOT NULL REFERENCES Club(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES "User"(id) ON DELETE CASCADE,
    member_since TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_club_member UNIQUE (club_id, user_id)
);

-- Club application table
CREATE TABLE ClubApplication (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    club_id UUID NOT NULL REFERENCES Club(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES "User"(id) ON DELETE CASCADE,
    application_text TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED')),
    applied_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP WITH TIME ZONE,
    processed_by UUID REFERENCES "User"(id) ON DELETE SET NULL,
    CONSTRAINT unique_application UNIQUE (club_id, user_id)
);

-- Post table
CREATE TABLE Post (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    club_id UUID NOT NULL REFERENCES Club(id) ON DELETE CASCADE,
    author_id UUID NOT NULL REFERENCES "User"(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Post likes junction table
CREATE TABLE PostLikes (
    id SERIAL PRIMARY KEY,
    post_id UUID NOT NULL REFERENCES Post(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES "User"(id) ON DELETE CASCADE,
    liked_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_post_like UNIQUE (post_id, user_id)
);

-- Post comments table
CREATE TABLE PostComment (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    post_id UUID NOT NULL REFERENCES Post(id) ON DELETE CASCADE,
    author_id UUID NOT NULL REFERENCES "User"(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Follow table (students following clubs)
CREATE TABLE Follow (
    id SERIAL PRIMARY KEY,
    club_id UUID NOT NULL REFERENCES Club(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES "User"(id) ON DELETE CASCADE,
    followed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_follow UNIQUE (club_id, user_id)
);

-- Notification table for application updates
CREATE TABLE Notification (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    application_id UUID NOT NULL REFERENCES ClubApplication(id) ON DELETE CASCADE,
    recipient_id UUID NOT NULL REFERENCES "User"(id) ON DELETE CASCADE,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP WITH TIME ZONE
);

-- Indexes for better performance
CREATE INDEX idx_club_university ON Club(university_id);
CREATE INDEX idx_club_admin ON Club(admin_id);
CREATE INDEX idx_user_university ON "User"(university_id);
CREATE INDEX idx_post_club ON Post(club_id);
CREATE INDEX idx_post_author ON Post(author_id);
CREATE INDEX idx_application_club ON ClubApplication(club_id);
CREATE INDEX idx_application_user ON ClubApplication(user_id);
CREATE INDEX idx_notification_recipient ON Notification(recipient_id);
CREATE INDEX idx_notification_application ON Notification(application_id);