
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `SPSM_ANNEXURL`
-- ----------------------------
DROP TABLE IF EXISTS `SPSM_ANNEXURL`;
CREATE TABLE `SPSM_ANNEXURL` (
 		`ANNEXURL_ID` varchar(100) NOT NULL,
		`ANNEXURL` varchar(255) DEFAULT NULL COMMENT '附件地址',
		`STATUS` varchar(255) DEFAULT NULL COMMENT '状态',
		`TARGETURLID` varchar(255) DEFAULT NULL COMMENT '目标地址id',
  		PRIMARY KEY (`ANNEXURL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
