CREATE SCHEMA leetcode;

-- Core tables
CREATE TABLE leetcode.users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255),
    birthday VARCHAR(255),
    avatar VARCHAR(255),
    country VARCHAR(255),
    school VARCHAR(255),
    github VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE leetcode.total_problems (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES leetcode.users(id),
    total_count INTEGER DEFAULT 0,
    easy_count INTEGER DEFAULT 0,
    medium_count INTEGER DEFAULT 0,
    hard_count INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE leetcode.total_solved (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES leetcode.users(id),
    total_count INTEGER DEFAULT 0,
    easy_count INTEGER DEFAULT 0,
    medium_count INTEGER DEFAULT 0,
    hard_count INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE leetcode.submission_stats (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES leetcode.users(id),
    points INTEGER DEFAULT 0,
    reputation INTEGER DEFAULT 0,
    global_ranking INTEGER,
    acceptance_rate DECIMAL(5,2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
