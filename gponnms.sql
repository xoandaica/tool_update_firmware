/*
Navicat MySQL Data Transfer

Source Server         : MySql
Source Server Version : 50621
Source Host           : localhost:3306
Source Database       : gponnms

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2016-10-06 14:51:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for action_logs
-- ----------------------------
DROP TABLE IF EXISTS `action_logs`;
CREATE TABLE `action_logs` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`action_object`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`action_time`  datetime NULL DEFAULT NULL ,
`action_type`  int(11) NULL DEFAULT NULL ,
`description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`username`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=586

;

-- ----------------------------
-- Records of action_logs
-- ----------------------------
BEGIN;
INSERT INTO `action_logs` VALUES ('575', 'Device', '2015-10-22 17:24:59', '3', 'IDs = 87,86,85,84', 'admin'), ('576', 'User', '2015-10-22 17:27:34', '3', '[8, 6, 5, 4, 3, 1]', 'admin'), ('577', 'Device', '2015-10-22 17:36:21', '3', 'IDs = 89', 'admin'), ('578', 'User', '2015-10-22 17:58:52', '3', '[18]', 'admin'), ('579', 'Device', '2015-10-22 18:04:49', '3', 'IDs = 90,88', 'admin'), ('580', 'Device', '2015-11-17 14:47:16', '3', 'IDs = 93', 'admin'), ('581', 'Device', '2015-11-17 14:50:22', '3', 'IDs = 94', 'admin'), ('582', 'Device', '2015-11-17 14:59:05', '3', 'IDs = 92', 'admin'), ('583', 'User', '2015-11-17 15:02:20', '3', '[20]', 'admin'), ('584', 'Device', '2015-11-18 12:52:03', '3', 'IDs = 91', 'admin'), ('585', 'Device', '2016-09-22 14:47:58', '3', 'IDs = 98,97,96,95', 'admin');
COMMIT;

-- ----------------------------
-- Table structure for app_user
-- ----------------------------
DROP TABLE IF EXISTS `app_user`;
CREATE TABLE `app_user` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`account_expired`  bit(1) NOT NULL ,
`account_locked`  bit(1) NOT NULL ,
`address`  varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`city`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`country`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`postal_code`  varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`province`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`credentials_expired`  bit(1) NOT NULL ,
`email`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`account_enabled`  bit(1) NULL DEFAULT NULL ,
`first_name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`last_name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`password`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`password_hint`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`phone_number`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`username`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`version`  int(11) NULL DEFAULT NULL ,
`website`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`department`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`),
UNIQUE INDEX `UK_1j9d9a06i600gd43uu3km82jw` (`email`) USING BTREE ,
UNIQUE INDEX `UK_3k4cplvh82srueuttfkwnylq0` (`username`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=39

;

-- ----------------------------
-- Records of app_user
-- ----------------------------
BEGIN;
INSERT INTO `app_user` VALUES ('-3', '\0', '\0', '', 'Denver', 'US', '80210', 'CO', '\0', 'two_roles_user@appfuse.org', '', 'Two Roles', 'User', '$2a$10$bH/ssqW8OhkTlIso9/yakubYODUOmh.6m5HEJvcBq3t3VdBh7ebqO', 'Not a female kitty.', '', 'two_roles_user', '1', 'http://raibledesigns.com', '', ''), ('-2', '\0', '\0', '', 'Denver', 'US', '80210', 'CO', '\0', 'matt@raibledesigns.com', '', 'Matt', 'Raible', '$2a$10$bH/ssqW8OhkTlIso9/yakubYODUOmh.6m5HEJvcBq3t3VdBh7ebqO', 'Not a female kitty.', '', 'admin', '1', 'http://raibledesigns.com', 'SSDC', 'test'), ('-1', '\0', '\0', '', 'Denver', 'US', '80210', 'CO', '\0', 'matt_raible@yahoo.com', '', 'Tomcat', 'User', '$2a$10$CnQVJ9bsWBjMpeSKrrdDEeuIptZxXrwtI6CZ/OgtNxhIgpKxXeT9y', 'A male kitty.', '', 'user', '1', 'http://tomcat.apache.org', '', 'Tesst user abc'), ('35', '\0', '\0', null, null, null, null, null, '\0', 'hue123@g.com', '', 'Pham', 'Hue', '$2a$10$N1P/nsy19HB0Zjuhy/GzcuzkzBNHUwF1ZevWcsUNWPK12prWb4O9i', null, '1234567890', 'hue123', '3', null, 'ass', 'ass'), ('36', '\0', '\0', null, null, null, null, null, '\0', 'longdq@vnpt-tehnology.vn', '', 'Dang Qui', 'Long', '$2a$10$8wjafVXpc2PcQDlaQNRVDOgFw7RszbbTXB5Caz/G/.QEkC4XPdWk6', null, '0975272784', 'superuser', '2', null, 'SSDC - NMS Team', 'Không được xóa.'), ('37', '\0', '\0', null, null, null, null, null, '\0', '1213@gmail.com', '', 'nsi', 'nsi', '$2a$10$eqWsFrKEpl6JeyA3Cg/tZe7Be9DZVy1OK3JpGhbyfTp/g6CWPMFYu', null, '0987677777777', 'vld', '4', null, 'NSI', ''), ('38', '\0', '\0', null, null, null, null, null, '\0', 'vld@gmail.com', '', 'Ha', 'Hop', '$2a$10$eSUv7w9aDPBwLcILP/ufeerHAhnGgATd37.1dxadSwjP2FKJz829C', null, '0946439358', 'hop', '4', null, 'NSIâ', 'NSI');
COMMIT;

-- ----------------------------
-- Table structure for area
-- ----------------------------
DROP TABLE IF EXISTS `area`;
CREATE TABLE `area` (
`id`  bigint(11) NOT NULL AUTO_INCREMENT ,
`name`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`area_type`  int(11) NULL DEFAULT NULL COMMENT '0 : Viet Nam\n1 : Province\n2: District ' ,
`parent_id`  bigint(11) NULL DEFAULT NULL ,
`description`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`),
FOREIGN KEY (`parent_id`) REFERENCES `area` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `fk_area_area_idx` (`parent_id`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='lưu trữ thông tin nhóm vùng'
AUTO_INCREMENT=35

;

-- ----------------------------
-- Records of area
-- ----------------------------
BEGIN;
INSERT INTO `area` VALUES ('-2', 'Ungroup', '0', null, 'Ungroup'), ('-1', 'Việt Nam', '0', null, 'Việt Nam'), ('2', 'Hà Nội', '1', '-1', 'Hà Nội, Việt Nam'), ('3', 'Hà Đông', '2', '2', 'Hà Đông, Hà Nội, Việt Nam'), ('4', 'Quảng Ninh', '1', '-1', 'Tỉnh Quảng Ninh, Việt Nam'), ('5', 'Uông Bí', '2', '4', 'Uông Bí, Quảng Ninh, Việt Nam'), ('6', 'Cẩm Phả', '2', '4', 'Cẩm Phả, Quảng Ninh, Việt Nam'), ('7', 'Vĩnh Phúc', '1', '-1', 'Vĩnh Phúc, Việt Nam'), ('9', 'Bình Xuyên', '2', '7', 'Bình Xuyên, Vĩnh Phúc, Việt Nam'), ('10', 'Bắc Kạn', '1', '-1', 'Bắc Kạn, Việt Nam'), ('11', 'Vĩnh Yên', '2', '7', 'Thành Phố Vĩnh Yên, Vĩnh Phúc, Việt Nam'), ('12', 'Thái Nguyên', '1', '-1', 'Thái Nguyên, Việt Nam'), ('14', 'Hà Nam', '1', '-1', 'Hà Nam, Việt Nam'), ('15', 'Phủ Lý', '2', '14', 'Phủ Lý, Hà Nam, Việt Nam, việt nam'), ('17', 'Hoàn Kiếm', '2', '2', ''), ('24', 'Hải Dương', '1', '-1', 'aa'), ('26', 'Ninh Giang', '2', '24', 'adasd '), ('27', 'Bắc Giang', '1', '-1', 'llaa'), ('28', 'Tiên Yên', '2', '4', ''), ('32', 'Thanh Hóa', '1', '-1', ''), ('33', 'Hậu Lộc', '2', '32', ''), ('34', 'TP Bắc Giang', '2', '27', '');
COMMIT;

-- ----------------------------
-- Table structure for area_device_mapping
-- ----------------------------
DROP TABLE IF EXISTS `area_device_mapping`;
CREATE TABLE `area_device_mapping` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`area_code`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`district`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`ip`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`mac_address`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`province`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`serial_number`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=109

;

-- ----------------------------
-- Records of area_device_mapping
-- ----------------------------
BEGIN;
INSERT INTO `area_device_mapping` VALUES ('69', null, 'cau giay', null, '12', 'Ha noi', '12'), ('70', null, 'Hoàn Kiếm', null, 'AA:AB:AD:CE', 'Hà Nội', '1234567936'), ('71', null, 'Uông Bí', null, 'AA:AB:AD:CD', 'Quảng Ninh', '1234567935'), ('72', null, null, null, null, 'Quảng Ninh', '1234567934'), ('73', null, 'Hà Đông', null, null, 'Hà Nội', '1234567933'), ('74', null, null, null, null, 'Hà Nội', '1234567932'), ('76', null, null, null, null, 'Hà Nam', '1234567930'), ('77', null, null, null, null, 'Thái Nguyên', '1234567929'), ('78', null, 'Vĩnh Yên', null, null, 'Vĩnh Phúc', '1234567928'), ('79', null, 'Bình Xuyên', null, null, 'Vĩnh Phúc', '1234567927'), ('80', null, null, null, null, 'Bắc Kạn', '1234567926'), ('81', null, 'Hà Đông', null, '', 'Hà Nội', '1234567910'), ('82', null, 'Hoàn Kiếm', null, '', 'Hà Nội', '1234567911'), ('83', null, 'Uông Bí', null, '', 'Quảng Ninh', '1234567912'), ('84', null, null, null, null, 'Quảng Ninh', '1234567913'), ('85', null, 'Hà Đông', null, null, 'Hà Nội', '1234567914'), ('86', null, null, null, null, 'Hà Nội', '1234567915'), ('87', null, 'Phủ Lý', null, null, 'Hà Nam', '1234567916'), ('88', null, null, null, null, 'Hà Nam', '1234567917'), ('89', null, null, null, null, 'Thái Nguyên', '1234567918'), ('90', null, 'Vĩnh Yên', null, null, 'Vĩnh Phúc', '1234567919'), ('91', null, '', null, null, 'VP', '1234567920'), ('92', null, 'Hoàn Kiếm', null, '', 'Hà Nội', '1234567909'), ('93', null, 'Uông Bí', null, '', 'Quảng Ninh', '1234567908'), ('94', null, null, null, null, 'Quảng Ninh', '1234567907'), ('99', null, null, null, null, 'Thái Nguyên', '1234567902'), ('100', null, 'Vĩnh Yên', null, null, 'Vĩnh Phúc', '1234567901'), ('101', null, '', null, null, 'VP', '1234567900'), ('102', null, 'Cau Giay', null, 'm1', 'Ha Noi', 's1'), ('105', null, 'Quan 1', null, 'm3', 'TP.HCM', 's3'), ('107', null, 'Y Yen', null, null, 'Nam Dinh', 's2'), ('108', null, 'Ninh Giang', null, '84:84', 'Hải Dương', 's5');
COMMIT;

-- ----------------------------
-- Table structure for device
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
`id`  bigint(11) NOT NULL AUTO_INCREMENT ,
`mac`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`serial_number`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`model_id`  int(11) NULL DEFAULT NULL COMMENT 'Đối với thiết bị có model chưa tồn tại trong hệ thống thì đặt mặc định là UNDEFINED' ,
`area_id`  bigint(11) NULL DEFAULT NULL COMMENT 'Thiết bị lần đầu kết nói tới hệ thống đặt là NULL' ,
`cpu`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`ram`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`rom`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`cpe_username`  varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT 'User name XMPP account cua STB' ,
`product_class`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`connection_request`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`cpe_password`  varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT 'Password xmpp account cua STB' ,
`firmware_version`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`cpe_status`  int(11) NULL DEFAULT NULL COMMENT '0= OFF, 1= ON' ,
`firmware_status`  int(11) NULL DEFAULT NULL COMMENT '0: chưa update firmware mới nhất\n1: đã update firmware mới nhất\nfimware mới nhất tính theo release date ở bảng FIRMWARE' ,
`stb_username`  varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT 'Thong tin ve username xac thuc he thong MyTV cua STB' ,
`stb_password`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Thong tin dia chi IP cua thiet bi. IP sau NAT' ,
`ip_address`  varchar(15) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`homepage_url`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`upgrade_url`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`),
FOREIGN KEY (`model_id`) REFERENCES `device_model` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`area_id`) REFERENCES `area` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `fk_device_device_model_idx` (`model_id`) USING BTREE ,
INDEX `fk_device_area_idx` (`area_id`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
AUTO_INCREMENT=102

;

-- ----------------------------
-- Records of device
-- ----------------------------
BEGIN;
INSERT INTO `device` VALUES ('99', null, 'VNPT001dcabc', '8', null, null, null, null, null, null, 'http://10.2.8.203:30005/', null, 'G4.16A.03RC3', '0', '0', null, null, null, null, null), ('100', null, 'VNPT0083c66e', '8', null, null, null, null, null, null, 'http://10.2.8.202:30005/', null, 'G6.16A.02RTM', '0', '0', null, null, null, null, null), ('101', null, 'VNPT007edb9a', '8', null, null, null, null, null, null, 'http://10.84.3.157:30005/', null, 'G6.16A.02RTM', '0', '0', null, null, null, null, null);
COMMIT;

-- ----------------------------
-- Table structure for device_model
-- ----------------------------
DROP TABLE IF EXISTS `device_model`;
CREATE TABLE `device_model` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`description`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
COMMENT='Lũu trữ thông tin về loại STB'
AUTO_INCREMENT=10

;

-- ----------------------------
-- Records of device_model
-- ----------------------------
BEGIN;
INSERT INTO `device_model` VALUES ('6', 'iGate ONT v1', '96318REF_P300'), ('7', 'iGate ONT v2', '968380GERG'), ('8', 'ONT iGate v2', '968380GERG'), ('9', '968380GERG', null);
COMMIT;

-- ----------------------------
-- Table structure for firmware
-- ----------------------------
DROP TABLE IF EXISTS `firmware`;
CREATE TABLE `firmware` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`device_usage_number`  int(11) NULL DEFAULT 0 ,
`firmware_path`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`release_date`  timestamp NULL DEFAULT NULL ,
`release_note`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`version`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`fw_default`  int(2) NULL DEFAULT 0 ,
`model_id`  int(11) NOT NULL ,
PRIMARY KEY (`id`),
FOREIGN KEY (`model_id`) REFERENCES `device_model` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `FK_6tntimen0c4ed5dg423r77ojl` (`model_id`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=21

;

-- ----------------------------
-- Records of firmware
-- ----------------------------
BEGIN;
INSERT INTO `firmware` VALUES ('17', '0', 'http://10.84.3.144:8080/firmware/G1.12L.06RTM-150714_1538', '2015-10-22 17:34:56', '', 'G1.12L.06RTM', '0', '6'), ('19', '0', 'http://10.84.3.144:8080/firmware/G4.16L.06RC1_150922_1101.w', '2015-10-22 17:59:10', '', 'G4.16L.06RC1', '1', '7'), ('20', '0', 'http://10.84.3.202:8080/firmware/[SSDC_OEXAM]_Plan_Weekly_2016 (2).xlsx', '2016-03-30 15:33:41', '', '1.0.1', '1', '6');
COMMIT;

-- ----------------------------
-- Table structure for policy
-- ----------------------------
DROP TABLE IF EXISTS `policy`;
CREATE TABLE `policy` (
`id`  bigint(11) NOT NULL AUTO_INCREMENT ,
`name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`model_id`  int(11) NULL DEFAULT NULL ,
`start_time`  datetime NULL DEFAULT NULL ,
`end_time`  datetime NULL DEFAULT NULL ,
`policy_status`  int(11) NULL DEFAULT NULL COMMENT '0: chưa thực hiện\n, 1: đang thực hiện, 2: đã thực hiện' ,
`enable`  int(11) NULL DEFAULT NULL COMMENT 'trạng thái kích hoạt của policy \n0: inactive\n1: active' ,
`device_success_no`  int(11) NULL DEFAULT NULL COMMENT 'Số lượng thíe bị update thành công' ,
`device_failed_no`  int(11) NULL DEFAULT NULL COMMENT 'Số lượng thiết bị update failed' ,
`firmware_id`  int(11) NULL DEFAULT NULL ,
PRIMARY KEY (`id`),
FOREIGN KEY (`model_id`) REFERENCES `device_model` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`firmware_id`) REFERENCES `firmware` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
INDEX `fk_policy_device_model_idx` (`model_id`) USING BTREE ,
INDEX `fk_policy_firmware` (`firmware_id`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
COMMENT='bảng này lữu trữ các thông tin về policy để update firmware'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of policy
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for policy_area
-- ----------------------------
DROP TABLE IF EXISTS `policy_area`;
CREATE TABLE `policy_area` (
`policy_id`  bigint(20) NOT NULL ,
`area_id`  bigint(20) NOT NULL ,
PRIMARY KEY (`policy_id`, `area_id`),
FOREIGN KEY (`area_id`) REFERENCES `area` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`policy_id`) REFERENCES `policy` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `fk_policy_area_area` (`area_id`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Records of policy_area
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for policy_history
-- ----------------------------
DROP TABLE IF EXISTS `policy_history`;
CREATE TABLE `policy_history` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`end_time`  timestamp NULL DEFAULT NULL ,
`firmware_new_version`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`firmware_old_version`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`start_time`  timestamp NULL DEFAULT NULL ,
`status`  int(11) NULL DEFAULT NULL COMMENT '0-fail, 1-success, 2- updating' ,
`device_id`  bigint(20) NOT NULL ,
`policy_id`  bigint(20) NOT NULL ,
`device_serial_number`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`),
FOREIGN KEY (`device_id`) REFERENCES `device` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
FOREIGN KEY (`policy_id`) REFERENCES `policy` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
INDEX `fk_policy_history_policy` (`policy_id`) USING BTREE ,
INDEX `fk_policy_history_device` (`device_id`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of policy_history
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`description`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
INSERT INTO `role` VALUES ('-2', 'Default role for all Users', 'ROLE_USER'), ('-1', 'Administrator role (can edit Users)', 'ROLE_ADMIN');
COMMIT;

-- ----------------------------
-- Table structure for update_firmware_logs
-- ----------------------------
DROP TABLE IF EXISTS `update_firmware_logs`;
CREATE TABLE `update_firmware_logs` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`device_id`  int(11) NULL DEFAULT NULL ,
`new_firmware_id`  int(11) NULL DEFAULT NULL ,
`status`  int(11) NULL DEFAULT NULL ,
`update_time`  date NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of update_firmware_logs
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
`user_id`  bigint(20) NOT NULL ,
`role_id`  bigint(20) NOT NULL ,
PRIMARY KEY (`user_id`, `role_id`),
FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `FK_it77eq964jhfqtu54081ebtio` (`role_id`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Records of user_role
-- ----------------------------
BEGIN;
INSERT INTO `user_role` VALUES ('-3', '-2'), ('-2', '-2'), ('-1', '-2'), ('35', '-2'), ('36', '-2'), ('37', '-2'), ('38', '-2'), ('-3', '-1'), ('-2', '-1'), ('35', '-1'), ('36', '-1'), ('37', '-1'), ('38', '-1');
COMMIT;

-- ----------------------------
-- Auto increment value for action_logs
-- ----------------------------
ALTER TABLE `action_logs` AUTO_INCREMENT=586;

-- ----------------------------
-- Auto increment value for app_user
-- ----------------------------
ALTER TABLE `app_user` AUTO_INCREMENT=39;

-- ----------------------------
-- Auto increment value for area
-- ----------------------------
ALTER TABLE `area` AUTO_INCREMENT=35;

-- ----------------------------
-- Auto increment value for area_device_mapping
-- ----------------------------
ALTER TABLE `area_device_mapping` AUTO_INCREMENT=109;
DROP TRIGGER IF EXISTS `check_latest_firmware_insert`;
DELIMITER ;;
CREATE TRIGGER `check_latest_firmware_insert` BEFORE INSERT ON `device` FOR EACH ROW BEGIN
DECLARE maxdate TIMESTAMP;
DECLARE fversion VARCHAR(50); 


	SELECT  MAX(release_date) INTO maxdate  from firmware f WHERE f.model_id = NEW.model_id ; 
  SELECT f.version INTO fversion FROM firmware f WHERE  f.model_id = NEW.model_id and f.release_date = maxdate;

-- SET NEW.cpe_password = NEW.model_id;  

	IF (NEW.firmware_version = fversion) THEN 
		SET NEW.firmware_status = 1; -- latest firmware 
	ELSE
		SET NEW.firmware_status = 0; -- NOT latest firmware 
	END IF;



END
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `check_latest_firmware_update`;
DELIMITER ;;
CREATE TRIGGER `check_latest_firmware_update` BEFORE UPDATE ON `device` FOR EACH ROW BEGIN
DECLARE maxdate TIMESTAMP;
DECLARE fversion VARCHAR(50); 

IF((NEW.firmware_version <> OLD.firmware_version) OR (NEW.model_id <> OLD.model_id))THEN

	SELECT  MAX(release_date) INTO maxdate  from firmware f WHERE f.model_id = NEW.model_id ; 
  SELECT f.version INTO fversion FROM firmware f WHERE  f.model_id = NEW.model_id and f.release_date = maxdate;

-- SET NEW.cpe_password = NEW.model_id;  

	IF (NEW.firmware_version = fversion) THEN 
		SET NEW.firmware_status = 1; -- latest firmware 
	ELSE
		SET NEW.firmware_status = 0; -- NOT latest firmware 
	END IF;

END IF;

END
;;
DELIMITER ;

-- ----------------------------
-- Auto increment value for device
-- ----------------------------
ALTER TABLE `device` AUTO_INCREMENT=102;

-- ----------------------------
-- Auto increment value for device_model
-- ----------------------------
ALTER TABLE `device_model` AUTO_INCREMENT=10;

-- ----------------------------
-- Auto increment value for firmware
-- ----------------------------
ALTER TABLE `firmware` AUTO_INCREMENT=21;
DROP TRIGGER IF EXISTS `update_device_usage_number_after_update_firmware`;
DELIMITER ;;
CREATE TRIGGER `update_device_usage_number_after_update_firmware` AFTER UPDATE ON `policy` FOR EACH ROW BEGIN
DECLARE fw_id integer;

SET fw_id = NEW.firmware_id;

IF(NEW.device_success_no > OLD.device_success_no) 
THEN
   UPDATE firmware SET device_usage_number = device_usage_number + 1 WHERE id = fw_id;  
END IF;

END
;;
DELIMITER ;

-- ----------------------------
-- Auto increment value for policy
-- ----------------------------
ALTER TABLE `policy` AUTO_INCREMENT=1;
DROP TRIGGER IF EXISTS `update_policy_count_after_history_insert_fail`;
DELIMITER ;;
CREATE TRIGGER `update_policy_count_after_history_insert_fail` AFTER INSERT ON `policy_history` FOR EACH ROW BEGIN
DECLARE po_id integer;

SET po_id = NEW.policy_id;


IF(NEW.`status` = 1) 
THEN
   UPDATE policy SET device_success_no = device_success_no + 1 WHERE id = po_id;  
END IF;

IF(NEW.`status` = 0) 
THEN
   UPDATE policy SET device_failed_no = device_failed_no + 1 WHERE id = po_id;     
END IF;

END
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `update_policy_count_after_history_update`;
DELIMITER ;;
CREATE TRIGGER `update_policy_count_after_history_update` BEFORE UPDATE ON `policy_history` FOR EACH ROW BEGIN
DECLARE po_id integer;

SET po_id = NEW.policy_id;


IF(NEW.`status` = 1) 
THEN
   UPDATE policy SET device_success_no = device_success_no + 1 WHERE id = po_id;  
END IF;

IF(NEW.`status` = 0) 
THEN
   UPDATE policy SET device_failed_no = device_failed_no + 1 WHERE id = po_id;     
END IF;

END
;;
DELIMITER ;

-- ----------------------------
-- Auto increment value for policy_history
-- ----------------------------
ALTER TABLE `policy_history` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for role
-- ----------------------------
ALTER TABLE `role` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for update_firmware_logs
-- ----------------------------
ALTER TABLE `update_firmware_logs` AUTO_INCREMENT=1;
