/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 100411
 Source Host           : localhost:3306
 Source Schema         : inventoryapp

 Target Server Type    : MySQL
 Target Server Version : 100411
 File Encoding         : 65001

 Date: 18/04/2020 20:54:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `itemcode` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `itemname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `groupname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `purchaseprice` float(10, 2) NULL DEFAULT NULL,
  `sellingprice` float(10, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for stock
-- ----------------------------
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock`  (
  `item_id` int(11) NOT NULL,
  `warehouse_id` int(11) NOT NULL,
  `qty` float NULL DEFAULT NULL,
  UNIQUE INDEX `uq_stock`(`item_id`, `warehouse_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for transdetail
-- ----------------------------
DROP TABLE IF EXISTS `transdetail`;
CREATE TABLE `transdetail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `header_id` int(30) NOT NULL,
  `header_flag` int(255) NULL DEFAULT NULL,
  `transdate` date NULL DEFAULT NULL,
  `item_id` int(255) NOT NULL,
  `qty` float(100, 0) NULL DEFAULT NULL,
  `warehouse_id` int(11) NULL DEFAULT NULL,
  `purchaseprice` float(10, 2) NULL DEFAULT NULL,
  `sellingprice` float(10, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for transheader
-- ----------------------------
DROP TABLE IF EXISTS `transheader`;
CREATE TABLE `transheader`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `transno` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `header_flag` int(255) NULL DEFAULT NULL,
  `transdate` date NULL DEFAULT NULL,
  `warehouse_id` int(255) NOT NULL,
  `notes` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for warehouse
-- ----------------------------
DROP TABLE IF EXISTS `warehouse`;
CREATE TABLE `warehouse`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `warehousename` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Triggers structure for table transdetail
-- ----------------------------
DROP TRIGGER IF EXISTS `insert_transdetail`;
delimiter ;;
CREATE TRIGGER `insert_transdetail` AFTER INSERT ON `transdetail` FOR EACH ROW INSERT INTO stock
	(item_id, warehouse_id, qty)
VALUES
	(new.item_id, new.warehouse_id, new.qty)
ON DUPLICATE KEY UPDATE
	qty     = qty + new.qty
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table transdetail
-- ----------------------------
DROP TRIGGER IF EXISTS `update_transdetail`;
delimiter ;;
CREATE TRIGGER `update_transdetail` AFTER UPDATE ON `transdetail` FOR EACH ROW INSERT INTO stock
	(item_id, warehouse_id, qty)
VALUES
	(new.item_id, new.warehouse_id, new.qty)
ON DUPLICATE KEY UPDATE
	qty     = qty - old.qty + new.qty
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table transdetail
-- ----------------------------
DROP TRIGGER IF EXISTS `delete_transdetail`;
delimiter ;;
CREATE TRIGGER `delete_transdetail` AFTER DELETE ON `transdetail` FOR EACH ROW INSERT INTO stock
	(item_id, warehouse_id, qty)
VALUES
	(old.item_id, old.warehouse_id, 0)
ON DUPLICATE KEY UPDATE
	qty     = qty - old.qty
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
