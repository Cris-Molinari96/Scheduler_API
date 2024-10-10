CREATE TABLE IF NOT EXISTS INITIALIZATION_CONTROL (
    initialized BOOLEAN NOT NULL
);

INSERT INTO INITIALIZATION_CONTROL (initialized)
SELECT FALSE
WHERE NOT EXISTS (SELECT 1 FROM INITIALIZATION_CONTROL);

INSERT INTO JOB_CONFIGURATIONS (job_name, cron, api_url, base_url)
SELECT 'dailyReport', '0 0 12 * * ?', '/api/daily-report', 'https://example.com'
WHERE NOT EXISTS (SELECT 1 FROM INITIALIZATION_CONTROL WHERE initialized = TRUE);

INSERT INTO JOB_CONFIGURATIONS (job_name, cron, api_url, base_url)
SELECT 'weeklyBackup', '0 0 2 ? * SUN', '/api/weekly-backup', 'https://example.com'
WHERE NOT EXISTS (SELECT 1 FROM INITIALIZATION_CONTROL WHERE initialized = TRUE);

INSERT INTO JOB_CONFIGURATIONS (job_name, cron, api_url, base_url)
SELECT 'monthlyAnalysis', '0 0 3 1 * ?', '/api/monthly-analysis', 'https://example.com'
WHERE NOT EXISTS (SELECT 1 FROM INITIALIZATION_CONTROL WHERE initialized = TRUE);

UPDATE INITIALIZATION_CONTROL SET initialized = TRUE;