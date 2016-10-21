
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `SPSM_TYPE`
-- ----------------------------
DROP TABLE IF EXISTS `SPSM_TYPE`;
CREATE TABLE `SPSM_TYPE` (
 		`TYPE_ID` varchar(100) NOT NULL,
		`TYPENAME` varchar(255) DEFAULT NULL COMMENT '类型名',
  		PRIMARY KEY (`TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
