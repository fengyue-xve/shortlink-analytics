CREATE DATABASE IF NOT EXISTS shortlink_analytics DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shortlink_analytics;

DROP TABLE IF EXISTS visit_log;
DROP TABLE IF EXISTS short_link;
DROP TABLE IF EXISTS campaign;
DROP TABLE IF EXISTS sys_user;

CREATE TABLE sys_user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50) DEFAULT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE campaign (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    target_audience VARCHAR(100) DEFAULT NULL,
    channel VARCHAR(100) DEFAULT NULL,
    description VARCHAR(500) DEFAULT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    start_time DATETIME DEFAULT NULL,
    end_time DATETIME DEFAULT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投放活动表';

CREATE TABLE short_link (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    campaign_id BIGINT DEFAULT NULL,
    original_url VARCHAR(1000) NOT NULL,
    short_code VARCHAR(16) NOT NULL,
    title VARCHAR(100) DEFAULT NULL,
    status TINYINT NOT NULL DEFAULT 1 COMMENT '0禁用 1启用',
    expire_time DATETIME DEFAULT NULL,
    visit_count BIGINT NOT NULL DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_short_code (short_code),
    KEY idx_user_id (user_id),
    KEY idx_campaign_id (campaign_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短链接表';

CREATE TABLE visit_log (
    id BIGINT NOT NULL AUTO_INCREMENT,
    link_id BIGINT NOT NULL,
    short_code VARCHAR(16) NOT NULL,
    ip_address VARCHAR(64) DEFAULT NULL,
    user_agent VARCHAR(500) DEFAULT NULL,
    referer VARCHAR(500) DEFAULT NULL,
    visit_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_link_id (link_id),
    KEY idx_short_code (short_code),
    KEY idx_visit_time (visit_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='访问日志表';
