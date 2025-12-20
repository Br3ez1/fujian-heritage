/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 90300
 Source Host           : localhost:3306
 Source Schema         : fujian_heritage

 Target Server Type    : MySQL
 Target Server Version : 90300
 File Encoding         : 65001

 Date: 20/12/2025 20:02:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity_signup
-- ----------------------------
DROP TABLE IF EXISTS `activity_signup`;
CREATE TABLE `activity_signup`  (
  `signup_id` bigint NOT NULL AUTO_INCREMENT,
  `activity_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `status` int NULL DEFAULT 1,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`signup_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity_signup
-- ----------------------------

-- ----------------------------
-- Table structure for biz_activity
-- ----------------------------
DROP TABLE IF EXISTS `biz_activity`;
CREATE TABLE `biz_activity`  (
  `activity_id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动标题',
  `activity_time` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动时间描述',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地点',
  `max_people` int NULL DEFAULT 50 COMMENT '最大人数',
  PRIMARY KEY (`activity_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '研学活动表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_activity
-- ----------------------------
INSERT INTO `biz_activity` VALUES (1, '小小惠安女服饰体验', '本周六 14:00-16:00', '泉州非遗馆 · 民俗展厅', 20);
INSERT INTO `biz_activity` VALUES (2, '德化白瓷拉坯初体验', '本周日 09:30-11:30', '德化陶瓷学院 · 实训基地', 15);
INSERT INTO `biz_activity` VALUES (3, '福州软木画制作亲子课', '下周六 15:00-17:00', '三坊七巷 · 软木画馆', 10);
INSERT INTO `biz_activity` VALUES (4, '南音古韵赏析与乐器体验', '下周日 19:00-21:00', '泉州南音艺苑', 50);
INSERT INTO `biz_activity` VALUES (5, '莆田木雕技艺观摩会', '2026-01-15 10:00', '莆田工艺美术城', 30);
INSERT INTO `biz_activity` VALUES (10, '“非遗进校园” - 剪纸艺术公开课', '本周三 15:30', '福州大学城·学生活动中心', 100);
INSERT INTO `biz_activity` VALUES (11, '厦门漆线雕技艺传承班（初级）', '2026-02-01起 每周末', '厦门市美术馆', 15);
INSERT INTO `biz_activity` VALUES (12, '探秘武夷山：采茶制茶深度游', '2026-04-05 (清明假期)', '武夷山风景名胜区', 30);
INSERT INTO `biz_activity` VALUES (13, '莆仙戏脸谱绘制亲子活动', '下周六 09:30', '莆田市群众艺术馆', 20);
INSERT INTO `biz_activity` VALUES (14, '客家元宵习俗体验夜游', '2026-02-12 (元宵节)', '龙岩连城县', 50);

-- ----------------------------
-- Table structure for biz_activity_signup
-- ----------------------------
DROP TABLE IF EXISTS `biz_activity_signup`;
CREATE TABLE `biz_activity_signup`  (
  `signup_id` bigint NOT NULL AUTO_INCREMENT,
  `activity_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `status` tinyint NULL DEFAULT 0 COMMENT '0:已提交 1:已审核',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`signup_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '活动报名记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_activity_signup
-- ----------------------------
INSERT INTO `biz_activity_signup` VALUES (1, 1, 1, 0, '2025-12-16 13:42:52');
INSERT INTO `biz_activity_signup` VALUES (2, 1, 2, 0, '2025-12-16 13:42:52');
INSERT INTO `biz_activity_signup` VALUES (3, 2, 1, 0, '2025-12-16 13:42:52');
INSERT INTO `biz_activity_signup` VALUES (4, 4, 1, 0, '2025-12-16 13:43:09');
INSERT INTO `biz_activity_signup` VALUES (5, 3, 1, 0, '2025-12-16 13:43:12');
INSERT INTO `biz_activity_signup` VALUES (6, 1, 3, 0, '2025-12-16 16:17:14');
INSERT INTO `biz_activity_signup` VALUES (7, 3, 4, 1, '2025-12-16 17:15:52');
INSERT INTO `biz_activity_signup` VALUES (8, 3, 2, 1, '2025-12-16 17:15:52');
INSERT INTO `biz_activity_signup` VALUES (9, 3, 5, 0, '2025-12-16 17:15:52');
INSERT INTO `biz_activity_signup` VALUES (10, 1, 4, 1, '2025-12-16 17:15:52');
INSERT INTO `biz_activity_signup` VALUES (11, 4, 5, 0, '2025-12-16 17:15:52');

-- ----------------------------
-- Table structure for biz_heritage
-- ----------------------------
DROP TABLE IF EXISTS `biz_heritage`;
CREATE TABLE `biz_heritage`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目编号 (如 VIII-43)',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目名称',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类别 (传统技艺/美术/音乐等)',
  `level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '国家级' COMMENT '级别',
  `region` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申报地区 (如 福州市)',
  `batch` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '批次',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '详细介绍',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面图',
  `status` int NULL DEFAULT 1 COMMENT '状态: 0待审核 1已发布',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '展示图',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '非遗项目名录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_heritage
-- ----------------------------
INSERT INTO `biz_heritage` VALUES (1, 'II-24', '南音', '传统音乐', '国家级', '泉州市', NULL, '中国现存最古老的乐种之一...', NULL, 1, NULL);
INSERT INTO `biz_heritage` VALUES (2, 'VIII-43', '福州脱胎漆器髹饰技艺', '传统技艺', '国家级', '福州市', NULL, '质地轻巧坚固，色泽鲜艳古朴...', NULL, 1, NULL);
INSERT INTO `biz_heritage` VALUES (3, 'VIII-67', '德化瓷烧制技艺', '传统技艺', '国家级', '泉州市德化县', NULL, '白如玉，明如镜，薄如纸，声如磬...', NULL, 1, NULL);
INSERT INTO `biz_heritage` VALUES (4, 'VII-16', '中秋博饼', '民俗', '国家级', '厦门市', NULL, '博饼（bó bǐng），是福建省厦门市、漳州市，泉州市，金门县等地的传统民俗活动，始于清初，是郑成功屯兵厦门时为解士兵中秋思乡之情、激励鼓舞士气而发明的。', NULL, 1, NULL);
INSERT INTO `biz_heritage` VALUES (5, 'VIII-203', '漳浦剪纸', '传统美术', '国家级', '漳州市漳浦县', NULL, '漳浦剪纸以其独特的艺术风格、浓郁的原始趣味和稚拙的美感，在中国民间艺术中独树一帜。', NULL, 1, NULL);
INSERT INTO `biz_heritage` VALUES (6, 'VIII-204', '柘荣剪纸', '传统美术', '国家级', '宁德市柘荣县', NULL, '柘荣剪纸保留了最原始的剪纸艺术手法，题材广泛，多表现民间生活场景。', NULL, 1, NULL);
INSERT INTO `biz_heritage` VALUES (7, 'VIII-45', '莆田木雕', '传统美术', '国家级', '莆田市', NULL, '莆田木雕兴于唐宋，盛于明清，素以“精微透雕”著称。主要用于建筑装饰、家具及佛像雕刻。', NULL, 1, NULL);
INSERT INTO `biz_heritage` VALUES (8, 'VIII-92', '福州茉莉花茶窨制工艺', '传统技艺', '国家级', '福州市', NULL, '福州茉莉花茶窨制工艺是将茶叶和茉莉花进行拼和、窨制，使茶叶吸收花香而成的传统工艺。', NULL, 1, NULL);
INSERT INTO `biz_heritage` VALUES (9, 'IV-89', '高甲戏', '传统戏剧', '国家级', '泉州市', NULL, '高甲戏是闽南诸剧种中流播区域最广、观众面最多的一个地方戏曲剧种。', NULL, 1, NULL);
INSERT INTO `biz_heritage` VALUES (10, 'IV-90', '歌仔戏', '传统戏剧', '国家级', '厦门市/漳州市', NULL, '歌仔戏是唯一发源于中国台湾，而后传回福建闽南地区的汉族传统戏曲剧种。', NULL, 1, NULL);
INSERT INTO `biz_heritage` VALUES (11, 'VIII-68', '建窑建盏烧制技艺', '传统技艺', '国家级', '南平市建阳区', NULL, '建盏是宋代八大名瓷之一黑瓷的代表，产于宋建州（今福建省南平市建阳区）。', NULL, 1, NULL);
INSERT INTO `biz_heritage` VALUES (12, 'VIII-123', '连城培田客家古民居营造技艺', '传统技艺', '国家级', '龙岩市连城县', NULL, '培田古民居建筑群是客家建筑文化的经典之作，有“民间故宫”之美誉。', NULL, 1, NULL);
INSERT INTO `biz_heritage` VALUES (13, 'II-25', '闽西汉剧', '传统戏剧', '国家级', '龙岩市', NULL, '闽西汉剧是福建省主要的地方戏曲剧种之一，流行于以龙岩为中心的闽西各县。', NULL, 1, NULL);
INSERT INTO `biz_heritage` VALUES (14, 'VIII-46', '惠安石雕', '传统美术', '国家级', '泉州市惠安县', NULL, '惠安石雕历史悠久，技艺精湛，素有“中华一绝”之美称，被誉为“世界石雕之都”。', NULL, 1, NULL);

-- ----------------------------
-- Table structure for biz_order
-- ----------------------------
DROP TABLE IF EXISTS `biz_order`;
CREATE TABLE `biz_order`  (
  `order_id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '总金额',
  `status` int NULL DEFAULT 0 COMMENT '状态: 0待支付 1待发货 2已发货 3已完成',
  `receiver_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货地址',
  `contact` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `product_summary` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品摘要',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文创商城订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_order
-- ----------------------------
INSERT INTO `biz_order` VALUES (1, 2, 'OR20251220150442964', 384.00, 3, '柳文财', '福建', '13800000000', '手绘福州油纸伞, 手绘福州油纸伞, 手绘福州油纸伞', '2025-12-20 15:04:42', '2025-12-20 15:05:16');
INSERT INTO `biz_order` VALUES (2, 3, 'OR20251220151500453', 598.00, 2, '林卓越', '福建', '13800000000', '德化白瓷茶具套装, 德化白瓷茶具套装', '2025-12-20 15:15:01', '2025-12-20 15:16:00');
INSERT INTO `biz_order` VALUES (3, 2, 'OR20251220152322633', 396.00, 1, '柳文财', '福建', '13800000000', '漳浦剪纸装裱画-百福图, 漳浦剪纸装裱画-百福图', '2025-12-20 15:23:23', '2025-12-20 15:23:23');

-- ----------------------------
-- Table structure for biz_post
-- ----------------------------
DROP TABLE IF EXISTS `biz_post`;
CREATE TABLE `biz_post`  (
  `post_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '发帖人ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签 (JSON格式或逗号分隔)',
  `likes` int NULL DEFAULT 0 COMMENT '点赞数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '社区帖子表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_post
-- ----------------------------
INSERT INTO `biz_post` VALUES (1, 1, '终于入手了心心念念的建盏！', '开箱视频来啦，这个兔毫纹路真的太美了，手感也超级好。强烈推荐给大家去商城的茶文化专区看看，用来喝武夷岩茶简直绝配！ #建盏 #非遗好物', '建盏,好物推荐,生活美学', 45, '2025-12-16 11:42:52');
INSERT INTO `biz_post` VALUES (2, 2, '探访泉州提线木偶戏，太震撼了', '今天有幸在泉州观看了提线木偶戏表演《钟馗醉酒》，技艺精湛，令人叹为观止。木偶在老艺人的手中仿佛有了生命，每一个动作都栩栩如生。非遗传承不易，大家多多支持！', '泉州,非遗打卡,传统戏曲', 89, '2025-12-16 08:42:52');
INSERT INTO `biz_post` VALUES (3, 1, '沉浸式体验漆线雕工艺', '精微细致的漆线雕，真的是时间的艺术。看着老师傅一点点盘线，内心无比平静。这种“错彩镂金”的美，照片根本拍不出来万分之一。', '漆线雕,匠人精神,厦门', 120, '2025-12-15 13:42:52');
INSERT INTO `biz_post` VALUES (4, 2, '福州三坊七巷的非遗之旅', '周末去了一趟三坊七巷，不仅看到了软木画，还体验了油纸伞的制作。虽然自己画的有点丑，但是过程很有趣！', '福州,三坊七巷,非遗体验', 67, '2025-12-14 13:42:52');
INSERT INTO `biz_post` VALUES (5, 1, '妈祖信俗：感受信仰的力量', '在湄洲岛参加了妈祖祭典，场面非常宏大。这不仅仅是迷信，更是一种流淌在闽南人血脉里的文化认同和精神寄托。', '莆田,妈祖,民俗文化', 210, '2025-12-13 13:42:52');
INSERT INTO `biz_post` VALUES (6, 1, '打卡泉州簪花围，做一回蟳埔女', '“今生戴花，来世漂亮”。终于来到了泉州蟳埔村，体验了非遗簪花围。阿姨的手法非常娴熟，满头的鲜花不仅是装饰，更是对美好生活的热爱。走在蚝壳厝的巷子里，仿佛穿越了时光。强烈推荐大家来体验！', '泉州,簪花围,非遗体验,生活美学', 128, '2025-12-16 15:12:05');
INSERT INTO `biz_post` VALUES (7, 2, '偶遇福州软木画老艺人', '周末在三坊七巷闲逛，在一间不起眼的小店里遇到了一位正在制作软木画的老爷爷。那刀工简直神了，薄如蝉翼的软木片在他手里变成了亭台楼阁。老爷爷说现在学这个的年轻人不多了，希望这项技艺能一直传承下去。买了一个小摆件支持一下！', '福州,软木画,匠人精神', 0, '2025-12-16 15:40:23');
INSERT INTO `biz_post` VALUES (10, 3, '福州茉莉花茶的正确冲泡姿势', '很多人觉得茉莉花茶只有香气没有茶味，其实是冲泡方法不对。建议水温85-90度，不要闷泡太久...', '茶文化,生活小窍门', 23, '2025-12-16 13:15:52');
INSERT INTO `biz_post` VALUES (11, 4, '厦大旁边的这家漆线雕体验馆太棒了', '今天和舍友去体验了漆线雕，真的太考验耐心了！老师傅说“一条线，一辈子”，肃然起敬。', '厦门,非遗体验,漆线雕', 56, '2025-12-16 11:15:52');
INSERT INTO `biz_post` VALUES (12, 5, '中秋博饼规则全解析', '马上中秋了，给外地朋友科普一下我们闽南的博饼规则。一秀、二举、四进、三红、对堂、状元...', '中秋,博饼,民俗', 102, '2025-12-16 05:15:52');
INSERT INTO `biz_post` VALUES (13, 2, '寻找最美土楼：南靖之旅', '不仅仅是四菜一汤，还有很多未被开发的土楼。云水谣古镇真的很有感觉。', '土楼,摄影,旅行', 88, '2025-12-15 17:15:52');
INSERT INTO `biz_post` VALUES (14, 1, '非遗传承人的日常', '今天继续打磨这件半成品。做手艺耐得住寂寞，才能守得住繁华。', '匠心,手艺人', 245, '2025-12-14 17:15:52');
INSERT INTO `biz_post` VALUES (15, 3, '泉州西街美食攻略+非遗打卡', '满煎糕、面线糊、润饼菜...吃饱了去看看开元寺，听一曲南音，这才是泉州生活。', '泉州,美食,南音', 167, '2025-12-13 17:15:52');

-- ----------------------------
-- Table structure for biz_product
-- ----------------------------
DROP TABLE IF EXISTS `biz_product`;
CREATE TABLE `biz_product`  (
  `product_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品分类',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `stock` int NULL DEFAULT 999 COMMENT '库存',
  `heritage_id` bigint NULL DEFAULT NULL COMMENT '关联的非遗项目ID (可选)',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品详情',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片链接',
  PRIMARY KEY (`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文创商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_product
-- ----------------------------
INSERT INTO `biz_product` VALUES (1, '手绘福州油纸伞', '漆艺', 128.00, 999, 2, '/images/p1.jpg', NULL, 'https://img.zcool.cn/community/01d90d5764ff4f0000018c1b30521b.jpg@1280w_1l_2o_100sh.jpg');
INSERT INTO `biz_product` VALUES (2, '德化白瓷茶具套装', '陶瓷', 299.00, 999, 3, '/images/p2.jpg', NULL, 'https://img.zcool.cn/community/01d90d5764ff4f0000018c1b30521b.jpg@1280w_1l_2o_100sh.jpg');
INSERT INTO `biz_product` VALUES (3, '创意博饼套装-状元插金花', '民俗周边', 68.00, 500, NULL, '/images/p3.jpg', '包含全套博饼碗、骰子及精美说明书，中秋聚会必备。', 'https://img.zcool.cn/community/01d90d5764ff4f0000018c1b30521b.jpg@1280w_1l_2o_100sh.jpg');
INSERT INTO `biz_product` VALUES (4, '漳浦剪纸装裱画-百福图', '传统美术', 198.00, 50, NULL, '/images/p4.jpg', '手工剪制，寓意吉祥，已装裱，适合家居悬挂。', 'https://img.zcool.cn/community/01d90d5764ff4f0000018c1b30521b.jpg@1280w_1l_2o_100sh.jpg');
INSERT INTO `biz_product` VALUES (5, '柘荣剪纸DIY体验包', '传统美术', 29.90, 200, NULL, '/images/p5.jpg', '内含剪刀、红纸及入门图样，适合亲子互动。', 'https://img.zcool.cn/community/01d90d5764ff4f0000018c1b30521b.jpg@1280w_1l_2o_100sh.jpg');
INSERT INTO `biz_product` VALUES (6, '莆田精微透雕笔筒-松鹤延年', '木雕', 880.00, 10, NULL, '/images/p6.jpg', '大师监制，纯手工雕刻，细节惊人，收藏佳品。', 'https://img.zcool.cn/community/01d90d5764ff4f0000018c1b30521b.jpg@1280w_1l_2o_100sh.jpg');
INSERT INTO `biz_product` VALUES (7, '特级福州茉莉花茶礼盒', '茶文化', 368.00, 100, NULL, '/images/p7.jpg', '六窨一提，香气扑鼻，送礼首选。', 'https://img.zcool.cn/community/01d90d5764ff4f0000018c1b30521b.jpg@1280w_1l_2o_100sh.jpg');
INSERT INTO `biz_product` VALUES (8, '高甲戏丑角Q版盲盒', '潮玩文创', 59.00, 300, NULL, NULL, '将传统戏曲角色动漫化，隐藏款为“柯派丑行”。', 'https://img.zcool.cn/community/01d90d5764ff4f0000018c1b30521b.jpg@1280w_1l_2o_100sh.jpg');
INSERT INTO `biz_product` VALUES (9, '建盏-银斑兔毫盏（主人杯）', '陶瓷', 450.00, 80, NULL, NULL, '器型古朴，斑纹清晰，还原宋代点茶风韵。', 'https://img.zcool.cn/community/01d90d5764ff4f0000018c1b30521b.jpg@1280w_1l_2o_100sh.jpg');
INSERT INTO `biz_product` VALUES (10, '建盏-曜变天目茶盏', '陶瓷', 12800.00, 2, NULL, NULL, '极品收藏级，光照下呈现七彩光晕，宛如星空。', 'https://img.zcool.cn/community/01d90d5764ff4f0000018c1b30521b.jpg@1280w_1l_2o_100sh.jpg');
INSERT INTO `biz_product` VALUES (11, '惠安影雕摆件-惠安女', '石雕', 260.00, 30, NULL, NULL, '在黑石上通过针点凿刻成像，黑白分明，永不褪色。', 'https://img.zcool.cn/community/01d90d5764ff4f0000018c1b30521b.jpg@1280w_1l_2o_100sh.jpg');
INSERT INTO `biz_product` VALUES (12, '客家土楼拼装模型', '文创模型', 128.00, 150, NULL, NULL, '还原土楼结构，无需胶水，榫卯结构拼装。', 'https://img.zcool.cn/community/01d90d5764ff4f0000018c1b30521b.jpg@1280w_1l_2o_100sh.jpg');
INSERT INTO `biz_product` VALUES (13, '福州三宝礼盒(漆器/角梳/纸伞)', '精选礼盒', 520.00, 20, NULL, NULL, '一次集齐福州三宝，商务伴手礼首选。', 'https://img.zcool.cn/community/01d90d5764ff4f0000018c1b30521b.jpg@1280w_1l_2o_100sh.jpg');
INSERT INTO `biz_product` VALUES (14, '厦门漆线雕-九龙壁挂盘', '漆艺', 1680.00, 5, NULL, NULL, '纯手工盘绕金线，立体感强，金碧辉煌。', 'https://img.zcool.cn/community/01d90d5764ff4f0000018c1b30521b.jpg@1280w_1l_2o_100sh.jpg');

-- ----------------------------
-- Table structure for log_ai_chat
-- ----------------------------
DROP TABLE IF EXISTS `log_ai_chat`;
CREATE TABLE `log_ai_chat`  (
  `log_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID (游客为NULL)',
  `question` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '用户提问',
  `answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'AI回答',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI对话日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of log_ai_chat
-- ----------------------------
INSERT INTO `log_ai_chat` VALUES (1, 1, '福州脱胎漆器特点', '福州脱胎漆器是福建省福州市特产，中国国家地理标志产品。它质地轻巧坚固，色泽鲜艳古朴。商城有精美的手绘油纸伞和软木画，您可以去看看。', '2025-12-16 13:21:23');
INSERT INTO `log_ai_chat` VALUES (2, 1, '推荐送长辈的礼物', '送长辈首推安溪铁观音礼盒，寓意安康；送友人可选手作惠安女公仔或漆线雕挂件，精致且富有闽地风情。', '2025-12-16 13:21:25');
INSERT INTO `log_ai_chat` VALUES (3, 1, '霞浦的历史', '这个问题很有趣！福建非遗文化博大精深，您可以去我们的[数字资源]板块查看更多纪录片，或者换个具体的问题问我。', '2025-12-16 13:21:30');
INSERT INTO `log_ai_chat` VALUES (4, 1, '福州有什么特产', '福州脱胎漆器是福建省福州市特产，中国国家地理标志产品。它质地轻巧坚固，色泽鲜艳古朴。商城有精美的手绘油纸伞和软木画，您可以去看看。', '2025-12-16 13:23:38');
INSERT INTO `log_ai_chat` VALUES (5, 1, '福州有什么特产', '福州脱胎漆器是福建省福州市特产，中国国家地理标志产品。它质地轻巧坚固，色泽鲜艳古朴。商城有精美的手绘油纸伞和软木画，您可以去看看。', '2025-12-16 13:25:23');
INSERT INTO `log_ai_chat` VALUES (6, 1, '福州有什么特产', '福州作为福建省会，文化底蕴深厚，特产融合了闽都工艺与自然馈赠。以下为您推荐几样最具代表性的福州风物：\n\n一、**工艺瑰宝**\n1. **脱胎漆器**（国家级非遗）\n   沈绍安始创的“三麻两布”技艺，器物轻若浮云而坚如金石，与北京景泰蓝、景德镇瓷器并称“中国工艺三宝”。建议至**三坊七巷漆艺博物馆**欣赏《赤宝砂观音瓶》等传世之作。\n\n2.**软木画**（福州三绝之一）\n   以栓树皮为纸、刻刀为笔，方寸间雕出亭台楼阁、山水意境。西园村匠人能用0.5毫米薄片镂刻出闽江舟影，堪称“立体的中国画”。\n\n二、**茶食雅韵**\n3. **茉莉花茶**（世界级香气记忆）\n   福州茉莉花茶窨制技艺是世界非遗。暑月午后，取玻璃盏冲泡“针王”，看银毫在茉莉香雾中舒展，正是老福州“一盏香片话家常”的日常。\n\n4. **永泰李干**（国家地理标志）\n   选用芙蓉李古法蜜渍，果肉呈现琥珀光泽。林则徐曾以“酸甘醒脾”称赞其风味，佐茶或入菜皆宜。\n\n三、**古早风味**\n5. **鱼丸燕皮**（海上福州的味道）\n   正宗鱼丸需用闽江口马鲛鱼捶打成茸，包入猪肉馅后在高汤中浮沉如月。百年老店“永和鱼丸”仍坚持手工现打，咬开时爆汁的鲜甜值得排队等候。\n\n6. **礼饼红纸包**（闽都婚俗符号）\n   冬瓜条、肥膘肉与糯米粉在红纸包裹中交融，饼面芝麻排列如星宿。旧时聘礼必备，如今在南后街老铺仍可见师傅用木模压出双喜纹样。\n\n四、**时令风物**\n7. **橄榄**（闽语“福果”）\n   霜降后采摘的檀香橄榄，初涩后甘的滋味被比作人生哲思。鼓岭农家会用陶瓮腌制成咸甜口的“橄榄角”，是炖汤提鲜的秘宝。\n\n若想将这些文化记忆带回家，推荐至**福建省非遗博览苑**或**福州市非遗展示馆**选购官方文创产品。例如脱胎漆器茶具套装、茉莉花茶窨制体验包等，既承载技艺精髓又适合日常使用。穿行于三坊七巷时，不妨留意那些挂着“老字号”牌匾的店铺，往往藏着四代传承的手工味道。', '2025-12-16 13:27:34');
INSERT INTO `log_ai_chat` VALUES (7, 1, '霞浦有什么特产', '霞浦作为福建宁德的重要沿海县，其特产与海洋文化和闽东山地特色紧密相连，以下为您梳理几类代表性物产：\n\n---\n\n### **一、海鲜干货与海产加工品**\n1. **霞浦紫菜**  \n   - **特点**：中国国家地理标志产品，以“头水紫菜”最为珍贵（每年首次收割），口感嫩滑、色泽乌亮，富含微量元素。传统晾晒工艺保留海味，可做紫菜饼、汤品或零食。\n   - **文化链接**：当地渔民延续“讨小海”习俗，收割时仍部分采用竹竿插网养殖的古法。\n\n2. **海带与海参**  \n   - 霞浦是中国南方最大的海带养殖基地，海带肥厚柔韧；另盛产仿刺参等，干货品质上乘。\n\n3. **鱼露与虾皮**  \n   - 沿用古法发酵的鱼露（俗称“虾油”）是闽菜提鲜关键；小规模渔村晒制的虾皮鲜甜无渣，常用于煲粥或拌菜。\n\n---\n\n### **二、山地农产与手作**\n1. **霞浦元宵茶**  \n   - 福建省最早上市的春茶之一，形美色翠、香气清高，明代已有种植记载，适合搭配茶点慢饮。\n\n2. **晚熟荔枝与蜜柚**  \n   - 利用沿海气候种植的“晚熟荔枝”（七月上市），果肉晶莹多汁；三沙蜜柚酸甜适中，常见于节庆祭祀。\n\n3. **畲族乌米饭**  \n   - 霞浦是畲族聚居地，农历三月三用乌稔树叶染制的糯米饭，寓意安康，现开发为便携零食。\n\n---\n\n### **三、非遗技艺相关文创**\n1. **闽东竹编与船模**  \n   - 沿海竹编渔具（如鱼篓）转型为灯罩、茶具等文创；传统木帆船模型承载“海上丝绸之路”记忆。\n\n2. **剪纸与畲族刺绣**  \n   - 霞浦剪纸融合海洋题材（如海浪、鱼群）；畲族凤凰纹刺绣手包、挂饰展现民族美学。\n\n---\n\n### **四、体验与购买建议**\n- **在地体验**：可参观**三沙渔港**的晒海带场景，或参与**半月里畲族村**的乌米饭制作。\n- **购买渠道**：如需选购正品特产，建议前往**霞浦县文旅局认证的展销中心**或关注**“福建非遗文创商城”**线上平台，确保品质的同时支持传统工艺传承。\n\n> 注：若逢渔汛期，还可品尝到现捕的**剑蛏、笔架（龟足）**等小众海鲜，其鲜味堪称“海味精粹”。霞浦物产之美，既在舌尖，更在山海交融的千年生计智慧之中。', '2025-12-16 13:29:58');
INSERT INTO `log_ai_chat` VALUES (8, 1, '推荐送长辈的礼物', '（轻抚茶盏，闽南口音温和）送长辈的礼物啊，就像我们闽南人泡茶，要讲究“和、敬、清、寂”。我推荐德化白瓷茶具——温润如玉的“中国白”，配上安溪铁观音，最合长辈心意。我们福建非遗文创商城有林睦殿大师手作的“明月杯”，一盏茶汤映月光，寓意团圆安康。', '2025-12-16 13:32:28');
INSERT INTO `log_ai_chat` VALUES (9, 1, '推荐送的福建特长', '您好！作为福建非遗文化的传承者，我为您推荐几款既能体现福建文化底蕴，又适合作为礼物的特色产品，它们承载着千年闽韵，每一件都是“会说话”的福建故事：\n\n---\n\n### **一、非遗匠心 · 传世之礼**\n1. **德化白瓷茶器**  \n   - **特色**：素有“中国白”之称，釉色温润如玉，明代何朝宗风格的佛像瓷塑更是“东方艺术珍品”。推荐**手绘青花茶具套装**，融合闽南茶文化，使用时仿佛能听见武夷山泉水的清音。\n   - **文化故事**：每件瓷器需经过72道工序，窑火中涅槃的“象牙白”，曾是海上丝绸之路的明星商品。\n\n2. **福州脱胎漆器**  \n   - **特色**：轻若浮云，亮如镜面，传统“赤宝砂”工艺让漆面流淌星河光泽。推荐**首饰盒或漆画摆件**，镶嵌螺钿的“夜光薄料”技术在暗处会泛起微光。\n   - **非遗密码**：郭沫若曾赞“天下谅无双，人间疑独绝”，其髹饰技艺列入联合国急需保护非遗名录。\n\n---\n\n### **二、风物雅韵 · 舌尖闽味**\n3. **闽南古法乌龙茶礼**  \n   - **推荐**：安溪铁观音（兰花香观音韵）+ 武夷山大红袍（岩骨花香），搭配**德化白瓷小泡罐**，组成“茶瓷双绝”礼盒。\n   - **冲泡贴士**：附赠闽南茶谚小笺：“一道水，二道茶，三道四道是精华”，教您用闽式工夫茶22器演绎茶道。\n\n4. **泉州漆线雕伴手礼**  \n   - **特色**：纯金漆线在瓶罐上盘绕出龙凤、闽南古厝纹样，可定制**迷你香料罐**，内装福建特色香橼蜜饯或古龙酱油膏。\n   - **技艺点睛**：漆线细过发丝却韧如弓弦，这项技艺曾专用于闽台神像塑金身。\n\n---\n\n### **三、文创巧思 · 时尚闽礼**\n5. **南音元素丝绸礼巾**  \n   - **设计**：将工乂谱符号、琵琶形制化作苏杭真丝上的暗纹，附二维码可聆听《梅花操》选段。\n   - **文化彩蛋**：图案中隐藏着泉州开元寺飞天乐伎的飘带曲线。\n\n6. **妈祖平安福袋**  \n   - **内涵**：湄洲女“帆船头”发型造型香囊，内装漳州水仙风干花与平潭海沙，寓意“一帆风顺”。\n   - **信俗延伸**：搭配手写“平安箴言”小卷轴，采用莆田传统金银纸工艺镶边。\n\n---\n\n### **温馨提示**\n- **文化共鸣**：赠礼时可附一句闽谚：“泉州人，个个猛”（寓意收礼者才华出众），增添趣味。\n- **购买指引**：如需选购正宗非遗作品，建议关注**福建省艺术馆非遗文创中心**或**各地非遗传承人工坊**，部分经典款可通过官方授权的文创商城入手，确保每件作品都有“非遗身份证”收藏证书。\n\n让这些凝聚着闽人智慧与山海灵气的礼物，替您诉说福建的“古早味”与“精气神”吧！', '2025-12-16 13:33:23');
INSERT INTO `log_ai_chat` VALUES (10, 1, '福建都有哪些出名的茶叶', '福建作为中国茶叶的重要发源地之一，出产的茶叶种类丰富、品质卓越，其中不少品种享誉海内外。以下是一些具有代表性的福建名茶，它们不仅是饮品，更是承载着闽地千年制茶技艺的非物质文化遗产：\n\n---\n\n### **1. 武夷岩茶（乌龙茶类）**\n- **核心产地**：武夷山（世界文化与自然双遗产地）\n- **特点**：以“岩骨花香”著称，茶树生长于丹霞地貌的岩缝中，茶汤醇厚带矿物感。传统制作技艺列入国家级非物质文化遗产。\n- **代表茶**：\n  - **大红袍**：岩茶之王，传说曾为明代皇家贡茶，香气浓郁持久。\n  - **水仙、肉桂**：武夷山两大当家品种，水仙醇柔，肉桂辛锐如桂皮香。\n  - **名丛系列**：铁罗汉、白鸡冠、水金龟、半天妖等“四大名丛”，各具风骨。\n\n---\n\n### **2. 安溪铁观音（乌龙茶类）**\n- **核心产地**：泉州安溪县（中国乌龙茶之乡）\n- **特点**：兰花香、观音韵，工艺繁复，需历经摇青、炒青、包揉等十余道工序。其制作技艺是国家级非遗。\n- **文化印记**：闽南茶文化核心载体，“功夫茶”习俗中不可或缺的茶品。\n\n---\n\n### **3. 福鼎白茶**\n- **核心产地**：宁德福鼎市（中国白茶原产地）\n- **特点**：不炒不揉，自然萎凋，最大程度保留茶叶本真。素有“一年茶、三年药、七年宝”之说。\n- **代表茶**：\n  - **白毫银针**：仅取单芽，满披白毫，如银似雪。\n  - **白牡丹、寿眉**：各具鲜醇或醇厚风味。\n- **非遗地位**：福鼎白茶制作技艺为国家级非遗。\n\n---\n\n### **4. 正山小种（红茶鼻祖）**\n- **核心产地**：武夷山桐木关（世界红茶发源地）\n- **特点**：烟熏工艺独创，带有松烟香、桂圆汤，开创了红茶的历史。传统制作技艺属省级非遗。\n- **衍生名茶**：金骏眉（现代创新高端红茶，金黄芽头，花果蜜香）。\n\n---\n\n### **5. 福州茉莉花茶**\n- **核心产地**：福州（全球重要农业文化遗产“福州茉莉花种植与茶文化系统”）\n- **特点**：“七窨一提”工艺，以绿茶为坯，反复窨制茉莉鲜花，茶引花香，花增茶味。窨制工艺为国家级非遗。\n- **历史地位**：清代贡茶，曾作为外交国礼。\n\n---\n\n### **6. 闽北水仙、闽南佛手等特色乌龙茶**\n- **漳平水仙**（龙岩）：唯一的乌龙茶紧压茶饼，传统技艺为省级非遗。\n- **永春佛手**（泉州）：叶片似佛手柑，香气清锐，制作技艺属省级非遗。\n\n---\n\n### **茶与福建非遗文化的交融**\n福建茶文化不仅是技艺，更融入生活与信仰：\n- **茶艺**：闽南“功夫茶”、闽北“武夷茶艺”均列入省级非遗，体现“和敬清寂”的哲学。\n- **茶俗**：客家擂茶（三明等地）将茶叶与谷物、草药共擂，是待客与节庆的重要仪式。\n- **茶与信仰**：武夷山“喊山祭茶”、安溪“茶王赛”等民俗，融合了民间信仰与生产传统。\n\n---\n\n若想体验这些茶叶的韵味，可关注福建非遗文创平台或茶产区官方渠道，许多品牌将传统技艺与现代设计结合，推出兼具文化内涵与实用性的茶品。饮一杯福建茶，仿佛能听见武夷山涧的水流、闻到安溪茶山的兰香，这便是闽茶跨越千年的生命律动。', '2025-12-16 13:34:21');
INSERT INTO `log_ai_chat` VALUES (11, 1, '推荐几个福州的非遗', '福州作为八闽首府，非遗资源如闽江潮涌般丰沛。以下为您推荐几项具有代表性的福州非遗瑰宝：\n\n1. **闽剧（福州戏）**  \n   福州方言古韵流淌的舞台艺术，已有400余年历史。其唱腔融合「逗腔」「江湖」「小调」三大体系，尤以《荔枝换绛桃》《贻顺哥烛蒂》等经典剧目展现福州人情风貌。表演中「三小戏」（小生、小旦、小丑）的诙谐灵动，恰似三坊七巷里飘出的茉莉清香。\n\n2. **软木画**  \n   诞生于西园村的「东方立体浮雕」，以栓树皮层为纸，刻刀为笔。匠人需掌握「挑、剪、镂、嵌」四绝，方能在方寸间再现鼓山松涛、镇海楼影。2018年曾创作《榕城十八景》系列，将马尾罗星塔的月光都凝在0.5毫米的薄木之中。\n\n3. **福州金箔锻造技艺**  \n   源自唐代「佛妆贴金」传统，历经十二道工序锤炼，十万次捶打方得蝉翼金箔。福州开元寺宋代千佛陶塔的金身、林则徐纪念馆匾额的流光，皆赖此技。当代更创新出「七彩金箔」，让传统技艺在漆画、寿山石雕中焕发新彩。\n\n4. **福州茉莉花茶窨制工艺**  \n   国家级非遗中唯一的茶类技艺。「九窨九提」的古老秘法，让绿茶在茉莉花苞中经历昼夜轮回。今年清明前，南后街老茶坊仍按古法举行「开窨仪式」，花香穿透七层棉纸的瞬间，正是陆羽《茶经》未曾记载的福州密码。\n\n5. **咏春拳（福州鹤拳分支）**  \n   不同于广府咏春，福州宗脉保留「三拜佛」起手式与「白鹤亮翅」身形。三坊七巷小黄楼曾为清代拳师黄莘田传艺之所，其「寸劲发六合」的要诀，与脱胎漆器「千层夹纻」的工艺竟有异曲同工之妙。\n\n若想亲睹这些非遗风华，可往**福建省非遗博览苑（南后街82号）**，每逢节气皆有大师展演。对衍生文创感兴趣，欢迎移步「闽礼」文创商城，那里有以软木画为灵感的夜灯、茉莉花茶制成的香膏等现代匠心之作。', '2025-12-16 16:57:36');
INSERT INTO `log_ai_chat` VALUES (12, 1, '福州有什么好吃的？', '福州美食众多，推荐您尝试佛跳墙、荔枝肉、福州鱼丸、肉燕（太平燕）、锅边糊等。', '2025-12-16 17:05:52');
INSERT INTO `log_ai_chat` VALUES (13, 1, '介绍一下德化白瓷', '德化白瓷是福建省德化县特产，中国国家地理标志产品。其质地坚密，晶莹如玉，釉面滋润似脂，故有“中国白”之美称。', '2025-12-16 17:10:52');
INSERT INTO `log_ai_chat` VALUES (14, 1, '德化白瓷介绍', '德化白瓷，又称“中国白”，是福建德化县千年窑火淬炼出的艺术瑰宝。其胎骨细密如脂，釉色温润似玉，在明代就被欧洲贵族誉为“东方艺术明珠”。德化瓷塑尤其精妙——何朝宗等大师创作的观音造像，衣纹流畅如云水，神态慈悲含禅意，堪称“瓷坛观音，天下共宝”。\n\n如今德化匠人既传承古法“象牙白”烧制技艺，又创新推出“孩儿红”“猪油白”等釉色。您若想近距离感受这“白如雪、明如镜、薄如纸、声如磬”的瓷韵，不妨移步我们福建非遗文创商城，那里有融合传统技艺与现代美学的茶器、香道用具等雅物可供赏鉴。', '2025-12-18 09:25:23');
INSERT INTO `log_ai_chat` VALUES (15, 1, '请详细介绍一下福建非遗项目：南音，包括它的历史渊源和艺术特点。', '（以闽南语腔调开场）哎呀，这位朋友真是有眼光！南音可是咱们闽南文化的“活化石”啊。让我泡壶铁观音，慢慢同您讲——\n\n南音起源于唐宋时期，是中原古乐与闽南本土音乐融合的结晶。您听那琵琶横抱的姿势，正是唐代教坊遗风；唱词中保留的“恁”“阮”等古语，活脱脱是穿越千年的声音密码。明朝时随闽商下南洋，现在东南亚的“福建会馆”里还能听到原汁原味的南音呢！\n\n（轻叩茶盏打节拍）要说艺术特点嘛——首先是“慢”：一曲《梅花操》要唱三炷香功夫，每个字都在鼻腔、胸腔、腹腔转三转。其次是“雅”：乐器必有“上四管”（洞箫、二弦、三弦、琵琶）和“下四管”（响盏、小叫、四宝、扁鼓），演奏时讲究“起、承、转、合”如文人作画。最妙的是“韧”：老艺人常说“南音无谱，心中有尺”，那些《走马》《百鸟归巢》的工乂谱，其实都是师徒口传心授的活传统。\n\n（突然压低声音）去年元宵，我在泉州府文庙听八十岁的蔡雅艺先生唱《陈三五娘》，那洞箫声一起啊，满庭的玉兰花都跟着颤——这才是真正的“御前清音”呐！', '2025-12-18 09:30:47');
INSERT INTO `log_ai_chat` VALUES (16, 1, '请详细介绍一下福建非遗项目：南音，包括它的历史渊源和艺术特点。', '（以闽南语腔调开场）哎呀，这位朋友真是识货！南音可是咱们闽南文化的“活化石”啊。让我给您慢慢道来——\n\n南音起源于唐，形成于宋，在泉州一带发源，至今已有一千多年历史。您知道吗？它保留着汉代《相和歌》“丝竹更相和，执节者歌”的古老形制，（手指轻敲桌面模拟拍板节奏）用的工尺谱比现代简谱早了七百多年呢！\n\n（端起茶杯润喉）最妙的是它的“管门”体系——五空管、四空管、五空四亻乂管，就像闽南老厝的榫卯结构，严谨又灵动。乐器更是讲究：洞箫要取竹节等长的“十目九节”，琵琶横抱如揽月，二弦的弓毛要用马尾八十根...（突然压低声音）去年我们在中山路老茶馆办雅集，台湾来的弦友听到《八骏马》第三段“骅骝开道”时，眼泪都掉到茶杯里了。\n\n（展开手掌比划）您要是想亲身体验，每月初一十五泉州府文庙有免费场，金苍绣的戏服在灯下会泛出暗纹龙鳞。对了，（从袖中取出手机）这是我们非遗文创商城的二维码，里头有南音元素的茶器香器，买对相思木拍板还能送工尺谱描红本哩！', '2025-12-18 10:05:42');
INSERT INTO `log_ai_chat` VALUES (17, 1, '请详细介绍一下福建非遗项目：福州脱胎漆器髹饰技艺，包括它的历史渊源和艺术特点。', '（以闽地特有的温润语调）说起福州脱胎漆器啊，那可是咱们闽都的\"三宝\"之首呢！这项技艺最早可以追溯到南宋时期，在清代沈绍安先生手中真正发扬光大。您知道吗？沈先生从破旧匾额夹苎工艺中得到启发，独创了\"脱胎\"技法——先用泥土塑形，再用夏布和生漆逐层裱褙，最后浸水脱去泥胎，留下轻如蝉翼的漆器骨架。这手艺啊，就像闽江的水，看似柔润却蕴含着千年不褪色的坚韧。\n\n（轻轻展开一卷仿古漆画）您瞧这牡丹纹样，用的是传承三百多年的\"薄料拍敷\"绝技——将金箔银箔研磨成粉调入漆中，再用特制丝棉球轻轻拍打上色。阳光下看，这些花纹会随着光线流转产生微妙变化，我们福州人称之为\"漆语\"。当年在巴拿马万国博览会上，那对《荷叶瓶》就是靠着这种流光溢彩的\"闽漆秘色\"摘得金奖呢！\n\n（指向工作坊里正在阴干的半成品）现在这些作品啊，都保存在三坊七巷的漆艺博物馆里。若是您想请一件回家，不妨去西湖畔的文创商城看看，那里有老师傅们新制的脱胎漆器茶具，既保留了古法，又融入了福州茉莉花茶的清香意境呢。', '2025-12-18 10:11:31');
INSERT INTO `log_ai_chat` VALUES (18, 1, '德化白瓷介绍', '德化白瓷，又称“中国白”，是福建省德化县传承千年的陶瓷瑰宝，以其**温润如玉、胎质细腻、釉色如脂**的特点享誉世界。明代何朝宗等大师创作的瓷塑作品，更是将德化白瓷推向艺术巅峰，被誉为“东方艺术明珠”。\n\n### **三大核心特色**\n1. **釉色之美**  \n   独特的乳白釉色，如凝脂冻玉，光照下透出暖黄或粉润光泽，西方人称之为“鹅绒白”“象牙白”。这种釉色源于德化特有的高岭土与匠人对窑火温度的精准掌控。\n\n2. **胎骨之精**  \n   胎土淘洗极细，烧结后致密透光，叩击声如金石。薄胎作品可透光见影，厚重器物则显沉稳雍容。\n\n3. **塑形之绝**  \n   尤以佛道造像闻名——观音衣袂翩跹如行云流水，达摩须发纤毫毕现。何朝宗“八字诀”（雕、塑、捏、推、接、贴、刻、划）至今仍是行业秘传。\n\n### **千年窑火传承**\n- **宋代奠基**：德化窑始烧于宋，早期青白瓷已显玉质感。\n- **明代鼎盛**：随着“海上丝绸之路”远销欧洲，成为贵族珍藏。法国人称为“Blanc de Chine”（中国白）。\n- **当代创新**：非遗传承人将传统技法与现代美学结合，推出茶器、香道、艺术瓷等新品，让古老技艺融入当代生活。\n\n### **鉴别小窍门**\n- 真品触手温润，仿品常带玻璃质冷感。\n- 自然光下细看釉面，经典德化白瓷有**糯米胎般的微透质感**。\n- 人物雕塑神态静谧祥和，衣纹线条如风吹水流，机械仿品往往生硬呆板。\n\n### **活态传承**\n今日德化匠人既复刻明代经典，也创作符合现代审美的茶器、花器。当地每年举办“中国白”国际陶瓷艺术大赛，千年窑火在创新中生生不息。\n\n若您想近距离感受这份“土与火的艺术”，欢迎关注德化非遗展演或官方文创平台，那里有匠人亲手制作的茶盏、香炉等雅器，让东方美学融入日常点滴。', '2025-12-18 10:15:49');
INSERT INTO `log_ai_chat` VALUES (19, 1, '请详细介绍一下福建非遗项目：福州茉莉花茶窨制工艺，包括它的历史渊源和艺术特点。', '（轻抿一口茉莉花茶，茶香氤氲）说起这福州茉莉花茶窨制工艺啊，那可是咱们闽都的\"天香\"绝艺。早在宋代，福州就有\"茉莉盈城飘香\"的盛景，到了明清时期，窨制工艺已臻成熟——您知道吗？光绪年间福州茉莉花茶还曾远销欧美，在巴拿马万国博览会上拿过金奖呢！\n\n（轻抚茶盏边缘）这手艺最妙处在于\"七窨一提\"的匠心：每年盛夏子夜，待茉莉花将开未开时采摘，与烘青绿茶层层相叠。花吐香，茶吸香，历经九道工序、百余日守候，方成就\"茶引花香，花增茶味\"的灵韵。您瞧这茶汤，澄黄明亮；细闻之，既有绿茶的清冽，又带茉莉的鲜灵，正所谓\"冰糖甜、龙眼味\"，这便是福州地理标志的独到之处了。\n\n（将茶盏推向光处）去年这项技艺刚入选联合国教科文组织非遗名录，现在三坊七巷里还能见到老师傅现场演示古法窨制。若想品味这缕穿越千年的芬芳，不妨去我们福建非遗文创商城看看，那儿有结合现代设计的茉莉花茶礼盒，把福州城的夏夜香气都封存在里头哩。', '2025-12-18 10:16:40');
INSERT INTO `log_ai_chat` VALUES (20, 1, '推荐送长辈的礼物', '（用闽南语亲切问候）阿伯阿嬷好！送长辈礼物，最合心意莫过于咱厝的非遗好物。推荐三样“福气满满”的伴手礼：\n\n1️⃣ **德化白瓷“福寿双全”茶具**  \n   - 选用“中国白”温润如玉的羊脂瓷，壶身手工雕刻寿桃纹，寓意“茶寿延年”\n   - 配上闽南老茶人最爱的乌龙茶饼，整套用大红漆器礼盒装着，喜庆又雅致\n\n2️⃣ **漆线雕“松鹤延年”摆件**  \n   - 厦门蔡氏传人手工盘绕金线，仙鹤羽毛细过发丝却根根立体\n   - 特别适合摆在客厅多宝阁，晨起喝茶时看着就心情舒畅\n\n3️⃣ **闽南红砖拓片养生礼盒**  \n   - 将老厝墙角的“龟背纹”“万字纹”拓在宣纸上，裱成便携画卷\n   - 附赠泉州老药铺配的端午养生香囊，檀香里带着淡淡茶花香\n\n（压低声音像说悄悄话）要是想看看实物，建议去**三坊七巷的福建非遗文创馆**，那儿老师傅现场演示技艺，还能在礼盒提手上系段闽剧水袖流苏，走动时飘飘扬扬特别有韵味。需要帮您问问最近有没有孝亲敬老主题的体验活动吗？', '2025-12-18 11:30:09');
INSERT INTO `log_ai_chat` VALUES (21, 1, '推荐送长辈的礼物', '为长辈挑选礼物时，可以结合福建的非物质文化遗产特色，选择既有文化内涵又实用贴心的礼物。以下推荐几类适合赠送长辈的福建非遗礼物：\n\n---\n\n### **一、传统工艺类（兼具艺术与实用性）**\n1. **福州脱胎漆器**  \n   - **特点**：轻巧耐用、色泽典雅，是“福州三宝”之一，适合作为家居装饰或收藏品。  \n   - **推荐**：漆器茶具、花瓶、首饰盒，既显品味又实用。  \n\n2. **德化白瓷**  \n   - **特点**：温润如玉，素有“中国白”之称，工艺精湛。  \n   - **推荐**：白瓷茶具、观音像、摆件，适合喜爱茶道或佛教文化的长辈。  \n\n3. **闽侯木雕（软木画）**  \n   - **特点**：以精细的雕刻技艺展现山水意境，富有文人气息。  \n   - **推荐**：木雕屏风、挂画，适合装饰家居，寓意吉祥。  \n\n---\n\n### **二、茶文化类（福建茶非遗）**\n1. **武夷岩茶（大红袍）制作技艺**  \n   - **推荐**：精选武夷岩茶礼盒，搭配建盏或紫砂壶，适合爱茶的长辈。  \n   - **贴士**：可附茶文化解说，增加礼物的仪式感。  \n\n2. **安溪铁观音制作技艺**  \n   - **推荐**：传统炭焙铁观音，香气醇厚，对长辈肠胃温和。  \n\n---\n\n### **三、健康养生类**\n1. **闽南药膳（如泉州养生汤料包）**  \n   - **特点**：结合福建本地草药，适合煲汤调理身体。  \n   - **推荐**：搭配非遗药膳配方，如“四物汤”“陈皮老姜茶”等。  \n\n2. **福州线面（寿面）**  \n   - **特点**：手工制作，寓意长寿，适合生日或节庆赠送。  \n   - **贴士**：搭配礼盒包装，附上吉祥话更显心意。  \n\n---\n\n### **四、服饰文化类**\n1. **闽绣（福州软木画刺绣）**  \n   - **推荐**：刺绣丝巾、披肩，图案常为花鸟或福字，典雅大方。  \n\n2. **惠安女服饰元素饰品**  \n   - **推荐**：改良款布包、刺绣钱包，融合传统元素，日常可用。  \n\n---\n\n### **五、体验类礼物（适合新潮长辈）**\n1. **非遗体验券**  \n   - **推荐**：闽剧演出票、茶艺 workshop、漆器制作体验课，让长辈感受文化魅力。  \n\n2. **定制影集**  \n   - **推荐**：将家族照片与福建非遗元素（如土楼、闽剧脸谱）结合，制作成纪念册。  \n\n---\n\n### **挑选建议**\n- **注重实用性**：长辈多偏好实用物品，如茶具、服饰、养生品。  \n- **寓意吉祥**：选择有“福”“寿”“安康”等元素的设计，如漆器福字摆件。  \n- **包装精致**：非遗礼物适合搭配传统礼盒，附上手写祝福卡更显诚意。  \n\n如果长辈有特定喜好（如喝茶、听戏、收藏），可进一步细化选择。需要更具体的推荐，可以告诉我长辈的兴趣哦！', '2025-12-20 19:46:13');
INSERT INTO `log_ai_chat` VALUES (22, 1, '霞浦的历史', '霞浦县位于福建省东北部，隶属宁德市，是闽东沿海的历史文化名县，其历史源远流长，与海洋文化、移民迁徙、海上贸易及多元信仰紧密相连。以下是霞浦历史的主要脉络：\n\n---\n\n### **一、古代建制与早期开发**\n1. **先秦至秦汉**  \n   - 早在新石器时代，霞浦已有先民活动，留下壳丘头等遗址。\n   - 秦汉时期属闽越地，汉武帝平定闽越后，隶属会稽郡。三国时期，东吴在此设立温麻船屯（治所在今沙江镇），成为重要的水军基地和造船中心，为海上活动奠定基础。\n\n2. **晋唐建制**  \n   - 西晋太康三年（282年）设立温麻县，为霞浦建县之始，属晋安郡。\n   - 唐代属长溪县，隶属福州。这一时期，中原移民南迁，带来农耕与手工业技术，沿海渔业和盐业逐渐发展。\n\n---\n\n### **二、宋元明清的繁荣与变迁**\n1. **宋代**  \n   - 北宋元丰四年（1081年）设立长溪县，隶属福州。南宋时，海上贸易兴盛，霞浦的赤岸、三沙等地成为重要港口，与泉州、福州联动，参与“海上丝绸之路”贸易。\n   - **赤岸**尤为著名，是日本遣唐使和商船的重要登陆点，至今保留相关遗迹。\n\n2. **元代**  \n   - 属福州路福宁州。元朝重视海运，霞浦的盐业和渔业规模进一步扩大。\n\n3. **明代**  \n   - 明成化九年（1473年）设立福宁州，霞浦为州治所在地，成为闽东政治、经济中心。\n   - 倭寇侵扰频繁，霞浦沿海修筑烽火台、城堡（如大京古城堡），形成独特的海防文化。\n\n4. **清代**  \n   - 雍正十二年（1734年）升福宁州为福宁府，霞浦为府治。清代中后期，茶叶、海产贸易繁荣，三沙港成为闽浙沿海重要商埠。\n   - 基督教、天主教随海上贸易传入，留下多元宗教遗迹。\n\n---\n\n### **三、近现代发展**\n1. **民国时期**  \n   - 1913年废府置县，正式定名“霞浦县”。抗战时期，沿海港口受封锁，经济一度停滞，但渔业和盐业仍是支柱产业。\n\n2. **新中国成立后**  \n   - 1988年宁德撤地设市，霞浦属宁德市管辖。\n   - 改革开放后，霞浦依托海洋资源，发展水产养殖（如海带、紫菜、大黄鱼）、海上贸易和旅游业，成为“中国海带之乡”“中国紫菜之乡”。\n\n---\n\n### **四、历史文化特色**\n1. **海洋文化遗产**  \n   - **妈祖信仰**：沿海村落遍布妈祖庙，体现海洋族群的守护神崇拜。\n   - **疍民文化**：历史上疍民（水上居民）在霞浦沿海生活，留下独特的船屋、渔歌等文化遗产。\n   - **海上丝绸之路遗迹**：赤岸码头、宋代古窑址等见证古代贸易往来。\n\n2. **非遗与民俗**  \n   - **霞浦布袋戏**、**铁枝木偶戏**等民间艺术被列入非物质文化遗产。\n   - **传统节庆**如“妈祖诞辰”“海上龙舟赛”融合闽东与海洋特色。\n\n3. **历史名人**  \n   - **林嵩**：唐代诗人，福建首位进士，霞浦赤岸人。  \n   - **王伯大**：宋代名臣，以清廉著称。  \n   - **张以宁**：元末明初文学家，被誉为“闽中诗坛宗师”。\n\n---\n\n### **五、今日霞浦**\n霞浦以“中国最美滩涂”闻名，摄影和旅游业蓬勃发展（如北岐、小皓滩涂）。同时，作为福建海洋经济重点区域，现代水产养殖、海上风电等产业与历史文化保护并重，延续着“山海交融”的独特风貌。\n\n---\n\n霞浦的历史是一部**海洋与陆地交织的史诗**，从古代军港到贸易枢纽，从海防前线到生态宜居之地，其文化底蕴深厚，是闽东地区海洋文明的重要缩影。', '2025-12-20 19:55:23');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码(加密存储)',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `role` tinyint NULL DEFAULT 0 COMMENT '角色 0:普通用户 1:管理员',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '123456', '系统管理员', NULL, 1, '2025-12-13 15:07:17');
INSERT INTO `sys_user` VALUES (2, 'liuwencai', '123456', '柳文财', NULL, 0, '2025-12-13 15:07:17');
INSERT INTO `sys_user` VALUES (3, 'lzy', '123456', '林卓越', NULL, 0, '2025-12-16 16:06:14');
INSERT INTO `sys_user` VALUES (4, 'thesis_demo', '123456', 'demon', NULL, 0, '2025-12-16 16:57:00');
INSERT INTO `sys_user` VALUES (5, 'heritage_master', '123456', '非遗传承人_林师傅', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', 0, '2025-12-16 17:15:52');
INSERT INTO `sys_user` VALUES (6, 'tea_lover_99', '123456', '武夷茶客', 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png', 0, '2025-12-16 17:15:52');
INSERT INTO `sys_user` VALUES (7, 'history_geek', '123456', '闽都旧事', 'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png', 0, '2025-12-16 17:15:52');
INSERT INTO `sys_user` VALUES (8, 'art_student_xm', '123456', '厦大艺术生', 'https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg', 0, '2025-12-16 17:15:52');
INSERT INTO `sys_user` VALUES (9, 'travel_fujian', '123456', '清新福建游', 'https://cube.elemecdn.com/c/6e/8c505d054d5b244d2d4d98993f4e3png.png', 0, '2025-12-16 17:15:52');
INSERT INTO `sys_user` VALUES (10, 'test01', '123456', '测试', NULL, 0, '2025-12-18 10:11:11');

SET FOREIGN_KEY_CHECKS = 1;
