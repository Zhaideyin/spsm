
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `SPSM_WEBINFO`
-- ----------------------------
DROP TABLE IF EXISTS `SPSM_WEBINFO`;
CREATE TABLE `SPSM_WEBINFO` (
 		`WEBINFO_ID` varchar(100) NOT NULL,
		`SEED` varchar(255) DEFAULT NULL COMMENT '种子地址',
		`URLREX` varchar(255) DEFAULT NULL COMMENT 'url过滤正则表达式',
		`TITLETAG` varchar(255) DEFAULT NULL COMMENT '标题标签',
		`CONTENTTAG` varchar(255) DEFAULT NULL COMMENT '内容标签',
		`HASIMG` varchar(255) DEFAULT NULL COMMENT '是否含图片',
		`IMGREGEX` varchar(255) DEFAULT NULL COMMENT '图片过滤正则表达式',
		`IMGTAG` varchar(255) DEFAULT NULL COMMENT '图片标签',
		`HASDOC` varchar(255) DEFAULT NULL COMMENT '是否含有文件',
		`DOCREGEX` varchar(255) DEFAULT NULL COMMENT '文件过滤正则表达式',
		`DOCTAG` varchar(255) DEFAULT NULL COMMENT '文件标签',
		`TOTALPAGE` varchar(255) DEFAULT NULL COMMENT '网站总页数',
		`PAGEAJAXTAG` varchar(255) DEFAULT NULL COMMENT '网站分页是ajax分页设置',
		`PAGEGETTAG` varchar(255) DEFAULT NULL COMMENT '网站分页是get的',
		`PAGEMETHOD` varchar(255) DEFAULT NULL COMMENT '网站分页类型',
		`PAGEENCODING` varchar(255) DEFAULT NULL COMMENT '网页编码格式',
  		PRIMARY KEY (`WEBINFO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
