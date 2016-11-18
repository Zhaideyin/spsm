
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `SPSM_LISTTYPE`
-- ----------------------------
DROP TABLE IF EXISTS `SPSM_LISTTYPE`;
CREATE TABLE `SPSM_LISTTYPE` (
 		`LISTTYPE_ID` varchar(100) NOT NULL,
		`LISTTYPENAME` varchar(255) DEFAULT NULL COMMENT '列表名',
		`NAVBARID` varchar(255) DEFAULT NULL COMMENT '导航栏编号',
  		PRIMARY KEY (`LISTTYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
