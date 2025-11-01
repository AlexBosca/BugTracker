-- CONSTANTS FOR SEED DATA
DECLARE
  const_john_doe_id CONSTANT VARCHAR(50) := 'john.doe';
  const_jane_smith_id CONSTANT VARCHAR(50) := 'jane.smith';
  const_alex_dev_id CONSTANT VARCHAR(50) := 'alex.dev';

  const_project_bugsys CONSTANT VARCHAR(20) := 'BUGSYS';
  const_project_docs CONSTANT VARCHAR(20) := 'DOCS';
  const_project_auth CONSTANT VARCHAR(20) := 'AUTH';

  const_role_manager CONSTANT VARCHAR(20) := 'MANAGER';
  const_role_developer CONSTANT VARCHAR(20) := 'DEVELOPER';
  const_role_viewer CONSTANT VARCHAR(20) := 'VIEWER';

  const_issue_status_todo CONSTANT VARCHAR(20) := 'TO_DO';
  const_issue_status_inprogress CONSTANT VARCHAR(20) := 'IN_PROGRESS';
  const_issue_status_review CONSTANT VARCHAR(20) := 'REVIEW';
  const_issue_status_done CONSTANT VARCHAR(20) := 'DONE';
  const_issue_priority_low CONSTANT VARCHAR(20) := 'LOW';
  const_issue_priority_medium CONSTANT VARCHAR(20) := 'MEDIUM';
  const_issue_priority_high CONSTANT VARCHAR(20) := 'HIGH';

-- USERS
INSERT INTO users (user_id, first_name, last_name, email, password, global_role, enabled)
VALUES
  (const_john_doe_id, 'John', 'Doe', 'john.doe@example.com', '$2a$04$fZor7K4iO0QwoF6G0NgP4.VlXTeGQEfKGBJgQmijY9iwFO48lYL/2', 'ADMIN', 'true'),
  (const_jane_smith_id, 'Jane', 'Smith', 'jane.smith@example.com', '$2a$04$9yaaw3iQlesQA4MegURwueXhlcQ4699BkSmYlPoivplEHKoMOPAUG', 'USER', 'true'),
  (const_alex_dev_id, 'Alex', 'Developer', 'alex.dev@example.com', '$2a$04$77rWoLGov4kVGgO6CKM6i.Fwr4otKTlj8NuA6Lkj4nJuzBAQI3jGy', 'USER', 'true');

-- PROJECTS
INSERT INTO projects (project_key, name, description)
VALUES
  (const_project_bugsys, 'Bug Tracking System', 'System to manage bugs and issues.'),
  (const_project_docs, 'Documentation Portal', 'Internal technical documentation site.'),
  (const_project_auth, 'Authentication Service', 'Handles login and authentication.');

-- PROJECT ROLES
INSERT INTO project_roles (role_name, project_key)
VALUES
  (const_role_manager, const_project_bugsys),
  (const_role_developer, const_project_bugsys),
  (const_role_viewer, const_project_bugsys),
  (const_role_manager, const_project_docs),
  (const_role_viewer, const_project_docs),
  (const_role_manager, const_project_auth),
  (const_role_viewer, const_project_auth),
  (const_role_developer, const_project_auth);

-- USER PROJECT ROLES
INSERT INTO user_project_roles (user_id, project_key, role_name)
VALUES
  (const_john_doe_id, const_project_bugsys, const_role_manager),
  (const_alex_dev_id, const_project_bugsys, const_role_developer),
  (const_jane_smith_id, const_project_docs, const_role_viewer),
  (const_alex_dev_id, const_project_auth, const_role_developer),
  (const_jane_smith_id, const_project_auth, const_role_viewer),
  (const_john_doe_id, const_project_auth, const_role_manager);

-- ISSUES
INSERT INTO issues (issue_id, title, description, status, priority, project_key, assigned_user_id)
VALUES
  ('BUGSYS-1', 'Login not working', 'Login button does nothing when clicked.', const_issue_status_todo, const_issue_priority_high, const_project_bugsys, const_alex_dev_id),
  ('BUGSYS-2', 'Dashboard glitch', 'Cards overflow on small screens.', const_issue_status_inprogress, const_issue_priority_low, const_project_bugsys, const_jane_smith_id),
  ('BUGSYS-3', 'API timeout', 'API takes too long to respond.', const_issue_status_review, const_issue_priority_high, const_project_bugsys, const_john_doe_id),
  ('BUGSYS-4', 'Data not saving', 'Changes are not saved to the database.', const_issue_status_done, const_issue_priority_low, const_project_bugsys, const_alex_dev_id),
  ('DOCS-1', 'Update guide', 'Update Getting Started guide for Angular 16.', const_issue_status_todo, const_issue_priority_low, const_project_docs, const_jane_smith_id),
  ('DOCS-2', 'Fix typo in API docs', 'Correct spelling of "authorization".', const_issue_status_inprogress, const_issue_priority_low, const_project_docs, const_john_doe_id),
  ('DOCS-3', 'Add new section', 'Include section on advanced topics.', const_issue_status_review, const_issue_priority_low, const_project_docs, const_alex_dev_id),
  ('AUTH-1', 'Password reset link expired', 'User cannot reset password.', const_issue_status_todo, const_issue_priority_high, const_project_auth, const_john_doe_id),
  ('AUTH-2', 'Two-factor authentication issue', '2FA not sending codes.', const_issue_status_inprogress, const_issue_priority_high, const_project_auth, const_alex_dev_id),
  ('AUTH-3', 'Session timeout too short', 'User logged out too quickly.', const_issue_status_done, const_issue_priority_low, const_project_auth, const_jane_smith_id);
