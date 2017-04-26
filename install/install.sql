/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50711
 Source Host           : localhost
 Source Database       : domo

 Target Server Type    : MySQL
 Target Server Version : 50711
 File Encoding         : utf-8

 Date: 07/11/2016 15:36:26 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `db_fhdb`
-- ----------------------------
DROP TABLE IF EXISTS `db_fhdb`;
CREATE TABLE `db_fhdb` (
  `FHDB_ID` varchar(100) NOT NULL,
  `USERNAME` varchar(50) DEFAULT NULL COMMENT '操作用户',
  `BACKUP_TIME` varchar(32) DEFAULT NULL COMMENT '备份时间',
  `TABLENAME` varchar(50) DEFAULT NULL COMMENT '表名',
  `SQLPATH` varchar(300) DEFAULT NULL COMMENT '存储位置',
  `TYPE` int(1) NOT NULL COMMENT '类型',
  `DBSIZE` varchar(10) DEFAULT NULL COMMENT '文件大小',
  `BZ` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`FHDB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `db_timingbackup`
-- ----------------------------
DROP TABLE IF EXISTS `db_timingbackup`;
CREATE TABLE `db_timingbackup` (
  `TIMINGBACKUP_ID` varchar(100) NOT NULL,
  `JOBNAME` varchar(50) DEFAULT NULL COMMENT '任务名称',
  `CREATE_TIME` varchar(32) DEFAULT NULL COMMENT '创建时间',
  `TABLENAME` varchar(50) DEFAULT NULL COMMENT '表名',
  `STATUS` int(1) NOT NULL COMMENT '类型',
  `FHTIME` varchar(30) DEFAULT NULL COMMENT '时间规则',
  `TIMEEXPLAIN` varchar(100) DEFAULT NULL COMMENT '规则说明',
  `BZ` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`TIMINGBACKUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `db_timingbackup`
-- ----------------------------
BEGIN;
INSERT INTO `db_timingbackup` VALUES ('311e06c34a5e4518a86d5d30799f9b55', 'sys_app_user_515762', '2016-04-11 17:04:55', 'sys_app_user', '2', '1/2 * * ? * *', '每个月的 每周 每天 每小时执行一次', '备份任务');
COMMIT;

-- ----------------------------
--  Table structure for `oa_department`
-- ----------------------------
DROP TABLE IF EXISTS `oa_department`;
CREATE TABLE `oa_department` (
  `DEPARTMENT_ID` varchar(100) NOT NULL,
  `NAME` varchar(30) DEFAULT NULL COMMENT '名称',
  `NAME_EN` varchar(50) DEFAULT NULL COMMENT '英文',
  `BIANMA` varchar(50) DEFAULT NULL COMMENT '编码',
  `PARENT_ID` varchar(100) DEFAULT NULL COMMENT '上级ID',
  `BZ` varchar(255) DEFAULT NULL COMMENT '备注',
  `HEADMAN` varchar(30) DEFAULT NULL COMMENT '负责人',
  `TEL` varchar(50) DEFAULT NULL COMMENT '电话',
  `FUNCTIONS` varchar(255) DEFAULT NULL COMMENT '部门职能',
  `ADDRESS` varchar(255) DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`DEPARTMENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sys_app_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_user`;
CREATE TABLE `sys_app_user` (
  `USER_ID` varchar(100) NOT NULL,
  `USERNAME` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `RIGHTS` varchar(255) DEFAULT NULL,
  `ROLE_ID` varchar(100) DEFAULT NULL,
  `LAST_LOGIN` varchar(255) DEFAULT NULL,
  `IP` varchar(100) DEFAULT NULL,
  `STATUS` varchar(32) DEFAULT NULL,
  `BZ` varchar(255) DEFAULT NULL,
  `PHONE` varchar(100) DEFAULT NULL,
  `SFID` varchar(100) DEFAULT NULL,
  `START_TIME` varchar(100) DEFAULT NULL,
  `END_TIME` varchar(100) DEFAULT NULL,
  `YEARS` int(10) DEFAULT NULL,
  `NUMBER` varchar(100) DEFAULT NULL,
  `EMAIL` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_app_user`
-- ----------------------------
BEGIN;
INSERT INTO `sys_app_user` VALUES ('1e89e6504be349a68c025976b3ecc1d1', 'a1', '698d51a19d8a121ce581499d7b701668', '会员甲', '', '115b386ff04f4352b060dffcd2b5d1da', '', '', '1', '121', '1212', '1212', '2015-12-02', '2015-12-25', '2', '111', '313596790@qq.com'), ('ead1f56708e4409c8d071e0a699e5633', 'a2', 'bcbe3365e6ac95ea2c0343a2395834dd', '会员乙', '', '1b67fc82ce89457a8347ae53e43a347e', '', '', '0', '', '', '', '2015-12-01', '2015-12-24', '1', '121', '978336446@qq.com');
COMMIT;

-- ----------------------------
--  Table structure for `sys_createcode`
-- ----------------------------
DROP TABLE IF EXISTS `sys_createcode`;
CREATE TABLE `sys_createcode` (
  `CREATECODE_ID` varchar(100) NOT NULL,
  `PACKAGENAME` varchar(50) DEFAULT NULL COMMENT '包名',
  `OBJECTNAME` varchar(50) DEFAULT NULL COMMENT '类名',
  `TABLENAME` varchar(50) DEFAULT NULL COMMENT '表名',
  `FIELDLIST` varchar(5000) DEFAULT NULL COMMENT '属性集',
  `CREATETIME` varchar(100) DEFAULT NULL COMMENT '创建时间',
  `TITLE` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`CREATECODE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_createcode`
-- ----------------------------
BEGIN;
INSERT INTO `sys_createcode` VALUES ('002ea762e3e242a7a10ea5ca633701d8', 'system', 'Buttonrights', 'sys_,fh,BUTTONRIGHTS', 'NAME,fh,String,fh,名称,fh,是,fh,无,fh,255Q313596790', '2016-01-16 23:20:36', '按钮权限'), ('49d985e081ed44e6b34ba1b8c5466e39', 'fhdb', 'TimingBackUp', 'DB_,fh,TIMINGBACKUP', 'JOBNAME,fh,String,fh,任务名称,fh,否,fh,无,fh,50Q313596790CREATE_TIME,fh,Date,fh,创建时间,fh,否,fh,无,fh,32Q313596790TABLENAME,fh,String,fh,表名,fh,是,fh,无,fh,50Q313596790TYPE,fh,Integer,fh,类型,fh,否,fh,无,fh,1Q313596790FHTIME,fh,String,fh,时间规则,fh,是,fh,无,fh,30Q313596790TIMEEXPLAIN,fh,String,fh,规则说明,fh,是,fh,无,fh,100Q313596790BZ,fh,String,fh,备注,fh,是,fh,无,fh,255Q313596790', '2016-04-09 11:53:38', '定时备份'), ('4d3770c45f434d83852bdc7b290653a5', 'domo', 'ResearchAtricle', 'DOMO_,fh,RESEARCHATRICLE', 'TITLE,fh,String,fh,标题,fh,是,fh,无,fh,255Q313596790RELEASEDATE,fh,Date,fh,发布日期,fh,是,fh,无,fh,32Q313596790AUTHOR,fh,String,fh,作者,fh,是,fh,无,fh,255Q313596790CATEGORY,fh,String,fh,类型,fh,是,fh,无,fh,255Q313596790CONTENT,fh,String,fh,内容,fh,是,fh,无,fh,2048Q313596790PIC,fh,String,fh,图片,fh,是,fh,无,fh,255Q313596790', '2016-04-17 10:27:09', '研究文章'), ('62cb2680e04841e3bab772b8ef2a9932', 'domo', 'News', 'DOMO_,fh,NEWS', 'TITLE,fh,String,fh,标题,fh,是,fh,无,fh,255Q313596790NEWSDATE,fh,Date,fh,日期,fh,是,fh,无,fh,32Q313596790CATEGORY,fh,Integer,fh,类别,fh,是,fh,无,fh,11Q313596790DESCRIPTION,fh,String,fh,摘要,fh,是,fh,无,fh,255Q313596790URL,fh,String,fh,详情页,fh,是,fh,无,fh,255Q313596790PIC,fh,String,fh,图片id,fh,是,fh,无,fh,255Q313596790', '2016-04-16 09:22:54', '大墨新闻动态'), ('7987821ce92141bb90cd3bf16a66b174', 'domo', 'ResearchDoc', 'DOMO_,fh,RESEARCHDOC', 'TITLE,fh,String,fh,标题,fh,是,fh,无,fh,255Q313596790UPLOADDATE,fh,Date,fh,上传日期,fh,是,fh,无,fh,32Q313596790DESCRIPTION,fh,String,fh,摘要,fh,是,fh,无,fh,255Q313596790URL,fh,String,fh,链接,fh,是,fh,无,fh,255Q313596790', '2016-04-16 09:29:58', '大墨研究报告'), ('bf35ab8b2d064bf7928a04bba5e5a6dd', 'system', 'Fhsms', 'SYS_,fh,FHSMS', 'CONTENT,fh,String,fh,内容,fh,是,fh,无,fh,1000Q313596790TYPE,fh,String,fh,类型,fh,否,fh,无,fh,5Q313596790TO_USERNAME,fh,String,fh,收信人,fh,是,fh,无,fh,255Q313596790FROM_USERNAME,fh,String,fh,发信人,fh,是,fh,无,fh,255Q313596790SEND_TIME,fh,String,fh,发信时间,fh,是,fh,无,fh,100Q313596790STATUS,fh,String,fh,状态,fh,否,fh,无,fh,5Q313596790SANME_ID,fh,String,fh,共同ID,fh,是,fh,无,fh,100Q313596790', '2016-03-27 21:39:45', '站内信'), ('c7586f931fd44c61beccd3248774c68c', 'system', 'Department', 'SYS_,fh,DEPARTMENT', 'NAME,fh,String,fh,名称,fh,是,fh,无,fh,30Q313596790NAME_EN,fh,String,fh,英文,fh,是,fh,无,fh,50Q313596790BIANMA,fh,String,fh,编码,fh,是,fh,无,fh,50Q313596790PARENT_ID,fh,String,fh,上级ID,fh,否,fh,无,fh,100Q313596790BZ,fh,String,fh,备注,fh,是,fh,无,fh,255Q313596790HEADMAN,fh,String,fh,负责人,fh,是,fh,无,fh,30Q313596790TEL,fh,String,fh,电话,fh,是,fh,无,fh,50Q313596790FUNCTIONS,fh,String,fh,部门职能,fh,是,fh,无,fh,255Q313596790ADDRESS,fh,String,fh,地址,fh,是,fh,无,fh,255Q313596790', '2015-12-20 01:49:25', '组织机构'), ('c937e21208914e5b8fb1202c685bbf2f', 'fhdb', 'Fhdb', 'DB_,fh,FHDB', 'USERNAME,fh,String,fh,操作用户,fh,否,fh,无,fh,50Q313596790BACKUP_TIME,fh,Date,fh,备份时间,fh,否,fh,无,fh,32Q313596790TABLENAME,fh,String,fh,表名,fh,是,fh,无,fh,50Q313596790SQLPATH,fh,String,fh,存储位置,fh,否,fh,无,fh,300Q313596790TYPE,fh,Integer,fh,类型,fh,是,fh,无,fh,1Q313596790DBSIZE,fh,String,fh,文件大小,fh,否,fh,无,fh,10Q313596790BZ,fh,String,fh,备注,fh,否,fh,无,fh,255Q313596790', '2016-03-30 13:46:54', '数据库管理'), ('d514dbd2474d4b6c8b6ab9904cc9cc7c', 'new', 'News', 'TB_,fh,NEWS', 'TITLE,fh,String,fh,标题,fh,是,fh,无,fh,255Q313596790', '2016-01-25 16:38:14', '新闻管理'), ('dbd7b8330d774dcabd184eca8668a295', 'system', 'Fhsms', 'SYS_,fh,FHSMS', 'CONTENT,fh,String,fh,内容,fh,是,fh,无,fh,1000Q313596790TYPE,fh,String,fh,类型,fh,否,fh,无,fh,5Q313596790TO_USERNAME,fh,String,fh,收信人,fh,是,fh,无,fh,255Q313596790FROM_USERNAME,fh,String,fh,发信人,fh,是,fh,无,fh,255Q313596790SEND_TIME,fh,String,fh,发信时间,fh,是,fh,无,fh,100Q313596790STATUS,fh,String,fh,状态,fh,否,fh,无,fh,5Q313596790SANME_ID,fh,String,fh,共同ID,fh,是,fh,无,fh,100Q313596790', '2016-01-23 01:44:15', '站内信'), ('dbea21ad945846ccb65c060231dd2e84', 'domo', 'Planner', 'DOMO_,fh,PLANNER', 'NAME,fh,String,fh,姓名,fh,是,fh,无,fh,255Q313596790TITLE,fh,String,fh,职称,fh,是,fh,无,fh,255Q313596790POSITION,fh,String,fh,职位,fh,是,fh,无,fh,255Q313596790LEVEL,fh,String,fh,评级,fh,是,fh,无,fh,255Q313596790FIELD,fh,String,fh,擅长领域,fh,是,fh,无,fh,255Q313596790PROFIT,fh,String,fh,收益,fh,是,fh,无,fh,255Q313596790EXPERIENCE,fh,String,fh,年限,fh,是,fh,无,fh,255Q313596790PIC,fh,String,fh,头像,fh,是,fh,无,fh,255Q313596790', '2016-04-16 09:37:44', '大墨理财师'), ('e37ec98c6ab5492d807fae5799399906', 'domo', 'PlannerShow', 'DOMO_,fh,PLANNERSHOW', 'PLANNERID,fh,String,fh,理财师id,fh,是,fh,无,fh,255Q313596790POSITION,fh,Integer,fh,展示位置,fh,是,fh,无,fh,11Q313596790', '2016-04-17 09:35:05', '大墨理财师展示'), ('f6daf855f4ec4df387e54e604349ae0d', 'domo', 'Partner', 'DOMO_,fh,PARTNER', 'NAME,fh,String,fh,名称,fh,是,fh,无,fh,255Q313596790LOGO,fh,String,fh,logo链接,fh,是,fh,无,fh,255Q313596790URL,fh,String,fh,链接,fh,是,fh,无,fh,255Q313596790CATEGORY,fh,String,fh,类别,fh,是,fh,无,fh,255Q313596790WEIGHT,fh,Integer,fh,权重,fh,是,fh,无,fh,11Q313596790', '2016-04-16 09:48:56', '大墨合作伙伴'), ('fb6d50c45c854160b4fc04e03a9c63e4', 'domo', 'Lesson', 'DOMO_,fh,LESSON', 'TITLE,fh,String,fh,标题,fh,是,fh,无,fh,255Q313596790DURING,fh,String,fh,时间,fh,是,fh,无,fh,255Q313596790PRICE,fh,String,fh,费用,fh,是,fh,无,fh,255Q313596790LESSONCOUNT,fh,String,fh,课时,fh,是,fh,无,fh,255Q313596790DESCRIPTION,fh,String,fh,摘要,fh,是,fh,无,fh,255Q313596790URL,fh,String,fh,详情链接,fh,是,fh,无,fh,255Q313596790PIC,fh,String,fh,图标,fh,是,fh,无,fh,255Q313596790', '2016-04-16 09:42:23', '大墨课程'), ('fe239f8742194481a5b56f90cad71520', 'system', 'Fhbutton', 'SYS_,fh,FHBUTTON', 'NAME,fh,String,fh,名称,fh,是,fh,无,fh,30Q313596790QX_NAME,fh,String,fh,权限标识,fh,是,fh,无,fh,50Q313596790BZ,fh,String,fh,备注,fh,是,fh,无,fh,255Q313596790', '2016-01-15 18:38:40', '按钮管理');
COMMIT;

-- ----------------------------
--  Table structure for `sys_dictionaries`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionaries`;
CREATE TABLE `sys_dictionaries` (
  `DICTIONARIES_ID` varchar(100) NOT NULL,
  `NAME` varchar(30) DEFAULT NULL COMMENT '名称',
  `NAME_EN` varchar(50) DEFAULT NULL COMMENT '英文',
  `BIANMA` varchar(50) DEFAULT NULL COMMENT '编码',
  `ORDER_BY` int(11) NOT NULL COMMENT '排序',
  `PARENT_ID` varchar(100) DEFAULT NULL COMMENT '上级ID',
  `BZ` varchar(255) DEFAULT NULL COMMENT '备注',
  `TBSNAME` varchar(100) DEFAULT NULL COMMENT '排查表',
  PRIMARY KEY (`DICTIONARIES_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sys_fhbutton`
-- ----------------------------
DROP TABLE IF EXISTS `sys_fhbutton`;
CREATE TABLE `sys_fhbutton` (
  `FHBUTTON_ID` varchar(100) NOT NULL,
  `NAME` varchar(30) DEFAULT NULL COMMENT '名称',
  `QX_NAME` varchar(50) DEFAULT NULL COMMENT '权限标识',
  `BZ` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`FHBUTTON_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_fhbutton`
-- ----------------------------
BEGIN;
INSERT INTO `sys_fhbutton` VALUES ('3542adfbda73410c976e185ffe50ad06', '导出EXCEL', 'toExcel', '导出EXCEL'), ('46992ea280ba4b72b29dedb0d4bc0106', '发邮件', 'email', '发送电子邮件'), ('4efa162fce8340f0bd2dcd3b11d327ec', '导入EXCEL', 'FromExcel', '导入EXCEL到系统用户'), ('cc51b694d5344d28a9aa13c84b7166cd', '发短信', 'sms', '发送短信'), ('da7fd386de0b49ce809984f5919022b8', '站内信', 'FHSMS', '发送站内信');
COMMIT;

-- ----------------------------
--  Table structure for `sys_fhsms`
-- ----------------------------
DROP TABLE IF EXISTS `sys_fhsms`;
CREATE TABLE `sys_fhsms` (
  `FHSMS_ID` varchar(100) NOT NULL,
  `CONTENT` varchar(1000) DEFAULT NULL COMMENT '内容',
  `TYPE` varchar(5) DEFAULT NULL COMMENT '类型',
  `TO_USERNAME` varchar(255) DEFAULT NULL COMMENT '收信人',
  `FROM_USERNAME` varchar(255) DEFAULT NULL COMMENT '发信人',
  `SEND_TIME` varchar(100) DEFAULT NULL COMMENT '发信时间',
  `STATUS` varchar(5) DEFAULT NULL COMMENT '状态',
  `SANME_ID` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`FHSMS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_fhsms`
-- ----------------------------
BEGIN;
INSERT INTO `sys_fhsms` VALUES ('05879f5868824f35932ee9f2062adc03', '你好', '2', 'admin', 'san', '2016-01-25 14:05:31', '1', 'b311e893228f42d5a05dbe16917fd16f'), ('060aaea8c1874dc88a694dc67ffbcf0a', 'test', '2', 'san', 'admin', '2016-04-15 21:33:00', '2', '052dd87714294fb5a7e0de5a9999e158'), ('2635dd035c6f4bb5a091abdd784bd899', '你好', '2', 'san', 'admin', '2016-01-25 14:05:02', '2', '1b7637306683460f89174c2b025862b5'), ('4ef2f5d53db041c4a487eb20ac1f2520', 'test', '2', 'san', 'admin', '2016-04-15 21:32:20', '2', '0cb2a9d88ebf4bd0a39002d63d951356'), ('52378ccd4e2d4fe08994d1652af87c68', '你好', '1', 'admin', 'san', '2016-01-25 16:26:44', '1', '920b20dafdfb4c09b560884eb277c51d'), ('77ed13f9c49a4c4bb460c41b8580dd36', 'gggg', '2', 'admin', 'san', '2016-01-24 21:22:43', '2', 'dd9ee339576e48c5b046b94fa1901d00'), ('8c5fecbc82cd4605b4a412cf43342821', 'test', '1', 'admin', 'san', '2016-04-15 21:33:00', '2', '052dd87714294fb5a7e0de5a9999e158'), ('98a6869f942042a1a037d9d9f01cb50f', '你好', '1', 'admin', 'san', '2016-01-25 14:05:02', '2', '1b7637306683460f89174c2b025862b5'), ('9e00295529014b6e8a27019cbccb3da1', '柔柔弱弱', '1', 'admin', 'san', '2016-01-24 21:22:57', '1', 'a29603d613ea4e54b5678033c1bf70a6'), ('d29787433a034cfba7d6d57db7331156', 'test', '1', 'admin', 'san', '2016-04-15 21:32:20', '2', '0cb2a9d88ebf4bd0a39002d63d951356'), ('d3aedeb430f640359bff86cd657a8f59', '你好', '1', 'admin', 'san', '2016-01-24 21:22:12', '1', 'f022fbdce3d845aba927edb698beb90b'), ('e5376b1bd54b489cb7f2203632bd74ec', '管理员好', '2', 'admin', 'san', '2016-01-25 14:06:13', '2', 'b347b2034faf43c79b54be4627f3bd2b'), ('e613ac0fcc454f32895a70b747bf4fb5', '你也好', '2', 'admin', 'san', '2016-01-25 16:27:54', '2', 'ce8dc3b15afb40f28090f8b8e13f078d'), ('f25e00cfafe741a3a05e3839b66dc7aa', '你好', '2', 'san', 'admin', '2016-01-25 16:26:44', '1', '920b20dafdfb4c09b560884eb277c51d');
COMMIT;

-- ----------------------------
--  Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `MENU_ID` int(11) NOT NULL,
  `MENU_NAME` varchar(255) DEFAULT NULL,
  `MENU_URL` varchar(255) DEFAULT NULL,
  `PARENT_ID` varchar(100) DEFAULT NULL,
  `MENU_ORDER` varchar(100) DEFAULT NULL,
  `MENU_ICON` varchar(60) DEFAULT NULL,
  `MENU_TYPE` varchar(10) DEFAULT NULL,
  `MENU_STATE` int(1) DEFAULT NULL,
  PRIMARY KEY (`MENU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_menu`
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES ('1', '系统管理', '#', '0', '1', 'menu-icon fa fa-desktop blue', '2', '1'), ('2', '权限管理', '#', '1', '1', 'menu-icon fa fa-lock black', '1', '1'), ('6', '信息管理', '#', '0', '5', 'menu-icon fa fa-credit-card green', '2', '1'), ('7', '图片管理', '#', '6', '1', 'menu-icon fa fa-folder-o pink', '2', '1'), ('8', '性能监控', 'druid/index.html', '9', '8', 'menu-icon fa fa-tachometer red', '1', '1'), ('9', '系统工具', '#', '0', '3', 'menu-icon fa fa-cog black', '2', '1'), ('10', '接口测试', 'tool/interfaceTest.do', '9', '2', 'menu-icon fa fa-exchange green', '1', '1'), ('11', '发送邮件', 'tool/goSendEmail.do', '9', '3', 'menu-icon fa fa-envelope-o green', '1', '1'), ('12', '置二维码', 'tool/goTwoDimensionCode.do', '9', '4', 'menu-icon fa fa-barcode green', '1', '1'), ('14', '地图工具', 'tool/map.do', '9', '6', 'menu-icon fa fa-globe black', '1', '1'), ('15', '微信管理', '#', '0', '4', 'menu-icon fa fa-comments purple', '2', '1'), ('16', '文本回复', 'textmsg/list.do', '15', '2', 'menu-icon fa fa-comment green', '2', '1'), ('17', '应用命令', 'command/list.do', '15', '4', 'menu-icon fa fa-comment grey', '2', '1'), ('18', '图文回复', 'imgmsg/list.do', '15', '3', 'menu-icon fa fa-comment pink', '2', '1'), ('19', '关注回复', 'textmsg/goSubscribe.do', '15', '1', 'menu-icon fa fa-comment orange', '2', '1'), ('20', '在线管理', 'onlinemanager/list.do', '1', '5', 'menu-icon fa fa-laptop green', '1', '1'), ('21', '打印测试', 'tool/printTest.do', '9', '7', 'menu-icon fa fa-hdd-o grey', '1', '1'), ('22', '一级菜单', '#', '0', '10', 'menu-icon fa fa-fire orange', '2', '1'), ('23', '二级菜单', '#', '22', '1', 'menu-icon fa fa-leaf black', '1', '1'), ('24', '三级菜单', '#', '23', '1', 'menu-icon fa fa-leaf black', '1', '1'), ('30', '四级菜单', '#', '24', '1', 'menu-icon fa fa-leaf black', '1', '1'), ('31', '五级菜单1', '#', '30', '1', 'menu-icon fa fa-leaf black', '1', '1'), ('32', '五级菜单2', '#', '30', '2', 'menu-icon fa fa-leaf black', '1', '1'), ('33', '六级菜单', '#', '31', '1', 'menu-icon fa fa-leaf black', '1', '1'), ('34', '六级菜单2', 'login_default.do', '31', '2', 'menu-icon fa fa-leaf black', '1', '1'), ('35', '四级菜单2', 'login_default.do', '24', '2', 'menu-icon fa fa-leaf black', '1', '1'), ('36', '角色(基础权限)', 'role.do', '2', '1', 'menu-icon fa fa-key orange', '1', '1'), ('37', '按钮权限', 'buttonrights/list.do', '2', '2', 'menu-icon fa fa-key green', '1', '1'), ('38', '菜单管理', 'menu/listAllMenu.do', '1', '3', 'menu-icon fa fa-folder-open-o brown', '1', '1'), ('39', '按钮管理', 'fhbutton/list.do', '1', '2', 'menu-icon fa fa-download orange', '1', '1'), ('40', '用户管理', '#', '0', '2', 'menu-icon fa fa-users blue', '2', '1'), ('41', '系统用户', 'user/listUsers.do', '40', '1', 'menu-icon fa fa-users green', '1', '1'), ('42', '会员管理', 'happuser/listUsers.do', '40', '2', 'menu-icon fa fa-users orange', '1', '1'), ('43', '数据字典', 'dictionaries/listAllDict.do?DICTIONARIES_ID=0', '1', '4', 'menu-icon fa fa-book purple', '1', '1'), ('44', '代码生成器', 'createCode/list.do', '9', '0', 'menu-icon fa fa-cogs brown', '1', '1'), ('45', '七级菜单1', '#', '33', '1', 'menu-icon fa fa-leaf black', '1', '1'), ('46', '七级菜单2', '#', '33', '2', 'menu-icon fa fa-leaf black', '1', '1'), ('47', '八级菜单', 'login_default.do', '45', '1', 'menu-icon fa fa-leaf black', '1', '1'), ('48', '图表报表', ' tool/fusionchartsdemo.do', '9', '5', 'menu-icon fa fa-bar-chart-o black', '1', '1'), ('50', '站内信', 'fhsms/list.do', '6', '2', 'menu-icon fa fa-envelope green', '1', '1'), ('51', '图片列表', 'pictures/list.do', '7', '1', 'menu-icon fa fa-folder-open-o green', '1', '1'), ('52', '图片爬虫', 'pictures/goImageCrawler.do', '7', '2', 'menu-icon fa fa-cloud-download green', '1', '1'), ('53', '表单构建器', 'tool/goFormbuilder.do', '9', '1', 'menu-icon fa fa-leaf black', '1', '1'), ('54', '数据库管理', '#', '0', '9', 'menu-icon fa fa-hdd-o blue', '2', '1'), ('55', '数据库备份', 'brdb/listAllTable.do', '54', '1', 'menu-icon fa fa-cloud-upload blue', '1', '1'), ('56', '数据库还原', 'brdb/list.do', '54', '3', 'menu-icon fa fa-cloud-download blue', '1', '1'), ('57', '备份定时器', 'timingbackup/list.do', '54', '2', 'menu-icon fa fa-tachometer blue', '1', '1'), ('58', 'SQL编辑器', 'sqledit/view.do', '54', '4', 'menu-icon fa fa-pencil-square-o blue', '1', '1'), ('59', 'OA办公', '#', '0', '6', 'menu-icon fa fa-laptop pink', '2', '1'), ('60', '组织机构', 'department/listAllDepartment.do?DEPARTMENT_ID=0', '59', '1', 'menu-icon fa fa-users green', '1', '1');
COMMIT;

-- ----------------------------
--  Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `ROLE_ID` varchar(100) NOT NULL,
  `ROLE_NAME` varchar(100) DEFAULT NULL,
  `RIGHTS` varchar(255) DEFAULT NULL,
  `PARENT_ID` varchar(100) DEFAULT NULL,
  `ADD_QX` varchar(255) DEFAULT NULL,
  `DEL_QX` varchar(255) DEFAULT NULL,
  `EDIT_QX` varchar(255) DEFAULT NULL,
  `CHA_QX` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES ('0f03a6c267a74b38b005f6b059ad75f2', 'test', '4565568595292120408006', '1', '0', '0', '0', '0'), ('1', '系统管理组', '4565568595292120408006', '0', '1', '1', '1', '1'), ('115b386ff04f4352b060dffcd2b5d1da', '中级会员', '498', '2', '0', '0', '0', '0'), ('1b67fc82ce89457a8347ae53e43a347e', '初级会员', '498', '2', '0', '0', '0', '0'), ('2', '会员组', '498', '0', '0', '0', '0', '1'), ('3264c8e83d0248bb9e3ea6195b4c0216', '一级管理员', '4565568595292120408006', '1', '2244102192095174', '2251798773489606', '1125898866646982', '560135202614009798'), ('46294b31a71c4600801724a6eb06bb26', '职位组', '', '0', '0', '0', '0', '0'), ('5466347ac07044cb8d82990ec7f3a90e', '主管', '', '46294b31a71c4600801724a6eb06bb26', '0', '0', '0', '0'), ('68f8e4a39efe47c7bb869e9d15ab925d', '二级管理员', '4565568595292120408006', '1', '0', '0', '2251798773489606', '0'), ('856849f422774ad390a4e564054d8cc8', '经理', '', '46294b31a71c4600801724a6eb06bb26', '0', '0', '0', '0'), ('8b70a7e67f2841e7aaba8a4d92e5ff6f', '高级会员', '498', '2', '0', '0', '0', '0'), ('c21cecf84048434b93383182b1d98cba', '组长', '', '46294b31a71c4600801724a6eb06bb26', '0', '0', '0', '0'), ('d449195cd8e7491080688c58e11452eb', '总监', '', '46294b31a71c4600801724a6eb06bb26', '0', '0', '0', '0'), ('de9de2f006e145a29d52dfadda295353', '三级管理员', '4565568595292120408006', '1', '0', '0', '0', '0');
COMMIT;

-- ----------------------------
--  Table structure for `sys_role_fhbutton`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_fhbutton`;
CREATE TABLE `sys_role_fhbutton` (
  `RB_ID` varchar(100) NOT NULL,
  `ROLE_ID` varchar(100) DEFAULT NULL,
  `BUTTON_ID` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`RB_ID`),
  KEY `角色表外键` (`ROLE_ID`) USING BTREE,
  KEY `fbutton` (`BUTTON_ID`),
  CONSTRAINT `fbutton` FOREIGN KEY (`BUTTON_ID`) REFERENCES `sys_fhbutton` (`FHBUTTON_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `frole` FOREIGN KEY (`ROLE_ID`) REFERENCES `sys_role` (`ROLE_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_role_fhbutton`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_fhbutton` VALUES ('14b5c28ea6ae4508b57d2d272ab3d5f1', '3264c8e83d0248bb9e3ea6195b4c0216', '46992ea280ba4b72b29dedb0d4bc0106'), ('1743733f366240c693c4295b527d1b0e', 'de9de2f006e145a29d52dfadda295353', '4efa162fce8340f0bd2dcd3b11d327ec'), ('3768e60edd1c4b5c9f1dd861188ae2f9', '3264c8e83d0248bb9e3ea6195b4c0216', 'cc51b694d5344d28a9aa13c84b7166cd'), ('8231c216fb514b4188e4162e629c6237', '3264c8e83d0248bb9e3ea6195b4c0216', '4efa162fce8340f0bd2dcd3b11d327ec'), ('9412d1d05162464c83658c7f89ab03f0', '68f8e4a39efe47c7bb869e9d15ab925d', '3542adfbda73410c976e185ffe50ad06'), ('96567633dd3548c9b75d28f430adf5a3', '3264c8e83d0248bb9e3ea6195b4c0216', 'da7fd386de0b49ce809984f5919022b8'), ('a1478f27c852459fa9cad04b642f4fb7', 'de9de2f006e145a29d52dfadda295353', '3542adfbda73410c976e185ffe50ad06'), ('ba6696b8761044618e44c7e02c9ba89e', '68f8e4a39efe47c7bb869e9d15ab925d', 'cc51b694d5344d28a9aa13c84b7166cd'), ('f0329033d0914faf8ea6e9ff252cc5e6', '68f8e4a39efe47c7bb869e9d15ab925d', '46992ea280ba4b72b29dedb0d4bc0106'), ('f627982cc9d4479dbc03af726dc6ac58', 'de9de2f006e145a29d52dfadda295353', 'cc51b694d5344d28a9aa13c84b7166cd');
COMMIT;

-- ----------------------------
--  Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `USER_ID` varchar(100) NOT NULL,
  `USERNAME` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `RIGHTS` varchar(255) DEFAULT NULL,
  `ROLE_ID` varchar(100) DEFAULT NULL,
  `LAST_LOGIN` varchar(255) DEFAULT NULL,
  `IP` varchar(100) DEFAULT NULL,
  `STATUS` varchar(32) DEFAULT NULL,
  `BZ` varchar(255) DEFAULT NULL,
  `SKIN` varchar(100) DEFAULT NULL,
  `EMAIL` varchar(32) DEFAULT NULL,
  `NUMBER` varchar(100) DEFAULT NULL,
  `PHONE` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sys_user`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES ('1', 'admin', 'de41b7fb99201d8334c23c014db35ecd92df81bc', 'DOMO', '1133671055321055258374707980945218933803269864762743594642571294', '1', '2016-07-11 15:36:00', '0:0:0:0:0:0:0:1', '0', 'admin', 'default', 'DOMO@DOMO.com', '001', '18788888888'), ('69177258a06e4927b4639ab1684c3320', 'san', '47c4a8dc64ac2f0bb46bbd8813b037c9718f9349', '三', '', '3264c8e83d0248bb9e3ea6195b4c0216', '2016-04-07 21:14:09', '127.0.0.1', '0', '111', 'default', '978336446@qq.com', '333', '13562202556'), ('9991f4d7782a4ccfb8a65bd96ea7aafa', 'lisi', '2612ade71c1e48cd7150b5f4df152faa699cedfe', '李四', '', '3264c8e83d0248bb9e3ea6195b4c0216', '2016-03-21 17:41:57', '127.0.0.1', '0', '小李', 'default', '313596790@qq.com', '1102', '13566233663'), ('e29149962e944589bb7da23ad18ddeed', 'zhangsan', 'c2da1419caf053885c492e10ebde421581cdc03f', '张三', '', '3264c8e83d0248bb9e3ea6195b4c0216', '', '', '0', '小张', 'default', 'zhangsan@www.com', '1101', '2147483647');
COMMIT;

-- ----------------------------
--  Table structure for `tb_pictures`
-- ----------------------------
DROP TABLE IF EXISTS `tb_pictures`;
CREATE TABLE `tb_pictures` (
  `PICTURES_ID` varchar(100) NOT NULL,
  `TITLE` varchar(255) DEFAULT NULL COMMENT '标题',
  `NAME` varchar(255) DEFAULT NULL COMMENT '文件名',
  `PATH` varchar(255) DEFAULT NULL COMMENT '路径',
  `CREATETIME` varchar(100) DEFAULT NULL COMMENT '创建时间',
  `MASTER_ID` varchar(255) DEFAULT NULL COMMENT '属于',
  `BZ` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`PICTURES_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `tb_pictures`
-- ----------------------------
BEGIN;
INSERT INTO `tb_pictures` VALUES ('1336df029a6640979152f9a985ec0ef7', '图片', '5d59ad69e3694392933e6f8d4d333707.jpg', '20160417/5d59ad69e3694392933e6f8d4d333707.jpg', '2016-04-17 17:49:08', '1', '大墨图片上传'), ('2126241ab5f247f097bfbbef15a68d23', '图片', 'c60b36179c134c53b47b14e0eaa2a02c.png', '20160416/c60b36179c134c53b47b14e0eaa2a02c.png', '2016-04-16 23:05:10', '1', '大墨图片上传'), ('2313970d41b140f0ba43154f677818f8', '图片', '835c9808c6c1487ba4f8a6dd0b1f3be1.png', '20160416/835c9808c6c1487ba4f8a6dd0b1f3be1.png', '2016-04-16 16:01:52', '1', '大墨图片上传'), ('30294da5975848dea107b44669def8dd', '图片', '62d60fece31c4409b93bb3f316b20eeb.jpg', '20160416/62d60fece31c4409b93bb3f316b20eeb.jpg', '2016-04-16 22:37:58', '1', '大墨图片上传'), ('3c5aa7c2d8ec4492a50ed76103097e90', '图片', 'f506bd0c82fa4de783186cfef84cee5b.png', '20160416/f506bd0c82fa4de783186cfef84cee5b.png', '2016-04-16 16:50:41', '1', '大墨图片上传'), ('4b6f67d4f0e54180a8b1af9d6eb2c4cc', '图片', 'd66863617063476784cc2483dfcbaefe.png', '20160417/d66863617063476784cc2483dfcbaefe.png', '2016-04-17 10:34:50', '1', '大墨图片上传'), ('55ee97c13d814b4db85baaa6a3e2e961', '图片', '5714274838b5409d9c71fb98a8b3fdce.png', '20160416/5714274838b5409d9c71fb98a8b3fdce.png', '2016-04-16 17:06:08', '1', '大墨图片上传'), ('7c088901e8284f0e8a8d6c85a6db8350', '图片', 'ace6e251461f4becbf4e3075d0d3d9e1.jpg', '20160416/ace6e251461f4becbf4e3075d0d3d9e1.jpg', '2016-04-16 16:51:33', '1', '大墨图片上传'), ('80918403ef7b46da8e526526740a6a54', '图片', 'cf3cd0338a5f4d63ae81e218fdb80242.jpg', '20160418/cf3cd0338a5f4d63ae81e218fdb80242.jpg', '2016-04-18 09:43:44', '1', '大墨图片上传'), ('8834ae01cbb642e9949612bc8101b809', '图片', '79c4f369ffd0412bba21865adea374ae.png', '20160416/79c4f369ffd0412bba21865adea374ae.png', '2016-04-16 22:01:54', '1', '大墨图片上传'), ('90b086ddb7ee4e849752312ba3ce588b', '图片', '6c9bbd2fbbdf4eea80295124a6ffaeab.jpg', '20160416/6c9bbd2fbbdf4eea80295124a6ffaeab.jpg', '2016-04-16 23:04:36', '1', '大墨图片上传'), ('b1003057342c422c831e3846c72c6c12', '图片', '84e0457e44fc41a3b392784d3b6eb8d7.jpg', '20160417/84e0457e44fc41a3b392784d3b6eb8d7.jpg', '2016-04-17 10:55:40', '1', '大墨图片上传'), ('b1a85d1632af4282b16dba1b29227468', '图片', '77d1b0bc470c4b6e900c6667cbca7c8d.png', '20160416/77d1b0bc470c4b6e900c6667cbca7c8d.png', '2016-04-16 16:10:46', '1', '大墨图片上传'), ('b7ff267a107c45baa5f21cf59871db73', '图片', '129cd20aaeca4aa3b1c081d98ad280c9.png', '20160416/129cd20aaeca4aa3b1c081d98ad280c9.png', '2016-04-16 23:33:13', '1', '大墨图片上传'), ('c34002ef68ff4a64b471bc5273186e1b', '图片', 'd1048fd955834ab0a0423c08fc0edfc7.png', '20160416/d1048fd955834ab0a0423c08fc0edfc7.png', '2016-04-16 17:06:24', '1', '大墨图片上传'), ('e89c04a4b5e1480697e5a2b3e0fd182f', '图片', 'a1e5361f0489485a9cbc4fa5cf6db292.png', '20160416/a1e5361f0489485a9cbc4fa5cf6db292.png', '2016-04-16 15:50:46', '1', '图片管理处上传'), ('e8e232ff00734c04be33b70b5166ae8d', '图片', '54812f8d27b1444d91b38451eefa8cdf.png', '20160416/54812f8d27b1444d91b38451eefa8cdf.png', '2016-04-16 16:43:44', '1', '大墨图片上传'), ('f5a4a29a08bb43478e07e486211d0b37', '图片', 'fd46e28c857a43e0b48db2e0e5c4a47e.png', '20160416/fd46e28c857a43e0b48db2e0e5c4a47e.png', '2016-04-16 22:37:00', '1', '大墨图片上传'), ('f840f027fbd3409c8bb5f55e09ae0d15', '图片', '7ba28b54baaf4eb29b2594b90353389b.png', '20160417/7ba28b54baaf4eb29b2594b90353389b.png', '2016-04-17 08:53:34', '1', '大墨图片上传');
COMMIT;

-- ----------------------------
--  Table structure for `weixin_command`
-- ----------------------------
DROP TABLE IF EXISTS `weixin_command`;
CREATE TABLE `weixin_command` (
  `COMMAND_ID` varchar(100) NOT NULL,
  `KEYWORD` varchar(255) DEFAULT NULL COMMENT '关键词',
  `COMMANDCODE` varchar(255) DEFAULT NULL COMMENT '应用路径',
  `CREATETIME` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `STATUS` int(1) NOT NULL COMMENT '状态',
  `BZ` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`COMMAND_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `weixin_imgmsg`
-- ----------------------------
DROP TABLE IF EXISTS `weixin_imgmsg`;
CREATE TABLE `weixin_imgmsg` (
  `IMGMSG_ID` varchar(100) NOT NULL,
  `KEYWORD` varchar(255) DEFAULT NULL COMMENT '关键词',
  `CREATETIME` varchar(100) DEFAULT NULL COMMENT '创建时间',
  `STATUS` int(11) NOT NULL COMMENT '状态',
  `BZ` varchar(255) DEFAULT NULL COMMENT '备注',
  `TITLE1` varchar(255) DEFAULT NULL COMMENT '标题1',
  `DESCRIPTION1` varchar(255) DEFAULT NULL COMMENT '描述1',
  `IMGURL1` varchar(255) DEFAULT NULL COMMENT '图片地址1',
  `TOURL1` varchar(255) DEFAULT NULL COMMENT '超链接1',
  `TITLE2` varchar(255) DEFAULT NULL COMMENT '标题2',
  `DESCRIPTION2` varchar(255) DEFAULT NULL COMMENT '描述2',
  `IMGURL2` varchar(255) DEFAULT NULL COMMENT '图片地址2',
  `TOURL2` varchar(255) DEFAULT NULL COMMENT '超链接2',
  `TITLE3` varchar(255) DEFAULT NULL COMMENT '标题3',
  `DESCRIPTION3` varchar(255) DEFAULT NULL COMMENT '描述3',
  `IMGURL3` varchar(255) DEFAULT NULL COMMENT '图片地址3',
  `TOURL3` varchar(255) DEFAULT NULL COMMENT '超链接3',
  `TITLE4` varchar(255) DEFAULT NULL COMMENT '标题4',
  `DESCRIPTION4` varchar(255) DEFAULT NULL COMMENT '描述4',
  `IMGURL4` varchar(255) DEFAULT NULL COMMENT '图片地址4',
  `TOURL4` varchar(255) DEFAULT NULL COMMENT '超链接4',
  `TITLE5` varchar(255) DEFAULT NULL COMMENT '标题5',
  `DESCRIPTION5` varchar(255) DEFAULT NULL COMMENT '描述5',
  `IMGURL5` varchar(255) DEFAULT NULL COMMENT '图片地址5',
  `TOURL5` varchar(255) DEFAULT NULL COMMENT '超链接5',
  `TITLE6` varchar(255) DEFAULT NULL COMMENT '标题6',
  `DESCRIPTION6` varchar(255) DEFAULT NULL COMMENT '描述6',
  `IMGURL6` varchar(255) DEFAULT NULL COMMENT '图片地址6',
  `TOURL6` varchar(255) DEFAULT NULL COMMENT '超链接6',
  `TITLE7` varchar(255) DEFAULT NULL COMMENT '标题7',
  `DESCRIPTION7` varchar(255) DEFAULT NULL COMMENT '描述7',
  `IMGURL7` varchar(255) DEFAULT NULL COMMENT '图片地址7',
  `TOURL7` varchar(255) DEFAULT NULL COMMENT '超链接7',
  `TITLE8` varchar(255) DEFAULT NULL COMMENT '标题8',
  `DESCRIPTION8` varchar(255) DEFAULT NULL COMMENT '描述8',
  `IMGURL8` varchar(255) DEFAULT NULL COMMENT '图片地址8',
  `TOURL8` varchar(255) DEFAULT NULL COMMENT '超链接8',
  PRIMARY KEY (`IMGMSG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `weixin_imgmsg`
-- ----------------------------
BEGIN;
INSERT INTO `weixin_imgmsg` VALUES ('380b2cb1f4954315b0e20618f7b5bd8f', '首页', '2015-05-10 20:51:09', '1', '图文回复', '图文回复标题', '图文回复描述', 'http://a.hiphotos.baidu.com/image/h%3D360/sign=c6c7e73ebc389b5027ffe654b535e5f1/a686c9177f3e6709392bb8df3ec79f3df8dc55e3.jpg', 'www.baidu.com', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '');
COMMIT;

-- ----------------------------
--  Table structure for `weixin_textmsg`
-- ----------------------------
DROP TABLE IF EXISTS `weixin_textmsg`;
CREATE TABLE `weixin_textmsg` (
  `TEXTMSG_ID` varchar(100) NOT NULL,
  `KEYWORD` varchar(255) DEFAULT NULL COMMENT '关键词',
  `CONTENT` varchar(255) DEFAULT NULL COMMENT '内容',
  `CREATETIME` varchar(100) DEFAULT NULL COMMENT '创建时间',
  `STATUS` int(2) DEFAULT NULL COMMENT '状态',
  `BZ` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`TEXTMSG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `weixin_textmsg`
-- ----------------------------
BEGIN;
INSERT INTO `weixin_textmsg` VALUES ('695cd74779734231928a253107ab0eeb', '吃饭', '吃了噢噢噢噢', '2015-05-10 22:52:27', '1', '文本回复'), ('d4738af7aea74a6ca1a5fb25a98f9acb', '关注', '这里是关注后回复的内容', '2015-05-11 02:12:36', '1', '关注回复');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
