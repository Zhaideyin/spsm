
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `spsm_TEST`
-- ----------------------------
DROP TABLE IF EXISTS `spsm_TEST`;
CREATE TABLE `spsm_TEST` (
 		`TEST_ID` varchar(100) NOT NULL,
		`NAME` varchar(255) DEFAULT NULL COMMENT '名字',
  		PRIMARY KEY (`TEST_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
