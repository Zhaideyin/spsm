
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `SPSM_SEEDURL`
-- ----------------------------
DROP TABLE IF EXISTS `SPSM_SEEDURL`;
CREATE TABLE `SPSM_SEEDURL` (
 		`SEEDURL_ID` varchar(100) NOT NULL,
		`SEEDURL` varchar(255) DEFAULT NULL COMMENT '种子地址',
		`STATUS` varchar(255) DEFAULT NULL COMMENT '状态',
  		PRIMARY KEY (`SEEDURL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
