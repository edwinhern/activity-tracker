CREATE SCHEMA leetcode;

-- Core tables
CREATE TABLE leetcode.users (
    id BIGSERIAL PRIMARY KEY,
    leetcode_username VARCHAR(255) UNIQUE NOT NULL,
    total_solved INTEGER DEFAULT 0,
    total_questions INTEGER DEFAULT 0,
    total_easy INTEGER DEFAULT 0,
    total_medium INTEGER DEFAULT 0,
    total_hard INTEGER DEFAULT 0,
    easy_solved INTEGER DEFAULT 0,
    medium_solved INTEGER DEFAULT 0,
    hard_solved INTEGER DEFAULT 0,
    acceptance_rate DECIMAL(5,2),
    contribution_points INTEGER DEFAULT 0,
    reputation INTEGER DEFAULT 0,
    global_ranking INTEGER,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE leetcode.problems (
    id BIGSERIAL PRIMARY KEY,
    leetcode_id INTEGER UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    difficulty VARCHAR(10) NOT NULL,
    category VARCHAR(50),
    acceptance_rate DECIMAL(5,2),
    likes INTEGER DEFAULT 0,
    dislikes INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT valid_difficulty CHECK (difficulty IN ('EASY', 'MEDIUM', 'HARD'))
);

CREATE TABLE leetcode.submissions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES leetcode.users(id),
    problem_id BIGINT REFERENCES leetcode.problems(id),
    submission_id VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    language VARCHAR(50) NOT NULL,
    runtime_ms INTEGER,
    memory_kb INTEGER,
    runtime_percentile DECIMAL(5,2),
    memory_percentile DECIMAL(5,2),
    submission_date TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT valid_status CHECK (status IN ('ACCEPTED', 'WRONG_ANSWER', 'TIME_LIMIT_EXCEEDED', 'RUNTIME_ERROR'))
);

-- Supporting tables
CREATE TABLE leetcode.problem_topics (
    problem_id BIGINT REFERENCES leetcode.problems(id),
    topic VARCHAR(50) NOT NULL,
    PRIMARY KEY (problem_id, topic)
);

CREATE TABLE leetcode.user_problem_status (
    user_id BIGINT REFERENCES leetcode.users(id),
    problem_id BIGINT REFERENCES leetcode.problems(id),
    status VARCHAR(20) NOT NULL,
    favorite BOOLEAN DEFAULT false,
    notes TEXT,
    last_attempted TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (user_id, problem_id),
    CONSTRAINT valid_status CHECK (status IN ('SOLVED', 'ATTEMPTED', 'TODO'))
);

-- Indexes
CREATE INDEX idx_submissions_user_date ON leetcode.submissions(user_id, submission_date);
CREATE INDEX idx_problems_difficulty ON leetcode.problems(difficulty);
CREATE INDEX idx_user_problem_status ON leetcode.user_problem_status(user_id, status);
