/*
 Navicat Premium Data Transfer

 Source Server         : nas
 Source Server Type    : MySQL
 Source Server Version : 90400 (9.4.0)
 Source Host           : nas.hery.cloud:3306
 Source Schema         : teaching-resource-repo

 Target Server Type    : MySQL
 Target Server Version : 90400 (9.4.0)
 File Encoding         : 65001

 Date: 30/11/2025 23:41:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_file
-- ----------------------------
DROP TABLE IF EXISTS `t_file`;
CREATE TABLE `t_file`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件ID',
  `hash` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '文件hash',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件名称',
  `size` double NOT NULL DEFAULT 0 COMMENT '文件大小',
  `duration` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '时长',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '文件图片',
  `kind` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '文件类型',
  `mime_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '类型',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '文件保存地址',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '文件后缀',
  `created_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`(45)) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '附件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_file
-- ----------------------------
INSERT INTO `t_file` VALUES ('1995146595404906497', '', '微信图片_20250806215140_19.jpg', 602887, '0', '', 'image', 'image/jpeg', 'http://nas.hery.cloud:9000/code-tracker/code-tracker/20251130/ed8d3b67-086a-4290-bbb8-1ed2dad98239_微信图片_20250806215140_19.jpg', 'jpg', '', '', '2025-11-30 23:03:10', '2025-11-30 23:03:10', 0);
INSERT INTO `t_file` VALUES ('1995146815639420930', '', '微信图片_20250806215140_19.jpg', 602887, '0', '', 'image', 'image/jpeg', 'http://nas.hery.cloud:9000/code-tracker/code-tracker/20251130/e0f3957b-e339-4cc6-8de2-8ab545628958_微信图片_20250806215140_19.jpg', 'jpg', '', '', '2025-11-30 23:04:02', '2025-11-30 23:09:39', 1);

-- ----------------------------
-- Table structure for t_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource`  (
  `id` bigint NOT NULL COMMENT '资源id',
  `author` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '作者',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '资源标题',
  `description` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '\r\n资源描述\r\n资源描述',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '资源内容',
  `view_count` int NOT NULL DEFAULT 0 COMMENT '浏览量',
  `type` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '第一级类型',
  `sub_type` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '第二级类型',
  `sub_sub_type` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '第三级类型',
  `created_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_resource
-- ----------------------------
INSERT INTO `t_resource` VALUES (1994469712023556098, '', '科研项目立项申请书模板', '标准科研项目立项申请报告，包含项目背景、研究内容、技术路线、预期成果等完整章节模板', '标准科研项目立项申请报告，包含项目背景、研究内容、技术路线、预期成果等完整章节模板标准科研项目立项申请报告，包含项目背景、研究内容、技术路线、预期成果等完整章节模板标准科研项目立项申请报告，包含项目背景、研究内容、技术路线、预期成果等完整章节模板标准科研项目立项申请报告，包含项目背景、研究内容、技术路线、预期成果等完整章节模板标准科研项目立项申请报告，包含项目背景、研究内容、技术路线、预期成果等完整章节模板', 111, 'research_results', 'article', 'project', '', '', '2025-11-29 02:13:30', '2025-11-30 23:37:54', 0);
INSERT INTO `t_resource` VALUES (1995113781821198338, '', '测试添加一个资源', '测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源', '测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源', 232, 'research_results', 'policy', 'proposal', '', '', '2025-11-30 20:52:46', '2025-11-30 23:37:56', 0);
INSERT INTO `t_resource` VALUES (1995115421890576386, '', '测试添加一个资源12', '测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源', '测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源测试添加一个资源', 4, 'research_results', 'plan', 'theoretical', '', '', '2025-11-30 20:59:18', '2025-11-30 23:37:04', 1);

SET FOREIGN_KEY_CHECKS = 1;
