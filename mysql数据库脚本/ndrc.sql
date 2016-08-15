/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50550
Source Host           : localhost:3306
Source Database       : ndrc

Target Server Type    : MYSQL
Target Server Version : 50550
File Encoding         : 65001

Date: 2016-07-14 10:41:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for db_fhdb
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
-- Records of db_fhdb
-- ----------------------------

-- ----------------------------
-- Table structure for db_timingbackup
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
-- Records of db_timingbackup
-- ----------------------------
INSERT INTO `db_timingbackup` VALUES ('311e06c34a5e4518a86d5d30799f9b55', 'sys_app_user_515762', '2016-04-11 17:04:55', 'sys_app_user', '2', '1/2 * * ? * *', '每个月的 每周 每天 每小时执行一次', '备份任务');

-- ----------------------------
-- Table structure for ndrc_worknews
-- ----------------------------
DROP TABLE IF EXISTS `ndrc_worknews`;
CREATE TABLE `ndrc_worknews` (
  `WORKNEWS_ID` varchar(100) NOT NULL,
  `TITLE` varchar(255) DEFAULT NULL COMMENT '标题',
  `NEWSDATE` varchar(32) DEFAULT NULL COMMENT '日期',
  `CATEGORY` varchar(255) DEFAULT NULL COMMENT '类别',
  `CONTENT` varchar(255) DEFAULT NULL COMMENT '新闻内容',
  `PIC` varchar(255) DEFAULT NULL COMMENT '图片',
  PRIMARY KEY (`WORKNEWS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ndrc_worknews
-- ----------------------------
INSERT INTO `ndrc_worknews` VALUES ('60444a6beef54cf8b6b207ec6a9b566a', 'test1', '2016-07-13', '新闻', '123', '20160713/0c6c3cec6b6f4a7b876b16525cb2986d.png');
INSERT INTO `ndrc_worknews` VALUES ('664e8fb995a64a888f820a7f32560be8', 'test2', '2016-07-13', '事实', '心理科来看看简单来1222222222222', '20160713/65b42842781141bfa7c537f28e4866ef.PNG');

-- ----------------------------
-- Table structure for oa_department
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
-- Records of oa_department
-- ----------------------------

-- ----------------------------
-- Table structure for sys_app_user
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
-- Records of sys_app_user
-- ----------------------------
INSERT INTO `sys_app_user` VALUES ('1e89e6504be349a68c025976b3ecc1d1', 'a1', '698d51a19d8a121ce581499d7b701668', '会员甲', '', '115b386ff04f4352b060dffcd2b5d1da', '', '', '1', '121', '1212', '1212', '2015-12-02', '2015-12-25', '2', '111', '313596790@qq.com');
INSERT INTO `sys_app_user` VALUES ('ead1f56708e4409c8d071e0a699e5633', 'a2', 'bcbe3365e6ac95ea2c0343a2395834dd', '会员乙', '', '1b67fc82ce89457a8347ae53e43a347e', '', '', '0', '', '', '', '2015-12-01', '2015-12-24', '1', '121', '978336446@qq.com');

-- ----------------------------
-- Table structure for sys_createcode
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
-- Records of sys_createcode
-- ----------------------------
INSERT INTO `sys_createcode` VALUES ('002ea762e3e242a7a10ea5ca633701d8', 'system', 'Buttonrights', 'sys_,fh,BUTTONRIGHTS', 'NAME,fh,String,fh,名称,fh,是,fh,无,fh,255Q313596790', '2016-01-16 23:20:36', '按钮权限');
INSERT INTO `sys_createcode` VALUES ('1e4862c9ca454e3dba7c08caeaec652e', 'ndrc', 'WorkNews', 'NDRC_,fh,WORKNEWS', 'TITLE,fh,String,fh,标题,fh,是,fh,无,fh,255Q313596790NEWSDATE,fh,Date,fh,日期,fh,是,fh,无,fh,32Q313596790CATEGORY,fh,String,fh,类别,fh,是,fh,无,fh,255Q313596790CONTENT,fh,String,fh,新闻内容,fh,是,fh,无,fh,255Q313596790PIC,fh,File,fh,图片,fh,是,fh,无,fh,255Q313596790', '2016-07-13 15:43:12', '工作动态');
INSERT INTO `sys_createcode` VALUES ('49d985e081ed44e6b34ba1b8c5466e39', 'fhdb', 'TimingBackUp', 'DB_,fh,TIMINGBACKUP', 'JOBNAME,fh,String,fh,任务名称,fh,否,fh,无,fh,50Q313596790CREATE_TIME,fh,Date,fh,创建时间,fh,否,fh,无,fh,32Q313596790TABLENAME,fh,String,fh,表名,fh,是,fh,无,fh,50Q313596790TYPE,fh,Integer,fh,类型,fh,否,fh,无,fh,1Q313596790FHTIME,fh,String,fh,时间规则,fh,是,fh,无,fh,30Q313596790TIMEEXPLAIN,fh,String,fh,规则说明,fh,是,fh,无,fh,100Q313596790BZ,fh,String,fh,备注,fh,是,fh,无,fh,255Q313596790', '2016-04-09 11:53:38', '定时备份');
INSERT INTO `sys_createcode` VALUES ('bf35ab8b2d064bf7928a04bba5e5a6dd', 'system', 'Fhsms', 'SYS_,fh,FHSMS', 'CONTENT,fh,String,fh,内容,fh,是,fh,无,fh,1000Q313596790TYPE,fh,String,fh,类型,fh,否,fh,无,fh,5Q313596790TO_USERNAME,fh,String,fh,收信人,fh,是,fh,无,fh,255Q313596790FROM_USERNAME,fh,String,fh,发信人,fh,是,fh,无,fh,255Q313596790SEND_TIME,fh,String,fh,发信时间,fh,是,fh,无,fh,100Q313596790STATUS,fh,String,fh,状态,fh,否,fh,无,fh,5Q313596790SANME_ID,fh,String,fh,共同ID,fh,是,fh,无,fh,100Q313596790', '2016-03-27 21:39:45', '站内信');
INSERT INTO `sys_createcode` VALUES ('c7586f931fd44c61beccd3248774c68c', 'system', 'Department', 'SYS_,fh,DEPARTMENT', 'NAME,fh,String,fh,名称,fh,是,fh,无,fh,30Q313596790NAME_EN,fh,String,fh,英文,fh,是,fh,无,fh,50Q313596790BIANMA,fh,String,fh,编码,fh,是,fh,无,fh,50Q313596790PARENT_ID,fh,String,fh,上级ID,fh,否,fh,无,fh,100Q313596790BZ,fh,String,fh,备注,fh,是,fh,无,fh,255Q313596790HEADMAN,fh,String,fh,负责人,fh,是,fh,无,fh,30Q313596790TEL,fh,String,fh,电话,fh,是,fh,无,fh,50Q313596790FUNCTIONS,fh,String,fh,部门职能,fh,是,fh,无,fh,255Q313596790ADDRESS,fh,String,fh,地址,fh,是,fh,无,fh,255Q313596790', '2015-12-20 01:49:25', '组织机构');
INSERT INTO `sys_createcode` VALUES ('c937e21208914e5b8fb1202c685bbf2f', 'fhdb', 'Fhdb', 'DB_,fh,FHDB', 'USERNAME,fh,String,fh,操作用户,fh,否,fh,无,fh,50Q313596790BACKUP_TIME,fh,Date,fh,备份时间,fh,否,fh,无,fh,32Q313596790TABLENAME,fh,String,fh,表名,fh,是,fh,无,fh,50Q313596790SQLPATH,fh,String,fh,存储位置,fh,否,fh,无,fh,300Q313596790TYPE,fh,Integer,fh,类型,fh,是,fh,无,fh,1Q313596790DBSIZE,fh,String,fh,文件大小,fh,否,fh,无,fh,10Q313596790BZ,fh,String,fh,备注,fh,否,fh,无,fh,255Q313596790', '2016-03-30 13:46:54', '数据库管理');
INSERT INTO `sys_createcode` VALUES ('d514dbd2474d4b6c8b6ab9904cc9cc7c', 'new', 'News', 'TB_,fh,NEWS', 'TITLE,fh,String,fh,标题,fh,是,fh,无,fh,255Q313596790', '2016-01-25 16:38:14', '新闻管理');
INSERT INTO `sys_createcode` VALUES ('dbd7b8330d774dcabd184eca8668a295', 'system', 'Fhsms', 'SYS_,fh,FHSMS', 'CONTENT,fh,String,fh,内容,fh,是,fh,无,fh,1000Q313596790TYPE,fh,String,fh,类型,fh,否,fh,无,fh,5Q313596790TO_USERNAME,fh,String,fh,收信人,fh,是,fh,无,fh,255Q313596790FROM_USERNAME,fh,String,fh,发信人,fh,是,fh,无,fh,255Q313596790SEND_TIME,fh,String,fh,发信时间,fh,是,fh,无,fh,100Q313596790STATUS,fh,String,fh,状态,fh,否,fh,无,fh,5Q313596790SANME_ID,fh,String,fh,共同ID,fh,是,fh,无,fh,100Q313596790', '2016-01-23 01:44:15', '站内信');
INSERT INTO `sys_createcode` VALUES ('fe239f8742194481a5b56f90cad71520', 'system', 'Fhbutton', 'SYS_,fh,FHBUTTON', 'NAME,fh,String,fh,名称,fh,是,fh,无,fh,30Q313596790QX_NAME,fh,String,fh,权限标识,fh,是,fh,无,fh,50Q313596790BZ,fh,String,fh,备注,fh,是,fh,无,fh,255Q313596790', '2016-01-15 18:38:40', '按钮管理');

-- ----------------------------
-- Table structure for sys_dictionaries
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
-- Records of sys_dictionaries
-- ----------------------------

-- ----------------------------
-- Table structure for sys_fhbutton
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
-- Records of sys_fhbutton
-- ----------------------------
INSERT INTO `sys_fhbutton` VALUES ('3542adfbda73410c976e185ffe50ad06', '导出EXCEL', 'toExcel', '导出EXCEL');
INSERT INTO `sys_fhbutton` VALUES ('46992ea280ba4b72b29dedb0d4bc0106', '发邮件', 'email', '发送电子邮件');
INSERT INTO `sys_fhbutton` VALUES ('4efa162fce8340f0bd2dcd3b11d327ec', '导入EXCEL', 'FromExcel', '导入EXCEL到系统用户');
INSERT INTO `sys_fhbutton` VALUES ('cc51b694d5344d28a9aa13c84b7166cd', '发短信', 'sms', '发送短信');
INSERT INTO `sys_fhbutton` VALUES ('da7fd386de0b49ce809984f5919022b8', '站内信', 'FHSMS', '发送站内信');

-- ----------------------------
-- Table structure for sys_fhsms
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
-- Records of sys_fhsms
-- ----------------------------
INSERT INTO `sys_fhsms` VALUES ('05879f5868824f35932ee9f2062adc03', '你好', '2', 'admin', 'san', '2016-01-25 14:05:31', '1', 'b311e893228f42d5a05dbe16917fd16f');
INSERT INTO `sys_fhsms` VALUES ('060aaea8c1874dc88a694dc67ffbcf0a', 'test', '2', 'san', 'admin', '2016-04-15 21:33:00', '2', '052dd87714294fb5a7e0de5a9999e158');
INSERT INTO `sys_fhsms` VALUES ('2635dd035c6f4bb5a091abdd784bd899', '你好', '2', 'san', 'admin', '2016-01-25 14:05:02', '2', '1b7637306683460f89174c2b025862b5');
INSERT INTO `sys_fhsms` VALUES ('4ef2f5d53db041c4a487eb20ac1f2520', 'test', '2', 'san', 'admin', '2016-04-15 21:32:20', '2', '0cb2a9d88ebf4bd0a39002d63d951356');
INSERT INTO `sys_fhsms` VALUES ('52378ccd4e2d4fe08994d1652af87c68', '你好', '1', 'admin', 'san', '2016-01-25 16:26:44', '1', '920b20dafdfb4c09b560884eb277c51d');
INSERT INTO `sys_fhsms` VALUES ('77ed13f9c49a4c4bb460c41b8580dd36', 'gggg', '2', 'admin', 'san', '2016-01-24 21:22:43', '2', 'dd9ee339576e48c5b046b94fa1901d00');
INSERT INTO `sys_fhsms` VALUES ('8c5fecbc82cd4605b4a412cf43342821', 'test', '1', 'admin', 'san', '2016-04-15 21:33:00', '2', '052dd87714294fb5a7e0de5a9999e158');
INSERT INTO `sys_fhsms` VALUES ('98a6869f942042a1a037d9d9f01cb50f', '你好', '1', 'admin', 'san', '2016-01-25 14:05:02', '2', '1b7637306683460f89174c2b025862b5');
INSERT INTO `sys_fhsms` VALUES ('9e00295529014b6e8a27019cbccb3da1', '柔柔弱弱', '1', 'admin', 'san', '2016-01-24 21:22:57', '1', 'a29603d613ea4e54b5678033c1bf70a6');
INSERT INTO `sys_fhsms` VALUES ('d29787433a034cfba7d6d57db7331156', 'test', '1', 'admin', 'san', '2016-04-15 21:32:20', '2', '0cb2a9d88ebf4bd0a39002d63d951356');
INSERT INTO `sys_fhsms` VALUES ('d3aedeb430f640359bff86cd657a8f59', '你好', '1', 'admin', 'san', '2016-01-24 21:22:12', '1', 'f022fbdce3d845aba927edb698beb90b');
INSERT INTO `sys_fhsms` VALUES ('e5376b1bd54b489cb7f2203632bd74ec', '管理员好', '2', 'admin', 'san', '2016-01-25 14:06:13', '2', 'b347b2034faf43c79b54be4627f3bd2b');
INSERT INTO `sys_fhsms` VALUES ('e613ac0fcc454f32895a70b747bf4fb5', '你也好', '2', 'admin', 'san', '2016-01-25 16:27:54', '2', 'ce8dc3b15afb40f28090f8b8e13f078d');
INSERT INTO `sys_fhsms` VALUES ('f25e00cfafe741a3a05e3839b66dc7aa', '你好', '2', 'san', 'admin', '2016-01-25 16:26:44', '1', '920b20dafdfb4c09b560884eb277c51d');

-- ----------------------------
-- Table structure for sys_menu
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
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '系统管理', '#', '0', '1', 'menu-icon fa fa-desktop blue', '2', '1');
INSERT INTO `sys_menu` VALUES ('2', '权限管理', '#', '1', '1', 'menu-icon fa fa-lock black', '1', '1');
INSERT INTO `sys_menu` VALUES ('6', '信息管理', '#', '0', '5', 'menu-icon fa fa-credit-card green', '2', '1');
INSERT INTO `sys_menu` VALUES ('7', '图片管理', '#', '6', '1', 'menu-icon fa fa-folder-o pink', '2', '1');
INSERT INTO `sys_menu` VALUES ('8', '性能监控', 'druid/index.html', '9', '8', 'menu-icon fa fa-tachometer red', '1', '1');
INSERT INTO `sys_menu` VALUES ('9', '系统工具', '#', '0', '3', 'menu-icon fa fa-cog black', '2', '1');
INSERT INTO `sys_menu` VALUES ('10', '接口测试', 'tool/interfaceTest.do', '9', '2', 'menu-icon fa fa-exchange green', '1', '1');
INSERT INTO `sys_menu` VALUES ('11', '发送邮件', 'tool/goSendEmail.do', '9', '3', 'menu-icon fa fa-envelope-o green', '1', '1');
INSERT INTO `sys_menu` VALUES ('12', '置二维码', 'tool/goTwoDimensionCode.do', '9', '4', 'menu-icon fa fa-barcode green', '1', '1');
INSERT INTO `sys_menu` VALUES ('14', '地图工具', 'tool/map.do', '9', '6', 'menu-icon fa fa-globe black', '1', '1');
INSERT INTO `sys_menu` VALUES ('15', '微信管理', '#', '0', '4', 'menu-icon fa fa-comments purple', '2', '1');
INSERT INTO `sys_menu` VALUES ('16', '文本回复', 'textmsg/list.do', '15', '2', 'menu-icon fa fa-comment green', '2', '1');
INSERT INTO `sys_menu` VALUES ('17', '应用命令', 'command/list.do', '15', '4', 'menu-icon fa fa-comment grey', '2', '1');
INSERT INTO `sys_menu` VALUES ('18', '图文回复', 'imgmsg/list.do', '15', '3', 'menu-icon fa fa-comment pink', '2', '1');
INSERT INTO `sys_menu` VALUES ('19', '关注回复', 'textmsg/goSubscribe.do', '15', '1', 'menu-icon fa fa-comment orange', '2', '1');
INSERT INTO `sys_menu` VALUES ('20', '在线管理', 'onlinemanager/list.do', '1', '5', 'menu-icon fa fa-laptop green', '1', '1');
INSERT INTO `sys_menu` VALUES ('21', '打印测试', 'tool/printTest.do', '9', '7', 'menu-icon fa fa-hdd-o grey', '1', '1');
INSERT INTO `sys_menu` VALUES ('22', '一级菜单', '#', '0', '10', 'menu-icon fa fa-fire orange', '2', '1');
INSERT INTO `sys_menu` VALUES ('23', '二级菜单', '#', '22', '1', 'menu-icon fa fa-leaf black', '1', '1');
INSERT INTO `sys_menu` VALUES ('24', '三级菜单', '#', '23', '1', 'menu-icon fa fa-leaf black', '1', '1');
INSERT INTO `sys_menu` VALUES ('30', '四级菜单', '#', '24', '1', 'menu-icon fa fa-leaf black', '1', '1');
INSERT INTO `sys_menu` VALUES ('31', '五级菜单1', '#', '30', '1', 'menu-icon fa fa-leaf black', '1', '1');
INSERT INTO `sys_menu` VALUES ('32', '五级菜单2', '#', '30', '2', 'menu-icon fa fa-leaf black', '1', '1');
INSERT INTO `sys_menu` VALUES ('33', '六级菜单', '#', '31', '1', 'menu-icon fa fa-leaf black', '1', '1');
INSERT INTO `sys_menu` VALUES ('34', '六级菜单2', 'login_default.do', '31', '2', 'menu-icon fa fa-leaf black', '1', '1');
INSERT INTO `sys_menu` VALUES ('35', '四级菜单2', 'login_default.do', '24', '2', 'menu-icon fa fa-leaf black', '1', '1');
INSERT INTO `sys_menu` VALUES ('36', '角色(基础权限)', 'role.do', '2', '1', 'menu-icon fa fa-key orange', '1', '1');
INSERT INTO `sys_menu` VALUES ('37', '按钮权限', 'buttonrights/list.do', '2', '2', 'menu-icon fa fa-key green', '1', '1');
INSERT INTO `sys_menu` VALUES ('38', '菜单管理', 'menu/listAllMenu.do', '1', '3', 'menu-icon fa fa-folder-open-o brown', '1', '1');
INSERT INTO `sys_menu` VALUES ('39', '按钮管理', 'fhbutton/list.do', '1', '2', 'menu-icon fa fa-download orange', '1', '1');
INSERT INTO `sys_menu` VALUES ('40', '用户管理', '#', '0', '2', 'menu-icon fa fa-users blue', '2', '1');
INSERT INTO `sys_menu` VALUES ('41', '系统用户', 'user/listUsers.do', '40', '1', 'menu-icon fa fa-users green', '1', '1');
INSERT INTO `sys_menu` VALUES ('42', '会员管理', 'happuser/listUsers.do', '40', '2', 'menu-icon fa fa-users orange', '1', '1');
INSERT INTO `sys_menu` VALUES ('43', '数据字典', 'dictionaries/listAllDict.do?DICTIONARIES_ID=0', '1', '4', 'menu-icon fa fa-book purple', '1', '1');
INSERT INTO `sys_menu` VALUES ('44', '代码生成器', 'createCode/list.do', '9', '0', 'menu-icon fa fa-cogs brown', '1', '1');
INSERT INTO `sys_menu` VALUES ('45', '七级菜单1', '#', '33', '1', 'menu-icon fa fa-leaf black', '1', '1');
INSERT INTO `sys_menu` VALUES ('46', '七级菜单2', '#', '33', '2', 'menu-icon fa fa-leaf black', '1', '1');
INSERT INTO `sys_menu` VALUES ('47', '八级菜单', 'login_default.do', '45', '1', 'menu-icon fa fa-leaf black', '1', '1');
INSERT INTO `sys_menu` VALUES ('48', '图表报表', ' tool/fusionchartsdemo.do', '9', '5', 'menu-icon fa fa-bar-chart-o black', '1', '1');
INSERT INTO `sys_menu` VALUES ('50', '站内信', 'fhsms/list.do', '6', '2', 'menu-icon fa fa-envelope green', '1', '1');
INSERT INTO `sys_menu` VALUES ('51', '图片列表', 'pictures/list.do', '7', '1', 'menu-icon fa fa-folder-open-o green', '1', '1');
INSERT INTO `sys_menu` VALUES ('52', '图片爬虫', 'pictures/goImageCrawler.do', '7', '2', 'menu-icon fa fa-cloud-download green', '1', '1');
INSERT INTO `sys_menu` VALUES ('53', '表单构建器', 'tool/goFormbuilder.do', '9', '1', 'menu-icon fa fa-leaf black', '1', '1');
INSERT INTO `sys_menu` VALUES ('54', '数据库管理', '#', '0', '9', 'menu-icon fa fa-hdd-o blue', '2', '1');
INSERT INTO `sys_menu` VALUES ('55', '数据库备份', 'brdb/listAllTable.do', '54', '1', 'menu-icon fa fa-cloud-upload blue', '1', '1');
INSERT INTO `sys_menu` VALUES ('56', '数据库还原', 'brdb/list.do', '54', '3', 'menu-icon fa fa-cloud-download blue', '1', '1');
INSERT INTO `sys_menu` VALUES ('57', '备份定时器', 'timingbackup/list.do', '54', '2', 'menu-icon fa fa-tachometer blue', '1', '1');
INSERT INTO `sys_menu` VALUES ('58', 'SQL编辑器', 'sqledit/view.do', '54', '4', 'menu-icon fa fa-pencil-square-o blue', '1', '1');
INSERT INTO `sys_menu` VALUES ('59', 'OA办公', '#', '0', '6', 'menu-icon fa fa-laptop pink', '2', '1');
INSERT INTO `sys_menu` VALUES ('60', '组织机构', 'department/listAllDepartment.do?DEPARTMENT_ID=0', '59', '1', 'menu-icon fa fa-users green', '1', '1');
INSERT INTO `sys_menu` VALUES ('61', '公共模块', '#', '0', '1', 'menu-icon fa fa-leaf black', '2', '1');
INSERT INTO `sys_menu` VALUES ('62', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('63', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('64', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('65', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('66', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('67', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('68', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('69', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('70', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('71', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('72', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('73', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('74', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('75', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('76', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('77', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('78', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('79', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('80', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('81', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('82', '工作动态', 'worknews/list.do', '61', '1', 'menu-icon fa fa-leaf black', '1', '1');
INSERT INTO `sys_menu` VALUES ('83', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('84', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('85', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('86', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('87', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('88', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('89', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('90', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('91', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('92', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('93', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('94', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('95', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('96', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('97', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('98', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('99', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('100', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('101', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);
INSERT INTO `sys_menu` VALUES ('102', null, null, null, null, 'menu-icon fa fa-leaf black', null, null);

-- ----------------------------
-- Table structure for sys_role
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
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('0f03a6c267a74b38b005f6b059ad75f2', 'test', '4835707889581584132595654', '1', '0', '0', '0', '0');
INSERT INTO `sys_role` VALUES ('1', '系统管理组', '4835707889581584132595654', '0', '1', '1', '1', '1');
INSERT INTO `sys_role` VALUES ('115b386ff04f4352b060dffcd2b5d1da', '中级会员', '498', '2', '0', '0', '0', '0');
INSERT INTO `sys_role` VALUES ('1b67fc82ce89457a8347ae53e43a347e', '初级会员', '498', '2', '0', '0', '0', '0');
INSERT INTO `sys_role` VALUES ('2', '会员组', '498', '0', '0', '0', '0', '1');
INSERT INTO `sys_role` VALUES ('3264c8e83d0248bb9e3ea6195b4c0216', '一级管理员', '4835707889581584132595654', '1', '2244102192095174', '2251798773489606', '1125898866646982', '560135202614009798');
INSERT INTO `sys_role` VALUES ('46294b31a71c4600801724a6eb06bb26', '职位组', '', '0', '0', '0', '0', '0');
INSERT INTO `sys_role` VALUES ('5466347ac07044cb8d82990ec7f3a90e', '主管', '', '46294b31a71c4600801724a6eb06bb26', '0', '0', '0', '0');
INSERT INTO `sys_role` VALUES ('68f8e4a39efe47c7bb869e9d15ab925d', '二级管理员', '4835707889581584132595654', '1', '0', '0', '2251798773489606', '0');
INSERT INTO `sys_role` VALUES ('856849f422774ad390a4e564054d8cc8', '经理', '', '46294b31a71c4600801724a6eb06bb26', '0', '0', '0', '0');
INSERT INTO `sys_role` VALUES ('8b70a7e67f2841e7aaba8a4d92e5ff6f', '高级会员', '498', '2', '0', '0', '0', '0');
INSERT INTO `sys_role` VALUES ('c21cecf84048434b93383182b1d98cba', '组长', '', '46294b31a71c4600801724a6eb06bb26', '0', '0', '0', '0');
INSERT INTO `sys_role` VALUES ('d449195cd8e7491080688c58e11452eb', '总监', '', '46294b31a71c4600801724a6eb06bb26', '0', '0', '0', '0');
INSERT INTO `sys_role` VALUES ('de9de2f006e145a29d52dfadda295353', '三级管理员', '4835707889581584132595654', '1', '0', '0', '0', '0');

-- ----------------------------
-- Table structure for sys_role_fhbutton
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
-- Records of sys_role_fhbutton
-- ----------------------------
INSERT INTO `sys_role_fhbutton` VALUES ('14b5c28ea6ae4508b57d2d272ab3d5f1', '3264c8e83d0248bb9e3ea6195b4c0216', '46992ea280ba4b72b29dedb0d4bc0106');
INSERT INTO `sys_role_fhbutton` VALUES ('1743733f366240c693c4295b527d1b0e', 'de9de2f006e145a29d52dfadda295353', '4efa162fce8340f0bd2dcd3b11d327ec');
INSERT INTO `sys_role_fhbutton` VALUES ('3768e60edd1c4b5c9f1dd861188ae2f9', '3264c8e83d0248bb9e3ea6195b4c0216', 'cc51b694d5344d28a9aa13c84b7166cd');
INSERT INTO `sys_role_fhbutton` VALUES ('8231c216fb514b4188e4162e629c6237', '3264c8e83d0248bb9e3ea6195b4c0216', '4efa162fce8340f0bd2dcd3b11d327ec');
INSERT INTO `sys_role_fhbutton` VALUES ('9412d1d05162464c83658c7f89ab03f0', '68f8e4a39efe47c7bb869e9d15ab925d', '3542adfbda73410c976e185ffe50ad06');
INSERT INTO `sys_role_fhbutton` VALUES ('96567633dd3548c9b75d28f430adf5a3', '3264c8e83d0248bb9e3ea6195b4c0216', 'da7fd386de0b49ce809984f5919022b8');
INSERT INTO `sys_role_fhbutton` VALUES ('a1478f27c852459fa9cad04b642f4fb7', 'de9de2f006e145a29d52dfadda295353', '3542adfbda73410c976e185ffe50ad06');
INSERT INTO `sys_role_fhbutton` VALUES ('ba6696b8761044618e44c7e02c9ba89e', '68f8e4a39efe47c7bb869e9d15ab925d', 'cc51b694d5344d28a9aa13c84b7166cd');
INSERT INTO `sys_role_fhbutton` VALUES ('f0329033d0914faf8ea6e9ff252cc5e6', '68f8e4a39efe47c7bb869e9d15ab925d', '46992ea280ba4b72b29dedb0d4bc0106');
INSERT INTO `sys_role_fhbutton` VALUES ('f627982cc9d4479dbc03af726dc6ac58', 'de9de2f006e145a29d52dfadda295353', 'cc51b694d5344d28a9aa13c84b7166cd');

-- ----------------------------
-- Table structure for sys_user
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
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', 'de41b7fb99201d8334c23c014db35ecd92df81bc', 'DOMO', '1133671055321055258374707980945218933803269864762743594642571294', '1', '2016-07-14 10:36:52', '127.0.0.1', '0', 'admin', 'default', 'DOMO@DOMO.com', '001', '18788888888');
INSERT INTO `sys_user` VALUES ('69177258a06e4927b4639ab1684c3320', 'san', '47c4a8dc64ac2f0bb46bbd8813b037c9718f9349', '三', '', '3264c8e83d0248bb9e3ea6195b4c0216', '2016-04-07 21:14:09', '127.0.0.1', '0', '111', 'default', '978336446@qq.com', '333', '13562202556');
INSERT INTO `sys_user` VALUES ('9991f4d7782a4ccfb8a65bd96ea7aafa', 'lisi', '2612ade71c1e48cd7150b5f4df152faa699cedfe', '李四', '', '3264c8e83d0248bb9e3ea6195b4c0216', '2016-03-21 17:41:57', '127.0.0.1', '0', '小李', 'default', '313596790@qq.com', '1102', '13566233663');
INSERT INTO `sys_user` VALUES ('e29149962e944589bb7da23ad18ddeed', 'zhangsan', 'c2da1419caf053885c492e10ebde421581cdc03f', '张三', '', '3264c8e83d0248bb9e3ea6195b4c0216', '', '', '0', '小张', 'default', 'zhangsan@www.com', '1101', '2147483647');

-- ----------------------------
-- Table structure for tb_pictures
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
-- Records of tb_pictures
-- ----------------------------
INSERT INTO `tb_pictures` VALUES ('1336df029a6640979152f9a985ec0ef7', '图片', '5d59ad69e3694392933e6f8d4d333707.jpg', '20160417/5d59ad69e3694392933e6f8d4d333707.jpg', '2016-04-17 17:49:08', '1', '大墨图片上传');
INSERT INTO `tb_pictures` VALUES ('2126241ab5f247f097bfbbef15a68d23', '图片', 'c60b36179c134c53b47b14e0eaa2a02c.png', '20160416/c60b36179c134c53b47b14e0eaa2a02c.png', '2016-04-16 23:05:10', '1', '大墨图片上传');
INSERT INTO `tb_pictures` VALUES ('2313970d41b140f0ba43154f677818f8', '图片', '835c9808c6c1487ba4f8a6dd0b1f3be1.png', '20160416/835c9808c6c1487ba4f8a6dd0b1f3be1.png', '2016-04-16 16:01:52', '1', '大墨图片上传');
INSERT INTO `tb_pictures` VALUES ('30294da5975848dea107b44669def8dd', '图片', '62d60fece31c4409b93bb3f316b20eeb.jpg', '20160416/62d60fece31c4409b93bb3f316b20eeb.jpg', '2016-04-16 22:37:58', '1', '大墨图片上传');
INSERT INTO `tb_pictures` VALUES ('3c5aa7c2d8ec4492a50ed76103097e90', '图片', 'f506bd0c82fa4de783186cfef84cee5b.png', '20160416/f506bd0c82fa4de783186cfef84cee5b.png', '2016-04-16 16:50:41', '1', '大墨图片上传');
INSERT INTO `tb_pictures` VALUES ('4069ffc2a7d34594a551c7693e439ffc', '图片', '0c6c3cec6b6f4a7b876b16525cb2986d.png', '20160713/0c6c3cec6b6f4a7b876b16525cb2986d.png', '2016-07-13 17:31:05', '1', '图片上传');
INSERT INTO `tb_pictures` VALUES ('4b6f67d4f0e54180a8b1af9d6eb2c4cc', '图片', 'd66863617063476784cc2483dfcbaefe.png', '20160417/d66863617063476784cc2483dfcbaefe.png', '2016-04-17 10:34:50', '1', '大墨图片上传');
INSERT INTO `tb_pictures` VALUES ('55ee97c13d814b4db85baaa6a3e2e961', '图片', '5714274838b5409d9c71fb98a8b3fdce.png', '20160416/5714274838b5409d9c71fb98a8b3fdce.png', '2016-04-16 17:06:08', '1', '大墨图片上传');
INSERT INTO `tb_pictures` VALUES ('7c088901e8284f0e8a8d6c85a6db8350', '图片', 'ace6e251461f4becbf4e3075d0d3d9e1.jpg', '20160416/ace6e251461f4becbf4e3075d0d3d9e1.jpg', '2016-04-16 16:51:33', '1', '大墨图片上传');
INSERT INTO `tb_pictures` VALUES ('80918403ef7b46da8e526526740a6a54', '图片', 'cf3cd0338a5f4d63ae81e218fdb80242.jpg', '20160418/cf3cd0338a5f4d63ae81e218fdb80242.jpg', '2016-04-18 09:43:44', '1', '大墨图片上传');
INSERT INTO `tb_pictures` VALUES ('8834ae01cbb642e9949612bc8101b809', '图片', '79c4f369ffd0412bba21865adea374ae.png', '20160416/79c4f369ffd0412bba21865adea374ae.png', '2016-04-16 22:01:54', '1', '大墨图片上传');
INSERT INTO `tb_pictures` VALUES ('8c22e1529dcc431896a8142e4017b318', '图片', 'ab198bfdf564446b9cbd7e152849eb40.jpg', '20160713/ab198bfdf564446b9cbd7e152849eb40.jpg', '2016-07-13 17:30:56', '1', '图片上传');
INSERT INTO `tb_pictures` VALUES ('90b086ddb7ee4e849752312ba3ce588b', '图片', '6c9bbd2fbbdf4eea80295124a6ffaeab.jpg', '20160416/6c9bbd2fbbdf4eea80295124a6ffaeab.jpg', '2016-04-16 23:04:36', '1', '大墨图片上传');
INSERT INTO `tb_pictures` VALUES ('94c6599e40fe4bdd8124c740dd6b3451', '图片', 'd12b3e4ece4d47dca2b68caa8cb58bb4.PNG', '20160714/d12b3e4ece4d47dca2b68caa8cb58bb4.PNG', '2016-07-14 09:22:00', '1', '图片上传');
INSERT INTO `tb_pictures` VALUES ('b1003057342c422c831e3846c72c6c12', '图片', '84e0457e44fc41a3b392784d3b6eb8d7.jpg', '20160417/84e0457e44fc41a3b392784d3b6eb8d7.jpg', '2016-04-17 10:55:40', '1', '大墨图片上传');
INSERT INTO `tb_pictures` VALUES ('b1a85d1632af4282b16dba1b29227468', '图片', '77d1b0bc470c4b6e900c6667cbca7c8d.png', '20160416/77d1b0bc470c4b6e900c6667cbca7c8d.png', '2016-04-16 16:10:46', '1', '大墨图片上传');
INSERT INTO `tb_pictures` VALUES ('b7ff267a107c45baa5f21cf59871db73', '图片', '129cd20aaeca4aa3b1c081d98ad280c9.png', '20160416/129cd20aaeca4aa3b1c081d98ad280c9.png', '2016-04-16 23:33:13', '1', '大墨图片上传');
INSERT INTO `tb_pictures` VALUES ('bcd055e6e77c4196b54b56df921f4da6', '图片', '65b42842781141bfa7c537f28e4866ef.PNG', '20160713/65b42842781141bfa7c537f28e4866ef.PNG', '2016-07-13 17:38:13', '1', '图片上传');
INSERT INTO `tb_pictures` VALUES ('c34002ef68ff4a64b471bc5273186e1b', '图片', 'd1048fd955834ab0a0423c08fc0edfc7.png', '20160416/d1048fd955834ab0a0423c08fc0edfc7.png', '2016-04-16 17:06:24', '1', '大墨图片上传');
INSERT INTO `tb_pictures` VALUES ('e89c04a4b5e1480697e5a2b3e0fd182f', '图片', 'a1e5361f0489485a9cbc4fa5cf6db292.png', '20160416/a1e5361f0489485a9cbc4fa5cf6db292.png', '2016-04-16 15:50:46', '1', '图片管理处上传');
INSERT INTO `tb_pictures` VALUES ('e8e232ff00734c04be33b70b5166ae8d', '图片', '54812f8d27b1444d91b38451eefa8cdf.png', '20160416/54812f8d27b1444d91b38451eefa8cdf.png', '2016-04-16 16:43:44', '1', '大墨图片上传');
INSERT INTO `tb_pictures` VALUES ('f5a4a29a08bb43478e07e486211d0b37', '图片', 'fd46e28c857a43e0b48db2e0e5c4a47e.png', '20160416/fd46e28c857a43e0b48db2e0e5c4a47e.png', '2016-04-16 22:37:00', '1', '大墨图片上传');
INSERT INTO `tb_pictures` VALUES ('f840f027fbd3409c8bb5f55e09ae0d15', '图片', '7ba28b54baaf4eb29b2594b90353389b.png', '20160417/7ba28b54baaf4eb29b2594b90353389b.png', '2016-04-17 08:53:34', '1', '大墨图片上传');

-- ----------------------------
-- Table structure for weixin_command
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
-- Records of weixin_command
-- ----------------------------

-- ----------------------------
-- Table structure for weixin_imgmsg
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
-- Records of weixin_imgmsg
-- ----------------------------
INSERT INTO `weixin_imgmsg` VALUES ('380b2cb1f4954315b0e20618f7b5bd8f', '首页', '2015-05-10 20:51:09', '1', '图文回复', '图文回复标题', '图文回复描述', 'http://a.hiphotos.baidu.com/image/h%3D360/sign=c6c7e73ebc389b5027ffe654b535e5f1/a686c9177f3e6709392bb8df3ec79f3df8dc55e3.jpg', 'www.baidu.com', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '');

-- ----------------------------
-- Table structure for weixin_textmsg
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
-- Records of weixin_textmsg
-- ----------------------------
INSERT INTO `weixin_textmsg` VALUES ('695cd74779734231928a253107ab0eeb', '吃饭', '吃了噢噢噢噢', '2015-05-10 22:52:27', '1', '文本回复');
INSERT INTO `weixin_textmsg` VALUES ('d4738af7aea74a6ca1a5fb25a98f9acb', '关注', '这里是关注后回复的内容', '2015-05-11 02:12:36', '1', '关注回复');
