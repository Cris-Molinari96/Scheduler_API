-- insert solo dati
INSERT INTO JOB_CONFIGURATIONS (job_name, cron, api_url, base_url, method)
VALUES ('infoCamere-report', '*/30 * * * * *', '/test1', 'http://localhost:8080/api-test', 'get');

INSERT INTO JOB_CONFIGURATIONS (job_name, cron, api_url, base_url, method)
VALUES ('frpc-report', '*/30 * * * * *', '/test2', 'http://localhost:8080/api-test', 'get');

INSERT INTO JOB_CONFIGURATIONS (job_name, cron, api_url, base_url, method)
VALUES ('cdor-report', '*/30 * * * * *', '/test3', 'http://localhost:8080/api-test', 'get');

INSERT INTO JOB_CONFIGURATIONS (job_name, cron, api_url, base_url, method)
VALUES ('post-job', '*/30 * * * * *', '/test-post', 'http://localhost:8080/api-test', 'post');

