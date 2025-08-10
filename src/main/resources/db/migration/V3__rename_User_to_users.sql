ALTER TABLE "User" RENAME TO users;
-- optional: rename index to keep naming consistent
ALTER INDEX IF EXISTS idx_user_university RENAME TO idx_users_university;