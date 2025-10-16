-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: ry-vue
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chat_attachment`
--

DROP TABLE IF EXISTS `chat_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_attachment` (
  `attachment_id` bigint NOT NULL COMMENT '附件唯一ID（雪花算法生成）',
  `message_id` bigint NOT NULL COMMENT '关联消息ID（一条消息可对应多个附件，如多图发送）',
  `attachment_type` tinyint NOT NULL COMMENT '附件类型：1-图片 2-语音',
  `attachment_url` varchar(255) NOT NULL COMMENT '附件存储URL（MinIO的访问地址，如http://minio:9000/chat-attach/202409/xxx.png）',
  `file_name` varchar(100) DEFAULT NULL COMMENT '原始文件名（如"IMG_2024.png"）',
  `file_size` bigint NOT NULL COMMENT '文件大小（字节，用于前端显示"2.5MB"）',
  `file_ext` varchar(10) DEFAULT NULL COMMENT '文件后缀（如"png""mp3"，便于筛选文件类型）',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间（null表示永久有效；如临时图片设为24小时后过期）',
  `is_valid` tinyint NOT NULL DEFAULT '1' COMMENT '是否有效：0-无效（已删除/过期） 1-有效',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`attachment_id`),
  KEY `idx_message_id` (`message_id`),
  KEY `idx_attachment_type` (`attachment_type`),
  KEY `idx_expire_time` (`expire_time`,`is_valid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息附件表（存储图片/语音等附件的元信息）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_attachment`
--

LOCK TABLES `chat_attachment` WRITE;
/*!40000 ALTER TABLE `chat_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_message`
--

DROP TABLE IF EXISTS `chat_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_message` (
  `message_id` bigint NOT NULL COMMENT '消息唯一ID（雪花算法生成，全局唯一）',
  `session_id` bigint NOT NULL COMMENT '所属会话ID（关联chat_session.session_id，聚合同一会话的消息）',
  `from_type` tinyint NOT NULL COMMENT '发送方类型：1-用户 2-骑手 3-商家 4-系统',
  `from_id` bigint NOT NULL COMMENT '发送方ID（系统消息from_id固定为0）',
  `to_type` tinyint NOT NULL COMMENT '接收方类型：1-用户 2-骑手 3-商家',
  `to_id` bigint NOT NULL COMMENT '接收方ID',
  `msg_type` tinyint NOT NULL COMMENT '消息类型：1-文本 2-图片 3-语音 4-系统通知',
  `msg_content` text COMMENT '消息内容：文本消息存内容；图片/语音存MinIO的URL；系统通知存模板内容',
  `msg_status` tinyint NOT NULL DEFAULT '0' COMMENT '消息状态：0-发送中 1-已送达 2-已读 3-已撤回 4-发送失败',
  `send_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息发送时间',
  `deliver_time` datetime DEFAULT NULL COMMENT '消息送达时间（仅用于需确认送达的场景）',
  `read_time` datetime DEFAULT NULL COMMENT '消息已读时间（接收方点击后更新）',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除（软删除）：0-未删除 1-已删除（仅对删除方隐藏）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`message_id`),
  KEY `idx_session_sendtime` (`session_id`,`send_time`),
  KEY `idx_session_status` (`session_id`,`msg_status`),
  KEY `idx_from` (`from_type`,`from_id`),
  KEY `idx_to` (`to_type`,`to_id`),
  KEY `idx_send_time` (`send_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='聊天消息表（存储单条消息的核心信息）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_message`
--

LOCK TABLES `chat_message` WRITE;
/*!40000 ALTER TABLE `chat_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_message_read`
--

DROP TABLE IF EXISTS `chat_message_read`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_message_read` (
  `read_id` bigint NOT NULL COMMENT '已读记录唯一ID（雪花算法生成）',
  `message_id` bigint NOT NULL COMMENT '关联消息ID（关联chat_message.message_id）',
  `reader_type` tinyint NOT NULL COMMENT '已读用户类型：1-用户 2-骑手 3-商家',
  `reader_id` bigint NOT NULL COMMENT '已读用户ID（谁已读这条消息）',
  `read_status` tinyint NOT NULL DEFAULT '0' COMMENT '已读状态：0-未读 1-已读',
  `read_time` datetime DEFAULT NULL COMMENT '已读时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`read_id`),
  UNIQUE KEY `uk_message_reader` (`message_id`,`reader_type`,`reader_id`),
  KEY `idx_reader_status` (`reader_type`,`reader_id`,`read_status`),
  KEY `idx_message_id` (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息已读状态表（追踪每条消息的已读情况，支撑群聊扩展）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_message_read`
--

LOCK TABLES `chat_message_read` WRITE;
/*!40000 ALTER TABLE `chat_message_read` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_message_read` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_session`
--

DROP TABLE IF EXISTS `chat_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_session` (
  `session_id` bigint NOT NULL COMMENT '会话唯一ID（雪花算法生成，全局唯一）',
  `from_type` tinyint NOT NULL COMMENT '发送方类型：1-用户 2-骑手 3-商家',
  `from_id` bigint NOT NULL COMMENT '发送方ID（关联user_db.user_id/ridder_db.ridder_id/merchant_db.merchant_base_id）',
  `to_type` tinyint NOT NULL COMMENT '接收方类型：1-用户 2-骑手 3-商家',
  `to_id` bigint NOT NULL COMMENT '接收方ID（关联对应业务库的主键）',
  `last_msg_id` bigint DEFAULT NULL COMMENT '最后一条消息的ID（关联chat_message.message_id）',
  `last_msg_content` varchar(500) DEFAULT NULL COMMENT '最后一条消息内容（冗余，用于会话列表快速展示）',
  `last_msg_type` tinyint DEFAULT NULL COMMENT '最后一条消息类型：1-文本 2-图片 3-语音 4-系统通知',
  `last_msg_time` datetime DEFAULT NULL COMMENT '最后一条消息发送时间',
  `unread_count` int NOT NULL DEFAULT '0' COMMENT '未读消息数（接收方视角，如用户A的会话中未读数量）',
  `session_status` tinyint NOT NULL DEFAULT '1' COMMENT '会话状态：0-已删除 1-正常 2-已屏蔽',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '会话创建时间（首次发消息时生成）',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '会话更新时间（最后一条消息发送/状态变更时更新）',
  PRIMARY KEY (`session_id`),
  UNIQUE KEY `uk_from_to` (`from_type`,`from_id`,`to_type`,`to_id`),
  KEY `idx_from` (`from_type`,`from_id`),
  KEY `idx_to` (`to_type`,`to_id`),
  KEY `idx_last_msg_time` (`last_msg_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='聊天会话表（管理双方的聊天窗口关系）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_session`
--

LOCK TABLES `chat_session` WRITE;
/*!40000 ALTER TABLE `chat_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gen_table`
--

DROP TABLE IF EXISTS `gen_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gen_table` (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table`
--

LOCK TABLES `gen_table` WRITE;
/*!40000 ALTER TABLE `gen_table` DISABLE KEYS */;
INSERT INTO `gen_table` VALUES (1,'secondhand_goods','二手商品表(简化版)',NULL,NULL,'SecondhandGoods','crud','','com.ruoyi.system','system','goods','二手商品(简化版)','ruoyi','0','/',NULL,'admin','2025-10-16 21:13:52','',NULL,NULL),(2,'secondhand_goods_image','二手商品图片表(支持1-9张图片)',NULL,NULL,'SecondhandGoodsImage','crud','','com.ruoyi.system','system','image','二手商品图片(支持1-9张图片)','ruoyi','0','/',NULL,'admin','2025-10-16 21:13:52','',NULL,NULL),(3,'user_address','用户地址表',NULL,NULL,'UserAddress','crud','','com.ruoyi.system','system','address','用户地址','ruoyi','0','/',NULL,'admin','2025-10-16 21:13:52','',NULL,NULL),(4,'user_bank_card','用户银行卡绑定表',NULL,NULL,'UserBankCard','crud','','com.ruoyi.system','system','card','用户银行卡绑定','ruoyi','0','/',NULL,'admin','2025-10-16 21:13:52','',NULL,NULL),(5,'user_base','用户基础信息表',NULL,NULL,'UserBase','crud','','com.ruoyi.system','system','base','用户基础信息','ruoyi','0','/',NULL,'admin','2025-10-16 21:13:52','',NULL,NULL),(6,'user_behavior_log','用户行为记录表',NULL,NULL,'UserBehaviorLog','crud','','com.ruoyi.system','system','log','用户行为记录','ruoyi','0','/',NULL,'admin','2025-10-16 21:13:52','',NULL,NULL),(7,'user_credit_score_record','用户信用分流水表',NULL,NULL,'UserCreditScoreRecord','crud','','com.ruoyi.system','system','record','用户信用分流水','ruoyi','0','/',NULL,'admin','2025-10-16 21:13:52','',NULL,NULL),(8,'user_preference_tag','用户偏好标签表',NULL,NULL,'UserPreferenceTag','crud','','com.ruoyi.system','system','tag','用户偏好标签','ruoyi','0','/',NULL,'admin','2025-10-16 21:13:52','',NULL,NULL),(9,'user_privacy','用户隐私设置表',NULL,NULL,'UserPrivacy','crud','','com.ruoyi.system','system','privacy','用户隐私设置','ruoyi','0','/',NULL,'admin','2025-10-16 21:13:52','',NULL,NULL),(10,'user_recommend_setting','用户个性化推荐设置表',NULL,NULL,'UserRecommendSetting','crud','','com.ruoyi.system','system','setting','用户个性化推荐设置','ruoyi','0','/',NULL,'admin','2025-10-16 21:13:52','',NULL,NULL),(11,'user_timetable','个人课表表',NULL,NULL,'UserTimetable','crud','','com.ruoyi.system','system','timetable','个人课','ruoyi','0','/',NULL,'admin','2025-10-16 21:13:52','',NULL,NULL),(12,'user_wallet','用户钱包表',NULL,NULL,'UserWallet','crud','','com.ruoyi.system','system','wallet','用户钱包','ruoyi','0','/',NULL,'admin','2025-10-16 21:13:52','',NULL,NULL),(13,'user_wallet_record','用户钱包流水表',NULL,NULL,'UserWalletRecord','crud','','com.ruoyi.system','system','record','用户钱包流水','ruoyi','0','/',NULL,'admin','2025-10-16 21:13:52','',NULL,NULL),(14,'rider_base','骑手基础信息表',NULL,NULL,'RiderBase','crud','','com.ruoyi.system','system','base','骑手基础信息','ruoyi','0','/',NULL,'admin','2025-10-16 21:15:22','',NULL,NULL),(15,'rider_evaluation','骑手评价表',NULL,NULL,'RiderEvaluation','crud','','com.ruoyi.system','system','evaluation','骑手评价','ruoyi','0','/',NULL,'admin','2025-10-16 21:15:22','',NULL,NULL),(16,'rider_location','骑手位置表',NULL,NULL,'RiderLocation','crud','','com.ruoyi.system','system','location','骑手位置','ruoyi','0','/',NULL,'admin','2025-10-16 21:15:22','',NULL,NULL),(17,'rider_order_rel','骑手接单关联表',NULL,NULL,'RiderOrderRel','crud','','com.ruoyi.system','system','rel','骑手接单关联','ruoyi','0','/',NULL,'admin','2025-10-16 21:15:22','',NULL,NULL),(18,'rider_wallet','骑手钱包表',NULL,NULL,'RiderWallet','crud','','com.ruoyi.system','system','wallet','骑手钱包','ruoyi','0','/',NULL,'admin','2025-10-16 21:15:22','',NULL,NULL),(19,'rider_wallet_record','骑手钱包流水表',NULL,NULL,'RiderWalletRecord','crud','','com.ruoyi.system','system','record','骑手钱包流水','ruoyi','0','/',NULL,'admin','2025-10-16 21:15:22','',NULL,NULL),(20,'goods_evaluation','商品评价表',NULL,NULL,'GoodsEvaluation','crud','','com.ruoyi.system','system','evaluation','商品评价','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:24','',NULL,NULL),(21,'goods_evaluation_image','商品评价图片表',NULL,NULL,'GoodsEvaluationImage','crud','','com.ruoyi.system','system','image','商品评价图片','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:24','',NULL,NULL),(22,'merchant_activity','商家活动表',NULL,NULL,'MerchantActivity','crud','','com.ruoyi.system','system','activity','商家活动','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:24','',NULL,NULL),(23,'merchant_address','商家地址表',NULL,NULL,'MerchantAddress','crud','','com.ruoyi.system','system','address','商家地址','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:24','',NULL,NULL),(24,'merchant_base','商家基础信息表',NULL,NULL,'MerchantBase','crud','','com.ruoyi.system','system','base','商家基础信息','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:24','',NULL,NULL),(25,'merchant_evaluation','商家评价表',NULL,NULL,'MerchantEvaluation','crud','','com.ruoyi.system','system','evaluation','商家评价','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:24','',NULL,NULL),(26,'merchant_goods','商品表',NULL,NULL,'MerchantGoods','crud','','com.ruoyi.system','system','goods','商品','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:24','',NULL,NULL),(27,'merchant_goods_image','商品图片关联表（支持多图展示）',NULL,NULL,'MerchantGoodsImage','crud','','com.ruoyi.system','system','image','商品图片关联（支持多图展示）','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:24','',NULL,NULL),(28,'merchant_wallet','商家钱包表',NULL,NULL,'MerchantWallet','crud','','com.ruoyi.system','system','wallet','商家钱包','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:24','',NULL,NULL),(29,'platform_admin','平台管理员表',NULL,NULL,'PlatformAdmin','crud','','com.ruoyi.system','system','admin','平台管理员','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:48','',NULL,NULL),(30,'platform_coupon','平台优惠券表',NULL,NULL,'PlatformCoupon','crud','','com.ruoyi.system','system','coupon','平台优惠券','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:48','',NULL,NULL),(31,'platform_operate_log','系统操作日志表',NULL,NULL,'PlatformOperateLog','crud','','com.ruoyi.system','system','log','系统操作日志','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:48','',NULL,NULL),(32,'platform_permission','权限表',NULL,NULL,'PlatformPermission','crud','','com.ruoyi.system','system','permission','权限','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:48','',NULL,NULL),(33,'platform_role','角色表',NULL,NULL,'PlatformRole','crud','','com.ruoyi.system','system','role','角色','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:49','',NULL,NULL),(34,'platform_role_mapping','角色-账号映射表（多角色登录路由核心）',NULL,NULL,'PlatformRoleMapping','crud','','com.ruoyi.system','system','mapping','角色-账号映射（多角色登录路由核心）','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:49','',NULL,NULL),(35,'platform_role_perm','角色权限关联表',NULL,NULL,'PlatformRolePerm','crud','','com.ruoyi.system','system','perm','角色权限关联','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:49','',NULL,NULL),(36,'platform_tag','平台标签体系表（管理用户和商品标签）',NULL,NULL,'PlatformTag','crud','','com.ruoyi.system','system','tag','平台标签体系（管理用户和商品标签）','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:49','',NULL,NULL),(37,'platform_workorder','客服工单表',NULL,NULL,'PlatformWorkorder','crud','','com.ruoyi.system','system','workorder','客服工单','ruoyi','0','/',NULL,'admin','2025-10-16 21:18:49','',NULL,NULL),(38,'order_coupon','订单优惠券表',NULL,NULL,'OrderCoupon','crud','','com.ruoyi.system','system','coupon','订单优惠券','ruoyi','0','/',NULL,'admin','2025-10-16 21:19:16','',NULL,NULL),(39,'order_delivery','订单配送表（含实际配送定位）',NULL,NULL,'OrderDelivery','crud','','com.ruoyi.system','system','delivery','订单配送（含实际配送定位）','ruoyi','0','/',NULL,'admin','2025-10-16 21:19:16','',NULL,NULL),(40,'order_errand_detail','跑腿订单明细表（不含地址信息）',NULL,NULL,'OrderErrandDetail','crud','','com.ruoyi.system','system','detail','跑腿订单明细（不含地址信息）','ruoyi','0','/',NULL,'admin','2025-10-16 21:19:16','',NULL,NULL),(41,'order_main','订单主表（整合地址与定位信息）',NULL,NULL,'OrderMain','crud','','com.ruoyi.system','system','main','订单主（整合地址与定位信息）','ruoyi','0','/',NULL,'admin','2025-10-16 21:19:16','',NULL,NULL),(42,'order_pay_record','订单支付记录表',NULL,NULL,'OrderPayRecord','crud','','com.ruoyi.system','system','record','订单支付记录','ruoyi','0','/',NULL,'admin','2025-10-16 21:19:16','',NULL,NULL),(43,'order_secondhand_detail','二手交易订单明细表（不含地址信息）',NULL,NULL,'OrderSecondhandDetail','crud','','com.ruoyi.system','system','detail','二手交易订单明细（不含地址信息）','ruoyi','0','/',NULL,'admin','2025-10-16 21:19:16','',NULL,NULL),(44,'order_takeout_detail','外卖订单明细表（不含地址信息）',NULL,NULL,'OrderTakeoutDetail','crud','','com.ruoyi.system','system','detail','外卖订单明细（不含地址信息）','ruoyi','0','/',NULL,'admin','2025-10-16 21:19:16','',NULL,NULL),(45,'platform_announcement','系统公告表',NULL,NULL,'PlatformAnnouncement','crud','','com.ruoyi.system','system','announcement','系统公告','ruoyi','0','/',NULL,'admin','2025-10-16 21:19:16','',NULL,NULL),(46,'chat_attachment','消息附件表（存储图片/语音等附件的元信息）',NULL,NULL,'ChatAttachment','crud','','com.ruoyi.system','system','attachment','消息附件（存储图片/语音等附件的元信息）','ruoyi','0','/',NULL,'admin','2025-10-16 21:19:56','',NULL,NULL),(47,'chat_message','聊天消息表（存储单条消息的核心信息）',NULL,NULL,'ChatMessage','crud','','com.ruoyi.system','system','message','聊天消息（存储单条消息的核心信息）','ruoyi','0','/',NULL,'admin','2025-10-16 21:19:56','',NULL,NULL),(48,'chat_message_read','消息已读状态表（追踪每条消息的已读情况，支撑群聊扩展）',NULL,NULL,'ChatMessageRead','crud','','com.ruoyi.system','system','read','消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）','ruoyi','0','/',NULL,'admin','2025-10-16 21:19:56','',NULL,NULL),(49,'chat_session','聊天会话表（管理双方的聊天窗口关系）',NULL,NULL,'ChatSession','crud','','com.ruoyi.system','system','session','聊天会话（管理双方的聊天窗口关系）','ruoyi','0','/',NULL,'admin','2025-10-16 21:19:56','',NULL,NULL);
/*!40000 ALTER TABLE `gen_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gen_table_column`
--

DROP TABLE IF EXISTS `gen_table_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gen_table_column` (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) DEFAULT '' COMMENT '字典类型',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB AUTO_INCREMENT=520 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table_column`
--

LOCK TABLES `gen_table_column` WRITE;
/*!40000 ALTER TABLE `gen_table_column` DISABLE KEYS */;
INSERT INTO `gen_table_column` VALUES (1,1,'secondhand_goods_id','二手商品唯一ID','bigint','Long','secondhandGoodsId','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:13:52','',NULL),(2,1,'user_base_id','发布用户ID(关联user_base.user_base_id)','bigint','Long','userBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:13:52','',NULL),(3,1,'goods_name','商品名称','varchar(100)','String','goodsName','0','0','1','1','1','1','1','LIKE','input','',3,'admin','2025-10-16 21:13:52','',NULL),(4,1,'category','商品分类(如数码产品/图书教材/服饰鞋包/生活用品/运动健身/美妆个护)','varchar(50)','String','category','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:13:52','',NULL),(5,1,'price','售价/估价','decimal(10,2)','BigDecimal','price','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:13:52','',NULL),(6,1,'description','商品描述(详细说明)','text','String','description','0','0','0','1','1','1','1','EQ','textarea','',6,'admin','2025-10-16 21:13:52','',NULL),(7,1,'status','商品状态:0-已下架 1-在售中 2-已售出 3-已预定','tinyint','Long','status','0','0','1','1','1','1','1','EQ','radio','',7,'admin','2025-10-16 21:13:52','',NULL),(8,1,'view_count','浏览次数','int','Long','viewCount','0','0','1','1','1','1','1','EQ','input','',8,'admin','2025-10-16 21:13:52','',NULL),(9,1,'favorite_count','收藏次数','int','Long','favoriteCount','0','0','1','1','1','1','1','EQ','input','',9,'admin','2025-10-16 21:13:52','',NULL),(10,1,'share_count','分享次数','int','Long','shareCount','0','0','1','1','1','1','1','EQ','input','',10,'admin','2025-10-16 21:13:52','',NULL),(11,1,'create_time','发布时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',11,'admin','2025-10-16 21:13:52','',NULL),(12,1,'update_time','最后更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',12,'admin','2025-10-16 21:13:52','',NULL),(13,1,'sold_time','售出时间','datetime','Date','soldTime','0','0','0','1','1','1','1','EQ','datetime','',13,'admin','2025-10-16 21:13:52','',NULL),(14,2,'secondhand_goods_image_id','图片唯一ID','bigint','Long','secondhandGoodsImageId','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:13:52','',NULL),(15,2,'secondhand_goods_id','关联商品ID','bigint','Long','secondhandGoodsId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:13:52','',NULL),(16,2,'image_url','图片URL(建议使用OSS存储)','varchar(255)','String','imageUrl','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:13:52','',NULL),(17,2,'is_main','是否主图:0-否 1-是(每个商品仅一张主图)','tinyint','Long','isMain','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:13:52','',NULL),(18,2,'sort_order','排序序号(升序排列)','int','Long','sortOrder','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:13:52','',NULL),(19,2,'create_time','上传时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',6,'admin','2025-10-16 21:13:52','',NULL),(20,3,'user_address_id','地址唯一ID','bigint','Long','userAddressId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:13:52','',NULL),(21,3,'user_base_id','所属用户ID（关联user_base.user_base_id）','bigint','Long','userBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:13:52','',NULL),(22,3,'receiver','收货人姓名','varchar(20)','String','receiver','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:13:52','',NULL),(23,3,'phone','收货人电话（AES加密）','varchar(20)','String','phone','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:13:52','',NULL),(24,3,'province','省份','varchar(20)','String','province','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:13:52','',NULL),(25,3,'city','城市','varchar(20)','String','city','0','0','1','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:13:52','',NULL),(26,3,'district','区县','varchar(20)','String','district','0','0','1','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:13:52','',NULL),(27,3,'detail_address','详细地址（如XX宿舍3栋201）','varchar(255)','String','detailAddress','0','0','1','1','1','1','1','EQ','input','',8,'admin','2025-10-16 21:13:52','',NULL),(28,3,'address_tag','地址标签（如DORM-宿舍/CLASSROOM-教室）','varchar(30)','String','addressTag','0','0','0','1','1','1','1','EQ','input','',9,'admin','2025-10-16 21:13:52','',NULL),(29,3,'longitude','经度','decimal(10,6)','BigDecimal','longitude','0','0','0','1','1','1','1','EQ','input','',10,'admin','2025-10-16 21:13:52','',NULL),(30,3,'latitude','纬度','decimal(10,6)','BigDecimal','latitude','0','0','0','1','1','1','1','EQ','input','',11,'admin','2025-10-16 21:13:52','',NULL),(31,3,'is_default','是否默认地址：0-否 1-是','tinyint','Long','isDefault','0','0','1','1','1','1','1','EQ','input','',12,'admin','2025-10-16 21:13:52','',NULL),(32,3,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',13,'admin','2025-10-16 21:13:52','',NULL),(33,3,'update_time','更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',14,'admin','2025-10-16 21:13:52','',NULL),(34,4,'id','主键','bigint','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:13:52','',NULL),(35,4,'user_base_id','所属用户ID','bigint','Long','userBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:13:52','',NULL),(36,4,'bank_name','银行名称','varchar(50)','String','bankName','0','0','1','1','1','1','1','LIKE','input','',3,'admin','2025-10-16 21:13:52','',NULL),(37,4,'bank_card_type','卡类型：1-储蓄卡 2-信用卡','tinyint','Long','bankCardType','0','0','1','1','1','1','1','EQ','select','',4,'admin','2025-10-16 21:13:52','',NULL),(38,4,'card_number','银行卡号（加密存储）','varchar(30)','String','cardNumber','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:13:52','',NULL),(39,4,'card_tail_number','卡号尾号（冗余）','varchar(10)','String','cardTailNumber','0','0','1','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:13:52','',NULL),(40,4,'holder_name','持卡人姓名','varchar(50)','String','holderName','0','0','1','1','1','1','1','LIKE','input','',7,'admin','2025-10-16 21:13:52','',NULL),(41,4,'id_number','身份证号（加密存储）','varchar(30)','String','idNumber','0','0','1','1','1','1','1','EQ','input','',8,'admin','2025-10-16 21:13:52','',NULL),(42,4,'reserve_phone','预留手机号（加密存储）','varchar(20)','String','reservePhone','0','0','1','1','1','1','1','EQ','input','',9,'admin','2025-10-16 21:13:52','',NULL),(43,4,'bind_status','绑定状态：1-正常 0-已解绑','tinyint','Long','bindStatus','0','0','1','1','1','1','1','EQ','radio','',10,'admin','2025-10-16 21:13:52','',NULL),(44,4,'create_time','绑定时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',11,'admin','2025-10-16 21:13:52','',NULL),(45,4,'update_time','更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',12,'admin','2025-10-16 21:13:52','',NULL),(46,5,'user_base_id','用户唯一ID（雪花算法）','bigint','Long','userBaseId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:13:52','',NULL),(47,5,'username','登录账号（唯一）','varchar(50)','String','username','0','0','1','1','1','1','1','LIKE','input','',2,'admin','2025-10-16 21:13:52','',NULL),(48,5,'password','密码（BCrypt加密）','varchar(100)','String','password','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:13:52','',NULL),(49,5,'nickname','用户昵称','varchar(50)','String','nickname','0','0','1','1','1','1','1','LIKE','input','',4,'admin','2025-10-16 21:13:52','',NULL),(50,5,'avatar','头像URL','varchar(255)','String','avatar','0','0','0','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:13:52','',NULL),(51,5,'student_id','学号（唯一）','varchar(20)','String','studentId','0','0','1','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:13:52','',NULL),(52,5,'college','所属学院（如计算机学院）','varchar(50)','String','college','0','0','1','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:13:52','',NULL),(53,5,'major','所属专业（如软件工程）','varchar(50)','String','major','0','0','1','1','1','1','1','EQ','input','',8,'admin','2025-10-16 21:13:52','',NULL),(54,5,'grade','年级（如2022级）','varchar(20)','String','grade','0','0','1','1','1','1','1','EQ','input','',9,'admin','2025-10-16 21:13:52','',NULL),(55,5,'gender','性别：1-男 2-女 0-未知','tinyint','Long','gender','0','0','0','1','1','1','1','EQ','input','',10,'admin','2025-10-16 21:13:52','',NULL),(56,5,'phone','联系电话（AES加密）','varchar(20)','String','phone','0','0','1','1','1','1','1','EQ','input','',11,'admin','2025-10-16 21:13:52','',NULL),(57,5,'credit_score','信用分（影响推荐优先级）','int','Long','creditScore','0','0','1','1','1','1','1','EQ','input','',12,'admin','2025-10-16 21:13:52','',NULL),(58,5,'account_status','账号状态：0-禁用 1-正常','tinyint','Long','accountStatus','0','0','1','1','1','1','1','EQ','radio','',13,'admin','2025-10-16 21:13:52','',NULL),(59,5,'last_login_time','最后登录时间','datetime','Date','lastLoginTime','0','0','0','1','1','1','1','EQ','datetime','',14,'admin','2025-10-16 21:13:52','',NULL),(60,5,'last_login_ip','最后登录IP','varchar(50)','String','lastLoginIp','0','0','0','1','1','1','1','EQ','input','',15,'admin','2025-10-16 21:13:52','',NULL),(61,5,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',16,'admin','2025-10-16 21:13:52','',NULL),(62,5,'update_time','更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',17,'admin','2025-10-16 21:13:52','',NULL),(63,6,'user_behavior_log_id','唯一ID','bigint','Long','userBehaviorLogId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:13:52','',NULL),(64,6,'user_base_id','所属用户ID（关联user_base.user_base_id）','bigint','Long','userBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:13:52','',NULL),(65,6,'behavior_type','行为类型：1-浏览商品 2-收藏商品 3-加入购物车 4-下单购买 5-取消订单 6-评价商品','tinyint','Long','behaviorType','0','0','1','1','1','1','1','EQ','select','',3,'admin','2025-10-16 21:13:52','',NULL),(66,6,'target_id','行为对象ID（如商品ID=123/商家ID=45）','bigint','Long','targetId','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:13:52','',NULL),(67,6,'target_type','对象类型：1-商品 2-商家 3-订单 4-活动','tinyint','Long','targetType','0','0','1','1','1','1','1','EQ','select','',5,'admin','2025-10-16 21:13:52','',NULL),(68,6,'target_name','对象名称（冗余，如\"珍珠奶茶\"）','varchar(100)','String','targetName','0','0','0','1','1','1','1','LIKE','input','',6,'admin','2025-10-16 21:13:52','',NULL),(69,6,'behavior_time','行为发生时间','datetime','Date','behaviorTime','0','0','1','1','1','1','1','EQ','datetime','',7,'admin','2025-10-16 21:13:52','',NULL),(70,6,'device','行为设备（如APP/小程序/H5）','varchar(30)','String','device','0','0','0','1','1','1','1','EQ','input','',8,'admin','2025-10-16 21:13:52','',NULL),(71,6,'scene','行为场景（如HOME-首页/SEARCH-搜索页）','varchar(30)','String','scene','0','0','0','1','1','1','1','EQ','input','',9,'admin','2025-10-16 21:13:52','',NULL),(72,6,'duration','停留时长（秒，仅behavior_type=1时有效）','int','Long','duration','0','0','0','1','1','1','1','EQ','input','',10,'admin','2025-10-16 21:13:52','',NULL),(73,6,'extra','额外信息（如搜索关键词\"平价奶茶\"）','varchar(500)','String','extra','0','0','0','1','1','1','1','EQ','textarea','',11,'admin','2025-10-16 21:13:52','',NULL),(74,7,'id','主键','bigint','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:13:52','',NULL),(75,7,'user_base_id','用户ID','bigint','Long','userBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:13:52','',NULL),(76,7,'change_score','分数变动（正负）','int','Long','changeScore','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:13:52','',NULL),(77,7,'desc','变动说明','varchar(100)','String','desc','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:13:52','',NULL),(78,7,'change_time','变动时间','datetime','Date','changeTime','0','0','1','1','1','1','1','EQ','datetime','',5,'admin','2025-10-16 21:13:52','',NULL),(79,8,'user_preference_tag_id','唯一ID','bigint','Long','userPreferenceTagId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:13:52','',NULL),(80,8,'user_base_id','所属用户ID（关联user_base.user_base_id）','bigint','Long','userBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:13:52','',NULL),(81,8,'tag_code','标签编码（唯一标识，如FOOD_SPICY/STATIONERY）','varchar(30)','String','tagCode','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:13:52','',NULL),(82,8,'tag_name','标签名称（如\"爱吃辣\"\"文具刚需\"）','varchar(50)','String','tagName','0','0','1','1','1','1','1','LIKE','input','',4,'admin','2025-10-16 21:13:52','',NULL),(83,8,'tag_type','标签类型（如FOOD-美食偏好/SHOPPING-购物偏好）','varchar(30)','String','tagType','0','0','1','1','1','1','1','EQ','select','',5,'admin','2025-10-16 21:13:52','',NULL),(84,8,'score','偏好分数（1-100，分数越高偏好越强）','int','Long','score','0','0','1','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:13:52','',NULL),(85,8,'source','标签来源：1-用户主动设置 2-系统行为分析 3-人工标注','tinyint','Long','source','0','0','1','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:13:52','',NULL),(86,8,'create_time','标签创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',8,'admin','2025-10-16 21:13:52','',NULL),(87,8,'update_time','分数更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',9,'admin','2025-10-16 21:13:52','',NULL),(88,9,'user_privacy_id','设置唯一ID','bigint','Long','userPrivacyId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:13:52','',NULL),(89,9,'user_base_id','所属用户ID','bigint','Long','userBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:13:52','',NULL),(90,9,'is_recommend','个性化推荐：0-关闭 1-开启','tinyint','Long','isRecommend','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:13:52','',NULL),(91,9,'is_location_permit','位置权限：0-关闭 1-开启','tinyint','Long','isLocationPermit','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:13:52','',NULL),(92,9,'update_time','最后更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',5,'admin','2025-10-16 21:13:52','',NULL),(93,10,'user_recommend_setting_id','唯一ID','bigint','Long','userRecommendSettingId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:13:52','',NULL),(94,10,'user_base_id','所属用户ID（关联user_base.user_base_id）','bigint','Long','userBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:13:52','',NULL),(95,10,'is_recommend_enabled','是否开启个性化推荐：0-关闭 1-开启','tinyint','Long','isRecommendEnabled','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:13:52','',NULL),(96,10,'recommend_freq','推荐频率：1-高频 2-中频 3-低频','tinyint','Long','recommendFreq','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:13:52','',NULL),(97,10,'shielded_tag_codes','屏蔽的标签编码（逗号分隔）','varchar(500)','String','shieldedTagCodes','0','0','0','1','1','1','1','EQ','textarea','',5,'admin','2025-10-16 21:13:52','',NULL),(98,10,'preferred_scene','偏好推荐场景（逗号分隔）','varchar(50)','String','preferredScene','0','0','0','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:13:52','',NULL),(99,10,'update_time','设置更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',7,'admin','2025-10-16 21:13:52','',NULL),(100,10,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',8,'admin','2025-10-16 21:13:52','',NULL),(101,11,'user_timetable_id','课表记录唯一ID','bigint','Long','userTimetableId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:13:52','',NULL),(102,11,'user_base_id','所属用户ID（关联user_base.user_base_id）','bigint','Long','userBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:13:52','',NULL),(103,11,'course_name','课程名称','varchar(50)','String','courseName','0','0','1','1','1','1','1','LIKE','input','',3,'admin','2025-10-16 21:13:52','',NULL),(104,11,'teacher_name','授课教师姓名','varchar(20)','String','teacherName','0','0','0','1','1','1','1','LIKE','input','',4,'admin','2025-10-16 21:13:52','',NULL),(105,11,'class_room','上课教室（如\"1号教学楼302\"）','varchar(50)','String','classRoom','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:13:52','',NULL),(106,11,'week_day','星期(1-周一 7-周日)','tinyint','Long','weekDay','0','0','1','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:13:52','',NULL),(107,11,'start_period','开始节次','tinyint','Long','startPeriod','0','0','1','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:13:52','',NULL),(108,11,'end_period','结束节次','tinyint','Long','endPeriod','0','0','1','1','1','1','1','EQ','input','',8,'admin','2025-10-16 21:13:52','',NULL),(109,11,'start_time','开始时间（如\"08:00\"）','varchar(20)','String','startTime','0','0','1','1','1','1','1','EQ','input','',9,'admin','2025-10-16 21:13:52','',NULL),(110,11,'end_time','结束时间（如\"09:40\"）','varchar(20)','String','endTime','0','0','1','1','1','1','1','EQ','input','',10,'admin','2025-10-16 21:13:52','',NULL),(111,11,'start_date','课程开始日期','date','Date','startDate','0','0','1','1','1','1','1','EQ','datetime','',11,'admin','2025-10-16 21:13:52','',NULL),(112,11,'end_date','课程结束日期','date','Date','endDate','0','0','1','1','1','1','1','EQ','datetime','',12,'admin','2025-10-16 21:13:52','',NULL),(113,11,'import_source','导入来源','varchar(20)','String','importSource','0','0','0','1','1','1','1','EQ','input','',13,'admin','2025-10-16 21:13:52','',NULL),(114,11,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',14,'admin','2025-10-16 21:13:52','',NULL),(115,12,'user_wallet_id','钱包唯一ID','bigint','Long','userWalletId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:13:52','',NULL),(116,12,'user_base_id','所属用户ID','bigint','Long','userBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:13:52','',NULL),(117,12,'balance','可用余额','decimal(10,2)','BigDecimal','balance','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:13:52','',NULL),(118,12,'freeze_amount','冻结金额','decimal(10,2)','BigDecimal','freezeAmount','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:13:52','',NULL),(119,12,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',5,'admin','2025-10-16 21:13:52','',NULL),(120,12,'update_time','最后更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',6,'admin','2025-10-16 21:13:52','',NULL),(121,13,'user_wallet_record_id','流水唯一ID','bigint','Long','userWalletRecordId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:13:52','',NULL),(122,13,'user_wallet_id','所属钱包ID','bigint','Long','userWalletId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:13:52','',NULL),(123,13,'user_base_id','所属用户ID','bigint','Long','userBaseId','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:13:52','',NULL),(124,13,'amount','金额(正数=收入，负数=支出)','decimal(10,2)','BigDecimal','amount','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:13:52','',NULL),(125,13,'trade_type','交易类型：1-充值 2-提现 3-外卖支付 4-跑腿支付 5-退款','tinyint','Long','tradeType','0','0','1','1','1','1','1','EQ','select','',5,'admin','2025-10-16 21:13:52','',NULL),(126,13,'related_id','关联订单ID','bigint','Long','relatedId','0','0','0','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:13:52','',NULL),(127,13,'trade_status','交易状态：0-处理中 1-成功 2-失败','tinyint','Long','tradeStatus','0','0','1','1','1','1','1','EQ','radio','',7,'admin','2025-10-16 21:13:52','',NULL),(128,13,'trade_time','交易时间','datetime','Date','tradeTime','0','0','1','1','1','1','1','EQ','datetime','',8,'admin','2025-10-16 21:13:52','',NULL),(129,13,'remark','备注','varchar(100)','String','remark','0','0','0','1','1','1',NULL,'EQ','input','',9,'admin','2025-10-16 21:13:52','',NULL),(130,14,'rider_base_id','骑手唯一ID','bigint','Long','riderBaseId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:15:22','',NULL),(131,14,'username','登录账号','varchar(50)','String','username','0','0','1','1','1','1','1','LIKE','input','',2,'admin','2025-10-16 21:15:22','',NULL),(132,14,'password','密码(BCrypt加密)','varchar(100)','String','password','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:15:22','',NULL),(133,14,'nickname','骑手昵称','varchar(50)','String','nickname','0','0','1','1','1','1','1','LIKE','input','',4,'admin','2025-10-16 21:15:22','',NULL),(134,14,'avatar','头像URL','varchar(255)','String','avatar','0','0','0','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:15:22','',NULL),(135,14,'real_name','真实姓名','varchar(20)','String','realName','0','0','1','1','1','1','1','LIKE','input','',6,'admin','2025-10-16 21:15:22','',NULL),(136,14,'id_card','身份证号(AES加密)','varchar(20)','String','idCard','0','0','1','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:15:22','',NULL),(137,14,'id_card_front','身份证正面照URL','varchar(255)','String','idCardFront','0','0','1','1','1','1','1','EQ','input','',8,'admin','2025-10-16 21:15:22','',NULL),(138,14,'id_card_back','身份证反面照URL','varchar(255)','String','idCardBack','0','0','1','1','1','1','1','EQ','input','',9,'admin','2025-10-16 21:15:22','',NULL),(139,14,'phone','联系电话','varchar(20)','String','phone','0','0','1','1','1','1','1','EQ','input','',10,'admin','2025-10-16 21:15:22','',NULL),(140,14,'audit_status','审核状态：0-待审核 1-通过 2-拒绝','tinyint','Long','auditStatus','0','0','1','1','1','1','1','EQ','radio','',11,'admin','2025-10-16 21:15:22','',NULL),(141,14,'work_status','工作状态：0-下线 1-上线 2-忙碌','tinyint','Long','workStatus','0','0','1','1','1','1','1','EQ','radio','',12,'admin','2025-10-16 21:15:22','',NULL),(142,14,'credit_score','服务信用分','int','Long','creditScore','0','0','1','1','1','1','1','EQ','input','',13,'admin','2025-10-16 21:15:22','',NULL),(143,14,'account_status','账号状态：0-禁用 1-正常','tinyint','Long','accountStatus','0','0','1','1','1','1','1','EQ','radio','',14,'admin','2025-10-16 21:15:22','',NULL),(144,14,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',15,'admin','2025-10-16 21:15:22','',NULL),(145,14,'update_time','最后更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',16,'admin','2025-10-16 21:15:22','',NULL),(146,15,'rider_evaluation_id','评价唯一ID','bigint','Long','riderEvaluationId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:15:22','',NULL),(147,15,'rider_base_id','骑手ID','bigint','Long','riderBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:15:22','',NULL),(148,15,'user_id','评价用户ID','bigint','Long','userId','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:15:22','',NULL),(149,15,'order_id','关联订单ID','bigint','Long','orderId','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:15:22','',NULL),(150,15,'rating','评分(1-5分)','tinyint','Long','rating','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:15:22','',NULL),(151,15,'speed_score','速度评分(1-5分)','tinyint','Long','speedScore','0','0','1','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:15:22','',NULL),(152,15,'attitude_score','态度评分(1-5分)','tinyint','Long','attitudeScore','0','0','1','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:15:22','',NULL),(153,15,'content','评价内容','text','String','content','0','0','0','1','1','1','1','EQ','editor','',8,'admin','2025-10-16 21:15:22','',NULL),(154,15,'create_time','评价时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',9,'admin','2025-10-16 21:15:22','',NULL),(155,16,'rider_location_id','位置记录唯一ID','bigint','Long','riderLocationId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:15:22','',NULL),(156,16,'rider_base_id','所属骑手ID','bigint','Long','riderBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:15:22','',NULL),(157,16,'longitude','当前经度','decimal(10,6)','BigDecimal','longitude','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:15:22','',NULL),(158,16,'latitude','当前纬度','decimal(10,6)','BigDecimal','latitude','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:15:22','',NULL),(159,16,'update_time','位置更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',5,'admin','2025-10-16 21:15:22','',NULL),(160,17,'rider_order_rel_id','关联记录唯一ID','bigint','Long','riderOrderRelId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:15:22','',NULL),(161,17,'rider_base_id','骑手ID','bigint','Long','riderBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:15:22','',NULL),(162,17,'order_id','关联订单ID','bigint','Long','orderId','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:15:22','',NULL),(163,17,'order_type','订单类型：1-外卖单 2-跑腿单','tinyint','Long','orderType','0','0','1','1','1','1','1','EQ','select','',4,'admin','2025-10-16 21:15:22','',NULL),(164,17,'receive_time','接单时间','datetime','Date','receiveTime','0','0','1','1','1','1','1','EQ','datetime','',5,'admin','2025-10-16 21:15:22','',NULL),(165,17,'pick_up_time','取货时间','datetime','Date','pickUpTime','0','0','0','1','1','1','1','EQ','datetime','',6,'admin','2025-10-16 21:15:22','',NULL),(166,17,'deliver_time','送达时间','datetime','Date','deliverTime','0','0','0','1','1','1','1','EQ','datetime','',7,'admin','2025-10-16 21:15:22','',NULL),(167,17,'delivery_status','配送状态：1-待取货 2-配送中 3-已送达 4-异常取消','tinyint','Long','deliveryStatus','0','0','1','1','1','1','1','EQ','radio','',8,'admin','2025-10-16 21:15:22','',NULL),(168,17,'abnormal_reason','异常原因','varchar(255)','String','abnormalReason','0','0','0','1','1','1','1','EQ','input','',9,'admin','2025-10-16 21:15:22','',NULL),(169,17,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',10,'admin','2025-10-16 21:15:22','',NULL),(170,17,'update_time','最后更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',11,'admin','2025-10-16 21:15:22','',NULL),(171,18,'rider_wallet_id','钱包唯一ID','bigint','Long','riderWalletId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:15:22','',NULL),(172,18,'rider_base_id','所属骑手ID','bigint','Long','riderBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:15:22','',NULL),(173,18,'balance','可用余额','decimal(10,2)','BigDecimal','balance','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:15:22','',NULL),(174,18,'freeze_amount','冻结金额','decimal(10,2)','BigDecimal','freezeAmount','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:15:22','',NULL),(175,18,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',5,'admin','2025-10-16 21:15:22','',NULL),(176,18,'update_time','最后更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',6,'admin','2025-10-16 21:15:22','',NULL),(177,19,'rider_wallet_record_id','流水唯一ID','bigint','Long','riderWalletRecordId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:15:22','',NULL),(178,19,'rider_wallet_id','所属钱包ID','bigint','Long','riderWalletId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:15:22','',NULL),(179,19,'rider_base_id','所属骑手ID','bigint','Long','riderBaseId','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:15:22','',NULL),(180,19,'amount','金额(正数=收入，负数=支出)','decimal(10,2)','BigDecimal','amount','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:15:22','',NULL),(181,19,'trade_type','交易类型：1-配送收入 2-提现 3-违规扣款 4-平台补贴','tinyint','Long','tradeType','0','0','1','1','1','1','1','EQ','select','',5,'admin','2025-10-16 21:15:22','',NULL),(182,19,'related_id','关联业务ID','bigint','Long','relatedId','0','0','0','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:15:22','',NULL),(183,19,'trade_status','交易状态：0-处理中 1-成功 2-失败','tinyint','Long','tradeStatus','0','0','1','1','1','1','1','EQ','radio','',7,'admin','2025-10-16 21:15:22','',NULL),(184,19,'trade_time','交易时间','datetime','Date','tradeTime','0','0','1','1','1','1','1','EQ','datetime','',8,'admin','2025-10-16 21:15:22','',NULL),(185,19,'remark','备注','varchar(100)','String','remark','0','0','0','1','1','1',NULL,'EQ','input','',9,'admin','2025-10-16 21:15:22','',NULL),(186,20,'goods_evaluation_id','评价唯一ID','bigint','Long','goodsEvaluationId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:24','',NULL),(187,20,'merchant_goods_id','商品ID','bigint','Long','merchantGoodsId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:18:24','',NULL),(188,20,'merchant_base_id','商家ID','bigint','Long','merchantBaseId','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:18:24','',NULL),(189,20,'user_id','评价用户ID','bigint','Long','userId','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:18:24','',NULL),(190,20,'order_id','关联订单ID','bigint','Long','orderId','0','0','0','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:18:24','',NULL),(191,20,'order_item_id','关联订单项ID','bigint','Long','orderItemId','0','0','0','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:18:24','',NULL),(192,20,'rating','商品评分(1-5分)','tinyint','Long','rating','0','0','1','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:18:24','',NULL),(193,20,'content','评价内容','text','String','content','0','0','0','1','1','1','1','EQ','editor','',8,'admin','2025-10-16 21:18:24','',NULL),(194,20,'is_anonymous','是否匿名：0-否 1-是','tinyint','Long','isAnonymous','0','0','1','1','1','1','1','EQ','input','',9,'admin','2025-10-16 21:18:24','',NULL),(195,20,'merchant_reply','商家回复','text','String','merchantReply','0','0','0','1','1','1','1','EQ','textarea','',10,'admin','2025-10-16 21:18:24','',NULL),(196,20,'create_time','评价时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',11,'admin','2025-10-16 21:18:24','',NULL),(197,20,'reply_time','回复时间','datetime','Date','replyTime','0','0','0','1','1','1','1','EQ','datetime','',12,'admin','2025-10-16 21:18:24','',NULL),(198,20,'useful_count','有用数（点赞数）','int','Long','usefulCount','0','0','1','1','1','1','1','EQ','input','',13,'admin','2025-10-16 21:18:24','',NULL),(199,21,'goods_evaluation_image_id','图片ID','bigint','Long','goodsEvaluationImageId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:24','',NULL),(200,21,'goods_evaluation_id','关联评价ID','bigint','Long','goodsEvaluationId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:18:24','',NULL),(201,21,'image_url','图片URL','varchar(255)','String','imageUrl','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:18:24','',NULL),(202,21,'sort_order','排序序号','int','Long','sortOrder','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:18:24','',NULL),(203,21,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',5,'admin','2025-10-16 21:18:24','',NULL),(204,22,'merchant_activity_id','活动唯一ID','bigint','Long','merchantActivityId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:24','',NULL),(205,22,'merchant_base_id','所属商家ID','bigint','Long','merchantBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:18:24','',NULL),(206,22,'activity_name','活动名称','varchar(100)','String','activityName','0','0','1','1','1','1','1','LIKE','input','',3,'admin','2025-10-16 21:18:24','',NULL),(207,22,'activity_type','活动类型','varchar(50)','String','activityType','0','0','1','1','1','1','1','EQ','select','',4,'admin','2025-10-16 21:18:24','',NULL),(208,22,'start_time','开始时间','datetime','Date','startTime','0','0','1','1','1','1','1','EQ','datetime','',5,'admin','2025-10-16 21:18:24','',NULL),(209,22,'end_time','结束时间','datetime','Date','endTime','0','0','1','1','1','1','1','EQ','datetime','',6,'admin','2025-10-16 21:18:24','',NULL),(210,22,'content','活动内容','text','String','content','0','0','0','1','1','1','1','EQ','editor','',7,'admin','2025-10-16 21:18:24','',NULL),(211,22,'status','状态：0-未开始 1-进行中 2-已结束','tinyint','Long','status','0','0','1','1','1','1','1','EQ','radio','',8,'admin','2025-10-16 21:18:24','',NULL),(212,22,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',9,'admin','2025-10-16 21:18:24','',NULL),(213,22,'update_time','最后更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',10,'admin','2025-10-16 21:18:24','',NULL),(214,23,'merchant_address_id','地址ID','bigint','Long','merchantAddressId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:24','',NULL),(215,23,'merchant_base_id','商家ID','bigint','Long','merchantBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:18:24','',NULL),(216,23,'province','省份','varchar(20)','String','province','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:18:24','',NULL),(217,23,'city','城市','varchar(20)','String','city','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:18:24','',NULL),(218,23,'district','区县','varchar(20)','String','district','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:18:24','',NULL),(219,23,'detail_address','详细地址','varchar(255)','String','detailAddress','0','0','1','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:18:24','',NULL),(220,23,'contact_person','联系人','varchar(20)','String','contactPerson','0','0','1','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:18:24','',NULL),(221,23,'contact_phone','联系电话','varchar(20)','String','contactPhone','0','0','1','1','1','1','1','EQ','input','',8,'admin','2025-10-16 21:18:24','',NULL),(222,23,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',9,'admin','2025-10-16 21:18:24','',NULL),(223,23,'update_time','更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',10,'admin','2025-10-16 21:18:24','',NULL),(224,24,'merchant_base_id','商家唯一ID','bigint','Long','merchantBaseId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:24','',NULL),(225,24,'username','登录账号','varchar(50)','String','username','0','0','1','1','1','1','1','LIKE','input','',2,'admin','2025-10-16 21:18:24','',NULL),(226,24,'password','密码(BCrypt加密)','varchar(100)','String','password','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:18:24','',NULL),(227,24,'merchant_name','商家名称','varchar(100)','String','merchantName','0','0','1','1','1','1','1','LIKE','input','',4,'admin','2025-10-16 21:18:24','',NULL),(228,24,'logo','商家Logo URL','varchar(255)','String','logo','0','0','0','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:18:24','',NULL),(229,24,'merchant_address_id','店铺地址ID','bigint','Long','merchantAddressId','0','0','1','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:18:24','',NULL),(230,24,'business_scope','经营范围','varchar(50)','String','businessScope','0','0','1','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:18:24','',NULL),(231,24,'business_hours','营业时间','varchar(100)','String','businessHours','0','0','1','1','1','1','1','EQ','input','',8,'admin','2025-10-16 21:18:24','',NULL),(232,24,'delivery_range','配送范围(公里)','decimal(5,2)','BigDecimal','deliveryRange','0','0','1','1','1','1','1','EQ','input','',9,'admin','2025-10-16 21:18:24','',NULL),(233,24,'min_order_amount','起送金额','decimal(10,2)','BigDecimal','minOrderAmount','0','0','1','1','1','1','1','EQ','input','',10,'admin','2025-10-16 21:18:24','',NULL),(234,24,'delivery_fee','基础配送费','decimal(10,2)','BigDecimal','deliveryFee','0','0','1','1','1','1','1','EQ','input','',11,'admin','2025-10-16 21:18:24','',NULL),(235,24,'license_img','营业执照URL','varchar(255)','String','licenseImg','0','0','1','1','1','1','1','EQ','input','',12,'admin','2025-10-16 21:18:24','',NULL),(236,24,'rating','商家评分','decimal(3,2)','BigDecimal','rating','0','0','1','1','1','1','1','EQ','input','',13,'admin','2025-10-16 21:18:24','',NULL),(237,24,'month_sales','月销量','int','Long','monthSales','0','0','1','1','1','1','1','EQ','input','',14,'admin','2025-10-16 21:18:24','',NULL),(238,24,'audit_status','审核状态：0-待审核 1-通过 2-拒绝','tinyint','Long','auditStatus','0','0','1','1','1','1','1','EQ','radio','',15,'admin','2025-10-16 21:18:24','',NULL),(239,24,'business_status','营业状态：0-停业 1-营业','tinyint','Long','businessStatus','0','0','1','1','1','1','1','EQ','radio','',16,'admin','2025-10-16 21:18:24','',NULL),(240,24,'longitude','店铺经度','decimal(10,6)','BigDecimal','longitude','0','0','0','1','1','1','1','EQ','input','',17,'admin','2025-10-16 21:18:24','',NULL),(241,24,'latitude','店铺纬度','decimal(10,6)','BigDecimal','latitude','0','0','0','1','1','1','1','EQ','input','',18,'admin','2025-10-16 21:18:24','',NULL),(242,24,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',19,'admin','2025-10-16 21:18:24','',NULL),(243,24,'update_time','最后更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',20,'admin','2025-10-16 21:18:24','',NULL),(244,25,'merchant_evaluation_id','评价唯一ID','bigint','Long','merchantEvaluationId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:24','',NULL),(245,25,'merchant_base_id','所属商家ID','bigint','Long','merchantBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:18:24','',NULL),(246,25,'user_id','评价用户ID','bigint','Long','userId','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:18:24','',NULL),(247,25,'order_id','关联订单ID','bigint','Long','orderId','0','0','0','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:18:24','',NULL),(248,25,'rating','评分(1-5分)','tinyint','Long','rating','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:18:24','',NULL),(249,25,'taste_score','口味评分(1-5分，仅餐饮类)','tinyint','Long','tasteScore','0','0','0','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:18:24','',NULL),(250,25,'package_score','包装评分(1-5分)','tinyint','Long','packageScore','0','0','0','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:18:24','',NULL),(251,25,'content','评价内容','text','String','content','0','0','0','1','1','1','1','EQ','editor','',8,'admin','2025-10-16 21:18:24','',NULL),(252,25,'img_urls','评价图片URL(逗号分隔)','varchar(1000)','String','imgUrls','0','0','0','1','1','1','1','EQ','textarea','',9,'admin','2025-10-16 21:18:24','',NULL),(253,25,'merchant_reply','商家回复','text','String','merchantReply','0','0','0','1','1','1','1','EQ','textarea','',10,'admin','2025-10-16 21:18:24','',NULL),(254,25,'create_time','评价时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',11,'admin','2025-10-16 21:18:24','',NULL),(255,25,'reply_time','回复时间','datetime','Date','replyTime','0','0','0','1','1','1','1','EQ','datetime','',12,'admin','2025-10-16 21:18:24','',NULL),(256,26,'merchant_goods_id','商品唯一ID','bigint','Long','merchantGoodsId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:24','',NULL),(257,26,'merchant_base_id','所属商家ID','bigint','Long','merchantBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:18:24','',NULL),(258,26,'goods_name','商品名称','varchar(100)','String','goodsName','0','0','1','1','1','1','1','LIKE','input','',3,'admin','2025-10-16 21:18:24','',NULL),(259,26,'category','商品分类','varchar(50)','String','category','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:18:24','',NULL),(260,26,'sub_category','商品子分类','varchar(50)','String','subCategory','0','0','0','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:18:24','',NULL),(261,26,'price','单价','decimal(10,2)','BigDecimal','price','0','0','1','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:18:24','',NULL),(262,26,'original_price','原价','decimal(10,2)','BigDecimal','originalPrice','0','0','0','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:18:24','',NULL),(263,26,'stock','库存','int','Long','stock','0','0','1','1','1','1','1','EQ','input','',8,'admin','2025-10-16 21:18:24','',NULL),(264,26,'sales_count','销量','bigint','Long','salesCount','0','0','1','1','1','1','1','EQ','input','',9,'admin','2025-10-16 21:18:24','',NULL),(265,26,'description','商品描述','text','String','description','0','0','0','1','1','1','1','EQ','textarea','',10,'admin','2025-10-16 21:18:24','',NULL),(266,26,'tag_codes','商品标签编码（逗号分隔，如FOOD_SPICY,FAST_FOOD）','varchar(500)','String','tagCodes','0','0','0','1','1','1','1','EQ','textarea','',11,'admin','2025-10-16 21:18:24','',NULL),(267,26,'status','状态：0-下架 1-上架','tinyint','Long','status','0','0','1','1','1','1','1','EQ','radio','',12,'admin','2025-10-16 21:18:24','',NULL),(268,26,'avg_rating','商品平均评分','decimal(3,2)','BigDecimal','avgRating','0','0','1','1','1','1','1','EQ','input','',13,'admin','2025-10-16 21:18:24','',NULL),(269,26,'rating_count','评分总次数','int','Long','ratingCount','0','0','1','1','1','1','1','EQ','input','',14,'admin','2025-10-16 21:18:24','',NULL),(270,26,'five_star_rate','五星好评率(%)','decimal(5,2)','BigDecimal','fiveStarRate','0','0','0','1','1','1','1','EQ','input','',15,'admin','2025-10-16 21:18:24','',NULL),(271,26,'four_star_rate','四星好评率(%)','decimal(5,2)','BigDecimal','fourStarRate','0','0','0','1','1','1','1','EQ','input','',16,'admin','2025-10-16 21:18:24','',NULL),(272,26,'three_star_rate','三星评价率(%)','decimal(5,2)','BigDecimal','threeStarRate','0','0','0','1','1','1','1','EQ','input','',17,'admin','2025-10-16 21:18:24','',NULL),(273,26,'two_star_rate','二星评价率(%)','decimal(5,2)','BigDecimal','twoStarRate','0','0','0','1','1','1','1','EQ','input','',18,'admin','2025-10-16 21:18:24','',NULL),(274,26,'one_star_rate','一星差评率(%)','decimal(5,2)','BigDecimal','oneStarRate','0','0','0','1','1','1','1','EQ','input','',19,'admin','2025-10-16 21:18:24','',NULL),(275,26,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',20,'admin','2025-10-16 21:18:24','',NULL),(276,26,'update_time','最后更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',21,'admin','2025-10-16 21:18:24','',NULL),(277,27,'merchant_goods_image_id','图片ID','bigint','Long','merchantGoodsImageId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:24','',NULL),(278,27,'merchant_goods_id','关联商品ID','bigint','Long','merchantGoodsId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:18:24','',NULL),(279,27,'image_url','图片URL','varchar(255)','String','imageUrl','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:18:24','',NULL),(280,27,'image_desc','图片描述（如\"商品正面图\"）','varchar(100)','String','imageDesc','0','0','0','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:18:24','',NULL),(281,27,'sort_order','排序序号（值越小越靠前）','int','Long','sortOrder','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:18:24','',NULL),(282,27,'is_main','是否主图：0-否 1-是','tinyint','Long','isMain','0','0','1','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:18:24','',NULL),(283,27,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',7,'admin','2025-10-16 21:18:24','',NULL),(284,27,'update_time','更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',8,'admin','2025-10-16 21:18:24','',NULL),(285,28,'merchant_wallet_id','钱包唯一ID','bigint','Long','merchantWalletId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:24','',NULL),(286,28,'merchant_base_id','所属商家ID','bigint','Long','merchantBaseId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:18:24','',NULL),(287,28,'balance','可用余额','decimal(10,2)','BigDecimal','balance','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:18:24','',NULL),(288,28,'freeze_amount','冻结金额','decimal(10,2)','BigDecimal','freezeAmount','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:18:24','',NULL),(289,28,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',5,'admin','2025-10-16 21:18:24','',NULL),(290,28,'update_time','最后更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',6,'admin','2025-10-16 21:18:24','',NULL),(291,29,'platform_admin_id','管理员唯一ID（雪花算法）','bigint','Long','platformAdminId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:48','',NULL),(292,29,'username','登录账号（唯一）','varchar(50)','String','username','0','0','1','1','1','1','1','LIKE','input','',2,'admin','2025-10-16 21:18:48','',NULL),(293,29,'password','密码（BCrypt加密存储）','varchar(100)','String','password','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:18:48','',NULL),(294,29,'real_name','真实姓名','varchar(20)','String','realName','0','0','1','1','1','1','1','LIKE','input','',4,'admin','2025-10-16 21:18:48','',NULL),(295,29,'phone','联系电话','varchar(20)','String','phone','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:18:48','',NULL),(296,29,'platform_role_id','关联角色ID（关联platform_role.platform_role_id）','bigint','Long','platformRoleId','0','0','1','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:18:48','',NULL),(297,29,'account_status','账号状态：0-禁用 1-正常','tinyint','Long','accountStatus','0','0','1','1','1','1','1','EQ','radio','',7,'admin','2025-10-16 21:18:48','',NULL),(298,29,'last_login_time','最后登录时间','datetime','Date','lastLoginTime','0','0','0','1','1','1','1','EQ','datetime','',8,'admin','2025-10-16 21:18:48','',NULL),(299,29,'last_login_ip','最后登录IP','varchar(50)','String','lastLoginIp','0','0','0','1','1','1','1','EQ','input','',9,'admin','2025-10-16 21:18:48','',NULL),(300,29,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',10,'admin','2025-10-16 21:18:48','',NULL),(301,29,'update_time','最后更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',11,'admin','2025-10-16 21:18:48','',NULL),(302,30,'platform_coupon_id','优惠券ID','bigint','Long','platformCouponId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:48','',NULL),(303,30,'coupon_no','优惠券编号','varchar(50)','String','couponNo','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:18:48','',NULL),(304,30,'coupon_name','优惠券名称','varchar(100)','String','couponName','0','0','1','1','1','1','1','LIKE','input','',3,'admin','2025-10-16 21:18:48','',NULL),(305,30,'coupon_type','类型：1-满减券 2-折扣券 3-固定金额券','tinyint','Long','couponType','0','0','1','1','1','1','1','EQ','select','',4,'admin','2025-10-16 21:18:48','',NULL),(306,30,'face_value','面值','decimal(10,2)','BigDecimal','faceValue','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:18:48','',NULL),(307,30,'min_spend','最低消费金额','decimal(10,2)','BigDecimal','minSpend','0','0','0','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:18:48','',NULL),(308,30,'discount','折扣率（如0.85=85折）','decimal(3,2)','BigDecimal','discount','0','0','0','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:18:48','',NULL),(309,30,'start_time','开始时间','datetime','Date','startTime','0','0','1','1','1','1','1','EQ','datetime','',8,'admin','2025-10-16 21:18:48','',NULL),(310,30,'end_time','结束时间','datetime','Date','endTime','0','0','1','1','1','1','1','EQ','datetime','',9,'admin','2025-10-16 21:18:48','',NULL),(311,30,'total_count','总发行量','int','Long','totalCount','0','0','1','1','1','1','1','EQ','input','',10,'admin','2025-10-16 21:18:48','',NULL),(312,30,'remain_count','剩余数量','int','Long','remainCount','0','0','1','1','1','1','1','EQ','input','',11,'admin','2025-10-16 21:18:48','',NULL),(313,30,'status','状态：0-未发布 1-已发布 2-已过期','tinyint','Long','status','0','0','1','1','1','1','1','EQ','radio','',12,'admin','2025-10-16 21:18:48','',NULL),(314,30,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',13,'admin','2025-10-16 21:18:48','',NULL),(315,30,'update_time','更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',14,'admin','2025-10-16 21:18:48','',NULL),(316,31,'platform_operate_log_id','日志唯一ID','bigint','Long','platformOperateLogId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:48','',NULL),(317,31,'admin_id','操作管理员ID（关联platform_admin.platform_admin_id）','bigint','Long','adminId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:18:48','',NULL),(318,31,'admin_name','管理员姓名（冗余）','varchar(20)','String','adminName','0','0','1','1','1','1','1','LIKE','input','',3,'admin','2025-10-16 21:18:48','',NULL),(319,31,'oper_type','操作类型（create/update/delete/audit）','varchar(20)','String','operType','0','0','1','1','1','1','1','EQ','select','',4,'admin','2025-10-16 21:18:48','',NULL),(320,31,'oper_module','操作模块（merchant/order/user/rider）','varchar(50)','String','operModule','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:18:48','',NULL),(321,31,'oper_content','操作内容（如\"审核商家ID=123通过\"）','text','String','operContent','0','0','1','1','1','1','1','EQ','editor','',6,'admin','2025-10-16 21:18:48','',NULL),(322,31,'ip_address','操作IP地址','varchar(50)','String','ipAddress','0','0','1','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:18:48','',NULL),(323,31,'oper_time','操作时间','datetime','Date','operTime','0','0','1','1','1','1','1','EQ','datetime','',8,'admin','2025-10-16 21:18:48','',NULL),(324,31,'user_agent','用户代理信息（浏览器/设备）','text','String','userAgent','0','0','0','1','1','1','1','EQ','textarea','',9,'admin','2025-10-16 21:18:48','',NULL),(325,32,'platform_permission_id','权限唯一ID','bigint','Long','platformPermissionId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:48','',NULL),(326,32,'perm_name','权限名称（如订单管理/商家审核）','varchar(50)','String','permName','0','0','1','1','1','1','1','LIKE','input','',2,'admin','2025-10-16 21:18:49','',NULL),(327,32,'perm_key','权限标识（如order:manage/merchant:audit）','varchar(100)','String','permKey','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:18:49','',NULL),(328,32,'perm_type','权限类型：1-菜单 2-按钮','tinyint','Long','permType','0','0','1','1','1','1','1','EQ','select','',4,'admin','2025-10-16 21:18:49','',NULL),(329,32,'parent_perm_id','父权限ID（用于构建权限树，关联platform_permission.platform_permission_id）','bigint','Long','parentPermId','0','0','0','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:18:49','',NULL),(330,32,'menu_path','菜单路径（仅perm_type=1时有值）','varchar(100)','String','menuPath','0','0','0','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:18:49','',NULL),(331,32,'sort','排序序号（值越小越靠前）','int','Long','sort','0','0','0','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:18:49','',NULL),(332,32,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',8,'admin','2025-10-16 21:18:49','',NULL),(333,32,'update_time','最后更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',9,'admin','2025-10-16 21:18:49','',NULL),(334,33,'platform_role_id','角色唯一ID','bigint','Long','platformRoleId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:49','',NULL),(335,33,'role_name','角色名称（如超级管理员/运营专员）','varchar(50)','String','roleName','0','0','1','1','1','1','1','LIKE','input','',2,'admin','2025-10-16 21:18:49','',NULL),(336,33,'role_code','角色编码（唯一标识，如ADMIN/OPERATOR）','varchar(50)','String','roleCode','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:18:49','',NULL),(337,33,'role_desc','角色描述','varchar(255)','String','roleDesc','0','0','0','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:18:49','',NULL),(338,33,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',5,'admin','2025-10-16 21:18:49','',NULL),(339,33,'update_time','最后更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',6,'admin','2025-10-16 21:18:49','',NULL),(340,34,'platform_role_mapping_id','主键ID','bigint','Long','platformRoleMappingId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:49','',NULL),(341,34,'username','登录账号（各角色的username字段）','varchar(50)','String','username','0','0','1','1','1','1','1','LIKE','input','',2,'admin','2025-10-16 21:18:49','',NULL),(342,34,'role_type','角色类型：1-学生用户 2-骑手 3-商家 4-平台管理员','tinyint','Long','roleType','0','0','1','1','1','1','1','EQ','select','',3,'admin','2025-10-16 21:18:49','',NULL),(343,34,'target_db','目标数据库：user_db/rider_db/merchant_db/platform_db','varchar(20)','String','targetDb','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:18:49','',NULL),(344,34,'target_table','目标表：user_base/rider_base/merchant_base/platform_admin','varchar(50)','String','targetTable','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:18:49','',NULL),(345,34,'account_status','账号全局状态：0-禁用 1-正常','tinyint','Long','accountStatus','0','0','1','1','1','1','1','EQ','radio','',6,'admin','2025-10-16 21:18:49','',NULL),(346,34,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',7,'admin','2025-10-16 21:18:49','',NULL),(347,34,'update_time','更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',8,'admin','2025-10-16 21:18:49','',NULL),(348,35,'platform_role_perm_id','关联唯一ID','bigint','Long','platformRolePermId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:49','',NULL),(349,35,'platform_role_id','角色ID（关联platform_role.platform_role_id）','bigint','Long','platformRoleId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:18:49','',NULL),(350,35,'platform_permission_id','权限ID（关联platform_permission.platform_permission_id）','bigint','Long','platformPermissionId','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:18:49','',NULL),(351,35,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',4,'admin','2025-10-16 21:18:49','',NULL),(352,36,'platform_tag_id','标签ID','bigint','Long','platformTagId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:49','',NULL),(353,36,'tag_code','标签编码（唯一）','varchar(30)','String','tagCode','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:18:49','',NULL),(354,36,'tag_name','标签名称','varchar(50)','String','tagName','0','0','1','1','1','1','1','LIKE','input','',3,'admin','2025-10-16 21:18:49','',NULL),(355,36,'tag_type','标签类型','varchar(30)','String','tagType','0','0','1','1','1','1','1','EQ','select','',4,'admin','2025-10-16 21:18:49','',NULL),(356,36,'tag_desc','标签描述','varchar(255)','String','tagDesc','0','0','0','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:18:49','',NULL),(357,36,'parent_code','父标签编码','varchar(30)','String','parentCode','0','0','0','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:18:49','',NULL),(358,36,'status','状态：0-禁用 1-启用','tinyint','Long','status','0','0','1','1','1','1','1','EQ','radio','',7,'admin','2025-10-16 21:18:49','',NULL),(359,36,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',8,'admin','2025-10-16 21:18:49','',NULL),(360,36,'update_time','更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',9,'admin','2025-10-16 21:18:49','',NULL),(361,37,'platform_workorder_id','工单唯一ID','bigint','Long','platformWorkorderId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:18:49','',NULL),(362,37,'order_id','关联订单ID（来自order_db）','bigint','Long','orderId','0','0','0','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:18:49','',NULL),(363,37,'user_id','提交用户ID（来自user/rider/merchant_db）','bigint','Long','userId','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:18:49','',NULL),(364,37,'user_type','用户类型：1-学生 2-骑手 3-商家','tinyint','Long','userType','0','0','1','1','1','1','1','EQ','select','',4,'admin','2025-10-16 21:18:49','',NULL),(365,37,'user_nickname','用户昵称（冗余）','varchar(50)','String','userNickname','0','0','1','1','1','1','1','LIKE','input','',5,'admin','2025-10-16 21:18:49','',NULL),(366,37,'content','工单内容（问题描述）','text','String','content','0','0','1','1','1','1','1','EQ','editor','',6,'admin','2025-10-16 21:18:49','',NULL),(367,37,'img_urls','问题图片URL（逗号分隔）','varchar(1000)','String','imgUrls','0','0','0','1','1','1','1','EQ','textarea','',7,'admin','2025-10-16 21:18:49','',NULL),(368,37,'workorder_type','工单类型：1-订单投诉 2-服务差评 3-系统故障 4-其他','tinyint','Long','workorderType','0','0','1','1','1','1','1','EQ','select','',8,'admin','2025-10-16 21:18:49','',NULL),(369,37,'handler_id','处理人ID（关联platform_admin.platform_admin_id）','bigint','Long','handlerId','0','0','0','1','1','1','1','EQ','input','',9,'admin','2025-10-16 21:18:49','',NULL),(370,37,'handler_name','处理人姓名（冗余）','varchar(20)','String','handlerName','0','0','0','1','1','1','1','LIKE','input','',10,'admin','2025-10-16 21:18:49','',NULL),(371,37,'handle_status','处理状态：0-待处理 1-处理中 2-已解决 3-已关闭','tinyint','Long','handleStatus','0','0','1','1','1','1','1','EQ','radio','',11,'admin','2025-10-16 21:18:49','',NULL),(372,37,'handle_result','处理结果','text','String','handleResult','0','0','0','1','1','1','1','EQ','textarea','',12,'admin','2025-10-16 21:18:49','',NULL),(373,37,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',13,'admin','2025-10-16 21:18:49','',NULL),(374,37,'handle_time','处理时间','datetime','Date','handleTime','0','0','0','1','1','1','1','EQ','datetime','',14,'admin','2025-10-16 21:18:49','',NULL),(375,37,'close_time','关闭时间','datetime','Date','closeTime','0','0','0','1','1','1','1','EQ','datetime','',15,'admin','2025-10-16 21:18:49','',NULL),(376,38,'order_coupon_id','ID','bigint','Long','orderCouponId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:19:16','',NULL),(377,38,'order_main_id','订单ID','bigint','Long','orderMainId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:19:16','',NULL),(378,38,'coupon_id','优惠券ID','bigint','Long','couponId','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:19:16','',NULL),(379,38,'coupon_name','优惠券名称','varchar(100)','String','couponName','0','0','1','1','1','1','1','LIKE','input','',4,'admin','2025-10-16 21:19:16','',NULL),(380,38,'discount_amount','优惠金额','decimal(10,2)','BigDecimal','discountAmount','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:19:16','',NULL),(381,38,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',6,'admin','2025-10-16 21:19:16','',NULL),(382,39,'order_delivery_id','配送记录ID','bigint','Long','orderDeliveryId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:19:16','',NULL),(383,39,'order_main_id','订单ID','bigint','Long','orderMainId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:19:16','',NULL),(384,39,'rider_id','骑手ID','bigint','Long','riderId','0','0','0','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:19:16','',NULL),(385,39,'rider_nickname','骑手昵称(冗余)','varchar(50)','String','riderNickname','0','0','0','1','1','1','1','LIKE','input','',4,'admin','2025-10-16 21:19:16','',NULL),(386,39,'delivery_fee','配送费（可基于主表取货-送货坐标计算）','decimal(10,2)','BigDecimal','deliveryFee','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:19:16','',NULL),(387,39,'actual_pick_longitude','实际取货经度','decimal(11,8)','BigDecimal','actualPickLongitude','0','0','0','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:19:16','',NULL),(388,39,'actual_pick_latitude','实际取货纬度','decimal(10,8)','BigDecimal','actualPickLatitude','0','0','0','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:19:16','',NULL),(389,39,'actual_deliver_longitude','实际送达经度','decimal(11,8)','BigDecimal','actualDeliverLongitude','0','0','0','1','1','1','1','EQ','input','',8,'admin','2025-10-16 21:19:16','',NULL),(390,39,'actual_deliver_latitude','实际送达纬度','decimal(10,8)','BigDecimal','actualDeliverLatitude','0','0','0','1','1','1','1','EQ','input','',9,'admin','2025-10-16 21:19:16','',NULL),(391,39,'assign_time','派单时间','datetime','Date','assignTime','0','0','0','1','1','1','1','EQ','datetime','',10,'admin','2025-10-16 21:19:16','',NULL),(392,39,'receive_time','接单时间','datetime','Date','receiveTime','0','0','0','1','1','1','1','EQ','datetime','',11,'admin','2025-10-16 21:19:16','',NULL),(393,39,'pick_time','取货时间','datetime','Date','pickTime','0','0','0','1','1','1','1','EQ','datetime','',12,'admin','2025-10-16 21:19:16','',NULL),(394,39,'deliver_time','送达时间','datetime','Date','deliverTime','0','0','0','1','1','1','1','EQ','datetime','',13,'admin','2025-10-16 21:19:16','',NULL),(395,39,'delivery_status','配送状态：0-待分配 1-已接单 2-已取货 3-已送达','tinyint','Long','deliveryStatus','0','0','1','1','1','1','1','EQ','radio','',14,'admin','2025-10-16 21:19:16','',NULL),(396,40,'order_errand_detail_id','明细唯一ID','bigint','Long','orderErrandDetailId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:19:16','',NULL),(397,40,'order_main_id','关联订单ID','bigint','Long','orderMainId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:19:16','',NULL),(398,40,'errand_type','跑腿类型：1-帮我送 2-帮我买','tinyint','Long','errandType','0','0','1','1','1','1','1','EQ','select','',3,'admin','2025-10-16 21:19:16','',NULL),(399,40,'goods_desc','物品/商品描述（如“生日蛋糕/6寸”“ textbooks/高等数学”）','varchar(255)','String','goodsDesc','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:19:16','',NULL),(400,40,'expected_time','期望送达时间','datetime','Date','expectedTime','0','0','0','1','1','1','1','EQ','datetime','',5,'admin','2025-10-16 21:19:16','',NULL),(401,40,'advance_amount','骑手垫付金额（帮我买场景专用）','decimal(10,2)','BigDecimal','advanceAmount','0','0','0','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:19:16','',NULL),(402,40,'tip_amount','小费金额','decimal(10,2)','BigDecimal','tipAmount','0','0','0','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:19:16','',NULL),(403,40,'buy_photo_url','代付凭证','varchar(255)','String','buyPhotoUrl','0','0','0','1','1','1','1','EQ','input','',8,'admin','2025-10-16 21:19:16','',NULL),(404,41,'order_main_id','订单唯一ID','bigint','Long','orderMainId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:19:16','',NULL),(405,41,'order_no','订单编号','varchar(32)','String','orderNo','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:19:16','',NULL),(406,41,'user_id','下单用户ID（关联user_db.user_base.user_base_id）','bigint','Long','userId','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:19:16','',NULL),(407,41,'user_nickname','用户昵称(冗余)','varchar(50)','String','userNickname','0','0','1','1','1','1','1','LIKE','input','',4,'admin','2025-10-16 21:19:16','',NULL),(408,41,'order_type','订单类型：1-外卖单 2-跑腿单 3-二手交易单','tinyint','Long','orderType','0','0','1','1','1','1','1','EQ','select','',5,'admin','2025-10-16 21:19:16','',NULL),(409,41,'total_amount','订单总金额','decimal(10,2)','BigDecimal','totalAmount','0','0','1','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:19:16','',NULL),(410,41,'pay_amount','实付金额','decimal(10,2)','BigDecimal','payAmount','0','0','1','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:19:16','',NULL),(411,41,'discount_amount','优惠金额','decimal(10,2)','BigDecimal','discountAmount','0','0','1','1','1','1','1','EQ','input','',8,'admin','2025-10-16 21:19:16','',NULL),(412,41,'pay_status','支付状态：0-未支付 1-已支付 2-退款中 3-已退款','tinyint','Long','payStatus','0','0','1','1','1','1','1','EQ','radio','',9,'admin','2025-10-16 21:19:16','',NULL),(413,41,'pay_time','支付时间','datetime','Date','payTime','0','0','0','1','1','1','1','EQ','datetime','',10,'admin','2025-10-16 21:19:16','',NULL),(414,41,'pay_type','支付方式：1-余额 2-微信 3-支付宝','tinyint','Long','payType','0','0','0','1','1','1','1','EQ','select','',11,'admin','2025-10-16 21:19:16','',NULL),(415,41,'order_status','订单状态：1-待接单 2-待取货 3-配送中 4-已完成 5-已取消','tinyint','Long','orderStatus','0','0','1','1','1','1','1','EQ','radio','',12,'admin','2025-10-16 21:19:16','',NULL),(416,41,'cancel_reason','取消原因','varchar(255)','String','cancelReason','0','0','0','1','1','1','1','EQ','input','',13,'admin','2025-10-16 21:19:16','',NULL),(417,41,'cancel_operator','取消操作人','varchar(50)','String','cancelOperator','0','0','0','1','1','1','1','EQ','input','',14,'admin','2025-10-16 21:19:16','',NULL),(418,41,'pick_address_id','取货地址ID（外卖关联merchant_db.merchant_address.merchant_address_id；其他关联user_db.user_address.user_address_id）','bigint','Long','pickAddressId','0','0','1','1','1','1','1','EQ','input','',15,'admin','2025-10-16 21:19:16','',NULL),(419,41,'pick_address','取货地址文本（冗余，如“XX食堂3楼奶茶店”“XX宿舍2栋101”）','varchar(255)','String','pickAddress','0','0','1','1','1','1','1','EQ','input','',16,'admin','2025-10-16 21:19:16','',NULL),(420,41,'pick_contact','取货联系人','varchar(20)','String','pickContact','0','0','1','1','1','1','1','EQ','input','',17,'admin','2025-10-16 21:19:16','',NULL),(421,41,'pick_phone','取货电话（AES加密，与user_db加密标准一致）','varchar(20)','String','pickPhone','0','0','1','1','1','1','1','EQ','input','',18,'admin','2025-10-16 21:19:16','',NULL),(422,41,'pick_longitude','取货经度（定位功能填充，WGS84坐标系，精度1米内）','decimal(11,8)','BigDecimal','pickLongitude','0','0','0','1','1','1','1','EQ','input','',19,'admin','2025-10-16 21:19:16','',NULL),(423,41,'pick_latitude','取货纬度（定位功能填充，WGS84坐标系，精度1米内）','decimal(10,8)','BigDecimal','pickLatitude','0','0','0','1','1','1','1','EQ','input','',20,'admin','2025-10-16 21:19:16','',NULL),(424,41,'deliver_address_id','送货地址ID（关联user_db.user_address.user_address_id，线下二手单可空）','bigint','Long','deliverAddressId','0','0','0','1','1','1','1','EQ','input','',21,'admin','2025-10-16 21:19:16','',NULL),(425,41,'deliver_address','送货地址文本（冗余，如“XX教学楼503室”）','varchar(255)','String','deliverAddress','0','0','0','1','1','1','1','EQ','input','',22,'admin','2025-10-16 21:19:16','',NULL),(426,41,'deliver_contact','收货联系人','varchar(20)','String','deliverContact','0','0','0','1','1','1','1','EQ','input','',23,'admin','2025-10-16 21:19:16','',NULL),(427,41,'deliver_phone','收货电话（AES加密，与user_db加密标准一致）','varchar(20)','String','deliverPhone','0','0','0','1','1','1','1','EQ','input','',24,'admin','2025-10-16 21:19:16','',NULL),(428,41,'deliver_longitude','送货经度（定位功能填充，WGS84坐标系）','decimal(11,8)','BigDecimal','deliverLongitude','0','0','0','1','1','1','1','EQ','input','',25,'admin','2025-10-16 21:19:16','',NULL),(429,41,'deliver_latitude','送货纬度（定位功能填充，WGS84坐标系）','decimal(10,8)','BigDecimal','deliverLatitude','0','0','0','1','1','1','1','EQ','input','',26,'admin','2025-10-16 21:19:16','',NULL),(430,41,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',27,'admin','2025-10-16 21:19:16','',NULL),(431,41,'update_time','最后更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',28,'admin','2025-10-16 21:19:16','',NULL),(432,41,'complete_time','完成时间','datetime','Date','completeTime','0','0','0','1','1','1','1','EQ','datetime','',29,'admin','2025-10-16 21:19:16','',NULL),(433,42,'order_pay_record_id','支付记录ID','bigint','Long','orderPayRecordId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:19:16','',NULL),(434,42,'order_main_id','订单ID','bigint','Long','orderMainId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:19:16','',NULL),(435,42,'pay_no','支付单号','varchar(64)','String','payNo','0','0','0','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:19:16','',NULL),(436,42,'pay_amount','支付金额','decimal(10,2)','BigDecimal','payAmount','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:19:16','',NULL),(437,42,'pay_type','支付方式：1-余额 2-微信 3-支付宝','tinyint','Long','payType','0','0','1','1','1','1','1','EQ','select','',5,'admin','2025-10-16 21:19:16','',NULL),(438,42,'pay_status','支付状态：0-处理中 1-成功 2-失败','tinyint','Long','payStatus','0','0','1','1','1','1','1','EQ','radio','',6,'admin','2025-10-16 21:19:16','',NULL),(439,42,'pay_time','支付时间','datetime','Date','payTime','0','0','0','1','1','1','1','EQ','datetime','',7,'admin','2025-10-16 21:19:16','',NULL),(440,42,'callback_data','支付回调数据','text','String','callbackData','0','0','0','1','1','1','1','EQ','textarea','',8,'admin','2025-10-16 21:19:16','',NULL),(441,43,'order_secondhand_detail_id','明细唯一ID','bigint','Long','orderSecondhandDetailId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:19:16','',NULL),(442,43,'order_main_id','关联订单ID','bigint','Long','orderMainId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:19:16','',NULL),(443,43,'goods_id','二手商品ID','bigint','Long','goodsId','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:19:16','',NULL),(444,43,'goods_name','商品名称(冗余)','varchar(100)','String','goodsName','0','0','1','1','1','1','1','LIKE','input','',4,'admin','2025-10-16 21:19:16','',NULL),(445,43,'seller_id','卖家ID（关联user_db.user_base.user_base_id）','bigint','Long','sellerId','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:19:16','',NULL),(446,43,'sell_way','交易方式：1-线上 2-线下','tinyint','Long','sellWay','0','0','1','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:19:16','',NULL),(447,43,'seller_nickname','卖家昵称(冗余)','varchar(50)','String','sellerNickname','0','0','1','1','1','1','1','LIKE','input','',7,'admin','2025-10-16 21:19:16','',NULL),(448,43,'deposit_amount','担保金金额','decimal(10,2)','BigDecimal','depositAmount','0','0','1','1','1','1','1','EQ','input','',8,'admin','2025-10-16 21:19:16','',NULL),(449,43,'confirm_time','买家确认收货时间','datetime','Date','confirmTime','0','0','0','1','1','1','1','EQ','datetime','',9,'admin','2025-10-16 21:19:16','',NULL),(450,43,'evaluate_status','评价状态：0-未评价 1-已评价','tinyint','Long','evaluateStatus','0','0','1','1','1','1','1','EQ','radio','',10,'admin','2025-10-16 21:19:16','',NULL),(451,44,'order_takeout_detail_id','明细唯一ID','bigint','Long','orderTakeoutDetailId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:19:16','',NULL),(452,44,'order_main_id','关联订单ID','bigint','Long','orderMainId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:19:16','',NULL),(453,44,'merchant_id','商家ID（关联merchant_db.merchant_base.merchant_base_id）','bigint','Long','merchantId','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:19:16','',NULL),(454,44,'merchant_name','商家名称(冗余)','varchar(100)','String','merchantName','0','0','1','1','1','1','1','LIKE','input','',4,'admin','2025-10-16 21:19:16','',NULL),(455,44,'goods_id','商品ID（关联merchant_db.merchant_goods.merchant_goods_id）','bigint','Long','goodsId','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:19:16','',NULL),(456,44,'goods_name','商品名称(冗余)','varchar(100)','String','goodsName','0','0','1','1','1','1','1','LIKE','input','',6,'admin','2025-10-16 21:19:16','',NULL),(457,44,'goods_price','商品单价','decimal(10,2)','BigDecimal','goodsPrice','0','0','1','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:19:16','',NULL),(458,44,'quantity','购买数量','int','Long','quantity','0','0','1','1','1','1','1','EQ','input','',8,'admin','2025-10-16 21:19:16','',NULL),(459,44,'subtotal','小计金额','decimal(10,2)','BigDecimal','subtotal','0','0','1','1','1','1','1','EQ','input','',9,'admin','2025-10-16 21:19:16','',NULL),(460,44,'goods_spec','商品规格（如“中杯/少糖”）','varchar(100)','String','goodsSpec','0','0','0','1','1','1','1','EQ','input','',10,'admin','2025-10-16 21:19:16','',NULL),(461,44,'goods_tags','商品标签(冗余，如“甜口/冰饮”，用于推荐)','varchar(200)','String','goodsTags','0','0','0','1','1','1','1','EQ','input','',11,'admin','2025-10-16 21:19:16','',NULL),(462,45,'platform_announcement_id','公告唯一ID','bigint','Long','platformAnnouncementId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:19:16','',NULL),(463,45,'title','公告标题','varchar(100)','String','title','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:19:16','',NULL),(464,45,'content','公告内容','text','String','content','0','0','1','1','1','1','1','EQ','editor','',3,'admin','2025-10-16 21:19:16','',NULL),(465,45,'publisher_id','发布人ID（关联platform_admin.platform_admin_id）','bigint','Long','publisherId','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:19:16','',NULL),(466,45,'publisher_name','发布人姓名（冗余）','varchar(20)','String','publisherName','0','0','1','1','1','1','1','LIKE','input','',5,'admin','2025-10-16 21:19:16','',NULL),(467,45,'publish_time','发布时间','datetime','Date','publishTime','0','0','1','1','1','1','1','EQ','datetime','',6,'admin','2025-10-16 21:19:16','',NULL),(468,45,'status','状态：0-草稿 1-已发布 2-已下架','tinyint','Long','status','0','0','1','1','1','1','1','EQ','radio','',7,'admin','2025-10-16 21:19:16','',NULL),(469,45,'read_count','阅读量','int','Long','readCount','0','0','1','1','1','1','1','EQ','input','',8,'admin','2025-10-16 21:19:16','',NULL),(470,45,'is_top','是否置顶：0-否 1-是','tinyint','Long','isTop','0','0','1','1','1','1','1','EQ','input','',9,'admin','2025-10-16 21:19:16','',NULL),(471,45,'create_time','创建时间','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',10,'admin','2025-10-16 21:19:16','',NULL),(472,45,'update_time','最后更新时间','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',11,'admin','2025-10-16 21:19:16','',NULL),(473,46,'attachment_id','附件唯一ID（雪花算法生成）','bigint','Long','attachmentId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:19:56','',NULL),(474,46,'message_id','关联消息ID（一条消息可对应多个附件，如多图发送）','bigint','Long','messageId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:19:56','',NULL),(475,46,'attachment_type','附件类型：1-图片 2-语音','tinyint','Long','attachmentType','0','0','1','1','1','1','1','EQ','select','',3,'admin','2025-10-16 21:19:56','',NULL),(476,46,'attachment_url','附件存储URL（MinIO的访问地址，如http://minio:9000/chat-attach/202409/xxx.png）','varchar(255)','String','attachmentUrl','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:19:56','',NULL),(477,46,'file_name','原始文件名（如\"IMG_2024.png\"）','varchar(100)','String','fileName','0','0','0','1','1','1','1','LIKE','input','',5,'admin','2025-10-16 21:19:56','',NULL),(478,46,'file_size','文件大小（字节，用于前端显示\"2.5MB\"）','bigint','Long','fileSize','0','0','1','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:19:56','',NULL),(479,46,'file_ext','文件后缀（如\"png\"\"mp3\"，便于筛选文件类型）','varchar(10)','String','fileExt','0','0','0','1','1','1','1','EQ','input','',7,'admin','2025-10-16 21:19:56','',NULL),(480,46,'expire_time','过期时间（null表示永久有效；如临时图片设为24小时后过期）','datetime','Date','expireTime','0','0','0','1','1','1','1','EQ','datetime','',8,'admin','2025-10-16 21:19:56','',NULL),(481,46,'is_valid','是否有效：0-无效（已删除/过期） 1-有效','tinyint','Long','isValid','0','0','1','1','1','1','1','EQ','input','',9,'admin','2025-10-16 21:19:56','',NULL),(482,46,'create_time',NULL,'datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',10,'admin','2025-10-16 21:19:56','',NULL),(483,46,'update_time',NULL,'datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',11,'admin','2025-10-16 21:19:56','',NULL),(484,47,'message_id','消息唯一ID（雪花算法生成，全局唯一）','bigint','Long','messageId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:19:56','',NULL),(485,47,'session_id','所属会话ID（关联chat_session.session_id，聚合同一会话的消息）','bigint','Long','sessionId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:19:56','',NULL),(486,47,'from_type','发送方类型：1-用户 2-骑手 3-商家 4-系统','tinyint','Long','fromType','0','0','1','1','1','1','1','EQ','select','',3,'admin','2025-10-16 21:19:56','',NULL),(487,47,'from_id','发送方ID（系统消息from_id固定为0）','bigint','Long','fromId','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:19:56','',NULL),(488,47,'to_type','接收方类型：1-用户 2-骑手 3-商家','tinyint','Long','toType','0','0','1','1','1','1','1','EQ','select','',5,'admin','2025-10-16 21:19:56','',NULL),(489,47,'to_id','接收方ID','bigint','Long','toId','0','0','1','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:19:56','',NULL),(490,47,'msg_type','消息类型：1-文本 2-图片 3-语音 4-系统通知','tinyint','Long','msgType','0','0','1','1','1','1','1','EQ','select','',7,'admin','2025-10-16 21:19:56','',NULL),(491,47,'msg_content','消息内容：文本消息存内容；图片/语音存MinIO的URL；系统通知存模板内容','text','String','msgContent','0','0','0','1','1','1','1','EQ','editor','',8,'admin','2025-10-16 21:19:56','',NULL),(492,47,'msg_status','消息状态：0-发送中 1-已送达 2-已读 3-已撤回 4-发送失败','tinyint','Long','msgStatus','0','0','1','1','1','1','1','EQ','radio','',9,'admin','2025-10-16 21:19:56','',NULL),(493,47,'send_time','消息发送时间','datetime','Date','sendTime','0','0','1','1','1','1','1','EQ','datetime','',10,'admin','2025-10-16 21:19:56','',NULL),(494,47,'deliver_time','消息送达时间（仅用于需确认送达的场景）','datetime','Date','deliverTime','0','0','0','1','1','1','1','EQ','datetime','',11,'admin','2025-10-16 21:19:56','',NULL),(495,47,'read_time','消息已读时间（接收方点击后更新）','datetime','Date','readTime','0','0','0','1','1','1','1','EQ','datetime','',12,'admin','2025-10-16 21:19:56','',NULL),(496,47,'is_deleted','是否删除（软删除）：0-未删除 1-已删除（仅对删除方隐藏）','tinyint','Long','isDeleted','0','0','1','1','1','1','1','EQ','input','',13,'admin','2025-10-16 21:19:56','',NULL),(497,47,'create_time',NULL,'datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',14,'admin','2025-10-16 21:19:56','',NULL),(498,47,'update_time',NULL,'datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',15,'admin','2025-10-16 21:19:56','',NULL),(499,48,'read_id','已读记录唯一ID（雪花算法生成）','bigint','Long','readId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:19:56','',NULL),(500,48,'message_id','关联消息ID（关联chat_message.message_id）','bigint','Long','messageId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-10-16 21:19:56','',NULL),(501,48,'reader_type','已读用户类型：1-用户 2-骑手 3-商家','tinyint','Long','readerType','0','0','1','1','1','1','1','EQ','select','',3,'admin','2025-10-16 21:19:56','',NULL),(502,48,'reader_id','已读用户ID（谁已读这条消息）','bigint','Long','readerId','0','0','1','1','1','1','1','EQ','input','',4,'admin','2025-10-16 21:19:56','',NULL),(503,48,'read_status','已读状态：0-未读 1-已读','tinyint','Long','readStatus','0','0','1','1','1','1','1','EQ','radio','',5,'admin','2025-10-16 21:19:56','',NULL),(504,48,'read_time','已读时间','datetime','Date','readTime','0','0','0','1','1','1','1','EQ','datetime','',6,'admin','2025-10-16 21:19:56','',NULL),(505,48,'create_time',NULL,'datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',7,'admin','2025-10-16 21:19:56','',NULL),(506,48,'update_time',NULL,'datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',8,'admin','2025-10-16 21:19:56','',NULL),(507,49,'session_id','会话唯一ID（雪花算法生成，全局唯一）','bigint','Long','sessionId','1','0','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-10-16 21:19:56','',NULL),(508,49,'from_type','发送方类型：1-用户 2-骑手 3-商家','tinyint','Long','fromType','0','0','1','1','1','1','1','EQ','select','',2,'admin','2025-10-16 21:19:56','',NULL),(509,49,'from_id','发送方ID（关联user_db.user_id/ridder_db.ridder_id/merchant_db.merchant_base_id）','bigint','Long','fromId','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-10-16 21:19:56','',NULL),(510,49,'to_type','接收方类型：1-用户 2-骑手 3-商家','tinyint','Long','toType','0','0','1','1','1','1','1','EQ','select','',4,'admin','2025-10-16 21:19:56','',NULL),(511,49,'to_id','接收方ID（关联对应业务库的主键）','bigint','Long','toId','0','0','1','1','1','1','1','EQ','input','',5,'admin','2025-10-16 21:19:56','',NULL),(512,49,'last_msg_id','最后一条消息的ID（关联chat_message.message_id）','bigint','Long','lastMsgId','0','0','0','1','1','1','1','EQ','input','',6,'admin','2025-10-16 21:19:56','',NULL),(513,49,'last_msg_content','最后一条消息内容（冗余，用于会话列表快速展示）','varchar(500)','String','lastMsgContent','0','0','0','1','1','1','1','EQ','editor','',7,'admin','2025-10-16 21:19:56','',NULL),(514,49,'last_msg_type','最后一条消息类型：1-文本 2-图片 3-语音 4-系统通知','tinyint','Long','lastMsgType','0','0','0','1','1','1','1','EQ','select','',8,'admin','2025-10-16 21:19:56','',NULL),(515,49,'last_msg_time','最后一条消息发送时间','datetime','Date','lastMsgTime','0','0','0','1','1','1','1','EQ','datetime','',9,'admin','2025-10-16 21:19:56','',NULL),(516,49,'unread_count','未读消息数（接收方视角，如用户A的会话中未读数量）','int','Long','unreadCount','0','0','1','1','1','1','1','EQ','input','',10,'admin','2025-10-16 21:19:56','',NULL),(517,49,'session_status','会话状态：0-已删除 1-正常 2-已屏蔽','tinyint','Long','sessionStatus','0','0','1','1','1','1','1','EQ','radio','',11,'admin','2025-10-16 21:19:56','',NULL),(518,49,'create_time','会话创建时间（首次发消息时生成）','datetime','Date','createTime','0','0','1','1',NULL,NULL,NULL,'EQ','datetime','',12,'admin','2025-10-16 21:19:56','',NULL),(519,49,'update_time','会话更新时间（最后一条消息发送/状态变更时更新）','datetime','Date','updateTime','0','0','1','1','1',NULL,NULL,'EQ','datetime','',13,'admin','2025-10-16 21:19:56','',NULL);
/*!40000 ALTER TABLE `gen_table_column` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods_evaluation`
--

DROP TABLE IF EXISTS `goods_evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods_evaluation` (
  `goods_evaluation_id` bigint NOT NULL COMMENT '评价唯一ID',
  `merchant_goods_id` bigint NOT NULL COMMENT '商品ID',
  `merchant_base_id` bigint NOT NULL COMMENT '商家ID',
  `user_id` bigint NOT NULL COMMENT '评价用户ID',
  `order_id` bigint DEFAULT NULL COMMENT '关联订单ID',
  `order_item_id` bigint DEFAULT NULL COMMENT '关联订单项ID',
  `rating` tinyint NOT NULL COMMENT '商品评分(1-5分)',
  `content` text COMMENT '评价内容',
  `is_anonymous` tinyint NOT NULL DEFAULT '0' COMMENT '是否匿名：0-否 1-是',
  `merchant_reply` text COMMENT '商家回复',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  `reply_time` datetime DEFAULT NULL COMMENT '回复时间',
  `useful_count` int NOT NULL DEFAULT '0' COMMENT '有用数（点赞数）',
  PRIMARY KEY (`goods_evaluation_id`),
  KEY `idx_goods_id` (`merchant_goods_id`),
  KEY `idx_merchant_id` (`merchant_base_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_rating` (`rating`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_goods_evaluation_goods` FOREIGN KEY (`merchant_goods_id`) REFERENCES `merchant_goods` (`merchant_goods_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_goods_evaluation_merchant` FOREIGN KEY (`merchant_base_id`) REFERENCES `merchant_base` (`merchant_base_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品评价表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods_evaluation`
--

LOCK TABLES `goods_evaluation` WRITE;
/*!40000 ALTER TABLE `goods_evaluation` DISABLE KEYS */;
/*!40000 ALTER TABLE `goods_evaluation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods_evaluation_image`
--

DROP TABLE IF EXISTS `goods_evaluation_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods_evaluation_image` (
  `goods_evaluation_image_id` bigint NOT NULL COMMENT '图片ID',
  `goods_evaluation_id` bigint NOT NULL COMMENT '关联评价ID',
  `image_url` varchar(255) NOT NULL COMMENT '图片URL',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序序号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`goods_evaluation_image_id`),
  KEY `idx_evaluation_id` (`goods_evaluation_id`),
  CONSTRAINT `fk_eval_image_evaluation` FOREIGN KEY (`goods_evaluation_id`) REFERENCES `goods_evaluation` (`goods_evaluation_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品评价图片表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods_evaluation_image`
--

LOCK TABLES `goods_evaluation_image` WRITE;
/*!40000 ALTER TABLE `goods_evaluation_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `goods_evaluation_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `merchant_activity`
--

DROP TABLE IF EXISTS `merchant_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `merchant_activity` (
  `merchant_activity_id` bigint NOT NULL COMMENT '活动唯一ID',
  `merchant_base_id` bigint NOT NULL COMMENT '所属商家ID',
  `activity_name` varchar(100) NOT NULL COMMENT '活动名称',
  `activity_type` varchar(50) NOT NULL COMMENT '活动类型',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `content` text COMMENT '活动内容',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-未开始 1-进行中 2-已结束',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`merchant_activity_id`),
  KEY `idx_merchant_id` (`merchant_base_id`),
  KEY `idx_status` (`status`),
  KEY `idx_time_range` (`start_time`,`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商家活动表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `merchant_activity`
--

LOCK TABLES `merchant_activity` WRITE;
/*!40000 ALTER TABLE `merchant_activity` DISABLE KEYS */;
/*!40000 ALTER TABLE `merchant_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `merchant_address`
--

DROP TABLE IF EXISTS `merchant_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `merchant_address` (
  `merchant_address_id` bigint NOT NULL COMMENT '地址ID',
  `merchant_base_id` bigint NOT NULL COMMENT '商家ID',
  `province` varchar(20) NOT NULL COMMENT '省份',
  `city` varchar(20) NOT NULL COMMENT '城市',
  `district` varchar(20) NOT NULL COMMENT '区县',
  `detail_address` varchar(255) NOT NULL COMMENT '详细地址',
  `contact_person` varchar(20) NOT NULL COMMENT '联系人',
  `contact_phone` varchar(20) NOT NULL COMMENT '联系电话',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`merchant_address_id`),
  UNIQUE KEY `uk_merchant_id` (`merchant_base_id`),
  KEY `idx_district` (`district`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商家地址表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `merchant_address`
--

LOCK TABLES `merchant_address` WRITE;
/*!40000 ALTER TABLE `merchant_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `merchant_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `merchant_base`
--

DROP TABLE IF EXISTS `merchant_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `merchant_base` (
  `merchant_base_id` bigint NOT NULL COMMENT '商家唯一ID',
  `username` varchar(50) NOT NULL COMMENT '登录账号',
  `password` varchar(100) NOT NULL COMMENT '密码(BCrypt加密)',
  `merchant_name` varchar(100) NOT NULL COMMENT '商家名称',
  `logo` varchar(255) DEFAULT NULL COMMENT '商家Logo URL',
  `merchant_address_id` bigint NOT NULL COMMENT '店铺地址ID',
  `business_scope` varchar(50) NOT NULL COMMENT '经营范围',
  `business_hours` varchar(100) NOT NULL COMMENT '营业时间',
  `delivery_range` decimal(5,2) NOT NULL COMMENT '配送范围(公里)',
  `min_order_amount` decimal(10,2) NOT NULL COMMENT '起送金额',
  `delivery_fee` decimal(10,2) NOT NULL COMMENT '基础配送费',
  `license_img` varchar(255) NOT NULL COMMENT '营业执照URL',
  `rating` decimal(3,2) NOT NULL DEFAULT '4.00' COMMENT '商家评分',
  `month_sales` int NOT NULL DEFAULT '0' COMMENT '月销量',
  `audit_status` tinyint NOT NULL DEFAULT '0' COMMENT '审核状态：0-待审核 1-通过 2-拒绝',
  `business_status` tinyint NOT NULL DEFAULT '1' COMMENT '营业状态：0-停业 1-营业',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '店铺经度',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '店铺纬度',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`merchant_base_id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_merchant_name` (`merchant_name`),
  KEY `idx_audit_status` (`audit_status`),
  KEY `idx_business_status` (`business_status`),
  KEY `idx_rating_sales` (`rating`,`month_sales`),
  KEY `idx_lon_lat` (`longitude`,`latitude`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商家基础信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `merchant_base`
--

LOCK TABLES `merchant_base` WRITE;
/*!40000 ALTER TABLE `merchant_base` DISABLE KEYS */;
/*!40000 ALTER TABLE `merchant_base` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `merchant_evaluation`
--

DROP TABLE IF EXISTS `merchant_evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `merchant_evaluation` (
  `merchant_evaluation_id` bigint NOT NULL COMMENT '评价唯一ID',
  `merchant_base_id` bigint NOT NULL COMMENT '所属商家ID',
  `user_id` bigint NOT NULL COMMENT '评价用户ID',
  `order_id` bigint DEFAULT NULL COMMENT '关联订单ID',
  `rating` tinyint NOT NULL COMMENT '评分(1-5分)',
  `taste_score` tinyint DEFAULT NULL COMMENT '口味评分(1-5分，仅餐饮类)',
  `package_score` tinyint DEFAULT NULL COMMENT '包装评分(1-5分)',
  `content` text COMMENT '评价内容',
  `img_urls` varchar(1000) DEFAULT NULL COMMENT '评价图片URL(逗号分隔)',
  `merchant_reply` text COMMENT '商家回复',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  `reply_time` datetime DEFAULT NULL COMMENT '回复时间',
  PRIMARY KEY (`merchant_evaluation_id`),
  KEY `idx_merchant_id` (`merchant_base_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_rating` (`rating`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商家评价表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `merchant_evaluation`
--

LOCK TABLES `merchant_evaluation` WRITE;
/*!40000 ALTER TABLE `merchant_evaluation` DISABLE KEYS */;
/*!40000 ALTER TABLE `merchant_evaluation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `merchant_goods`
--

DROP TABLE IF EXISTS `merchant_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `merchant_goods` (
  `merchant_goods_id` bigint NOT NULL COMMENT '商品唯一ID',
  `merchant_base_id` bigint NOT NULL COMMENT '所属商家ID',
  `goods_name` varchar(100) NOT NULL COMMENT '商品名称',
  `category` varchar(50) NOT NULL COMMENT '商品分类',
  `sub_category` varchar(50) DEFAULT NULL COMMENT '商品子分类',
  `price` decimal(10,2) NOT NULL COMMENT '单价',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `stock` int NOT NULL DEFAULT '0' COMMENT '库存',
  `sales_count` bigint NOT NULL DEFAULT '0' COMMENT '销量',
  `description` text COMMENT '商品描述',
  `tag_codes` varchar(500) DEFAULT NULL COMMENT '商品标签编码（逗号分隔，如FOOD_SPICY,FAST_FOOD）',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-下架 1-上架',
  `avg_rating` decimal(3,2) NOT NULL DEFAULT '4.00' COMMENT '商品平均评分',
  `rating_count` int NOT NULL DEFAULT '0' COMMENT '评分总次数',
  `five_star_rate` decimal(5,2) DEFAULT '0.00' COMMENT '五星好评率(%)',
  `four_star_rate` decimal(5,2) DEFAULT '0.00' COMMENT '四星好评率(%)',
  `three_star_rate` decimal(5,2) DEFAULT '0.00' COMMENT '三星评价率(%)',
  `two_star_rate` decimal(5,2) DEFAULT '0.00' COMMENT '二星评价率(%)',
  `one_star_rate` decimal(5,2) DEFAULT '0.00' COMMENT '一星差评率(%)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`merchant_goods_id`),
  KEY `idx_merchant_id` (`merchant_base_id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_sales_count` (`sales_count`),
  KEY `idx_tag_codes` (`tag_codes`),
  KEY `idx_avg_rating` (`avg_rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `merchant_goods`
--

LOCK TABLES `merchant_goods` WRITE;
/*!40000 ALTER TABLE `merchant_goods` DISABLE KEYS */;
/*!40000 ALTER TABLE `merchant_goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `merchant_goods_image`
--

DROP TABLE IF EXISTS `merchant_goods_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `merchant_goods_image` (
  `merchant_goods_image_id` bigint NOT NULL COMMENT '图片ID',
  `merchant_goods_id` bigint NOT NULL COMMENT '关联商品ID',
  `image_url` varchar(255) NOT NULL COMMENT '图片URL',
  `image_desc` varchar(100) DEFAULT NULL COMMENT '图片描述（如"商品正面图"）',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序序号（值越小越靠前）',
  `is_main` tinyint NOT NULL DEFAULT '0' COMMENT '是否主图：0-否 1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`merchant_goods_image_id`),
  KEY `idx_goods_id` (`merchant_goods_id`),
  KEY `idx_goods_main` (`merchant_goods_id`,`is_main`),
  CONSTRAINT `fk_image_goods_merchant` FOREIGN KEY (`merchant_goods_id`) REFERENCES `merchant_goods` (`merchant_goods_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品图片关联表（支持多图展示）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `merchant_goods_image`
--

LOCK TABLES `merchant_goods_image` WRITE;
/*!40000 ALTER TABLE `merchant_goods_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `merchant_goods_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `merchant_wallet`
--

DROP TABLE IF EXISTS `merchant_wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `merchant_wallet` (
  `merchant_wallet_id` bigint NOT NULL COMMENT '钱包唯一ID',
  `merchant_base_id` bigint NOT NULL COMMENT '所属商家ID',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '可用余额',
  `freeze_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '冻结金额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`merchant_wallet_id`),
  UNIQUE KEY `uk_merchant_id` (`merchant_base_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商家钱包表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `merchant_wallet`
--

LOCK TABLES `merchant_wallet` WRITE;
/*!40000 ALTER TABLE `merchant_wallet` DISABLE KEYS */;
/*!40000 ALTER TABLE `merchant_wallet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_coupon`
--

DROP TABLE IF EXISTS `order_coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_coupon` (
  `order_coupon_id` bigint NOT NULL COMMENT 'ID',
  `order_main_id` bigint NOT NULL COMMENT '订单ID',
  `coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `coupon_name` varchar(100) NOT NULL COMMENT '优惠券名称',
  `discount_amount` decimal(10,2) NOT NULL COMMENT '优惠金额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`order_coupon_id`),
  KEY `idx_order_id` (`order_main_id`),
  KEY `idx_coupon_id` (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单优惠券表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_coupon`
--

LOCK TABLES `order_coupon` WRITE;
/*!40000 ALTER TABLE `order_coupon` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_coupon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_delivery`
--

DROP TABLE IF EXISTS `order_delivery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_delivery` (
  `order_delivery_id` bigint NOT NULL COMMENT '配送记录ID',
  `order_main_id` bigint NOT NULL COMMENT '订单ID',
  `rider_id` bigint DEFAULT NULL COMMENT '骑手ID',
  `rider_nickname` varchar(50) DEFAULT NULL COMMENT '骑手昵称(冗余)',
  `delivery_fee` decimal(10,2) NOT NULL COMMENT '配送费（可基于主表取货-送货坐标计算）',
  `actual_pick_longitude` decimal(11,8) DEFAULT NULL COMMENT '实际取货经度',
  `actual_pick_latitude` decimal(10,8) DEFAULT NULL COMMENT '实际取货纬度',
  `actual_deliver_longitude` decimal(11,8) DEFAULT NULL COMMENT '实际送达经度',
  `actual_deliver_latitude` decimal(10,8) DEFAULT NULL COMMENT '实际送达纬度',
  `assign_time` datetime DEFAULT NULL COMMENT '派单时间',
  `receive_time` datetime DEFAULT NULL COMMENT '接单时间',
  `pick_time` datetime DEFAULT NULL COMMENT '取货时间',
  `deliver_time` datetime DEFAULT NULL COMMENT '送达时间',
  `delivery_status` tinyint NOT NULL DEFAULT '0' COMMENT '配送状态：0-待分配 1-已接单 2-已取货 3-已送达',
  PRIMARY KEY (`order_delivery_id`),
  UNIQUE KEY `uk_order_id` (`order_main_id`),
  KEY `idx_rider_id` (`rider_id`),
  KEY `idx_delivery_status` (`delivery_status`),
  KEY `idx_actual_pick_location` (`actual_pick_longitude`,`actual_pick_latitude`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单配送表（含实际配送定位）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_delivery`
--

LOCK TABLES `order_delivery` WRITE;
/*!40000 ALTER TABLE `order_delivery` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_delivery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_errand_detail`
--

DROP TABLE IF EXISTS `order_errand_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_errand_detail` (
  `order_errand_detail_id` bigint NOT NULL COMMENT '明细唯一ID',
  `order_main_id` bigint NOT NULL COMMENT '关联订单ID',
  `errand_type` tinyint NOT NULL COMMENT '跑腿类型：1-帮我送 2-帮我买',
  `goods_desc` varchar(255) NOT NULL COMMENT '物品/商品描述（如“生日蛋糕/6寸”“ textbooks/高等数学”）',
  `expected_time` datetime DEFAULT NULL COMMENT '期望送达时间',
  `advance_amount` decimal(10,2) DEFAULT '0.00' COMMENT '骑手垫付金额（帮我买场景专用）',
  `tip_amount` decimal(10,2) DEFAULT '0.00' COMMENT '小费金额',
  `buy_photo_url` varchar(255) DEFAULT NULL COMMENT '代付凭证',
  PRIMARY KEY (`order_errand_detail_id`),
  KEY `idx_order_id` (`order_main_id`),
  KEY `idx_errand_type` (`errand_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='跑腿订单明细表（不含地址信息）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_errand_detail`
--

LOCK TABLES `order_errand_detail` WRITE;
/*!40000 ALTER TABLE `order_errand_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_errand_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_main`
--

DROP TABLE IF EXISTS `order_main`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_main` (
  `order_main_id` bigint NOT NULL COMMENT '订单唯一ID',
  `order_no` varchar(32) NOT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL COMMENT '下单用户ID（关联user_db.user_base.user_base_id）',
  `user_nickname` varchar(50) NOT NULL COMMENT '用户昵称(冗余)',
  `order_type` tinyint NOT NULL COMMENT '订单类型：1-外卖单 2-跑腿单 3-二手交易单',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `pay_amount` decimal(10,2) NOT NULL COMMENT '实付金额',
  `discount_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '优惠金额',
  `pay_status` tinyint NOT NULL DEFAULT '0' COMMENT '支付状态：0-未支付 1-已支付 2-退款中 3-已退款',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `pay_type` tinyint DEFAULT NULL COMMENT '支付方式：1-余额 2-微信 3-支付宝',
  `order_status` tinyint NOT NULL DEFAULT '1' COMMENT '订单状态：1-待接单 2-待取货 3-配送中 4-已完成 5-已取消',
  `cancel_reason` varchar(255) DEFAULT NULL COMMENT '取消原因',
  `cancel_operator` varchar(50) DEFAULT NULL COMMENT '取消操作人',
  `pick_address_id` bigint NOT NULL COMMENT '取货地址ID（外卖关联merchant_db.merchant_address.merchant_address_id；其他关联user_db.user_address.user_address_id）',
  `pick_address` varchar(255) NOT NULL COMMENT '取货地址文本（冗余，如“XX食堂3楼奶茶店”“XX宿舍2栋101”）',
  `pick_contact` varchar(20) NOT NULL COMMENT '取货联系人',
  `pick_phone` varchar(20) NOT NULL COMMENT '取货电话（AES加密，与user_db加密标准一致）',
  `pick_longitude` decimal(11,8) DEFAULT NULL COMMENT '取货经度（定位功能填充，WGS84坐标系，精度1米内）',
  `pick_latitude` decimal(10,8) DEFAULT NULL COMMENT '取货纬度（定位功能填充，WGS84坐标系，精度1米内）',
  `deliver_address_id` bigint DEFAULT NULL COMMENT '送货地址ID（关联user_db.user_address.user_address_id，线下二手单可空）',
  `deliver_address` varchar(255) DEFAULT NULL COMMENT '送货地址文本（冗余，如“XX教学楼503室”）',
  `deliver_contact` varchar(20) DEFAULT NULL COMMENT '收货联系人',
  `deliver_phone` varchar(20) DEFAULT NULL COMMENT '收货电话（AES加密，与user_db加密标准一致）',
  `deliver_longitude` decimal(11,8) DEFAULT NULL COMMENT '送货经度（定位功能填充，WGS84坐标系）',
  `deliver_latitude` decimal(10,8) DEFAULT NULL COMMENT '送货纬度（定位功能填充，WGS84坐标系）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`order_main_id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_order_type` (`order_type`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_pay_status` (`pay_status`),
  KEY `idx_pick_location` (`pick_longitude`,`pick_latitude`) COMMENT '取货经纬度索引（附近取货点查询）',
  KEY `idx_deliver_location` (`deliver_longitude`,`deliver_latitude`) COMMENT '送货经纬度索引（配送范围校验）',
  KEY `idx_pick_address_id` (`pick_address_id`),
  KEY `idx_deliver_address_id` (`deliver_address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单主表（整合地址与定位信息）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_main`
--

LOCK TABLES `order_main` WRITE;
/*!40000 ALTER TABLE `order_main` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_main` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_pay_record`
--

DROP TABLE IF EXISTS `order_pay_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_pay_record` (
  `order_pay_record_id` bigint NOT NULL COMMENT '支付记录ID',
  `order_main_id` bigint NOT NULL COMMENT '订单ID',
  `pay_no` varchar(64) DEFAULT NULL COMMENT '支付单号',
  `pay_amount` decimal(10,2) NOT NULL COMMENT '支付金额',
  `pay_type` tinyint NOT NULL COMMENT '支付方式：1-余额 2-微信 3-支付宝',
  `pay_status` tinyint NOT NULL COMMENT '支付状态：0-处理中 1-成功 2-失败',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `callback_data` text COMMENT '支付回调数据',
  PRIMARY KEY (`order_pay_record_id`),
  KEY `idx_order_id` (`order_main_id`),
  KEY `idx_pay_no` (`pay_no`),
  KEY `idx_pay_status` (`pay_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单支付记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_pay_record`
--

LOCK TABLES `order_pay_record` WRITE;
/*!40000 ALTER TABLE `order_pay_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_pay_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_secondhand_detail`
--

DROP TABLE IF EXISTS `order_secondhand_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_secondhand_detail` (
  `order_secondhand_detail_id` bigint NOT NULL COMMENT '明细唯一ID',
  `order_main_id` bigint NOT NULL COMMENT '关联订单ID',
  `goods_id` bigint NOT NULL COMMENT '二手商品ID',
  `goods_name` varchar(100) NOT NULL COMMENT '商品名称(冗余)',
  `seller_id` bigint NOT NULL COMMENT '卖家ID（关联user_db.user_base.user_base_id）',
  `sell_way` tinyint NOT NULL COMMENT '交易方式：1-线上 2-线下',
  `seller_nickname` varchar(50) NOT NULL COMMENT '卖家昵称(冗余)',
  `deposit_amount` decimal(10,2) NOT NULL COMMENT '担保金金额',
  `confirm_time` datetime DEFAULT NULL COMMENT '买家确认收货时间',
  `evaluate_status` tinyint NOT NULL DEFAULT '0' COMMENT '评价状态：0-未评价 1-已评价',
  PRIMARY KEY (`order_secondhand_detail_id`),
  KEY `idx_order_id` (`order_main_id`),
  KEY `idx_seller_id` (`seller_id`),
  KEY `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='二手交易订单明细表（不含地址信息）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_secondhand_detail`
--

LOCK TABLES `order_secondhand_detail` WRITE;
/*!40000 ALTER TABLE `order_secondhand_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_secondhand_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_takeout_detail`
--

DROP TABLE IF EXISTS `order_takeout_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_takeout_detail` (
  `order_takeout_detail_id` bigint NOT NULL COMMENT '明细唯一ID',
  `order_main_id` bigint NOT NULL COMMENT '关联订单ID',
  `merchant_id` bigint NOT NULL COMMENT '商家ID（关联merchant_db.merchant_base.merchant_base_id）',
  `merchant_name` varchar(100) NOT NULL COMMENT '商家名称(冗余)',
  `goods_id` bigint NOT NULL COMMENT '商品ID（关联merchant_db.merchant_goods.merchant_goods_id）',
  `goods_name` varchar(100) NOT NULL COMMENT '商品名称(冗余)',
  `goods_price` decimal(10,2) NOT NULL COMMENT '商品单价',
  `quantity` int NOT NULL COMMENT '购买数量',
  `subtotal` decimal(10,2) NOT NULL COMMENT '小计金额',
  `goods_spec` varchar(100) DEFAULT NULL COMMENT '商品规格（如“中杯/少糖”）',
  `goods_tags` varchar(200) DEFAULT NULL COMMENT '商品标签(冗余，如“甜口/冰饮”，用于推荐)',
  PRIMARY KEY (`order_takeout_detail_id`),
  KEY `idx_order_id` (`order_main_id`),
  KEY `idx_merchant_id` (`merchant_id`),
  KEY `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='外卖订单明细表（不含地址信息）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_takeout_detail`
--

LOCK TABLES `order_takeout_detail` WRITE;
/*!40000 ALTER TABLE `order_takeout_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_takeout_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `platform_admin`
--

DROP TABLE IF EXISTS `platform_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `platform_admin` (
  `platform_admin_id` bigint NOT NULL COMMENT '管理员唯一ID（雪花算法）',
  `username` varchar(50) NOT NULL COMMENT '登录账号（唯一）',
  `password` varchar(100) NOT NULL COMMENT '密码（BCrypt加密存储）',
  `real_name` varchar(20) NOT NULL COMMENT '真实姓名',
  `phone` varchar(20) NOT NULL COMMENT '联系电话',
  `platform_role_id` bigint NOT NULL COMMENT '关联角色ID（关联platform_role.platform_role_id）',
  `account_status` tinyint NOT NULL DEFAULT '1' COMMENT '账号状态：0-禁用 1-正常',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`platform_admin_id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_role_id` (`platform_role_id`),
  KEY `idx_account_status` (`account_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='平台管理员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platform_admin`
--

LOCK TABLES `platform_admin` WRITE;
/*!40000 ALTER TABLE `platform_admin` DISABLE KEYS */;
/*!40000 ALTER TABLE `platform_admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `platform_announcement`
--

DROP TABLE IF EXISTS `platform_announcement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `platform_announcement` (
  `platform_announcement_id` bigint NOT NULL COMMENT '公告唯一ID',
  `title` varchar(100) NOT NULL COMMENT '公告标题',
  `content` text NOT NULL COMMENT '公告内容',
  `publisher_id` bigint NOT NULL COMMENT '发布人ID（关联platform_admin.platform_admin_id）',
  `publisher_name` varchar(20) NOT NULL COMMENT '发布人姓名（冗余）',
  `publish_time` datetime NOT NULL COMMENT '发布时间',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-草稿 1-已发布 2-已下架',
  `read_count` int NOT NULL DEFAULT '0' COMMENT '阅读量',
  `is_top` tinyint NOT NULL DEFAULT '0' COMMENT '是否置顶：0-否 1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`platform_announcement_id`),
  KEY `idx_status_top` (`status`,`is_top`),
  KEY `idx_publish_time` (`publish_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platform_announcement`
--

LOCK TABLES `platform_announcement` WRITE;
/*!40000 ALTER TABLE `platform_announcement` DISABLE KEYS */;
/*!40000 ALTER TABLE `platform_announcement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `platform_coupon`
--

DROP TABLE IF EXISTS `platform_coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `platform_coupon` (
  `platform_coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `coupon_no` varchar(50) NOT NULL COMMENT '优惠券编号',
  `coupon_name` varchar(100) NOT NULL COMMENT '优惠券名称',
  `coupon_type` tinyint NOT NULL COMMENT '类型：1-满减券 2-折扣券 3-固定金额券',
  `face_value` decimal(10,2) NOT NULL COMMENT '面值',
  `min_spend` decimal(10,2) DEFAULT '0.00' COMMENT '最低消费金额',
  `discount` decimal(3,2) DEFAULT NULL COMMENT '折扣率（如0.85=85折）',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `total_count` int NOT NULL COMMENT '总发行量',
  `remain_count` int NOT NULL COMMENT '剩余数量',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-未发布 1-已发布 2-已过期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`platform_coupon_id`),
  UNIQUE KEY `uk_coupon_no` (`coupon_no`),
  KEY `idx_status_time` (`status`,`start_time`,`end_time`),
  KEY `idx_coupon_type` (`coupon_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='平台优惠券表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platform_coupon`
--

LOCK TABLES `platform_coupon` WRITE;
/*!40000 ALTER TABLE `platform_coupon` DISABLE KEYS */;
/*!40000 ALTER TABLE `platform_coupon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `platform_operate_log`
--

DROP TABLE IF EXISTS `platform_operate_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `platform_operate_log` (
  `platform_operate_log_id` bigint NOT NULL COMMENT '日志唯一ID',
  `admin_id` bigint NOT NULL COMMENT '操作管理员ID（关联platform_admin.platform_admin_id）',
  `admin_name` varchar(20) NOT NULL COMMENT '管理员姓名（冗余）',
  `oper_type` varchar(20) NOT NULL COMMENT '操作类型（create/update/delete/audit）',
  `oper_module` varchar(50) NOT NULL COMMENT '操作模块（merchant/order/user/rider）',
  `oper_content` text NOT NULL COMMENT '操作内容（如"审核商家ID=123通过"）',
  `ip_address` varchar(50) NOT NULL COMMENT '操作IP地址',
  `oper_time` datetime NOT NULL COMMENT '操作时间',
  `user_agent` text COMMENT '用户代理信息（浏览器/设备）',
  PRIMARY KEY (`platform_operate_log_id`),
  KEY `idx_admin_id` (`admin_id`),
  KEY `idx_oper_module` (`oper_module`),
  KEY `idx_oper_time` (`oper_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platform_operate_log`
--

LOCK TABLES `platform_operate_log` WRITE;
/*!40000 ALTER TABLE `platform_operate_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `platform_operate_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `platform_permission`
--

DROP TABLE IF EXISTS `platform_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `platform_permission` (
  `platform_permission_id` bigint NOT NULL COMMENT '权限唯一ID',
  `perm_name` varchar(50) NOT NULL COMMENT '权限名称（如订单管理/商家审核）',
  `perm_key` varchar(100) NOT NULL COMMENT '权限标识（如order:manage/merchant:audit）',
  `perm_type` tinyint NOT NULL COMMENT '权限类型：1-菜单 2-按钮',
  `parent_perm_id` bigint DEFAULT NULL COMMENT '父权限ID（用于构建权限树，关联platform_permission.platform_permission_id）',
  `menu_path` varchar(100) DEFAULT NULL COMMENT '菜单路径（仅perm_type=1时有值）',
  `sort` int DEFAULT '0' COMMENT '排序序号（值越小越靠前）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`platform_permission_id`),
  UNIQUE KEY `uk_perm_key` (`perm_key`),
  KEY `idx_parent_id` (`parent_perm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platform_permission`
--

LOCK TABLES `platform_permission` WRITE;
/*!40000 ALTER TABLE `platform_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `platform_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `platform_role`
--

DROP TABLE IF EXISTS `platform_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `platform_role` (
  `platform_role_id` bigint NOT NULL COMMENT '角色唯一ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称（如超级管理员/运营专员）',
  `role_code` varchar(50) NOT NULL COMMENT '角色编码（唯一标识，如ADMIN/OPERATOR）',
  `role_desc` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`platform_role_id`),
  UNIQUE KEY `uk_role_name` (`role_name`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platform_role`
--

LOCK TABLES `platform_role` WRITE;
/*!40000 ALTER TABLE `platform_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `platform_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `platform_role_mapping`
--

DROP TABLE IF EXISTS `platform_role_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `platform_role_mapping` (
  `platform_role_mapping_id` bigint NOT NULL COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '登录账号（各角色的username字段）',
  `role_type` tinyint NOT NULL COMMENT '角色类型：1-学生用户 2-骑手 3-商家 4-平台管理员',
  `target_db` varchar(20) NOT NULL COMMENT '目标数据库：user_db/rider_db/merchant_db/platform_db',
  `target_table` varchar(50) NOT NULL COMMENT '目标表：user_base/rider_base/merchant_base/platform_admin',
  `account_status` tinyint NOT NULL DEFAULT '1' COMMENT '账号全局状态：0-禁用 1-正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`platform_role_mapping_id`),
  UNIQUE KEY `uk_username_role` (`username`,`role_type`),
  KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色-账号映射表（多角色登录路由核心）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platform_role_mapping`
--

LOCK TABLES `platform_role_mapping` WRITE;
/*!40000 ALTER TABLE `platform_role_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `platform_role_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `platform_role_perm`
--

DROP TABLE IF EXISTS `platform_role_perm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `platform_role_perm` (
  `platform_role_perm_id` bigint NOT NULL COMMENT '关联唯一ID',
  `platform_role_id` bigint NOT NULL COMMENT '角色ID（关联platform_role.platform_role_id）',
  `platform_permission_id` bigint NOT NULL COMMENT '权限ID（关联platform_permission.platform_permission_id）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`platform_role_perm_id`),
  UNIQUE KEY `uk_role_perm` (`platform_role_id`,`platform_permission_id`),
  KEY `idx_perm_id` (`platform_permission_id`),
  CONSTRAINT `fk_role_perm_perm` FOREIGN KEY (`platform_permission_id`) REFERENCES `platform_permission` (`platform_permission_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_role_perm_role` FOREIGN KEY (`platform_role_id`) REFERENCES `platform_role` (`platform_role_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色权限关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platform_role_perm`
--

LOCK TABLES `platform_role_perm` WRITE;
/*!40000 ALTER TABLE `platform_role_perm` DISABLE KEYS */;
/*!40000 ALTER TABLE `platform_role_perm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `platform_tag`
--

DROP TABLE IF EXISTS `platform_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `platform_tag` (
  `platform_tag_id` bigint NOT NULL COMMENT '标签ID',
  `tag_code` varchar(30) NOT NULL COMMENT '标签编码（唯一）',
  `tag_name` varchar(50) NOT NULL COMMENT '标签名称',
  `tag_type` varchar(30) NOT NULL COMMENT '标签类型',
  `tag_desc` varchar(255) DEFAULT NULL COMMENT '标签描述',
  `parent_code` varchar(30) DEFAULT NULL COMMENT '父标签编码',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用 1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`platform_tag_id`),
  UNIQUE KEY `uk_tag_code` (`tag_code`),
  KEY `idx_tag_type` (`tag_type`),
  KEY `idx_parent_code` (`parent_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='平台标签体系表（管理用户和商品标签）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platform_tag`
--

LOCK TABLES `platform_tag` WRITE;
/*!40000 ALTER TABLE `platform_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `platform_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `platform_workorder`
--

DROP TABLE IF EXISTS `platform_workorder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `platform_workorder` (
  `platform_workorder_id` bigint NOT NULL COMMENT '工单唯一ID',
  `order_id` bigint DEFAULT NULL COMMENT '关联订单ID（来自order_db）',
  `user_id` bigint NOT NULL COMMENT '提交用户ID（来自user/rider/merchant_db）',
  `user_type` tinyint NOT NULL COMMENT '用户类型：1-学生 2-骑手 3-商家',
  `user_nickname` varchar(50) NOT NULL COMMENT '用户昵称（冗余）',
  `content` text NOT NULL COMMENT '工单内容（问题描述）',
  `img_urls` varchar(1000) DEFAULT NULL COMMENT '问题图片URL（逗号分隔）',
  `workorder_type` tinyint NOT NULL COMMENT '工单类型：1-订单投诉 2-服务差评 3-系统故障 4-其他',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID（关联platform_admin.platform_admin_id）',
  `handler_name` varchar(20) DEFAULT NULL COMMENT '处理人姓名（冗余）',
  `handle_status` tinyint NOT NULL DEFAULT '0' COMMENT '处理状态：0-待处理 1-处理中 2-已解决 3-已关闭',
  `handle_result` text COMMENT '处理结果',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `close_time` datetime DEFAULT NULL COMMENT '关闭时间',
  PRIMARY KEY (`platform_workorder_id`),
  KEY `idx_user_id_type` (`user_id`,`user_type`),
  KEY `idx_handle_status` (`handle_status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客服工单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platform_workorder`
--

LOCK TABLES `platform_workorder` WRITE;
/*!40000 ALTER TABLE `platform_workorder` DISABLE KEYS */;
/*!40000 ALTER TABLE `platform_workorder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_blob_triggers`
--

DROP TABLE IF EXISTS `qrtz_blob_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_blob_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Blob类型的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_blob_triggers`
--

LOCK TABLES `qrtz_blob_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_blob_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_blob_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_calendars`
--

DROP TABLE IF EXISTS `qrtz_calendars`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_calendars` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`,`calendar_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='日历信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_calendars`
--

LOCK TABLES `qrtz_calendars` WRITE;
/*!40000 ALTER TABLE `qrtz_calendars` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_calendars` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_cron_triggers`
--

DROP TABLE IF EXISTS `qrtz_cron_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_cron_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Cron类型的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_cron_triggers`
--

LOCK TABLES `qrtz_cron_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_cron_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_cron_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_fired_triggers`
--

DROP TABLE IF EXISTS `qrtz_fired_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_fired_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint NOT NULL COMMENT '触发的时间',
  `sched_time` bigint NOT NULL COMMENT '定时器制定的时间',
  `priority` int NOT NULL COMMENT '优先级',
  `state` varchar(16) NOT NULL COMMENT '状态',
  `job_name` varchar(200) DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`,`entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='已触发的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_fired_triggers`
--

LOCK TABLES `qrtz_fired_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_fired_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_fired_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_job_details`
--

DROP TABLE IF EXISTS `qrtz_job_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_job_details` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) NOT NULL COMMENT '任务组名',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务详细信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_job_details`
--

LOCK TABLES `qrtz_job_details` WRITE;
/*!40000 ALTER TABLE `qrtz_job_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_job_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_locks`
--

DROP TABLE IF EXISTS `qrtz_locks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_locks` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`,`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='存储的悲观锁信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_locks`
--

LOCK TABLES `qrtz_locks` WRITE;
/*!40000 ALTER TABLE `qrtz_locks` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_locks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_paused_trigger_grps`
--

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='暂停的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_paused_trigger_grps`
--

LOCK TABLES `qrtz_paused_trigger_grps` WRITE;
/*!40000 ALTER TABLE `qrtz_paused_trigger_grps` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_paused_trigger_grps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_scheduler_state`
--

DROP TABLE IF EXISTS `qrtz_scheduler_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_scheduler_state` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`,`instance_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='调度器状态表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_scheduler_state`
--

LOCK TABLES `qrtz_scheduler_state` WRITE;
/*!40000 ALTER TABLE `qrtz_scheduler_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_scheduler_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_simple_triggers`
--

DROP TABLE IF EXISTS `qrtz_simple_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_simple_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='简单触发器的信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_simple_triggers`
--

LOCK TABLES `qrtz_simple_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_simple_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_simple_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_simprop_triggers`
--

DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_simprop_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='同步机制的行锁表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_simprop_triggers`
--

LOCK TABLES `qrtz_simprop_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_simprop_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_simprop_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_triggers`
--

DROP TABLE IF EXISTS `qrtz_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) NOT NULL COMMENT '触发器的类型',
  `start_time` bigint NOT NULL COMMENT '开始时间',
  `end_time` bigint DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  KEY `sched_name` (`sched_name`,`job_name`,`job_group`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='触发器详细信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_triggers`
--

LOCK TABLES `qrtz_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rider_base`
--

DROP TABLE IF EXISTS `rider_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rider_base` (
  `rider_base_id` bigint NOT NULL COMMENT '骑手唯一ID',
  `username` varchar(50) NOT NULL COMMENT '登录账号',
  `password` varchar(100) NOT NULL COMMENT '密码(BCrypt加密)',
  `nickname` varchar(50) NOT NULL COMMENT '骑手昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `real_name` varchar(20) NOT NULL COMMENT '真实姓名',
  `id_card` varchar(20) NOT NULL COMMENT '身份证号(AES加密)',
  `id_card_front` varchar(255) NOT NULL COMMENT '身份证正面照URL',
  `id_card_back` varchar(255) NOT NULL COMMENT '身份证反面照URL',
  `phone` varchar(20) NOT NULL COMMENT '联系电话',
  `audit_status` tinyint NOT NULL DEFAULT '0' COMMENT '审核状态：0-待审核 1-通过 2-拒绝',
  `work_status` tinyint NOT NULL DEFAULT '0' COMMENT '工作状态：0-下线 1-上线 2-忙碌',
  `credit_score` int NOT NULL DEFAULT '600' COMMENT '服务信用分',
  `account_status` tinyint NOT NULL DEFAULT '1' COMMENT '账号状态：0-禁用 1-正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`rider_base_id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_id_card` (`id_card`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_audit_status` (`audit_status`),
  KEY `idx_work_status` (`work_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='骑手基础信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rider_base`
--

LOCK TABLES `rider_base` WRITE;
/*!40000 ALTER TABLE `rider_base` DISABLE KEYS */;
/*!40000 ALTER TABLE `rider_base` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rider_evaluation`
--

DROP TABLE IF EXISTS `rider_evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rider_evaluation` (
  `rider_evaluation_id` bigint NOT NULL COMMENT '评价唯一ID',
  `rider_base_id` bigint NOT NULL COMMENT '骑手ID',
  `user_id` bigint NOT NULL COMMENT '评价用户ID',
  `order_id` bigint NOT NULL COMMENT '关联订单ID',
  `rating` tinyint NOT NULL COMMENT '评分(1-5分)',
  `speed_score` tinyint NOT NULL COMMENT '速度评分(1-5分)',
  `attitude_score` tinyint NOT NULL COMMENT '态度评分(1-5分)',
  `content` text COMMENT '评价内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  PRIMARY KEY (`rider_evaluation_id`),
  KEY `idx_rider_id` (`rider_base_id`),
  KEY `idx_rating` (`rating`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='骑手评价表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rider_evaluation`
--

LOCK TABLES `rider_evaluation` WRITE;
/*!40000 ALTER TABLE `rider_evaluation` DISABLE KEYS */;
/*!40000 ALTER TABLE `rider_evaluation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rider_location`
--

DROP TABLE IF EXISTS `rider_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rider_location` (
  `rider_location_id` bigint NOT NULL COMMENT '位置记录唯一ID',
  `rider_base_id` bigint NOT NULL COMMENT '所属骑手ID',
  `longitude` decimal(10,6) NOT NULL COMMENT '当前经度',
  `latitude` decimal(10,6) NOT NULL COMMENT '当前纬度',
  `update_time` datetime NOT NULL COMMENT '位置更新时间',
  PRIMARY KEY (`rider_location_id`),
  UNIQUE KEY `uk_rider_id` (`rider_base_id`),
  KEY `idx_update_time` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='骑手位置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rider_location`
--

LOCK TABLES `rider_location` WRITE;
/*!40000 ALTER TABLE `rider_location` DISABLE KEYS */;
/*!40000 ALTER TABLE `rider_location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rider_order_rel`
--

DROP TABLE IF EXISTS `rider_order_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rider_order_rel` (
  `rider_order_rel_id` bigint NOT NULL COMMENT '关联记录唯一ID',
  `rider_base_id` bigint NOT NULL COMMENT '骑手ID',
  `order_id` bigint NOT NULL COMMENT '关联订单ID',
  `order_type` tinyint NOT NULL COMMENT '订单类型：1-外卖单 2-跑腿单',
  `receive_time` datetime NOT NULL COMMENT '接单时间',
  `pick_up_time` datetime DEFAULT NULL COMMENT '取货时间',
  `deliver_time` datetime DEFAULT NULL COMMENT '送达时间',
  `delivery_status` tinyint NOT NULL COMMENT '配送状态：1-待取货 2-配送中 3-已送达 4-异常取消',
  `abnormal_reason` varchar(255) DEFAULT NULL COMMENT '异常原因',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`rider_order_rel_id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_rider_id` (`rider_base_id`),
  KEY `idx_delivery_status` (`delivery_status`),
  KEY `idx_receive_time` (`receive_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='骑手接单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rider_order_rel`
--

LOCK TABLES `rider_order_rel` WRITE;
/*!40000 ALTER TABLE `rider_order_rel` DISABLE KEYS */;
/*!40000 ALTER TABLE `rider_order_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rider_wallet`
--

DROP TABLE IF EXISTS `rider_wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rider_wallet` (
  `rider_wallet_id` bigint NOT NULL COMMENT '钱包唯一ID',
  `rider_base_id` bigint NOT NULL COMMENT '所属骑手ID',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '可用余额',
  `freeze_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '冻结金额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`rider_wallet_id`),
  UNIQUE KEY `uk_rider_id` (`rider_base_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='骑手钱包表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rider_wallet`
--

LOCK TABLES `rider_wallet` WRITE;
/*!40000 ALTER TABLE `rider_wallet` DISABLE KEYS */;
/*!40000 ALTER TABLE `rider_wallet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rider_wallet_record`
--

DROP TABLE IF EXISTS `rider_wallet_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rider_wallet_record` (
  `rider_wallet_record_id` bigint NOT NULL COMMENT '流水唯一ID',
  `rider_wallet_id` bigint NOT NULL COMMENT '所属钱包ID',
  `rider_base_id` bigint NOT NULL COMMENT '所属骑手ID',
  `amount` decimal(10,2) NOT NULL COMMENT '金额(正数=收入，负数=支出)',
  `trade_type` tinyint NOT NULL COMMENT '交易类型：1-配送收入 2-提现 3-违规扣款 4-平台补贴',
  `related_id` bigint DEFAULT NULL COMMENT '关联业务ID',
  `trade_status` tinyint NOT NULL COMMENT '交易状态：0-处理中 1-成功 2-失败',
  `trade_time` datetime NOT NULL COMMENT '交易时间',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`rider_wallet_record_id`),
  KEY `idx_rider_id` (`rider_base_id`),
  KEY `idx_wallet_id` (`rider_wallet_id`),
  KEY `idx_trade_time` (`trade_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='骑手钱包流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rider_wallet_record`
--

LOCK TABLES `rider_wallet_record` WRITE;
/*!40000 ALTER TABLE `rider_wallet_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `rider_wallet_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `secondhand_goods`
--

DROP TABLE IF EXISTS `secondhand_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `secondhand_goods` (
  `secondhand_goods_id` bigint NOT NULL AUTO_INCREMENT COMMENT '二手商品唯一ID',
  `user_base_id` bigint NOT NULL COMMENT '发布用户ID(关联user_base.user_base_id)',
  `goods_name` varchar(100) NOT NULL COMMENT '商品名称',
  `category` varchar(50) NOT NULL COMMENT '商品分类(如数码产品/图书教材/服饰鞋包/生活用品/运动健身/美妆个护)',
  `price` decimal(10,2) NOT NULL COMMENT '售价/估价',
  `description` text COMMENT '商品描述(详细说明)',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '商品状态:0-已下架 1-在售中 2-已售出 3-已预定',
  `view_count` int NOT NULL DEFAULT '0' COMMENT '浏览次数',
  `favorite_count` int NOT NULL DEFAULT '0' COMMENT '收藏次数',
  `share_count` int NOT NULL DEFAULT '0' COMMENT '分享次数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `sold_time` datetime DEFAULT NULL COMMENT '售出时间',
  PRIMARY KEY (`secondhand_goods_id`),
  KEY `idx_user_id` (`user_base_id`) COMMENT '用户ID索引',
  KEY `idx_category` (`category`) COMMENT '分类索引(支持分类筛选)',
  KEY `idx_status` (`status`) COMMENT '状态索引(过滤已售商品)',
  KEY `idx_price` (`price`) COMMENT '价格索引(支持价格区间查询)',
  KEY `idx_create_time` (`create_time` DESC) COMMENT '发布时间索引(最新商品排序)',
  KEY `idx_category_status_time` (`category`,`status`,`create_time` DESC) COMMENT '复合索引(分类+状态+时间)',
  CONSTRAINT `fk_secondhand_user` FOREIGN KEY (`user_base_id`) REFERENCES `user_base` (`user_base_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='二手商品表(简化版)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `secondhand_goods`
--

LOCK TABLES `secondhand_goods` WRITE;
/*!40000 ALTER TABLE `secondhand_goods` DISABLE KEYS */;
/*!40000 ALTER TABLE `secondhand_goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `secondhand_goods_image`
--

DROP TABLE IF EXISTS `secondhand_goods_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `secondhand_goods_image` (
  `secondhand_goods_image_id` bigint NOT NULL AUTO_INCREMENT COMMENT '图片唯一ID',
  `secondhand_goods_id` bigint NOT NULL COMMENT '关联商品ID',
  `image_url` varchar(255) NOT NULL COMMENT '图片URL(建议使用OSS存储)',
  `is_main` tinyint NOT NULL DEFAULT '0' COMMENT '是否主图:0-否 1-是(每个商品仅一张主图)',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序序号(升序排列)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  PRIMARY KEY (`secondhand_goods_image_id`),
  KEY `idx_goods_id` (`secondhand_goods_id`) COMMENT '商品ID索引',
  KEY `idx_goods_main` (`secondhand_goods_id`,`is_main`) COMMENT '商品+主图索引(快速查询主图)',
  KEY `idx_goods_sort` (`secondhand_goods_id`,`sort_order`) COMMENT '商品+排序索引(图片排序)',
  CONSTRAINT `fk_image_goods` FOREIGN KEY (`secondhand_goods_id`) REFERENCES `secondhand_goods` (`secondhand_goods_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='二手商品图片表(支持1-9张图片)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `secondhand_goods_image`
--

LOCK TABLES `secondhand_goods_image` WRITE;
/*!40000 ALTER TABLE `secondhand_goods_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `secondhand_goods_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='参数配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` VALUES (1,'主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','Y','admin','2025-10-16 20:42:08','',NULL,'蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),(2,'用户管理-账号初始密码','sys.user.initPassword','123456','Y','admin','2025-10-16 20:42:08','',NULL,'初始化密码 123456'),(3,'主框架页-侧边栏主题','sys.index.sideTheme','theme-dark','Y','admin','2025-10-16 20:42:08','',NULL,'深色主题theme-dark，浅色主题theme-light'),(4,'账号自助-验证码开关','sys.account.captchaEnabled','true','Y','admin','2025-10-16 20:42:08','',NULL,'是否开启验证码功能（true开启，false关闭）'),(5,'账号自助-是否开启用户注册功能','sys.account.registerUser','false','Y','admin','2025-10-16 20:42:08','',NULL,'是否开启注册用户功能（true开启，false关闭）'),(6,'用户登录-黑名单列表','sys.login.blackIPList','','Y','admin','2025-10-16 20:42:08','',NULL,'设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）'),(7,'用户管理-初始密码修改策略','sys.account.initPasswordModify','1','Y','admin','2025-10-16 20:42:08','',NULL,'0：初始密码修改策略关闭，没有任何提示，1：提醒用户，如果未修改初始密码，则在登录时就会提醒修改密码对话框'),(8,'用户管理-账号密码更新周期','sys.account.passwordValidateDays','0','Y','admin','2025-10-16 20:42:08','',NULL,'密码更新周期（填写数字，数据初始化值为0不限制，若修改必须为大于0小于365的正整数），如果超过这个周期登录系统时，则在登录时就会提醒修改密码对话框');
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (100,0,'0','若依科技',0,'若依','15888888888','ry@qq.com','0','0','admin','2025-10-16 20:42:02','',NULL),(101,100,'0,100','深圳总公司',1,'若依','15888888888','ry@qq.com','0','0','admin','2025-10-16 20:42:02','',NULL),(102,100,'0,100','长沙分公司',2,'若依','15888888888','ry@qq.com','0','0','admin','2025-10-16 20:42:02','',NULL),(103,101,'0,100,101','研发部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2025-10-16 20:42:02','',NULL),(104,101,'0,100,101','市场部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2025-10-16 20:42:02','',NULL),(105,101,'0,100,101','测试部门',3,'若依','15888888888','ry@qq.com','0','0','admin','2025-10-16 20:42:02','',NULL),(106,101,'0,100,101','财务部门',4,'若依','15888888888','ry@qq.com','0','0','admin','2025-10-16 20:42:02','',NULL),(107,101,'0,100,101','运维部门',5,'若依','15888888888','ry@qq.com','0','0','admin','2025-10-16 20:42:02','',NULL),(108,102,'0,100,102','市场部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2025-10-16 20:42:02','',NULL),(109,102,'0,100,102','财务部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2025-10-16 20:42:03','',NULL);
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_data`
--

DROP TABLE IF EXISTS `sys_dict_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_data` (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_data`
--

LOCK TABLES `sys_dict_data` WRITE;
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
INSERT INTO `sys_dict_data` VALUES (1,1,'男','0','sys_user_sex','','','Y','0','admin','2025-10-16 20:42:07','',NULL,'性别男'),(2,2,'女','1','sys_user_sex','','','N','0','admin','2025-10-16 20:42:07','',NULL,'性别女'),(3,3,'未知','2','sys_user_sex','','','N','0','admin','2025-10-16 20:42:07','',NULL,'性别未知'),(4,1,'显示','0','sys_show_hide','','primary','Y','0','admin','2025-10-16 20:42:07','',NULL,'显示菜单'),(5,2,'隐藏','1','sys_show_hide','','danger','N','0','admin','2025-10-16 20:42:07','',NULL,'隐藏菜单'),(6,1,'正常','0','sys_normal_disable','','primary','Y','0','admin','2025-10-16 20:42:07','',NULL,'正常状态'),(7,2,'停用','1','sys_normal_disable','','danger','N','0','admin','2025-10-16 20:42:07','',NULL,'停用状态'),(8,1,'正常','0','sys_job_status','','primary','Y','0','admin','2025-10-16 20:42:07','',NULL,'正常状态'),(9,2,'暂停','1','sys_job_status','','danger','N','0','admin','2025-10-16 20:42:07','',NULL,'停用状态'),(10,1,'默认','DEFAULT','sys_job_group','','','Y','0','admin','2025-10-16 20:42:07','',NULL,'默认分组'),(11,2,'系统','SYSTEM','sys_job_group','','','N','0','admin','2025-10-16 20:42:07','',NULL,'系统分组'),(12,1,'是','Y','sys_yes_no','','primary','Y','0','admin','2025-10-16 20:42:07','',NULL,'系统默认是'),(13,2,'否','N','sys_yes_no','','danger','N','0','admin','2025-10-16 20:42:07','',NULL,'系统默认否'),(14,1,'通知','1','sys_notice_type','','warning','Y','0','admin','2025-10-16 20:42:07','',NULL,'通知'),(15,2,'公告','2','sys_notice_type','','success','N','0','admin','2025-10-16 20:42:07','',NULL,'公告'),(16,1,'正常','0','sys_notice_status','','primary','Y','0','admin','2025-10-16 20:42:07','',NULL,'正常状态'),(17,2,'关闭','1','sys_notice_status','','danger','N','0','admin','2025-10-16 20:42:07','',NULL,'关闭状态'),(18,99,'其他','0','sys_oper_type','','info','N','0','admin','2025-10-16 20:42:07','',NULL,'其他操作'),(19,1,'新增','1','sys_oper_type','','info','N','0','admin','2025-10-16 20:42:07','',NULL,'新增操作'),(20,2,'修改','2','sys_oper_type','','info','N','0','admin','2025-10-16 20:42:07','',NULL,'修改操作'),(21,3,'删除','3','sys_oper_type','','danger','N','0','admin','2025-10-16 20:42:07','',NULL,'删除操作'),(22,4,'授权','4','sys_oper_type','','primary','N','0','admin','2025-10-16 20:42:07','',NULL,'授权操作'),(23,5,'导出','5','sys_oper_type','','warning','N','0','admin','2025-10-16 20:42:07','',NULL,'导出操作'),(24,6,'导入','6','sys_oper_type','','warning','N','0','admin','2025-10-16 20:42:07','',NULL,'导入操作'),(25,7,'强退','7','sys_oper_type','','danger','N','0','admin','2025-10-16 20:42:07','',NULL,'强退操作'),(26,8,'生成代码','8','sys_oper_type','','warning','N','0','admin','2025-10-16 20:42:07','',NULL,'生成操作'),(27,9,'清空数据','9','sys_oper_type','','danger','N','0','admin','2025-10-16 20:42:07','',NULL,'清空操作'),(28,1,'成功','0','sys_common_status','','primary','N','0','admin','2025-10-16 20:42:08','',NULL,'正常状态'),(29,2,'失败','1','sys_common_status','','danger','N','0','admin','2025-10-16 20:42:08','',NULL,'停用状态');
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_type` (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `dict_type` (`dict_type`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT INTO `sys_dict_type` VALUES (1,'用户性别','sys_user_sex','0','admin','2025-10-16 20:42:07','',NULL,'用户性别列表'),(2,'菜单状态','sys_show_hide','0','admin','2025-10-16 20:42:07','',NULL,'菜单状态列表'),(3,'系统开关','sys_normal_disable','0','admin','2025-10-16 20:42:07','',NULL,'系统开关列表'),(4,'任务状态','sys_job_status','0','admin','2025-10-16 20:42:07','',NULL,'任务状态列表'),(5,'任务分组','sys_job_group','0','admin','2025-10-16 20:42:07','',NULL,'任务分组列表'),(6,'系统是否','sys_yes_no','0','admin','2025-10-16 20:42:07','',NULL,'系统是否列表'),(7,'通知类型','sys_notice_type','0','admin','2025-10-16 20:42:07','',NULL,'通知类型列表'),(8,'通知状态','sys_notice_status','0','admin','2025-10-16 20:42:07','',NULL,'通知状态列表'),(9,'操作类型','sys_oper_type','0','admin','2025-10-16 20:42:07','',NULL,'操作类型列表'),(10,'系统状态','sys_common_status','0','admin','2025-10-16 20:42:07','',NULL,'登录状态列表');
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_job`
--

DROP TABLE IF EXISTS `sys_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_job` (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`,`job_name`,`job_group`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务调度表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_job`
--

LOCK TABLES `sys_job` WRITE;
/*!40000 ALTER TABLE `sys_job` DISABLE KEYS */;
INSERT INTO `sys_job` VALUES (1,'系统默认（无参）','DEFAULT','ryTask.ryNoParams','0/10 * * * * ?','3','1','1','admin','2025-10-16 20:42:08','',NULL,''),(2,'系统默认（有参）','DEFAULT','ryTask.ryParams(\'ry\')','0/15 * * * * ?','3','1','1','admin','2025-10-16 20:42:08','',NULL,''),(3,'系统默认（多参）','DEFAULT','ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)','0/20 * * * * ?','3','1','1','admin','2025-10-16 20:42:08','',NULL,'');
/*!40000 ALTER TABLE `sys_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_job_log`
--

DROP TABLE IF EXISTS `sys_job_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_job_log` (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) DEFAULT NULL COMMENT '日志信息',
  `status` char(1) DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) DEFAULT '' COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务调度日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_job_log`
--

LOCK TABLES `sys_job_log` WRITE;
/*!40000 ALTER TABLE `sys_job_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_job_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_logininfor`
--

DROP TABLE IF EXISTS `sys_logininfor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_logininfor` (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`),
  KEY `idx_sys_logininfor_s` (`status`),
  KEY `idx_sys_logininfor_lt` (`login_time`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统访问记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_logininfor`
--

LOCK TABLES `sys_logininfor` WRITE;
/*!40000 ALTER TABLE `sys_logininfor` DISABLE KEYS */;
INSERT INTO `sys_logininfor` VALUES (100,'admin','127.0.0.1','内网IP','Chrome 14','Windows 10','1','验证码错误','2025-10-16 20:42:49'),(101,'admin','127.0.0.1','内网IP','Chrome 14','Windows 10','0','登录成功','2025-10-16 20:42:55');
/*!40000 ALTER TABLE `sys_logininfor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) DEFAULT '' COMMENT '路由名称',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'系统管理',0,1,'system',NULL,'','',1,0,'M','0','0','','system','admin','2025-10-16 20:42:03','',NULL,'系统管理目录'),(2,'系统监控',0,2,'monitor',NULL,'','',1,0,'M','0','0','','monitor','admin','2025-10-16 20:42:03','',NULL,'系统监控目录'),(3,'系统工具',0,3,'tool',NULL,'','',1,0,'M','0','0','','tool','admin','2025-10-16 20:42:03','',NULL,'系统工具目录'),(4,'若依官网',0,4,'http://ruoyi.vip',NULL,'','',0,0,'M','0','0','','guide','admin','2025-10-16 20:42:03','',NULL,'若依官网地址'),(100,'用户管理',1,1,'user','system/user/index','','',1,0,'C','0','0','system:user:list','user','admin','2025-10-16 20:42:03','',NULL,'用户管理菜单'),(101,'角色管理',1,2,'role','system/role/index','','',1,0,'C','0','0','system:role:list','peoples','admin','2025-10-16 20:42:03','',NULL,'角色管理菜单'),(102,'菜单管理',1,3,'menu','system/menu/index','','',1,0,'C','0','0','system:menu:list','tree-table','admin','2025-10-16 20:42:03','',NULL,'菜单管理菜单'),(103,'部门管理',1,4,'dept','system/dept/index','','',1,0,'C','0','0','system:dept:list','tree','admin','2025-10-16 20:42:03','',NULL,'部门管理菜单'),(104,'岗位管理',1,5,'post','system/post/index','','',1,0,'C','0','0','system:post:list','post','admin','2025-10-16 20:42:03','',NULL,'岗位管理菜单'),(105,'字典管理',1,6,'dict','system/dict/index','','',1,0,'C','0','0','system:dict:list','dict','admin','2025-10-16 20:42:03','',NULL,'字典管理菜单'),(106,'参数设置',1,7,'config','system/config/index','','',1,0,'C','0','0','system:config:list','edit','admin','2025-10-16 20:42:03','',NULL,'参数设置菜单'),(107,'通知公告',1,8,'notice','system/notice/index','','',1,0,'C','0','0','system:notice:list','message','admin','2025-10-16 20:42:03','',NULL,'通知公告菜单'),(108,'日志管理',1,9,'log','','','',1,0,'M','0','0','','log','admin','2025-10-16 20:42:03','',NULL,'日志管理菜单'),(109,'在线用户',2,1,'online','monitor/online/index','','',1,0,'C','0','0','monitor:online:list','online','admin','2025-10-16 20:42:03','',NULL,'在线用户菜单'),(110,'定时任务',2,2,'job','monitor/job/index','','',1,0,'C','0','0','monitor:job:list','job','admin','2025-10-16 20:42:03','',NULL,'定时任务菜单'),(111,'数据监控',2,3,'druid','monitor/druid/index','','',1,0,'C','0','0','monitor:druid:list','druid','admin','2025-10-16 20:42:03','',NULL,'数据监控菜单'),(112,'服务监控',2,4,'server','monitor/server/index','','',1,0,'C','0','0','monitor:server:list','server','admin','2025-10-16 20:42:03','',NULL,'服务监控菜单'),(113,'缓存监控',2,5,'cache','monitor/cache/index','','',1,0,'C','0','0','monitor:cache:list','redis','admin','2025-10-16 20:42:03','',NULL,'缓存监控菜单'),(114,'缓存列表',2,6,'cacheList','monitor/cache/list','','',1,0,'C','0','0','monitor:cache:list','redis-list','admin','2025-10-16 20:42:03','',NULL,'缓存列表菜单'),(115,'表单构建',3,1,'build','tool/build/index','','',1,0,'C','0','0','tool:build:list','build','admin','2025-10-16 20:42:03','',NULL,'表单构建菜单'),(116,'代码生成',3,2,'gen','tool/gen/index','','',1,0,'C','0','0','tool:gen:list','code','admin','2025-10-16 20:42:03','',NULL,'代码生成菜单'),(117,'系统接口',3,3,'swagger','tool/swagger/index','','',1,0,'C','0','0','tool:swagger:list','swagger','admin','2025-10-16 20:42:04','',NULL,'系统接口菜单'),(500,'操作日志',108,1,'operlog','monitor/operlog/index','','',1,0,'C','0','0','monitor:operlog:list','form','admin','2025-10-16 20:42:04','',NULL,'操作日志菜单'),(501,'登录日志',108,2,'logininfor','monitor/logininfor/index','','',1,0,'C','0','0','monitor:logininfor:list','logininfor','admin','2025-10-16 20:42:04','',NULL,'登录日志菜单'),(1000,'用户查询',100,1,'','','','',1,0,'F','0','0','system:user:query','#','admin','2025-10-16 20:42:04','',NULL,''),(1001,'用户新增',100,2,'','','','',1,0,'F','0','0','system:user:add','#','admin','2025-10-16 20:42:04','',NULL,''),(1002,'用户修改',100,3,'','','','',1,0,'F','0','0','system:user:edit','#','admin','2025-10-16 20:42:04','',NULL,''),(1003,'用户删除',100,4,'','','','',1,0,'F','0','0','system:user:remove','#','admin','2025-10-16 20:42:04','',NULL,''),(1004,'用户导出',100,5,'','','','',1,0,'F','0','0','system:user:export','#','admin','2025-10-16 20:42:04','',NULL,''),(1005,'用户导入',100,6,'','','','',1,0,'F','0','0','system:user:import','#','admin','2025-10-16 20:42:04','',NULL,''),(1006,'重置密码',100,7,'','','','',1,0,'F','0','0','system:user:resetPwd','#','admin','2025-10-16 20:42:04','',NULL,''),(1007,'角色查询',101,1,'','','','',1,0,'F','0','0','system:role:query','#','admin','2025-10-16 20:42:04','',NULL,''),(1008,'角色新增',101,2,'','','','',1,0,'F','0','0','system:role:add','#','admin','2025-10-16 20:42:04','',NULL,''),(1009,'角色修改',101,3,'','','','',1,0,'F','0','0','system:role:edit','#','admin','2025-10-16 20:42:04','',NULL,''),(1010,'角色删除',101,4,'','','','',1,0,'F','0','0','system:role:remove','#','admin','2025-10-16 20:42:04','',NULL,''),(1011,'角色导出',101,5,'','','','',1,0,'F','0','0','system:role:export','#','admin','2025-10-16 20:42:04','',NULL,''),(1012,'菜单查询',102,1,'','','','',1,0,'F','0','0','system:menu:query','#','admin','2025-10-16 20:42:04','',NULL,''),(1013,'菜单新增',102,2,'','','','',1,0,'F','0','0','system:menu:add','#','admin','2025-10-16 20:42:04','',NULL,''),(1014,'菜单修改',102,3,'','','','',1,0,'F','0','0','system:menu:edit','#','admin','2025-10-16 20:42:04','',NULL,''),(1015,'菜单删除',102,4,'','','','',1,0,'F','0','0','system:menu:remove','#','admin','2025-10-16 20:42:04','',NULL,''),(1016,'部门查询',103,1,'','','','',1,0,'F','0','0','system:dept:query','#','admin','2025-10-16 20:42:04','',NULL,''),(1017,'部门新增',103,2,'','','','',1,0,'F','0','0','system:dept:add','#','admin','2025-10-16 20:42:04','',NULL,''),(1018,'部门修改',103,3,'','','','',1,0,'F','0','0','system:dept:edit','#','admin','2025-10-16 20:42:04','',NULL,''),(1019,'部门删除',103,4,'','','','',1,0,'F','0','0','system:dept:remove','#','admin','2025-10-16 20:42:04','',NULL,''),(1020,'岗位查询',104,1,'','','','',1,0,'F','0','0','system:post:query','#','admin','2025-10-16 20:42:04','',NULL,''),(1021,'岗位新增',104,2,'','','','',1,0,'F','0','0','system:post:add','#','admin','2025-10-16 20:42:04','',NULL,''),(1022,'岗位修改',104,3,'','','','',1,0,'F','0','0','system:post:edit','#','admin','2025-10-16 20:42:04','',NULL,''),(1023,'岗位删除',104,4,'','','','',1,0,'F','0','0','system:post:remove','#','admin','2025-10-16 20:42:04','',NULL,''),(1024,'岗位导出',104,5,'','','','',1,0,'F','0','0','system:post:export','#','admin','2025-10-16 20:42:04','',NULL,''),(1025,'字典查询',105,1,'#','','','',1,0,'F','0','0','system:dict:query','#','admin','2025-10-16 20:42:04','',NULL,''),(1026,'字典新增',105,2,'#','','','',1,0,'F','0','0','system:dict:add','#','admin','2025-10-16 20:42:04','',NULL,''),(1027,'字典修改',105,3,'#','','','',1,0,'F','0','0','system:dict:edit','#','admin','2025-10-16 20:42:04','',NULL,''),(1028,'字典删除',105,4,'#','','','',1,0,'F','0','0','system:dict:remove','#','admin','2025-10-16 20:42:04','',NULL,''),(1029,'字典导出',105,5,'#','','','',1,0,'F','0','0','system:dict:export','#','admin','2025-10-16 20:42:04','',NULL,''),(1030,'参数查询',106,1,'#','','','',1,0,'F','0','0','system:config:query','#','admin','2025-10-16 20:42:04','',NULL,''),(1031,'参数新增',106,2,'#','','','',1,0,'F','0','0','system:config:add','#','admin','2025-10-16 20:42:04','',NULL,''),(1032,'参数修改',106,3,'#','','','',1,0,'F','0','0','system:config:edit','#','admin','2025-10-16 20:42:04','',NULL,''),(1033,'参数删除',106,4,'#','','','',1,0,'F','0','0','system:config:remove','#','admin','2025-10-16 20:42:04','',NULL,''),(1034,'参数导出',106,5,'#','','','',1,0,'F','0','0','system:config:export','#','admin','2025-10-16 20:42:04','',NULL,''),(1035,'公告查询',107,1,'#','','','',1,0,'F','0','0','system:notice:query','#','admin','2025-10-16 20:42:04','',NULL,''),(1036,'公告新增',107,2,'#','','','',1,0,'F','0','0','system:notice:add','#','admin','2025-10-16 20:42:04','',NULL,''),(1037,'公告修改',107,3,'#','','','',1,0,'F','0','0','system:notice:edit','#','admin','2025-10-16 20:42:04','',NULL,''),(1038,'公告删除',107,4,'#','','','',1,0,'F','0','0','system:notice:remove','#','admin','2025-10-16 20:42:04','',NULL,''),(1039,'操作查询',500,1,'#','','','',1,0,'F','0','0','monitor:operlog:query','#','admin','2025-10-16 20:42:04','',NULL,''),(1040,'操作删除',500,2,'#','','','',1,0,'F','0','0','monitor:operlog:remove','#','admin','2025-10-16 20:42:04','',NULL,''),(1041,'日志导出',500,3,'#','','','',1,0,'F','0','0','monitor:operlog:export','#','admin','2025-10-16 20:42:04','',NULL,''),(1042,'登录查询',501,1,'#','','','',1,0,'F','0','0','monitor:logininfor:query','#','admin','2025-10-16 20:42:04','',NULL,''),(1043,'登录删除',501,2,'#','','','',1,0,'F','0','0','monitor:logininfor:remove','#','admin','2025-10-16 20:42:04','',NULL,''),(1044,'日志导出',501,3,'#','','','',1,0,'F','0','0','monitor:logininfor:export','#','admin','2025-10-16 20:42:04','',NULL,''),(1045,'账户解锁',501,4,'#','','','',1,0,'F','0','0','monitor:logininfor:unlock','#','admin','2025-10-16 20:42:04','',NULL,''),(1046,'在线查询',109,1,'#','','','',1,0,'F','0','0','monitor:online:query','#','admin','2025-10-16 20:42:04','',NULL,''),(1047,'批量强退',109,2,'#','','','',1,0,'F','0','0','monitor:online:batchLogout','#','admin','2025-10-16 20:42:04','',NULL,''),(1048,'单条强退',109,3,'#','','','',1,0,'F','0','0','monitor:online:forceLogout','#','admin','2025-10-16 20:42:04','',NULL,''),(1049,'任务查询',110,1,'#','','','',1,0,'F','0','0','monitor:job:query','#','admin','2025-10-16 20:42:04','',NULL,''),(1050,'任务新增',110,2,'#','','','',1,0,'F','0','0','monitor:job:add','#','admin','2025-10-16 20:42:05','',NULL,''),(1051,'任务修改',110,3,'#','','','',1,0,'F','0','0','monitor:job:edit','#','admin','2025-10-16 20:42:05','',NULL,''),(1052,'任务删除',110,4,'#','','','',1,0,'F','0','0','monitor:job:remove','#','admin','2025-10-16 20:42:05','',NULL,''),(1053,'状态修改',110,5,'#','','','',1,0,'F','0','0','monitor:job:changeStatus','#','admin','2025-10-16 20:42:05','',NULL,''),(1054,'任务导出',110,6,'#','','','',1,0,'F','0','0','monitor:job:export','#','admin','2025-10-16 20:42:05','',NULL,''),(1055,'生成查询',116,1,'#','','','',1,0,'F','0','0','tool:gen:query','#','admin','2025-10-16 20:42:05','',NULL,''),(1056,'生成修改',116,2,'#','','','',1,0,'F','0','0','tool:gen:edit','#','admin','2025-10-16 20:42:05','',NULL,''),(1057,'生成删除',116,3,'#','','','',1,0,'F','0','0','tool:gen:remove','#','admin','2025-10-16 20:42:05','',NULL,''),(1058,'导入代码',116,4,'#','','','',1,0,'F','0','0','tool:gen:import','#','admin','2025-10-16 20:42:05','',NULL,''),(1059,'预览代码',116,5,'#','','','',1,0,'F','0','0','tool:gen:preview','#','admin','2025-10-16 20:42:05','',NULL,''),(1060,'生成代码',116,6,'#','','','',1,0,'F','0','0','tool:gen:code','#','admin','2025-10-16 20:42:05','',NULL,'');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice`
--

DROP TABLE IF EXISTS `sys_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_notice` (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice`
--

LOCK TABLES `sys_notice` WRITE;
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
INSERT INTO `sys_notice` VALUES (1,'温馨提醒：2018-07-01 若依新版本发布啦','2',_binary '新版本内容','0','admin','2025-10-16 20:42:08','',NULL,'管理员'),(2,'维护通知：2018-07-01 若依系统凌晨维护','1',_binary '维护内容','0','admin','2025-10-16 20:42:08','',NULL,'管理员');
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oper_log`
--

DROP TABLE IF EXISTS `sys_oper_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oper_log` (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint DEFAULT '0' COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`),
  KEY `idx_sys_oper_log_bt` (`business_type`),
  KEY `idx_sys_oper_log_s` (`status`),
  KEY `idx_sys_oper_log_ot` (`oper_time`)
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oper_log`
--

LOCK TABLES `sys_oper_log` WRITE;
/*!40000 ALTER TABLE `sys_oper_log` DISABLE KEYS */;
INSERT INTO `sys_oper_log` VALUES (100,'创建表',0,'com.ruoyi.generator.controller.GenController.createTableSave()','POST',1,'admin','研发部门','/tool/gen/createTable','127.0.0.1','内网IP','{\"sql\":\"CREATE TABLE `user_base` (\\n  `user_base_id` bigint NOT NULL COMMENT \'用户唯一ID（雪花算法）\',\\n  `username` varchar(50) NOT NULL COMMENT \'登录账号（唯一）\',\\n  `password` varchar(100) NOT NULL COMMENT \'密码（BCrypt加密）\',\\n  `nickname` varchar(50) NOT NULL COMMENT \'用户昵称\',\\n  `avatar` varchar(255) DEFAULT NULL COMMENT \'头像URL\',\\n  `student_id` varchar(20) NOT NULL COMMENT \'学号（唯一）\',\\n  `college` varchar(50) NOT NULL COMMENT \'所属学院（如计算机学院）\',\\n  `major` varchar(50) NOT NULL COMMENT \'所属专业（如软件工程）\',\\n  `grade` varchar(20) NOT NULL COMMENT \'年级（如2022级）\',\\n  `gender` tinyint DEFAULT NULL COMMENT \'性别：1-男 2-女 0-未知\',\\n  `phone` varchar(20) NOT NULL COMMENT \'联系电话（AES加密）\',\\n  `credit_score` int NOT NULL DEFAULT 600 COMMENT \'信用分（影响推荐优先级）\',\\n  `account_status` tinyint NOT NULL DEFAULT 1 COMMENT \'账号状态：0-禁用 1-正常\',\\n  `last_login_time` datetime DEFAULT NULL COMMENT \'最后登录时间\',\\n  `last_login_ip` varchar(50) DEFAULT NULL COMMENT \'最后登录IP\',\\n  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT \'创建时间\',\\n  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT \'更新时间\',\\n  PRIMARY KEY (`user_base_id`),\\n  UNIQUE KEY `uk_username` (`username`) COMMENT \'登录账号唯一\',\\n  UNIQUE KEY `uk_student_id` (`student_id`) COMMENT \'学号唯一\',\\n  KEY `idx_college_grade` (`college`,`grade`) COMMENT \'学院+年级索引（推荐同群体热门商品）\',\\n  KEY `idx_account_status` (`account_status`) COMMENT \'账号状态索引（过滤禁用用户）\'\\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT=\'用户基础信息表\';\"}','{\"msg\":\"创建表结构异常\",\"code\":500}',0,NULL,'2025-10-16 21:00:51',21),(101,'创建表',0,'com.ruoyi.generator.controller.GenController.createTableSave()','POST',1,'admin','研发部门','/tool/gen/createTable','127.0.0.1','内网IP','{\"sql\":\"CREATE TABLE `user_base` (\\n  `user_base_id` bigint NOT NULL COMMENT \'用户唯一ID（雪花算法）\',\\n  `username` varchar(50) NOT NULL COMMENT \'登录账号（唯一）\',\\n  `password` varchar(100) NOT NULL COMMENT \'密码（BCrypt加密）\',\\n  `nickname` varchar(50) NOT NULL COMMENT \'用户昵称\',\\n  `avatar` varchar(255) DEFAULT NULL COMMENT \'头像URL\',\\n  `student_id` varchar(20) NOT NULL COMMENT \'学号（唯一）\',\\n  `college` varchar(50) NOT NULL COMMENT \'所属学院（如计算机学院）\',\\n  `major` varchar(50) NOT NULL COMMENT \'所属专业（如软件工程）\',\\n  `grade` varchar(20) NOT NULL COMMENT \'年级（如2022级）\',\\n  `gender` tinyint DEFAULT NULL COMMENT \'性别：1-男 2-女 0-未知\',\\n  `phone` varchar(20) NOT NULL COMMENT \'联系电话（AES加密）\',\\n  `credit_score` int NOT NULL DEFAULT 600 COMMENT \'信用分（影响推荐优先级）\',\\n  `account_status` tinyint NOT NULL DEFAULT 1 COMMENT \'账号状态：0-禁用 1-正常\',\\n  `last_login_time` datetime DEFAULT NULL COMMENT \'最后登录时间\',\\n  `last_login_ip` varchar(50) DEFAULT NULL COMMENT \'最后登录IP\',\\n  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT \'创建时间\',\\n  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT \'更新时间\',\\n  PRIMARY KEY (`user_base_id`),\\n  UNIQUE KEY `uk_username` (`username`) COMMENT \'登录账号唯一\',\\n  UNIQUE KEY `uk_student_id` (`student_id`) COMMENT \'学号唯一\',\\n  KEY `idx_college_grade` (`college`,`grade`) COMMENT \'学院+年级索引（推荐同群体热门商品）\',\\n  KEY `idx_account_status` (`account_status`) COMMENT \'账号状态索引（过滤禁用用户）\'\\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT=\'用户基础信息表\';\"}','{\"msg\":\"创建表结构异常\",\"code\":500}',0,NULL,'2025-10-16 21:05:18',2),(102,'创建表',0,'com.ruoyi.generator.controller.GenController.createTableSave()','POST',1,'admin','研发部门','/tool/gen/createTable','127.0.0.1','内网IP','{\"sql\":\"CREATE TABLE `user_base` (\\n  `user_base_id` bigint NOT NULL COMMENT \'用户唯一ID（雪花算法）\',\\n  `username` varchar(50) NOT NULL COMMENT \'登录账号（唯一）\',\\n  `password` varchar(100) NOT NULL COMMENT \'密码（BCrypt加密）\',\\n  `nickname` varchar(50) NOT NULL COMMENT \'用户昵称\',\\n  `avatar` varchar(255) DEFAULT NULL COMMENT \'头像URL\',\\n  `student_id` varchar(20) NOT NULL COMMENT \'学号（唯一）\',\\n  `college` varchar(50) NOT NULL COMMENT \'所属学院（如计算机学院）\',\\n  `major` varchar(50) NOT NULL COMMENT \'所属专业（如软件工程）\',\\n  `grade` varchar(20) NOT NULL COMMENT \'年级（如2022级）\',\\n  `gender` tinyint DEFAULT NULL COMMENT \'性别：1-男 2-女 0-未知\',\\n  `phone` varchar(20) NOT NULL COMMENT \'联系电话（AES加密）\',\\n  `credit_score` int NOT NULL DEFAULT 600 COMMENT \'信用分（影响推荐优先级）\',\\n  `account_status` tinyint NOT NULL DEFAULT 1 COMMENT \'账号状态：0-禁用 1-正常\',\\n  `last_login_time` datetime DEFAULT NULL COMMENT \'最后登录时间\',\\n  `last_login_ip` varchar(50) DEFAULT NULL COMMENT \'最后登录IP\',\\n  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT \'创建时间\',\\n  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT \'更新时间\',\\n  PRIMARY KEY (`user_base_id`),\\n  UNIQUE KEY `uk_username` (`username`) COMMENT \'登录账号唯一\',\\n  UNIQUE KEY `uk_student_id` (`student_id`) COMMENT \'学号唯一\',\\n  KEY `idx_college_grade` (`college`,`grade`) COMMENT \'学院+年级索引（推荐同群体热门商品）\',\\n  KEY `idx_account_status` (`account_status`) COMMENT \'账号状态索引（过滤禁用用户）\'\\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT=\'用户基础信息表\';\"}','{\"msg\":\"创建表结构异常\",\"code\":500}',0,NULL,'2025-10-16 21:05:28',1),(103,'创建表',0,'com.ruoyi.generator.controller.GenController.createTableSave()','POST',1,'admin','研发部门','/tool/gen/createTable','127.0.0.1','内网IP','{\"sql\":\"CREATE TABLE `user_base` (\\n  `user_base_id` bigint NOT NULL COMMENT \'用户唯一ID（雪花算法）\',\\n  `username` varchar(50) NOT NULL COMMENT \'登录账号（唯一）\',\\n  `password` varchar(100) NOT NULL COMMENT \'密码（BCrypt加密）\',\\n  `nickname` varchar(50) NOT NULL COMMENT \'用户昵称\',\\n  `avatar` varchar(255) DEFAULT NULL COMMENT \'头像URL\',\\n  `student_id` varchar(20) NOT NULL COMMENT \'学号（唯一）\',\\n  `college` varchar(50) NOT NULL COMMENT \'所属学院（如计算机学院）\',\\n  `major` varchar(50) NOT NULL COMMENT \'所属专业（如软件工程）\',\\n  `grade` varchar(20) NOT NULL COMMENT \'年级（如2022级）\',\\n  `gender` tinyint DEFAULT NULL COMMENT \'性别：1-男 2-女 0-未知\',\\n  `phone` varchar(20) NOT NULL COMMENT \'联系电话（AES加密）\',\\n  `credit_score` int NOT NULL DEFAULT 600 COMMENT \'信用分（影响推荐优先级）\',\\n  `account_status` tinyint NOT NULL DEFAULT 1 COMMENT \'账号状态：0-禁用 1-正常\',\\n  `last_login_time` datetime DEFAULT NULL COMMENT \'最后登录时间\',\\n  `last_login_ip` varchar(50) DEFAULT NULL COMMENT \'最后登录IP\',\\n  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT \'创建时间\',\\n  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT \'更新时间\',\\n  PRIMARY KEY (`user_base_id`),\\n  UNIQUE KEY `uk_username` (`username`) COMMENT \'登录账号唯一\',\\n  UNIQUE KEY `uk_student_id` (`student_id`) COMMENT \'学号唯一\',\\n  KEY `idx_college_grade` (`college`,`grade`) COMMENT \'学院+年级索引（推荐同群体热门商品）\',\\n  KEY `idx_account_status` (`account_status`) COMMENT \'账号状态索引（过滤禁用用户）\'\\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT=\'用户基础信息表\';\"}','{\"msg\":\"创建表结构异常\",\"code\":500}',0,NULL,'2025-10-16 21:06:20',3),(104,'创建表',0,'com.ruoyi.generator.controller.GenController.createTableSave()','POST',1,'admin','研发部门','/tool/gen/createTable','127.0.0.1','内网IP','{\"sql\":\"CREATE TABLE `user_base` (\\n  `user_base_id` bigint NOT NULL COMMENT \'用户唯一ID（雪花算法）\',\\n  `username` varchar(50) NOT NULL COMMENT \'登录账号（唯一）\',\\n  `password` varchar(100) NOT NULL COMMENT \'密码（BCrypt加密）\',\\n  `nickname` varchar(50) NOT NULL COMMENT \'用户昵称\',\\n  `avatar` varchar(255) DEFAULT NULL COMMENT \'头像URL\',\\n  `student_id` varchar(20) NOT NULL COMMENT \'学号（唯一）\',\\n  `college` varchar(50) NOT NULL COMMENT \'所属学院（如计算机学院）\',\\n  `major` varchar(50) NOT NULL COMMENT \'所属专业（如软件工程）\',\\n  `grade` varchar(20) NOT NULL COMMENT \'年级（如2022级）\',\\n  `gender` tinyint DEFAULT NULL COMMENT \'性别：1-男 2-女 0-未知\',\\n  `phone` varchar(20) NOT NULL COMMENT \'联系电话（AES加密）\',\\n  `credit_score` int NOT NULL DEFAULT 600 COMMENT \'信用分（影响推荐优先级）\',\\n  `account_status` tinyint NOT NULL DEFAULT 1 COMMENT \'账号状态：0-禁用 1-正常\',\\n  `last_login_time` datetime DEFAULT NULL COMMENT \'最后登录时间\',\\n  `last_login_ip` varchar(50) DEFAULT NULL COMMENT \'最后登录IP\',\\n  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT \'创建时间\',\\n  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT \'更新时间\',\\n  PRIMARY KEY (`user_base_id`),\\n  UNIQUE KEY `uk_username` (`username`) COMMENT \'登录账号唯一\',\\n  UNIQUE KEY `uk_student_id` (`student_id`) COMMENT \'学号唯一\',\\n  KEY `idx_college_grade` (`college`,`grade`) COMMENT \'学院+年级索引（推荐同群体热门商品）\',\\n  KEY `idx_account_status` (`account_status`) COMMENT \'账号状态索引（过滤禁用用户）\'\\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT=\'用户基础信息表\';\"}','{\"msg\":\"创建表结构异常\",\"code\":500}',0,NULL,'2025-10-16 21:06:24',2),(105,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin','研发部门','/tool/gen/importTable','127.0.0.1','内网IP','{\"tables\":\"secondhand_goods,secondhand_goods_image,user_address,user_bank_card,user_behavior_log,user_credit_score_record,user_privacy,user_recommend_setting,user_timetable,user_wallet,user_wallet_record,user_base,user_preference_tag\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-10-16 21:13:52',216),(106,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"secondhand_goods,secondhand_goods_image,user_address,user_bank_card,user_base,user_behavior_log,user_credit_score_record,user_preference_tag,user_privacy,user_recommend_setting\"}',NULL,0,NULL,'2025-10-16 21:13:56',337),(107,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin','研发部门','/tool/gen/importTable','127.0.0.1','内网IP','{\"tables\":\"rider_base,rider_evaluation,rider_order_rel,rider_location,rider_wallet,rider_wallet_record\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-10-16 21:15:22',94),(108,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin','研发部门','/tool/gen/importTable','127.0.0.1','内网IP','{\"tables\":\"merchant_address,merchant_wallet,goods_evaluation,goods_evaluation_image,merchant_activity,merchant_evaluation,merchant_goods_image,merchant_goods,merchant_base\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-10-16 21:18:24',123),(109,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin','研发部门','/tool/gen/importTable','127.0.0.1','内网IP','{\"tables\":\"platform_coupon,platform_role_mapping,platform_tag,platform_admin,platform_operate_log,platform_permission,platform_role,platform_role_perm,platform_workorder\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-10-16 21:18:49',103),(110,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin','研发部门','/tool/gen/importTable','127.0.0.1','内网IP','{\"tables\":\"order_coupon,order_delivery,order_errand_detail,order_main,order_pay_record,order_secondhand_detail,order_takeout_detail,platform_announcement\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-10-16 21:19:16',109),(111,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin','研发部门','/tool/gen/importTable','127.0.0.1','内网IP','{\"tables\":\"chat_attachment,chat_message,chat_message_read,chat_session\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-10-16 21:19:56',71),(112,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"chat_message,chat_attachment,chat_session,chat_message_read,platform_announcement,order_takeout_detail,order_pay_record,order_main,order_secondhand_detail,order_errand_detail,order_delivery,order_coupon,platform_tag,platform_role_perm,platform_role_mapping,platform_role,platform_workorder,platform_permission,platform_coupon,platform_admin,platform_operate_log,merchant_activity,merchant_address,merchant_base,merchant_evaluation,merchant_goods,merchant_goods_image,merchant_wallet,goods_evaluation,goods_evaluation_image,rider_wallet_record,rider_location,rider_evaluation,rider_base,rider_wallet,rider_order_rel,user_wallet_record,user_wallet,user_timetable,user_recommend_setting,user_privacy,user_preference_tag,user_behavior_log,user_base,user_credit_score_record,user_bank_card,user_address,secondhand_goods_image,secondhand_goods\"}',NULL,0,NULL,'2025-10-16 21:20:01',952);
/*!40000 ALTER TABLE `sys_oper_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_post`
--

DROP TABLE IF EXISTS `sys_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_post` (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='岗位信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_post`
--

LOCK TABLES `sys_post` WRITE;
/*!40000 ALTER TABLE `sys_post` DISABLE KEYS */;
INSERT INTO `sys_post` VALUES (1,'ceo','董事长',1,'0','admin','2025-10-16 20:42:03','',NULL,''),(2,'se','项目经理',2,'0','admin','2025-10-16 20:42:03','',NULL,''),(3,'hr','人力资源',3,'0','admin','2025-10-16 20:42:03','',NULL,''),(4,'user','普通员工',4,'0','admin','2025-10-16 20:42:03','',NULL,'');
/*!40000 ALTER TABLE `sys_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','admin',1,'1',1,1,'0','0','admin','2025-10-16 20:42:03','',NULL,'超级管理员'),(2,'普通角色','common',2,'2',1,1,'0','0','admin','2025-10-16 20:42:03','',NULL,'普通角色');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_dept`
--

DROP TABLE IF EXISTS `sys_role_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_dept` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和部门关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_dept`
--

LOCK TABLES `sys_role_dept` WRITE;
/*!40000 ALTER TABLE `sys_role_dept` DISABLE KEYS */;
INSERT INTO `sys_role_dept` VALUES (2,100),(2,101),(2,105);
/*!40000 ALTER TABLE `sys_role_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (2,1),(2,2),(2,3),(2,4),(2,100),(2,101),(2,102),(2,103),(2,104),(2,105),(2,106),(2,107),(2,108),(2,109),(2,110),(2,111),(2,112),(2,113),(2,114),(2,115),(2,116),(2,117),(2,500),(2,501),(2,1000),(2,1001),(2,1002),(2,1003),(2,1004),(2,1005),(2,1006),(2,1007),(2,1008),(2,1009),(2,1010),(2,1011),(2,1012),(2,1013),(2,1014),(2,1015),(2,1016),(2,1017),(2,1018),(2,1019),(2,1020),(2,1021),(2,1022),(2,1023),(2,1024),(2,1025),(2,1026),(2,1027),(2,1028),(2,1029),(2,1030),(2,1031),(2,1032),(2,1033),(2,1034),(2,1035),(2,1036),(2,1037),(2,1038),(2,1039),(2,1040),(2,1041),(2,1042),(2,1043),(2,1044),(2,1045),(2,1046),(2,1047),(2,1048),(2,1049),(2,1050),(2,1051),(2,1052),(2,1053),(2,1054),(2,1055),(2,1056),(2,1057),(2,1058),(2,1059),(2,1060);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `pwd_update_date` datetime DEFAULT NULL COMMENT '密码最后更新时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,103,'admin','若依','00','ry@163.com','15888888888','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2025-10-16 20:42:55','2025-10-16 20:42:03','admin','2025-10-16 20:42:03','',NULL,'管理员'),(2,105,'ry','若依','00','ry@qq.com','15666666666','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2025-10-16 20:42:03','2025-10-16 20:42:03','admin','2025-10-16 20:42:03','',NULL,'测试员');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_post`
--

DROP TABLE IF EXISTS `sys_user_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_post` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户与岗位关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_post`
--

LOCK TABLES `sys_user_post` WRITE;
/*!40000 ALTER TABLE `sys_user_post` DISABLE KEYS */;
INSERT INTO `sys_user_post` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `sys_user_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户和角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_address`
--

DROP TABLE IF EXISTS `user_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_address` (
  `user_address_id` bigint NOT NULL COMMENT '地址唯一ID',
  `user_base_id` bigint NOT NULL COMMENT '所属用户ID（关联user_base.user_base_id）',
  `receiver` varchar(20) NOT NULL COMMENT '收货人姓名',
  `phone` varchar(20) NOT NULL COMMENT '收货人电话（AES加密）',
  `province` varchar(20) NOT NULL COMMENT '省份',
  `city` varchar(20) NOT NULL COMMENT '城市',
  `district` varchar(20) NOT NULL COMMENT '区县',
  `detail_address` varchar(255) NOT NULL COMMENT '详细地址（如XX宿舍3栋201）',
  `address_tag` varchar(30) DEFAULT NULL COMMENT '地址标签（如DORM-宿舍/CLASSROOM-教室）',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
  `is_default` tinyint NOT NULL DEFAULT '0' COMMENT '是否默认地址：0-否 1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_address_id`),
  KEY `idx_user_default` (`user_base_id`,`is_default`) COMMENT '用户+默认地址索引',
  KEY `idx_address_tag` (`address_tag`) COMMENT '地址标签索引',
  KEY `idx_lon_lat` (`longitude`,`latitude`) COMMENT '经纬度索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户地址表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_address`
--

LOCK TABLES `user_address` WRITE;
/*!40000 ALTER TABLE `user_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_bank_card`
--

DROP TABLE IF EXISTS `user_bank_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_bank_card` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_base_id` bigint NOT NULL COMMENT '所属用户ID',
  `bank_name` varchar(50) NOT NULL COMMENT '银行名称',
  `bank_card_type` tinyint NOT NULL COMMENT '卡类型：1-储蓄卡 2-信用卡',
  `card_number` varchar(30) NOT NULL COMMENT '银行卡号（加密存储）',
  `card_tail_number` varchar(10) NOT NULL COMMENT '卡号尾号（冗余）',
  `holder_name` varchar(50) NOT NULL COMMENT '持卡人姓名',
  `id_number` varchar(30) NOT NULL COMMENT '身份证号（加密存储）',
  `reserve_phone` varchar(20) NOT NULL COMMENT '预留手机号（加密存储）',
  `bind_status` tinyint NOT NULL DEFAULT '1' COMMENT '绑定状态：1-正常 0-已解绑',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_base_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户银行卡绑定表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_bank_card`
--

LOCK TABLES `user_bank_card` WRITE;
/*!40000 ALTER TABLE `user_bank_card` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_bank_card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_base`
--

DROP TABLE IF EXISTS `user_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_base` (
  `user_base_id` bigint NOT NULL COMMENT '用户唯一ID（雪花算法）',
  `username` varchar(50) NOT NULL COMMENT '登录账号（唯一）',
  `password` varchar(100) NOT NULL COMMENT '密码（BCrypt加密）',
  `nickname` varchar(50) NOT NULL COMMENT '用户昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `student_id` varchar(20) NOT NULL COMMENT '学号（唯一）',
  `college` varchar(50) NOT NULL COMMENT '所属学院（如计算机学院）',
  `major` varchar(50) NOT NULL COMMENT '所属专业（如软件工程）',
  `grade` varchar(20) NOT NULL COMMENT '年级（如2022级）',
  `gender` tinyint DEFAULT NULL COMMENT '性别：1-男 2-女 0-未知',
  `phone` varchar(20) NOT NULL COMMENT '联系电话（AES加密）',
  `credit_score` int NOT NULL DEFAULT '600' COMMENT '信用分（影响推荐优先级）',
  `account_status` tinyint NOT NULL DEFAULT '1' COMMENT '账号状态：0-禁用 1-正常',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_base_id`),
  UNIQUE KEY `uk_username` (`username`) COMMENT '登录账号唯一',
  UNIQUE KEY `uk_student_id` (`student_id`) COMMENT '学号唯一',
  KEY `idx_college_grade` (`college`,`grade`) COMMENT '学院+年级索引（推荐同群体热门商品）',
  KEY `idx_account_status` (`account_status`) COMMENT '账号状态索引（过滤禁用用户）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户基础信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_base`
--

LOCK TABLES `user_base` WRITE;
/*!40000 ALTER TABLE `user_base` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_base` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_behavior_log`
--

DROP TABLE IF EXISTS `user_behavior_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_behavior_log` (
  `user_behavior_log_id` bigint NOT NULL COMMENT '唯一ID',
  `user_base_id` bigint NOT NULL COMMENT '所属用户ID（关联user_base.user_base_id）',
  `behavior_type` tinyint NOT NULL COMMENT '行为类型：1-浏览商品 2-收藏商品 3-加入购物车 4-下单购买 5-取消订单 6-评价商品',
  `target_id` bigint NOT NULL COMMENT '行为对象ID（如商品ID=123/商家ID=45）',
  `target_type` tinyint NOT NULL COMMENT '对象类型：1-商品 2-商家 3-订单 4-活动',
  `target_name` varchar(100) DEFAULT NULL COMMENT '对象名称（冗余，如"珍珠奶茶"）',
  `behavior_time` datetime NOT NULL COMMENT '行为发生时间',
  `device` varchar(30) DEFAULT NULL COMMENT '行为设备（如APP/小程序/H5）',
  `scene` varchar(30) DEFAULT NULL COMMENT '行为场景（如HOME-首页/SEARCH-搜索页）',
  `duration` int DEFAULT '0' COMMENT '停留时长（秒，仅behavior_type=1时有效）',
  `extra` varchar(500) DEFAULT NULL COMMENT '额外信息（如搜索关键词"平价奶茶"）',
  PRIMARY KEY (`user_behavior_log_id`),
  KEY `idx_user_behavior_time` (`user_base_id`,`behavior_type`,`behavior_time`) COMMENT '用户+行为类型+时间索引',
  KEY `idx_target_id_type` (`target_id`,`target_type`) COMMENT '对象ID+类型索引',
  KEY `idx_behavior_time` (`behavior_time`) COMMENT '行为时间索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户行为记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_behavior_log`
--

LOCK TABLES `user_behavior_log` WRITE;
/*!40000 ALTER TABLE `user_behavior_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_behavior_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_credit_score_record`
--

DROP TABLE IF EXISTS `user_credit_score_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_credit_score_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_base_id` bigint NOT NULL COMMENT '用户ID',
  `change_score` int NOT NULL COMMENT '分数变动（正负）',
  `desc` varchar(100) NOT NULL COMMENT '变动说明',
  `change_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '变动时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_time` (`user_base_id`,`change_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信用分流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_credit_score_record`
--

LOCK TABLES `user_credit_score_record` WRITE;
/*!40000 ALTER TABLE `user_credit_score_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_credit_score_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_preference_tag`
--

DROP TABLE IF EXISTS `user_preference_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_preference_tag` (
  `user_preference_tag_id` bigint NOT NULL COMMENT '唯一ID',
  `user_base_id` bigint NOT NULL COMMENT '所属用户ID（关联user_base.user_base_id）',
  `tag_code` varchar(30) NOT NULL COMMENT '标签编码（唯一标识，如FOOD_SPICY/STATIONERY）',
  `tag_name` varchar(50) NOT NULL COMMENT '标签名称（如"爱吃辣""文具刚需"）',
  `tag_type` varchar(30) NOT NULL COMMENT '标签类型（如FOOD-美食偏好/SHOPPING-购物偏好）',
  `score` int NOT NULL DEFAULT '10' COMMENT '偏好分数（1-100，分数越高偏好越强）',
  `source` tinyint NOT NULL COMMENT '标签来源：1-用户主动设置 2-系统行为分析 3-人工标注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '标签创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '分数更新时间',
  PRIMARY KEY (`user_preference_tag_id`),
  UNIQUE KEY `uk_user_tag` (`user_base_id`,`tag_code`) COMMENT '用户+标签编码唯一（避免重复标签）',
  KEY `idx_user_tag_type` (`user_base_id`,`tag_type`) COMMENT '用户+标签类型索引（按类型查偏好）',
  KEY `idx_tag_code_score` (`tag_code`,`score`) COMMENT '标签编码+分数索引（找高偏好该标签的用户）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户偏好标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_preference_tag`
--

LOCK TABLES `user_preference_tag` WRITE;
/*!40000 ALTER TABLE `user_preference_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_preference_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_privacy`
--

DROP TABLE IF EXISTS `user_privacy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_privacy` (
  `user_privacy_id` bigint NOT NULL COMMENT '设置唯一ID',
  `user_base_id` bigint NOT NULL COMMENT '所属用户ID',
  `is_recommend` tinyint NOT NULL DEFAULT '1' COMMENT '个性化推荐：0-关闭 1-开启',
  `is_location_permit` tinyint NOT NULL DEFAULT '1' COMMENT '位置权限：0-关闭 1-开启',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`user_privacy_id`),
  UNIQUE KEY `uk_user_id` (`user_base_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户隐私设置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_privacy`
--

LOCK TABLES `user_privacy` WRITE;
/*!40000 ALTER TABLE `user_privacy` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_privacy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_recommend_setting`
--

DROP TABLE IF EXISTS `user_recommend_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_recommend_setting` (
  `user_recommend_setting_id` bigint NOT NULL COMMENT '唯一ID',
  `user_base_id` bigint NOT NULL COMMENT '所属用户ID（关联user_base.user_base_id）',
  `is_recommend_enabled` tinyint NOT NULL DEFAULT '1' COMMENT '是否开启个性化推荐：0-关闭 1-开启',
  `recommend_freq` tinyint NOT NULL DEFAULT '2' COMMENT '推荐频率：1-高频 2-中频 3-低频',
  `shielded_tag_codes` varchar(500) DEFAULT NULL COMMENT '屏蔽的标签编码（逗号分隔）',
  `preferred_scene` varchar(50) DEFAULT NULL COMMENT '偏好推荐场景（逗号分隔）',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '设置更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_recommend_setting_id`),
  UNIQUE KEY `uk_user_id` (`user_base_id`) COMMENT '用户ID唯一（一对一关联）',
  KEY `idx_recommend_enabled` (`is_recommend_enabled`) COMMENT '推荐开启状态索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户个性化推荐设置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_recommend_setting`
--

LOCK TABLES `user_recommend_setting` WRITE;
/*!40000 ALTER TABLE `user_recommend_setting` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_recommend_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_timetable`
--

DROP TABLE IF EXISTS `user_timetable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_timetable` (
  `user_timetable_id` bigint NOT NULL COMMENT '课表记录唯一ID',
  `user_base_id` bigint NOT NULL COMMENT '所属用户ID（关联user_base.user_base_id）',
  `course_name` varchar(50) NOT NULL COMMENT '课程名称',
  `teacher_name` varchar(20) DEFAULT NULL COMMENT '授课教师姓名',
  `class_room` varchar(50) NOT NULL COMMENT '上课教室（如"1号教学楼302"）',
  `week_day` tinyint NOT NULL COMMENT '星期(1-周一 7-周日)',
  `start_period` tinyint NOT NULL COMMENT '开始节次',
  `end_period` tinyint NOT NULL COMMENT '结束节次',
  `start_time` varchar(20) NOT NULL COMMENT '开始时间（如"08:00"）',
  `end_time` varchar(20) NOT NULL COMMENT '结束时间（如"09:40"）',
  `start_date` date NOT NULL COMMENT '课程开始日期',
  `end_date` date NOT NULL COMMENT '课程结束日期',
  `import_source` varchar(20) DEFAULT NULL COMMENT '导入来源',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_timetable_id`),
  KEY `idx_user_week_time` (`user_base_id`,`week_day`,`end_time`) COMMENT '用户+星期+下课时间索引',
  KEY `idx_class_room` (`class_room`) COMMENT '教室索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='个人课表表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_timetable`
--

LOCK TABLES `user_timetable` WRITE;
/*!40000 ALTER TABLE `user_timetable` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_timetable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_wallet`
--

DROP TABLE IF EXISTS `user_wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_wallet` (
  `user_wallet_id` bigint NOT NULL COMMENT '钱包唯一ID',
  `user_base_id` bigint NOT NULL COMMENT '所属用户ID',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '可用余额',
  `freeze_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '冻结金额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`user_wallet_id`),
  UNIQUE KEY `uk_user_id` (`user_base_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户钱包表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_wallet`
--

LOCK TABLES `user_wallet` WRITE;
/*!40000 ALTER TABLE `user_wallet` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_wallet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_wallet_record`
--

DROP TABLE IF EXISTS `user_wallet_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_wallet_record` (
  `user_wallet_record_id` bigint NOT NULL COMMENT '流水唯一ID',
  `user_wallet_id` bigint NOT NULL COMMENT '所属钱包ID',
  `user_base_id` bigint NOT NULL COMMENT '所属用户ID',
  `amount` decimal(10,2) NOT NULL COMMENT '金额(正数=收入，负数=支出)',
  `trade_type` tinyint NOT NULL COMMENT '交易类型：1-充值 2-提现 3-外卖支付 4-跑腿支付 5-退款',
  `related_id` bigint DEFAULT NULL COMMENT '关联订单ID',
  `trade_status` tinyint NOT NULL COMMENT '交易状态：0-处理中 1-成功 2-失败',
  `trade_time` datetime NOT NULL COMMENT '交易时间',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_wallet_record_id`),
  KEY `idx_user_id` (`user_base_id`),
  KEY `idx_wallet_id` (`user_wallet_id`),
  KEY `idx_trade_time` (`trade_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户钱包流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_wallet_record`
--

LOCK TABLES `user_wallet_record` WRITE;
/*!40000 ALTER TABLE `user_wallet_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_wallet_record` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-16 21:22:16
