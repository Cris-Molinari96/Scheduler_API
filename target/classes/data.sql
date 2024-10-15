-- insert solo dati

INSERT INTO JOB_CONFIGURATIONS (job_name, cron, api_url, base_url)
VALUES ('dailyReport', '0 0 12 * * ?', '/api/daily-report', 'https://example.com');

INSERT INTO JOB_CONFIGURATIONS (job_name, cron, api_url, base_url)
VALUES ('weeklyBackup', '0 0 2 ? * SUN', '/api/weekly-backup', 'https://example.com');

INSERT INTO JOB_CONFIGURATIONS (job_name, cron, api_url, base_url)
VALUES ('monthlyAnalysis', '0 0 3 1 * ?', '/api/monthly-analysis', 'https://example.com');

