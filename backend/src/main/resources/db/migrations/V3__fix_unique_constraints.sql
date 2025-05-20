-- Drop old constraint (adjust name if different)
-- ALTER TABLE project_roles DROP CONSTRAINT project_roles_role_name_key;

-- -- Add composite unique constraint
-- ALTER TABLE project_roles
--   ADD CONSTRAINT unique_project_role UNIQUE (project_key, role_name);
