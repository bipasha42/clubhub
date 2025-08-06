-- Add any missing indexes for performance
CREATE INDEX IF NOT EXISTS idx_club_university ON club(university_id);
CREATE INDEX IF NOT EXISTS idx_club_category ON club(category);
CREATE INDEX IF NOT EXISTS idx_club_approved ON club(isApproved);
CREATE INDEX IF NOT EXISTS idx_university_country ON university(country_id);