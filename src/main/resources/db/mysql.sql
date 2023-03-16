/*
 Navicat Premium Data Transfer

 Source Server         : se3serverMysql
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : springbootjlvpC

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 02/04/2022 15:04:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `cid` bigint(0) NOT NULL AUTO_INCREMENT,
  `fid` bigint(0) NOT NULL DEFAULT 0,
  `uid` bigint(0) NOT NULL DEFAULT 0,
  `nickname` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `submit_time` datetime(0) NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `score` bigint(0) NOT NULL DEFAULT 0,
  PRIMARY KEY (`cid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, 3, 6, 'wz2', '2022-04-01 19:47:22', '[嘻嘻]', 5);
INSERT INTO `comment` VALUES (2, 4, 5, 'wz1', '2022-04-01 19:47:51', '[可怜]', 5);
INSERT INTO `comment` VALUES (3, 6, 5, 'wz1', '2022-04-01 23:51:43', '发现bug的手段好多样！', 5);


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
  `coordinate1` text DEFAULT NULL,
  `picture2` longtext DEFAULT NULL,
  `coordinate2` text DEFAULT NULL,
  `picture3` longtext DEFAULT NULL,
  `coordinate3` text DEFAULT NULL,
  `picture4` longtext DEFAULT NULL,
  `coordinate4` text DEFAULT NULL,
  `bug_description` longtext DEFAULT NULL,
  `bug_recurrent_steps` longtext DEFAULT NULL,
  `device_information` text DEFAULT NULL,
  `likes` int(11) NOT NULL DEFAULT 0,
  `dislikes` int(11) NOT NULL DEFAULT 0,
  `score` double NOT NULL default 0,
  `comment_num` int(11) NOT NULL DEFAULT 0,
  `matching` double default 0,
  `total_score` double NOT NULL default 0,
  `is_bad` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`fid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for config
-- ----------------------------
INSERT INTO `fetchmission` VALUES (5, 5, 8, '2022-04-01 20:14:16', 24, '2022-04-01 23:51:05', '发现页面的问题', 'AAAA', '12,233,899,100','BBBB', '100,0,0,3',NULL, NULL, NULL, NULL, '点击个人中心会一直转圈，再点返回反而进入了', '点击个人中心', '安卓机 小米10', 0, 0, 0, 0, 0, 0, 'False');

-- INSERT INTO `fetchmission` VALUES(null ,1, 1, "2022-01-08 00:00:00", 6000, "2022-02-08 15:00:00","khynb",null,null,null,null,null,null,null,3,19,2,1,False);
-- INSERT INTO `fetchmission` VALUES(null ,2, 1, "2022-01-18 00:00:00", 6000, "2022-01-19 16:00:00","khyyy",null,null,null,null,"fook","bug exsits","no idea",3,19,4,1,False);


DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, 'picture1', 'http://localhost:8080/springbootjlvpC/upload/1596202765029.jpg');
INSERT INTO `config` VALUES (2, 'picture2', 'http://localhost:8080/springbootjlvpC/upload/1596202778459.jpg');
INSERT INTO `config` VALUES (3, 'picture3', 'http://localhost:8080/springbootjlvpC/upload/1596202789362.jpg');
INSERT INTO `config` VALUES (4, 'picture4', 'http://localhost:8080/springbootjlvpC/upload/1596202801460.jpg');
INSERT INTO `config` VALUES (5, 'picture5', 'http://localhost:8080/springbootjlvpC/upload/1596202812156.jpg');
INSERT INTO `config` VALUES (6, 'homepage', 'http://localhost:8080/springbootjlvpC/upload/1596202819339.jpg');

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
  `fid` bigint(0) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`fid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file
-- ----------------------------

-- ----------------------------
-- Table structure for file_helper
-- ----------------------------
DROP TABLE IF EXISTS `file_helper`;
CREATE TABLE `file_helper`  (
  `fid` bigint(0) NOT NULL AUTO_INCREMENT,
  `file_url` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`fid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file_helper
-- ----------------------------
INSERT INTO `file_helper` VALUES (1, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E4%BC%97%E5%8C%85%E5%B7%A5%E4%BA%BA%E7%B3%BB%E7%BB%9F%E9%A1%BA%E5%BA%8F%E5%9B%BE.png?Expires=1964173169&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=7V14T8bPpbmkMhqYCnq%2B%2FUvd9g8%3D');
INSERT INTO `file_helper` VALUES (2, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3.txt?Expires=1964173177&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=eJosJEnzzemDlM4TuH0oKnuvJpY%3D');
INSERT INTO `file_helper` VALUES (3, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/191250134%2B%E6%B1%AA%E5%96%86%2B17793200801%2B%E5%A4%A7%E4%B8%89%E4%B8%8A%E5%BD%A2%E5%8A%BF%E4%B8%8E%E6%94%BF%E7%AD%96%E8%AF%BE%E7%A8%8B%E6%8A%A5%E5%91%8A.docx?Expires=1964173212&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=cP1vYa2LX5CDb1J%2BIzRg5gLdg1g%3D');
INSERT INTO `file_helper` VALUES (4, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3.txt?Expires=1964173218&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=Viyos%2F%2BEH%2FsZXix6HlEnGdQn7%2Bg%3D');
INSERT INTO `file_helper` VALUES (5, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3.txt?Expires=1964173259&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=vh%2FpsXH%2FYjCjK5cGRzzoDCjLnHs%3D');
INSERT INTO `file_helper` VALUES (6, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E9%A3%9E%E8%A1%8C%E6%A8%A1%E6%8B%9F%E8%BD%AF%E4%BB%B6%E7%AD%96%E7%95%A5%E6%A8%A1%E5%BC%8F%E7%B1%BB%E5%9B%BE.png?Expires=1964173408&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=N0qpDazJj6S7DYQ9V1RtfwrJoWM%3D');
INSERT INTO `file_helper` VALUES (7, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E5%8F%91%E5%8C%85%E6%96%B9%E9%A1%BA%E5%BA%8F%E5%9B%BE.png?Expires=1964175152&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=qGr2HYEJTqtuTfQvvwSBs%2FqBhgk%3D');
INSERT INTO `file_helper` VALUES (8, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/1.txt?Expires=1964175185&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=rqGJCkN770fE3gbRu9oPaxXYtjM%3D');
INSERT INTO `file_helper` VALUES (9, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/2.txt?Expires=1964175214&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=9rj3RyOwJzrgKeYqkIubYkKRge8%3D');
INSERT INTO `file_helper` VALUES (10, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/3.txt?Expires=1964175239&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=FNTLr6ugdGaOGZC3zWjOzNCU4bY%3D');
INSERT INTO `file_helper` VALUES (11, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E9%A3%9E%E8%A1%8C%E6%A8%A1%E6%8B%9F%E8%BD%AF%E4%BB%B6%E7%AD%96%E7%95%A5%E6%A8%A1%E5%BC%8F%E7%B1%BB%E5%9B%BE.png?Expires=1964175349&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=QvLThVawvRrLEktLjtcboHT9x8c%3D');
INSERT INTO `file_helper` VALUES (12, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/4.txt?Expires=1964183234&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=%2Fm361IbSt4b%2F0KiWz5oWGuBgk7M%3D');
INSERT INTO `file_helper` VALUES (13, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/5.txt?Expires=1964183328&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=Zslh1eFMv8b2HkKmMw8JGuRwTqw%3D');
INSERT INTO `file_helper` VALUES (14, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/4.txt?Expires=1964183506&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=TJ5YQRR317EaGgvGXwCOVgLDSlg%3D');
INSERT INTO `file_helper` VALUES (15, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E6%B5%8B%E8%AF%95%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A31.txt?Expires=1964186180&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=E7khoCH5MlUzx%2BdZSvMCG0PceRY%3D');
INSERT INTO `file_helper` VALUES (16, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E6%B5%8B%E8%AF%95%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A31.txt?Expires=1964186255&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=flh5ybrp%2BUjaKEyhKH0y4uXLLb0%3D');
INSERT INTO `file_helper` VALUES (17, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E6%B5%8B%E8%AF%95%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A33.txt?Expires=1964186375&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=zfU8A%2BvZ6Gqzcu0iZBoZ06p9xC0%3D');
INSERT INTO `file_helper` VALUES (18, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E6%B5%8B%E8%AF%95%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A31.txt?Expires=1964186512&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=IMAp7gPmYtCcIMg90cEVCFyLU4E%3D');
INSERT INTO `file_helper` VALUES (19, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E6%B5%8B%E8%AF%95%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A34.txt?Expires=1964186908&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=FqV151U7wJRqEB8DOWuOpLURqFg%3D');
INSERT INTO `file_helper` VALUES (20, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/ratings.csv?Expires=1964186938&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=w2Qy3OQP%2Fm9lRGnvFGP7ONrXaPQ%3D');
INSERT INTO `file_helper` VALUES (21, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E6%B5%8B%E8%AF%95%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A31.txt?Expires=1964187136&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=5KDlHXwELdut8z0AGAqbuyvDO9o%3D');
INSERT INTO `file_helper` VALUES (22, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E6%B5%8B%E8%AF%95%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A32.txt?Expires=1964187250&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=9aJzamCZw9pAHoY6jGJijm%2FGvfA%3D');
INSERT INTO `file_helper` VALUES (23, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/1.txt?Expires=1964187515&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=y46xi3%2FHGKO3rvZTpOqK1oBbVFM%3D');
INSERT INTO `file_helper` VALUES (24, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/1.txt?Expires=1964187536&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=8aLUG0hO7NolVioI8tyN1lbH%2FEo%3D');
INSERT INTO `file_helper` VALUES (25, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/3.txt?Expires=1964187577&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=IAh0oCfZ4rJjWHMdsQ%2BDKicOdMI%3D');
INSERT INTO `file_helper` VALUES (26, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202022-03-09%20011547.png?Expires=1964188055&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=9ura5U6aSrU4bEfuGwFivu0Ewp4%3D');
INSERT INTO `file_helper` VALUES (27, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202022-03-31%20122534.png?Expires=1964188057&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=c7uPOwz6qdgeCHP8vGp60d1lrlU%3D');
INSERT INTO `file_helper` VALUES (28, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202022-03-15%20101439.png?Expires=1964188058&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=KqRI9fDqF89A4fSju0EJ8E2p93o%3D');
INSERT INTO `file_helper` VALUES (29, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202022-03-22%20133314.png?Expires=1964188193&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=G9At3UJxcUm36ZxpsfwTErTIgdk%3D');
INSERT INTO `file_helper` VALUES (30, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202022-03-31%20122833.png?Expires=1964188258&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=FwNf2jxDODcRc8bUm1DnKf9jgFQ%3D');
INSERT INTO `file_helper` VALUES (31, 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202022-03-22%20133314.png?Expires=1964188263&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=6S6jeraxCfUbvspknx6uBj50NZM%3D');

-- ----------------------------
-- Table structure for mission
-- ----------------------------
DROP TABLE IF EXISTS `mission`;
CREATE TABLE `mission`  (
  `mid` bigint(0) NOT NULL AUTO_INCREMENT,
  `uid` bigint(0) NULL DEFAULT 0,
  `recruit_start` datetime(0) NULL DEFAULT NULL,
  `recruit_end` datetime(0) NULL DEFAULT NULL,
  `last_fetch_time` datetime(0) NULL DEFAULT NULL,
  `time_limit` int(0) NOT NULL DEFAULT 0,
  `worker_num` int(0) NOT NULL DEFAULT 0,
  `difficulty_level` int(0) NULL DEFAULT 0,
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mission_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `labels` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `device_req` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `exe_url` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `doc_url` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `state` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`mid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mission
-- ----------------------------
INSERT INTO `mission` VALUES (6, 7, '2022-04-01 20:13:11', '2023-04-02 00:00:00', '2022-04-01 20:13:11', 24, 12, 5, '11', '任务4', NULL, '功能测试,变异测试,灰盒测试', '笔记本电脑', 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/发包方顺序图.png?Expires=1964175152', 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/1.txt?Expires=1964187536&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=8aLUG0hO7NolVioI8tyN1lbH%2FEo%3D', 'Ongoing');
INSERT INTO `mission` VALUES (7, 7, '2022-04-01 20:13:37', '2023-04-02 00:00:00', '2022-04-01 20:13:37', 24, 12, 5, '23', '任务5', NULL, '功能测试,变异测试', '台式机', '', 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/1.txt?Expires=1964187536&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=8aLUG0hO7NolVioI8tyN1lbH%2FEo%3D', 'Ongoing');
INSERT INTO `mission` VALUES (8, 7, '2022-04-01 20:14:02', '2023-04-02 00:00:00', '2022-04-01 20:15:35', 24, 13, 5, '5666', '任务6', NULL, '功能测试,变异测试,动态测试', '台式机', '', 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/3.txt?Expires=1964187577&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=IAh0oCfZ4rJjWHMdsQ%2BDKicOdMI%3D', 'Ongoing');
INSERT INTO `mission` VALUES (9, 7, '2022-04-01 22:28:54', '2023-04-02 00:00:00', '2022-04-01 22:29:16', 5000, 3, 5, '但三矿饭', '任务7', NULL, '蜕变测试', '手机', '', 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/3.txt?Expires=1964187577&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=IAh0oCfZ4rJjWHMdsQ%2BDKicOdMI%3D', 'Ongoing');
INSERT INTO `mission` VALUES (10, 7, '2022-04-01 22:31:49', '2023-04-02 00:00:00', '2022-04-01 22:32:23', 1000, 13, 5, '三矿的', '任务8', NULL, '蜕变测试', '手机', '', 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/1.txt?Expires=1964187536&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=8aLUG0hO7NolVioI8tyN1lbH%2FEo%3D', 'Ongoing');
INSERT INTO `mission` VALUES (12, 7, '2022-04-01 23:29:09', '2023-04-02 00:00:00', '2022-04-01 23:29:09', 99, 99, 5, '999', '任务9', NULL, '模糊测试,压力测试,功能测试,差分测试', '手机', 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/ratings.csv?Expires=1964186938', 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/1.txt?Expires=1964187536&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=8aLUG0hO7NolVioI8tyN1lbH%2FEo%3D', 'Ongoing');

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`  (
  `rid` bigint(0) NOT NULL AUTO_INCREMENT,
  `fid` bigint(0) NOT NULL DEFAULT 0,
  `uid` bigint(0) NOT NULL DEFAULT 0,
  `nickname` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `submit_time` datetime(0) NULL DEFAULT NULL,
  `title` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `picture1` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `picture2` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `picture3` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `picture4` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `bug_description` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `bug_recurrent_steps` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `device_information` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`rid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of report
-- ----------------------------
INSERT INTO `report` VALUES (1, 5, 6, 'wz2', '2022-04-01 23:49:56', '协作报告：发现页面的另一处错误', 'http://file-collect.oss-cn-beijing.aliyuncs.com/collect/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202022-03-22%20133314.png?Expires=1964188193&OSSAccessKeyId=LTAI5tGkbp9z8XLMjFQbv72w&Signature=G9At3UJxcUm36ZxpsfwTErTIgdk%3D', NULL, NULL, NULL, '点击主页的个人中心，再点击修改密码，点击提交没有反应', '点击修改密码', '安卓机 Mate 40');

-- ----------------------------
-- Table structure for report_similarity
-- ----------------------------
DROP TABLE IF EXISTS `report_similarity`;
CREATE TABLE `report_similarity`  (
  `fid_a` bigint(0) NOT NULL DEFAULT 0,
  `fid_b` bigint(0) NOT NULL DEFAULT 0,
  `similarity` double NOT NULL DEFAULT 0
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of report_similarity
-- ----------------------------
INSERT INTO `report_similarity` VALUES (3, 4, 0.7116717100143433);
INSERT INTO `report_similarity` VALUES (5, 6, 0.3676248788833618);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `uid` bigint(0) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `username` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone_number` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `company_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `labels` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `devices` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `reputation` int(0) NULL DEFAULT 0,
  `ability` int(0) NULL DEFAULT 0,
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (4, 'zyltt', 'zylttt', '12345', 'admin', '12345678', 'khycsb@qq.com', NULL, NULL, NULL, 1, 0);
INSERT INTO `user` VALUES (5, 'wz1', 'wz1', '1', 'employee', '12312312312', '12@ww.com', '', '模糊测试,差分测试,功能测试', '嵌入式设备,笔记本电脑,台式机,手机,其他设备', 4, 0);
INSERT INTO `user` VALUES (6, 'wz2', 'wz2', '23', 'employee', '12312312312', '12@ww.com', '', '差分测试,模糊测试,功能测试', '嵌入式设备,笔记本电脑,台式机,手机,其他设备', 4, 0);
INSERT INTO `user` VALUES (7, 'wz3', 'wz3', '123', 'employer', '12312312312', '12@ww.com', 'niumadada', '', '', 0, 0);

SET FOREIGN_KEY_CHECKS = 1;
