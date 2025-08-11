-- Create Follow table
DROP TABLE IF EXISTS Follow;

CREATE TABLE Follow (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    club_id UUID NOT NULL REFERENCES Club(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    followed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_follow UNIQUE (club_id, user_id)
);

-- Insert default users from SecurityConfig
INSERT INTO users (id, email, password_hash, first_name, last_name, role, created_at, updated_at)
VALUES 
    (gen_random_uuid(), 'nibir@example.com', '$2a$10$RB6UanyfkSYFoZL/CzlPDOYy8H3Y0IGhOTQQTKbwm7fOiNyPh8wBm', 'Nibir', 'Admin', 'UNIVERSITY_ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'rifat@example.com', '$2a$10$bPmXUcYbO3Vh3.Qr69IgqOV9P92J1lXzepkY4VKLSBqCvoABe5N0S', 'Rifat', 'User', 'STUDENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'bup@example.com', '$2a$10$bPmXUcYbO3Vh3.Qr69IgqOV9P92J1lXzepkY4VKLSBqCvoABe5N0S', 'Bup', 'User', 'STUDENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
