
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `SOCKET_FROM8583`
-- ----------------------------
DROP TABLE IF EXISTS `SOCKET_FROM8583`;
CREATE TABLE `SOCKET_FROM8583` (
 		`FROM8583_ID` varchar(100) NOT NULL,
		`TOTEXT` varchar(255) DEFAULT NULL COMMENT '接受来自8583的报文',
		`STATE` varchar(255) DEFAULT NULL COMMENT '状态',
		`SENDTIME` varchar(32) DEFAULT NULL COMMENT '发送时间',
  		PRIMARY KEY (`FROM8583_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
