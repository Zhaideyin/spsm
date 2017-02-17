
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `SPSM_CROP`
-- ----------------------------
DROP TABLE IF EXISTS `SPSM_CROP`;
CREATE TABLE `SPSM_CROP` (
 		`CROP_ID` varchar(100) NOT NULL,
		`CROPNAME` varchar(255) DEFAULT NULL COMMENT '作物名',
		`CROPTYPEID` varchar(255) DEFAULT NULL COMMENT '所属作物类别',
  		PRIMARY KEY (`CROP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
