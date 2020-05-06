/*
Navicat MySQL Data Transfer

Source Server         : 本地mysql
Source Server Version : 50727
Source Host           : localhost:3306
Source Database       : apimonitor

Target Server Type    : MYSQL
Target Server Version : 50727
File Encoding         : 65001

Date: 2019-11-01 19:06:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for http_request
-- ----------------------------
DROP TABLE IF EXISTS `http_request`;
CREATE TABLE `http_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pguid` varchar(255) NOT NULL COMMENT '父主键（即http_sequence表的guid）',
  `guid` varchar(255) NOT NULL COMMENT '主键',
  `sort` int(11) NOT NULL COMMENT '序号',
  `url` varchar(255) NOT NULL COMMENT '地址',
  `httpMethod` varchar(255) NOT NULL COMMENT 'HTTP类型（GET, HEAD, POST, PUT, DELETE）',
  `paramType` varchar(255) DEFAULT '' COMMENT '参数类型',
  `headers` varchar(2000) DEFAULT NULL COMMENT '请求头部，格式（key::value\\nkey::value）',
  `parameters` varchar(2000) DEFAULT NULL COMMENT '请求参数，格式（key::value\\nkey::value）',
  `maxConnectionSeconds` int(11) DEFAULT '0' COMMENT '最大连接时间',
  `conditionType` varchar(255) DEFAULT NULL COMMENT '结果校验类型（CONTAINS, DOESNT_CONTAIN, STATUSCODE, DEFAULT）',
  `condition` varchar(2000) DEFAULT NULL COMMENT '结果校验内容',
  `resultType` varchar(255) DEFAULT 'JSON' COMMENT '返回结果的格式（XML, JSON）',
  `variables` varchar(2000) DEFAULT NULL COMMENT '变量定义，格式（key::value\\nkey::value）',
  `remark` text COMMENT '备注',
  `archived` tinyint(1) DEFAULT '0' COMMENT '存档（0-有效，1-删除）',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `token` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `guid` (`guid`),
  KEY `guid_index` (`guid`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8 COMMENT='http请求表';

-- ----------------------------
-- Records of http_request
-- ----------------------------
INSERT INTO `http_request` VALUES ('81', 'ae95e0ea647b4722b88aca589bf2a070', '7c2eb22da69840a1b3c59e48e85129b9', '1', 'http://154.223.71.76:29030/uaa/user?access_token=d3c1975e-3d30-4603-a3e2-e3694d2dff8e', 'GET', '', '', '', '0', 'STATUSCODE', '200', 'JSON', null, null, '1', '2019-10-21 14:59:39', null);
INSERT INTO `http_request` VALUES ('82', '57449c3c1484446485b894b510c96254', '9ea381f16a4c468983e62298619823fb', '1', 'http://154.223.71.76:29030/scmjAI/scmj/initcard', 'POST', 'raw', 'Content-Type::application/json', '{\r\n    \"gameId\": \"7bb242738df34d3b9696f4ddc1623961\",\r\n    \"dealer\": 0,\r\n    \"exchangeType\":2,\r\n    \"grade\":[-1,-1,1,2]  \r\n}', '0', 'STATUSCODE', '200', 'JSON', null, null, '0', '2019-10-24 16:14:13', null);
INSERT INTO `http_request` VALUES ('83', '4159edea7199446b9c19c7a8f8318415', '2aaf823056cc4f27a6445901d0e07bd4', '1', 'http://ddd', 'GET', 'raw', '', 'aaaa', '0', 'DEFAULT', '', 'JSON', null, null, '0', '2019-10-25 18:17:34', null);
INSERT INTO `http_request` VALUES ('84', 'f42ed4f975c04ae98795ccef95155a02', '7aca8345663d4d9793a6a54fc5490535', '1', 'http://154.223.71.76:29030/uaa/oauth/token', 'POST', 'x-www-form-urlencoded', 'Authorization::Basic d2ViQXBwOjEyMzQ1Ng==\r\nContent-Type::application/x-www-form-urlencoded', 'grant_type::refresh_token\r\nrefresh_token::519b0db9-80a8-4f4e-b421-e10ec72367e8', '0', 'STATUSCODE', '200', 'JSON', null, null, '0', '2019-10-28 16:37:37', null);
INSERT INTO `http_request` VALUES ('85', '374d4cdaf62e4280ba01070ae03e5a66', 'bce3ba929f884dfa8a742f3283aa83de', '1', 'http://www.baidu.com', 'GET', null, null, null, '30', 'DEFAULT', null, null, null, null, '1', '2019-10-30 10:21:40', '');

-- ----------------------------
-- Table structure for http_request_log
-- ----------------------------
DROP TABLE IF EXISTS `http_request_log`;
CREATE TABLE `http_request_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL COMMENT '父id（http_sequence_log表的id）',
  `ppguid` varchar(255) NOT NULL COMMENT '父主键（http_sequence表guid）',
  `pguid` varchar(255) NOT NULL COMMENT '父主键（http_request表guid）',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0-失败；1-成功）',
  `costTime` int(11) DEFAULT '0' COMMENT '请求耗时',
  `statusCode` varchar(255) DEFAULT NULL COMMENT '响应状态码',
  `responseBody` text COMMENT '响应结果',
  `log` text COMMENT '请求日志',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `guid_index` (`pguid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4717 DEFAULT CHARSET=utf8 COMMENT='http请求日志表';

-- ----------------------------
-- Records of http_request_log
-- ----------------------------
INSERT INTO `http_request_log` VALUES ('4713', '3661', 'f42ed4f975c04ae98795ccef95155a02', '7aca8345663d4d9793a6a54fc5490535', '1', '421', '200', '{\"access_token\":\"95909340-c8cf-442f-976c-f88a512deb55\",\"token_type\":\"bearer\",\"refresh_token\":\"519b0db9-80a8-4f4e-b421-e10ec72367e8\",\"expires_in\":43199,\"scope\":\"app\"}', null, '2019-10-30 09:33:41');
INSERT INTO `http_request_log` VALUES ('4714', '3662', 'f42ed4f975c04ae98795ccef95155a02', '7aca8345663d4d9793a6a54fc5490535', '1', '433', '200', '{\"access_token\":\"4267a720-3198-4abe-a21a-efe7ddab004e\",\"token_type\":\"bearer\",\"refresh_token\":\"519b0db9-80a8-4f4e-b421-e10ec72367e8\",\"expires_in\":43199,\"scope\":\"app\"}', null, '2019-10-30 09:35:00');
INSERT INTO `http_request_log` VALUES ('4715', '3663', 'f42ed4f975c04ae98795ccef95155a02', '7aca8345663d4d9793a6a54fc5490535', '1', '455', '200', '{\"access_token\":\"98c61a54-b63e-4534-a7d2-735009f563ec\",\"token_type\":\"bearer\",\"refresh_token\":\"519b0db9-80a8-4f4e-b421-e10ec72367e8\",\"expires_in\":43199,\"scope\":\"app\"}', null, '2019-10-30 09:40:00');

-- ----------------------------
-- Table structure for http_sequence
-- ----------------------------
DROP TABLE IF EXISTS `http_sequence`;
CREATE TABLE `http_sequence` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `guid` varchar(255) NOT NULL COMMENT '主键',
  `group` varchar(255) NOT NULL COMMENT '所属系统',
  `type` varchar(30) NOT NULL COMMENT '类型（SINGLE, SEQUENCE）',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `remark` text COMMENT '备注',
  `jobName` varchar(255) DEFAULT NULL COMMENT 'job名称',
  `enabled` tinyint(1) DEFAULT '0' COMMENT '是否启动监控（0-不启动，1-启动）',
  `frequency` varchar(255) DEFAULT NULL COMMENT '监控频率，默认THIRTY30秒',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `archived` tinyint(1) DEFAULT '0' COMMENT '存档（0-有效，1-删除）',
  `responseTime` int(4) DEFAULT '0' COMMENT '超时报警，单位ms',
  PRIMARY KEY (`id`),
  UNIQUE KEY `guid` (`guid`),
  KEY `guid_index` (`guid`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8 COMMENT='http序列表';

-- ----------------------------
-- Records of http_sequence
-- ----------------------------
INSERT INTO `http_sequence` VALUES ('66', 'ae95e0ea647b4722b88aca589bf2a070', 'AI监控测试', 'SINGLE', '川麻API可用性', 'yeyu@aiqiaiqi.cn', null, '0', 'FIVE', '2019-10-21 14:59:39', '1', '1000');
INSERT INTO `http_sequence` VALUES ('67', '57449c3c1484446485b894b510c96254', 'AI监控测试', 'SINGLE', '川麻起手牌接口', '762383979@qq.com', null, '0', 'SIXTY', '2019-10-24 16:14:13', '0', '1000');
INSERT INTO `http_sequence` VALUES ('74', 'f090c746c98e49f19d5656f185b7d647', 'AI监控测试', 'SINGLE', 'AAA', '', null, '0', 'THIRTY', '2019-10-25 18:07:14', '1', '0');
INSERT INTO `http_sequence` VALUES ('75', '6150530f2dec468fb14fca28c0f96f8b', 'AI监控测试', 'SINGLE', 'AAA', '', null, '0', 'THIRTY', '2019-10-25 18:10:00', '1', '0');
INSERT INTO `http_sequence` VALUES ('76', '43e10fd5106c4f98abc0f9ca3d7b8bf5', 'AI监控测试', 'SINGLE', 'ddd', '', null, '0', 'THIRTY', '2019-10-25 18:10:43', '1', '0');
INSERT INTO `http_sequence` VALUES ('77', '2b541f869b974986abee54e94a66d3c9', 'AI监控测试', 'SINGLE', 'AAA', '', null, '0', 'THIRTY', '2019-10-25 18:11:26', '1', '0');
INSERT INTO `http_sequence` VALUES ('78', '4159edea7199446b9c19c7a8f8318415', 'AI监控测试', 'SINGLE', 'AAA', '', null, '0', 'THIRTY', '2019-10-25 18:17:34', '0', '0');
INSERT INTO `http_sequence` VALUES ('79', 'f42ed4f975c04ae98795ccef95155a02', 'AI监控测试', 'SINGLE', '更新token', '', null, '0', 'FIVE_MINUTES', '2019-10-28 16:37:37', '0', '2000');
INSERT INTO `http_sequence` VALUES ('80', '374d4cdaf62e4280ba01070ae03e5a66', 'test', 'SINGLE', '374d4cdaf62e4280ba01070ae03e5a66', null, null, '0', 'THIRTY', '2019-10-30 10:21:40', '1', '0');

-- ----------------------------
-- Table structure for http_sequence_log
-- ----------------------------
DROP TABLE IF EXISTS `http_sequence_log`;
CREATE TABLE `http_sequence_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pguid` varchar(255) NOT NULL COMMENT '父主键（http_sequence表guid）',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0-失败；1-成功）',
  `costTime` int(11) DEFAULT '0' COMMENT '请求耗时',
  `log` text COMMENT '请求日志',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `guid_index` (`pguid`)
) ENGINE=InnoDB AUTO_INCREMENT=3665 DEFAULT CHARSET=utf8 COMMENT='http序列日志表';

-- ----------------------------
-- Records of http_sequence_log
-- ----------------------------
INSERT INTO `http_sequence_log` VALUES ('3661', 'f42ed4f975c04ae98795ccef95155a02', '1', '421', '', '2019-10-30 09:33:41');
INSERT INTO `http_sequence_log` VALUES ('3662', 'f42ed4f975c04ae98795ccef95155a02', '1', '433', '', '2019-10-30 09:35:00');
INSERT INTO `http_sequence_log` VALUES ('3663', 'f42ed4f975c04ae98795ccef95155a02', '1', '455', '', '2019-10-30 09:40:00');

-- ----------------------------
-- Table structure for http_system
-- ----------------------------
DROP TABLE IF EXISTS `http_system`;
CREATE TABLE `http_system` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '系统名称',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_index` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8 COMMENT='系统名称表';

-- ----------------------------
-- Records of http_system
-- ----------------------------
INSERT INTO `http_system` VALUES ('87', 'AI监控测试', '2019-10-21 11:35:50');
INSERT INTO `http_system` VALUES ('88', 'jextion1', '2019-10-30 10:22:10');

-- ----------------------------
-- Table structure for http_token
-- ----------------------------
DROP TABLE IF EXISTS `http_token`;
CREATE TABLE `http_token` (
  `token` varchar(255) DEFAULT '',
  `refresh` varchar(255) DEFAULT '',
  `expired` int(11) DEFAULT '0',
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of http_token
-- ----------------------------
INSERT INTO `http_token` VALUES ('98c61a54-b63e-4534-a7d2-735009f563ec', '519b0db9-80a8-4f4e-b421-e10ec72367e8', '43199', '1');

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------
INSERT INTO `qrtz_cron_triggers` VALUES ('scheduler', 'monitoring-instance-57449c3c1484446485b894b510c96254', 'DEFAULT', '0 0/1 * * * ?', 'Asia/Taipei');
INSERT INTO `qrtz_cron_triggers` VALUES ('scheduler', 'monitoring-instance-f42ed4f975c04ae98795ccef95155a02', 'DEFAULT', '0 0/5 * * * ?', 'Asia/Taipei');

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------
INSERT INTO `qrtz_job_details` VALUES ('scheduler', 'monitoring-instance-57449c3c1484446485b894b510c96254', 'DEFAULT', null, 'HttpMonitoringJob', '0', '1', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000C696E7374616E63654775696474002035373434396333633134383434343634383562383934623531306339363235347800);
INSERT INTO `qrtz_job_details` VALUES ('scheduler', 'monitoring-instance-f42ed4f975c04ae98795ccef95155a02', 'DEFAULT', null, 'HttpMonitoringJob', '0', '1', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000C696E7374616E63654775696474002066343265643466393735633034616539383739356363656639353135356130327800);

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('scheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`),
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------
INSERT INTO `qrtz_triggers` VALUES ('scheduler', 'monitoring-instance-57449c3c1484446485b894b510c96254', 'DEFAULT', 'monitoring-instance-57449c3c1484446485b894b510c96254', 'DEFAULT', null, '1572352200000', '1572352140000', '5', 'PAUSED', 'CRON', '1572343108000', '0', null, '0', '');
INSERT INTO `qrtz_triggers` VALUES ('scheduler', 'monitoring-instance-f42ed4f975c04ae98795ccef95155a02', 'DEFAULT', 'monitoring-instance-f42ed4f975c04ae98795ccef95155a02', 'DEFAULT', null, '1572399900000', '1572399600000', '5', 'PAUSED', 'CRON', '1572351202000', '0', null, '0', '');
