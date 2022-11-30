CREATE TABLE IF NOT EXISTS t_user (
    user_id VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100),
    phone_number VARCHAR(20),
    create_date timestamp,
    update_date timestamp
);

CREATE TABLE IF NOT EXISTS user_role (
    role VARCHAR(50) PRIMARY KEY,
    description VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_role_mapping (
    user_role_mapping_id SERIAL PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL
);
