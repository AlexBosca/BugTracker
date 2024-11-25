-- Create Users Table
CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    global_role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Projects Table
CREATE TABLE projects(
    id SERIAL PRIMARY KEY,
    project_key VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Issues Table
CREATE TABLE issues(
    id SERIAL PRIMARY KEY,
    issue_id VARCHAR(50) UNIQUE NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    project_key VARCHAR(50) NOT NULL,
    assigned_user_id VARCHAR(50),
    FOREIGN KEY (project_key) REFERENCES projects(project_key),
    FOREIGN KEY (assigned_user_id) REFERENCES users(user_id)
);

-- Create Project Roles Table
CREATE TABLE project_roles(
    id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL,
    project_key VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_key) REFERENCES projects(project_key)
);

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