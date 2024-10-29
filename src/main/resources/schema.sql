-- creazione db

CREATE TABLE IF NOT EXISTS INITIALIZATION_CONTROL (
                                                      initialized BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS JOB_CONFIGURATIONS (
                                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                  job_name VARCHAR(255) NOT NULL,
                                                  cron VARCHAR(255) NOT NULL,
                                                  api_url VARCHAR(255) NOT NULL,
                                                  base_url VARCHAR(255) NOT NULL,
                                                  method VARCHAR(255),
                                                  body_object TEXT
);

CREATE TABLE IF NOT EXISTS INITIALIZATION_CONTROL (
                                                      initialized BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS JOB_CONFIGURATIONS (
                                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                  job_name VARCHAR(255) NOT NULL,
                                                  cron VARCHAR(255) NOT NULL,
                                                  api_url VARCHAR(255) NOT NULL,
                                                  base_url VARCHAR(255) NOT NULL,
                                                  method VARCHAR(255),
                                                  body_object TEXT
);
