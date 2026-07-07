USE shortlink_analytics;

-- 默认账号：admin / 123456；启动后 DataInitializer 会自动转为 BCrypt
INSERT INTO sys_user (username, password, nickname, status)
VALUES ('admin', '123456', '管理员', 1);
