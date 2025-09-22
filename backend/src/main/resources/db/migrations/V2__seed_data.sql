-- USERS
INSERT INTO users (user_id, first_name, last_name, email, password, global_role, enabled)
VALUES
  ('john.doe', 'John', 'Doe', 'john.doe@example.com', 'password123', 'ADMIN', 'true'),
  ('jane.smith', 'Jane', 'Smith', 'jane.smith@example.com', 'password123', 'USER', 'true'),
  ('alex.dev', 'Alex', 'Developer', 'alex.dev@example.com', 'password123', 'USER', 'true');

-- PROJECTS
INSERT INTO projects (project_key, name, description)
VALUES
  ('BUGSYS', 'Bug Tracking System', 'System to manage bugs and issues.'),
  ('DOCS', 'Documentation Portal', 'Internal technical documentation site.'),
  ('AUTH', 'Authentication Service', 'Handles login and authentication.');

-- PROJECT ROLES
INSERT INTO project_roles (role_name, project_key)
VALUES
  ('MANAGER', 'BUGSYS'),
  ('DEVELOPER', 'BUGSYS'),
  ('VIEWER', 'BUGSYS'),
  ('MANAGER', 'DOCS'),
  ('VIEWER', 'DOCS'),
  ('MANAGER', 'AUTH'),
  ('VIEWER', 'AUTH'),
  ('DEVELOPER', 'AUTH');

-- USER PROJECT ROLES
INSERT INTO user_project_roles (user_id, project_key, role_name)
VALUES
  ('john.doe', 'BUGSYS', 'MANAGER'),
  ('alex.dev', 'BUGSYS', 'DEVELOPER'),
  ('jane.smith', 'DOCS', 'VIEWER'),
  ('alex.dev', 'AUTH', 'DEVELOPER'),
  ('jane.smith', 'AUTH', 'VIEWER'),
  ('john.doe', 'AUTH', 'MANAGER');

-- ISSUES
INSERT INTO issues (issue_id, title, description, status, priority, project_key, assigned_user_id)
VALUES
  ('BUGSYS-1', 'Login not working', 'Login button does nothing when clicked.', 'TO_DO', 'HIGH', 'BUGSYS', 'alex.dev'),
  ('BUGSYS-2', 'Dashboard glitch', 'Cards overflow on small screens.', 'IN_PROGRESS', 'MEDIUM', 'BUGSYS', 'jane.smith'),
  ('BUGSYS-3', 'API timeout', 'API takes too long to respond.', 'REVIEW', 'HIGH', 'BUGSYS', 'john.doe'),
  ('BUGSYS-4', 'Data not saving', 'Changes are not saved to the database.', 'DONE', 'LOW', 'BUGSYS', 'alex.dev'),
  ('DOCS-1', 'Update guide', 'Update Getting Started guide for Angular 16.', 'TO_DO', 'LOW', 'DOCS', 'jane.smith'),
  ('DOCS-2', 'Fix typo in API docs', 'Correct spelling of "authorization".', 'IN_PROGRESS', 'LOW', 'DOCS', 'john.doe'),
  ('DOCS-3', 'Add new section', 'Include section on advanced topics.', 'REVIEW', 'MEDIUM', 'DOCS', 'alex.dev'),
  ('AUTH-1', 'Password reset link expired', 'User cannot reset password.', 'TO_DO', 'HIGH', 'AUTH', 'john.doe'),
  ('AUTH-2', 'Two-factor authentication issue', '2FA not sending codes.', 'IN_PROGRESS', 'HIGH', 'AUTH', 'alex.dev'),
  ('AUTH-3', 'Session timeout too short', 'User logged out too quickly.', 'DONE', 'MEDIUM', 'AUTH', 'jane.smith');
