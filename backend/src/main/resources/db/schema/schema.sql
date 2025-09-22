-- Drop Users table if exists
DROP TABLE IF EXISTS users CASCADE;

-- Create Users Table
CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(50) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    global_role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    enabled BOOLEAN NOT NULL
);

-- Drop Projects table if exists
DROP TABLE IF EXISTS projects CASCADE;

-- Create Projects Table
CREATE TABLE projects(
    id SERIAL PRIMARY KEY,
    project_key VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Drop Issues table if exists
DROP TABLE IF EXISTS issues CASCADE;

-- Create Issues Table
CREATE TABLE issues(
    id SERIAL PRIMARY KEY,
    issue_id VARCHAR(50) UNIQUE NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
--    status VARCHAR(50) NOT NULL,
    status VARCHAR(50),
    priority VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    project_key VARCHAR(50),
    assigned_user_id VARCHAR(50),
    FOREIGN KEY (project_key) REFERENCES projects(project_key),
    FOREIGN KEY (assigned_user_id) REFERENCES users(user_id)
);

-- Drop Project Roles table if exists
DROP TABLE IF EXISTS project_roles CASCADE;

-- Create Project Roles Table
CREATE TABLE project_roles(
    id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    project_key VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_key) REFERENCES projects(project_key)
);

-- Drop User Project Roles table if exists
DROP TABLE IF EXISTS user_project_roles CASCADE;

-- Create User Project Roles Table
CREATE TABLE user_project_roles(
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    project_key VARCHAR(50) NOT NULL,
    role_name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (project_key) REFERENCES projects(project_key),
    FOREIGN KEY (role_name) REFERENCES project_roles(role_name)
);