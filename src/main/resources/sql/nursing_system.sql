-- ============================================
-- 护理系统 - 系统管理模块数据库表结构
-- 数据库：nursing_system
-- ============================================

-- ----------------------------
-- 1. 用户表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户 ID',
  `username` varchar(30) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码 (加密)',
  `nickname` varchar(30) DEFAULT NULL COMMENT '昵称',
  `email` varchar(50) DEFAULT '' COMMENT '邮箱',
  `phone` varchar(11) DEFAULT '' COMMENT '手机号',
  `gender` char(1) DEFAULT '0' COMMENT '性别 (0:男 1:女 2:未知)',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像路径',
  `status` char(1) DEFAULT '0' COMMENT '状态 (0:正常 1:停用)',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门 ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `deleted` char(1) DEFAULT '0' COMMENT '删除标志 (0:存在 1:删除)',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_username` (`username`) USING BTREE,
  KEY `idx_dept_id` (`dept_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ----------------------------
-- 2. 角色表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色 ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围 (1:全部数据权限 2:自定义数据权限 3:本部门数据权限 4:本部门及以下数据权限 5:仅本人数据权限)',
  `status` char(1) NOT NULL COMMENT '角色状态 (0:正常 1:停用)',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `deleted` char(1) DEFAULT '0' COMMENT '删除标志 (0:存在 1:删除)',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `uk_role_key` (`role_key`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_role_sort` (`role_sort`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- ----------------------------
-- 3. 菜单权限表
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单 ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单 ID',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `is_frame` int(1) DEFAULT '1' COMMENT '是否外链 (1 是 0 否)',
  `is_cache` int(1) DEFAULT '0' COMMENT '是否缓存 (1 是 0 否)',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型 (M 目录 C 菜单 F 按钮)',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态 (0 显示 1 隐藏)',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态 (0 正常 1 停用)',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `deleted` char(1) DEFAULT '0' COMMENT '删除标志 (0:存在 1:删除)',
  PRIMARY KEY (`menu_id`),
  KEY `idx_parent_id` (`parent_id`) USING BTREE,
  KEY `idx_menu_type` (`menu_type`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_order_num` (`order_num`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- ----------------------------
-- 4. 用户角色关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户 ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色 ID',
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE,
  CONSTRAINT `fk_sys_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_sys_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ----------------------------
-- 5. 角色菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色 ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单 ID',
  PRIMARY KEY (`role_id`,`menu_id`),
  KEY `idx_role_id` (`role_id`) USING BTREE,
  KEY `idx_menu_id` (`menu_id`) USING BTREE,
  CONSTRAINT `fk_sys_role_menu_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_sys_role_menu_menu` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`menu_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- ----------------------------
-- 初始化数据 - 管理员用户
-- 密码：admin123 (使用 BCrypt 加密)
-- ----------------------------
INSERT INTO `sys_user` VALUES 
(1, 'admin', '$2a$10$N.zmdr9k7uOQYYRqXnC4/OZmKDHbWvECcPxS3FzqPqJhYiLj8s5C6', '超级管理员', 'admin@nursing.com', '13800138000', '0', '', '0', NULL, 
'admin', NOW(), '', NULL, '管理员', '0');

-- ----------------------------
-- 初始化数据 - 角色
-- ----------------------------
INSERT INTO `sys_role` VALUES 
(1, '超级管理员', 'admin', 1, '1', '0', 'admin', NOW(), '', NULL, '超级管理员', '0'),
(2, '普通护士', 'nurse', 2, '3', '0', 'admin', NOW(), '', NULL, '普通护士角色', '0'),
(3, '实习护士', 'intern', 3, '5', '0', 'admin', NOW(), '', NULL, '实习护士角色', '0');

-- ----------------------------
-- 初始化数据 - 菜单
-- ----------------------------
INSERT INTO `sys_menu` VALUES 
-- 系统管理目录
(1, '系统管理', 0, 1, 'system', NULL, '', 1, 0, 'M', '0', '0', '', 'setting', 'admin', NOW(), '', NULL, '系统管理目录', '0'),
-- 用户管理
(100, '用户管理', 1, 1, 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', NOW(), '', NULL, '用户管理菜单', '0'),
(1001, '用户查询', 100, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', NOW(), '', NULL, '', '0'),
(1002, '用户新增', 100, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', NOW(), '', NULL, '', '0'),
(1003, '用户修改', 100, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', NOW(), '', NULL, '', '0'),
(1004, '用户删除', 100, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', NOW(), '', NULL, '', '0'),
-- 角色管理
(101, '角色管理', 1, 2, 'role', 'system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', NOW(), '', NULL, '角色管理菜单', '0'),
(1011, '角色查询', 101, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', NOW(), '', NULL, '', '0'),
(1012, '角色新增', 101, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', NOW(), '', NULL, '', '0'),
(1013, '角色修改', 101, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', NOW(), '', NULL, '', '0'),
(1014, '角色删除', 101, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', NOW(), '', NULL, '', '0'),
-- 菜单管理
(102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', NOW(), '', NULL, '菜单管理菜单', '0'),
(1021, '菜单查询', 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', NOW(), '', NULL, '', '0'),
(1022, '菜单新增', 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', NOW(), '', NULL, '', '0'),
(1023, '菜单修改', 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', NOW(), '', NULL, '', '0'),
(1024, '菜单删除', 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', NOW(), '', NULL, '', '0');

-- ----------------------------
-- 初始化数据 - 用户角色关联
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);

-- ----------------------------
-- 初始化数据 - 角色菜单关联 (超级管理员拥有所有权限)
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES 
(1, 1), (1, 100), (1, 1001), (1, 1002), (1, 1003), (1, 1004),
(1, 101), (1, 1011), (1, 1012), (1, 1013), (1, 1014),
(1, 102), (1, 1021), (1, 1022), (1, 1023), (1, 1024);
