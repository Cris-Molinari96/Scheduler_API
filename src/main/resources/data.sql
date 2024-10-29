INSERT INTO JOB_CONFIGURATIONS (job_name, cron, api_url, base_url, method, body_object)
VALUES ('infoCamere-report', '*/30 * * * * *', '/test1', 'http://localhost:8080/api-test', 'get', null);

INSERT INTO JOB_CONFIGURATIONS (job_name, cron, api_url, base_url, method, body_object)
VALUES ('frpc-report', '*/30 * * * * *', '/test2', 'http://localhost:8080/api-test', 'get', null);

INSERT INTO JOB_CONFIGURATIONS (job_name, cron, api_url, base_url, method, body_object)
VALUES ('cdor-report', '*/30 * * * * *', '/test3', 'http://localhost:8080/api-test', 'get', null);

INSERT INTO JOB_CONFIGURATIONS (job_name, cron, api_url, base_url, method, body_object)
VALUES ('post-job', '*/30 * * * * *', '/test-body-post', 'http://localhost:8080/api-test', 'post',
        '{
            "requestData": {
                "customer": {
                    "id": "CUST123",
                    "name": "Mario Rossi",
                    "email": "mario.rossi@example.com"
                },
                "order": {
                    "items": [
                        {
                            "productId": "P001",
                            "quantity": 2,
                            "price": 29.99
                        },
                        {
                            "productId": "P002",
                            "quantity": 1,
                            "price": 49.99
                        }
                    ],
                    "totalAmount": 109.97,
                    "currency": "EUR"
                }
            }
        }');