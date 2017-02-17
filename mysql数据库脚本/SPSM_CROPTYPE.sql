
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `SPSM_CROPTYPE`
-- ----------------------------
DROP TABLE IF EXISTS `SPSM_CROPTYPE`;
CREATE TABLE `SPSM_CROPTYPE` (
 		`CROPTYPE_ID` varchar(100) NOT NULL,
		`CROPTYPENAME` varchar(255) DEFAULT NULL COMMENT '作物类别名',
  		PRIMARY KEY (`CROPTYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
