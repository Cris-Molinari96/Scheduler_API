-- insert solo dati
INSERT INTO JOB_CONFIGURATIONS (job_name, cron, api_url, base_url)
VALUES ('infoCamere-report', '*/30 * * * * *', '/test1', 'http://localhost:8080/api-test');

INSERT INTO JOB_CONFIGURATIONS (job_name, cron, api_url, base_url)
VALUES ('frpc-report', '*/30 * * * * *', '/test2', 'http://localhost:8080/api-test');

INSERT INTO JOB_CONFIGURATIONS (job_name, cron, api_url, base_url)
VALUES ('cdor-report', '*/30 * * * * *', '/test3', 'http://localhost:8080/api-test');

