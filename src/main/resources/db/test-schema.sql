-- ----------------------------
-- Table structure for fetchmission
-- ----------------------------
DROP TABLE IF EXISTS `fetchmission`;
CREATE TABLE `fetchmission`  (
  `fid` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NOT NULL DEFAULT 0,
  `mid` bigint(20) NOT NULL DEFAULT 0,
  `fetch_date` datetime(0) DEFAULT NULL,
  `time_limit` int(11) DEFAULT 0,
  `submit_time` datetime(0) DEFAULT NULL,
  `title` text DEFAULT NULL,
  `picture1` longtext DEFAULT NULL,
  `picture2` longtext DEFAULT NULL,
  `picture3` longtext DEFAULT NULL,
  `picture4` longtext DEFAULT NULL,
  `bug_description` longtext DEFAULT NULL,
  `bug_recurrent_steps` longtext DEFAULT NULL,
  `device_information` text DEFAULT NULL,
  `likes` int(11) NOT NULL DEFAULT 0,
  `dislikes` int(11) NOT NULL DEFAULT 0,
  `score` double NOT NULL default 0,
  `comment_num` int(11) NOT NULL DEFAULT 0,
  `is_bad` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`fid`)
);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(200) DEFAULT NULL,
  `username` varchar(200) NOT NULL,
  `password` varchar(200) NOT NULL,
  `user_type` varchar(200) DEFAULT NULL,
  `phone_number` varchar(200) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `company_name` varchar(200) DEFAULT NULL,
  `labels` text DEFAULT NULL,
  `devices` longtext DEFAULT NULL,
  `reputation` int(11) DEFAULT 0,
  `ability` int(11) DEFAULT 0,
  PRIMARY KEY (`uid`)
);

-- ----------------------------
-- Table structure for mission
-- ----------------------------
DROP TABLE IF EXISTS `mission`;
CREATE TABLE `mission` (
  `mid` bigint(20) AUTO_INCREMENT,
  `uid` bigint(20) DEFAULT 0,
  `recruit_start` datetime(0) DEFAULT NULL,
  `recruit_end` datetime(0) DEFAULT NULL,
  `last_fetch_time` datetime(0) DEFAULT NULL,
  `time_limit` int(11) NOT NULL DEFAULT 0,
  `worker_num` int(11) NOT NULL DEFAULT 0,
  `difficulty_level` int(11) DEFAULT 0,
  `description` text DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `mission_type` varchar(200) DEFAULT NULL,
  `labels` text DEFAULT NULL,
  `device_req` varchar(200) DEFAULT NULL,
  `exe_url` longtext DEFAULT NULL,
  `doc_url` longtext DEFAULT NULL,
  `state` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`mid`)
);

-- ----------------------------
-- Table structure for files
-- ----------------------------
DROP TABLE IF EXISTS `file_helper`;
CREATE TABLE `file_helper` (
  `fid` bigint(20) AUTO_INCREMENT,
  `file_url` longtext DEFAULT NULL,
  PRIMARY KEY(`fid`)
);

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `cid` bigint(20) AUTO_INCREMENT,
  `fid` bigint(20) NOT NULL DEFAULT 0,
  `uid` bigint(20) NOT NULL DEFAULT 0,
  `nickname` varchar(200) DEFAULT NULL,
  `submit_time` datetime(0) DEFAULT NULL,
  `content` longtext DEFAULT NULL,
  `score` bigint(20) NOT NULL DEFAULT 0,
   PRIMARY KEY(`cid`)
);

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`  (
  `rid` bigint(20) NOT NULL AUTO_INCREMENT,
  `fid` bigint(20) NOT NULL DEFAULT 0,
  `uid` bigint(20) NOT NULL DEFAULT 0,
  `nickname` varchar(200) DEFAULT NULL,
  `submit_time` datetime(0) DEFAULT NULL,
  `title` text DEFAULT NULL,
  `picture1` longtext DEFAULT NULL,
  `picture2` longtext DEFAULT NULL,
  `picture3` longtext DEFAULT NULL,
  `picture4` longtext DEFAULT NULL,
  `bug_description` longtext DEFAULT NULL,
  `bug_recurrent_steps` longtext DEFAULT NULL,
  `device_information` text DEFAULT NULL,
  PRIMARY KEY (`rid`)
);

-- ----------------------------
-- Table structure for report_similarity
-- ----------------------------
DROP TABLE IF EXISTS `report_similarity`;
CREATE TABLE `report_similarity`(
    `fid_a` bigint(20) NOT NULL DEFAULT 0,
    `fid_b` bigint(20) NOT NULL DEFAULT 0,
    `similarity` double NOT NULL DEFAULT 0
);