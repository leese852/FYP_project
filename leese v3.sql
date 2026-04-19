-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- 主機: 127.0.0.1
-- 產生時間： 2026-04-17 15:10:57
-- 伺服器版本: 10.1.30-MariaDB
-- PHP 版本： 5.6.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `leese`
--

-- --------------------------------------------------------

--
-- 資料表結構 `address`
--

CREATE TABLE `address` (
  `id` int(11) NOT NULL,
  `userId` int(11) NOT NULL COMMENT '用戶id',
  `contactName` varchar(50) NOT NULL COMMENT '收貨人',
  `contactPhone` varchar(50) NOT NULL COMMENT '聯係電話',
  `address` varchar(512) NOT NULL COMMENT '地址',
  `isDefault` tinyint(4) DEFAULT '0' COMMENT '是否默認 1=default',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 1-刪除',
  `lng` decimal(10,7) DEFAULT NULL COMMENT '经度',
  `lat` decimal(10,7) DEFAULT NULL COMMENT '纬度'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 資料表的匯出資料 `address`
--

INSERT INTO `address` (`id`, `userId`, `contactName`, `contactPhone`, `address`, `isDefault`, `createTime`, `updateTime`, `isDelete`, `lng`, `lat`) VALUES
(2, 10, '张三', '53000849', '测试地址', 0, '2026-01-16 14:35:46', '2026-01-16 14:35:46', 0, NULL, NULL),
(3, 17, '张二', '53000849', '测试地址1', 1, '2026-01-16 15:14:57', '2026-01-16 15:14:57', 0, NULL, NULL),
(4, 17, '李四', '53000850', '地址2-非默认', 0, '2026-01-16 15:14:57', '2026-01-16 15:14:57', 0, NULL, NULL),
(5, 22, 'leese', '53000849', 'leese', 0, '2026-01-19 13:15:25', '2026-01-19 16:12:42', 0, NULL, NULL),
(6, 22, 'leese', '53000849', 'leese', 0, '2026-01-19 13:15:27', '2026-01-19 14:09:28', 0, NULL, NULL),
(7, 22, 'leese', '53000894', 'wdnmd', 1, '2026-01-19 13:16:40', '2026-01-19 14:09:31', 0, NULL, NULL),
(8, 24, 'eee', '12345678', 'Testtt', 1, '2026-01-30 10:09:48', '2026-01-30 10:09:48', 0, NULL, NULL),
(9, 27, 'adsaasd', '12312421', 'dafesfew', 0, '2026-02-02 22:42:10', '2026-02-02 22:42:10', 0, NULL, NULL),
(10, 28, 'cdvfd', '32442353', 'egfdrgdr', 0, '2026-02-16 16:30:58', '2026-02-16 16:30:58', 0, NULL, NULL),
(11, 29, 'asdas', '12345678', 'eadadaw', 0, '2026-04-17 19:38:41', '2026-04-17 19:38:41', 0, NULL, NULL),
(12, 29, 'adada', '13123112', 'Coordinates: 114.158980, 22.282999', 0, '2026-04-17 20:17:14', '2026-04-17 20:17:14', 0, '114.1589800', '22.2829990'),
(13, 29, 'hghjg', '12345678', 'Coordinates: 114.163725, 22.323574', 0, '2026-04-17 20:21:11', '2026-04-17 20:21:11', 0, '114.1637255', '22.3235739'),
(14, 29, '4', '12345678', 'Coordinates: 114.175980, 22.321783', 0, '2026-04-17 20:38:43', '2026-04-17 20:38:43', 0, '114.1759804', '22.3217829'),
(15, 29, 'asdas', '12345678', 'Coordinates: 114.180615, 22.324344', 0, '2026-04-17 20:49:37', '2026-04-17 20:49:37', 0, '114.1806148', '22.3243442'),
(16, 29, 'adad', '12345678', '香港旺角新填地街255～261號', 0, '2026-04-17 20:56:27', '2026-04-17 20:56:27', 0, '114.1685465', '22.3145574');

-- --------------------------------------------------------

--
-- 資料表結構 `cart`
--

CREATE TABLE `cart` (
  `id` int(11) NOT NULL,
  `name` varchar(32) DEFAULT NULL COMMENT '商品名称',
  `userId` int(11) NOT NULL COMMENT '主键',
  `dishId` int(11) DEFAULT NULL COMMENT '菜品id',
  `dishFlavor` varchar(50) DEFAULT NULL COMMENT '口味',
  `number` int(11) NOT NULL DEFAULT '1' COMMENT '数量',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 資料表的匯出資料 `cart`
--

INSERT INTO `cart` (`id`, `name`, `userId`, `dishId`, `dishFlavor`, `number`, `amount`, `create_time`) VALUES
(2134425602, '老版宫保鸡丁', 24, 1, '不辣, 多糖', 1, '35.00', NULL);

-- --------------------------------------------------------

--
-- 資料表結構 `categories`
--

CREATE TABLE `categories` (
  `id` int(11) NOT NULL,
  `categoryName` varchar(50) NOT NULL COMMENT '類別名稱',
  `displayOrder` int(11) DEFAULT '0' COMMENT '顯示權重',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 1-刪除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 資料表結構 `dish`
--

CREATE TABLE `dish` (
  `id` int(11) NOT NULL COMMENT 'dish id',
  `dishName` varchar(100) DEFAULT NULL COMMENT '菜品名稱',
  `price` decimal(10,2) NOT NULL COMMENT '價錢',
  `categoryId` int(11) NOT NULL COMMENT '類別ID',
  `description` varchar(512) DEFAULT NULL COMMENT '菜品介紹',
  `imgUrl` longblob COMMENT '圖片',
  `isAvailable` tinyint(4) DEFAULT '1' COMMENT '0-禁用/下架 1-正常',
  `createUser` int(11) DEFAULT NULL COMMENT '員工創建',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 1-刪除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 資料表的匯出資料 `dish`
--

INSERT INTO `dish` (`id`, `dishName`, `price`, `categoryId`, `description`, `imgUrl`, `isAvailable`, `createUser`, `createTime`, `updateTime`, `isDelete`) VALUES
(1, '老版宫保鸡丁', '35.00', 1, NULL, NULL, 1, NULL, '2025-12-17 16:56:28', '2025-12-17 16:56:28', 0),
(2, '53535353535', '58.00', 2, '新菜品', NULL, 1, NULL, '2025-12-17 16:56:28', '2026-01-30 10:15:37', 0),
(3, '三杯鸡', '120.00', 2, '传统三杯料理，鸡肉鲜嫩入味', 0x2f696d616765732f73616e6265696a692e6a7067, 1, 0, '2026-02-02 22:39:38', '2026-02-02 22:39:38', 0),
(4, '新菜品', '20.00', 1, '很好吃的新菜品', NULL, 1, NULL, '2026-01-23 16:25:49', '2026-01-23 16:25:49', 0),
(5, '珍珠奶茶', '45.00', 3, '经典波霸奶茶，甜度可调整', 0x2f696d616765732f627562626c652d7465612e6a7067, 1, 0, '2026-02-02 22:39:38', '2026-02-02 22:41:13', 0),
(6, '盐酥鸡', '55.00', 5, '现炸盐酥鸡，外酥内嫩', 0x2f696d616765732f79616e73756a692e6a7067, 1, 0, '2026-02-02 22:39:38', '2026-02-02 22:39:38', 0),
(7, '烫青菜', '30.00', 6, '时令蔬菜，可搭配蒜蓉或酱油', 0x2f696d616765732f766567657461626c65732e6a7067, 1, 0, '2026-02-02 22:39:38', '2026-02-02 22:39:38', 0),
(8, '红烧狮子头', '95.00', 2, '手工肉丸搭配特制红烧酱汁', 0x2f696d616765732f6c696f6e2d686561642e6a7067, 1, 0, '2026-02-02 22:39:38', '2026-02-02 22:39:38', 0),
(9, '芒果冰沙', '60.00', 4, '新鲜芒果制作的清凉冰沙', 0x2f696d616765732f6d616e676f2d736d6f6f746869652e6a7067, 1, 0, '2026-02-02 22:39:38', '2026-02-02 22:39:38', 0),
(10, '炒空心菜', '40.00', 6, '大火快炒的空心菜，锅气十足', 0x2f696d616765732f77617465722d7370696e6163682e6a7067, 1, 0, '2026-02-02 22:39:38', '2026-02-02 22:39:38', 0),
(11, '台式卤肉饭', '65.00', 1, '经典台湾卤肉，肥瘦相间，搭配卤蛋和青菜', 0x2f696d616765732f6c75726f7566616e2e6a7067, 1, 0, '2026-02-02 22:39:38', '2026-02-02 22:39:38', 0),
(22, '牛肉面', '85.00', 1, '手工面条搭配炖煮入味的牛腱肉', 0x2f696d616765732f6e6975726f756d69616e2e6a7067, 1, 0, '2026-02-02 22:39:38', '2026-02-02 22:39:38', 0),
(24, '蚵仔煎', '70.00', 2, '新鲜牡蛎搭配鸡蛋和青菜', 0x2f696d616765732f6f79737465722d6f6d656c65742e6a7067, 1, 0, '2026-02-02 22:39:38', '2026-02-02 22:40:54', 0),
(25, 'zczx', '123.00', 6, 'zscz', NULL, 1, NULL, '2026-02-03 17:09:21', '2026-02-03 17:09:21', 0);

-- --------------------------------------------------------

--
-- 資料表結構 `dish_flavor`
--

CREATE TABLE `dish_flavor` (
  `id` int(11) NOT NULL,
  `tag` varchar(64) NOT NULL COMMENT '标签 eg. 温度',
  `list` varchar(255) NOT NULL COMMENT '标签子列表 eg[热,温,冷]',
  `dishId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 資料表的匯出資料 `dish_flavor`
--

INSERT INTO `dish_flavor` (`id`, `tag`, `list`, `dishId`) VALUES
(-625426430, '甜度', '正常,少糖,多糖', 2),
(-625426429, '辣度', '不辣,微辣,中辣,重辣', 2),
(813101058, '辣度', '不辣,微辣,中辣,重辣', 1),
(825683970, '甜度', '正常,少糖,多糖', 1),
(1714831362, '辣度', '微辣，特辣', 4),
(1863307266, 'zzz', 'zz', 25),
(1863307267, 'zxczx', 'czz', 25);

-- --------------------------------------------------------

--
-- 資料表結構 `employee`
--

CREATE TABLE `employee` (
  `id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL DEFAULT '员工',
  `account` varchar(64) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(16) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `gender` tinyint(4) DEFAULT NULL,
  `pic` longblob,
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `userRole` int(11) NOT NULL DEFAULT '0' COMMENT '用户角色 0-普通用户 1-管理员',
  `create_user` int(11) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 資料表結構 `feedback`
--

CREATE TABLE `feedback` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `content` text NOT NULL COMMENT '反馈内容',
  `type` varchar(50) NOT NULL DEFAULT 'SUGGESTION' COMMENT '反馈类型：SUGGESTION/COMPLAINT/PRAISE',
  `status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING/PROCESSED',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0' COMMENT '逻辑删除 0-未删除 1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户反馈表';

--
-- 資料表的匯出資料 `feedback`
--

INSERT INTO `feedback` (`id`, `user_id`, `content`, `type`, `status`, `created_at`, `updated_at`, `is_deleted`) VALUES
(1, 1, '菜品味道很好，建议增加辣度选项', 'SUGGESTION', 'PROCESSED', '2026-01-29 17:13:16', '2026-01-30 11:18:12', 0),
(2, 2, '配送时间比预计晚了30分钟', 'COMPLAINT', 'PROCESSED', '2026-01-29 17:13:16', '2026-01-29 17:13:16', 0),
(3, 3, '服务态度非常好，下次还会来', 'PRAISE', 'PENDING', '2026-01-29 17:13:16', '2026-01-29 19:16:18', 0),
(4, 1, '希望可以有更多的支付方式', 'SUGGESTION', 'PROCESSED', '2026-01-29 17:13:16', '2026-01-29 17:13:16', 0),
(5, 24, 'ttttttt', 'SUGGESTION', 'PENDING', '2026-01-30 10:04:05', '2026-01-30 10:23:21', 1),
(6, 24, '7661867176178611', 'PRAISE', 'PROCESSED', '2026-01-30 10:06:37', '2026-01-30 11:18:07', 0),
(7, 26, '3333', 'SUGGESTION', 'PROCESSED', '2026-01-30 10:45:06', '2026-01-30 11:18:19', 0);

-- --------------------------------------------------------

--
-- 資料表結構 `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `orderId` varchar(50) NOT NULL COMMENT '訂單編號',
  `userId` int(11) NOT NULL COMMENT '用户id',
  `addressId` int(11) NOT NULL COMMENT '地址id',
  `status` tinyint(4) DEFAULT '1' COMMENT '1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款',
  `totalAmount` decimal(10,2) DEFAULT NULL COMMENT '縂價錢',
  `payMethod` varchar(50) DEFAULT NULL COMMENT '支付方法',
  `payStatus` tinyint(4) NOT NULL DEFAULT '0' COMMENT '支付状态 0未支付 1已支付 2已退款',
  `remark` varchar(512) DEFAULT NULL COMMENT '備注',
  `cancelReason` varchar(255) DEFAULT NULL COMMENT '取消原因',
  `cancelTime` datetime DEFAULT NULL COMMENT '取消時間',
  `rejectionReason` varchar(255) DEFAULT NULL COMMENT '拒绝原因',
  `estimatedDeliveryTime` datetime DEFAULT NULL COMMENT '預計送達時間',
  `deliveryStatus` tinyint(4) NOT NULL DEFAULT '1' COMMENT '配送狀態 1立即送出  0选择具体时间',
  `deliveryTime` datetime DEFAULT NULL COMMENT '送達時間',
  `packAmount` int(11) DEFAULT NULL COMMENT '打包費',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 1-刪除',
  `riderId` bigint(20) DEFAULT NULL COMMENT '配送騎手ID，關聯rider表',
  `restaurant_lat` decimal(10,8) DEFAULT NULL COMMENT '餐厅纬度',
  `restaurant_lng` decimal(11,8) DEFAULT NULL COMMENT '餐厅经度',
  `customer_lat` decimal(10,8) DEFAULT NULL COMMENT '顾客纬度',
  `customer_lng` decimal(11,8) DEFAULT NULL COMMENT '顾客经度',
  `rider_earning` decimal(10,2) DEFAULT '0.00' COMMENT '骑手实际收入',
  `earning_multiplier` decimal(3,2) DEFAULT '1.00' COMMENT '收入倍数（1.2/1.0/0.9）',
  `actual_delivery_time` datetime DEFAULT NULL COMMENT '实际送达时间',
  `estimated_delivery_duration` int(11) DEFAULT NULL COMMENT '预计送达耗时（秒）',
  `preparation_status` tinyint(4) DEFAULT '0' COMMENT '0-待取餐,1-制作中,2-已取餐',
  `pickup_time` datetime DEFAULT NULL COMMENT '骑手取餐时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 資料表的匯出資料 `orders`
--

INSERT INTO `orders` (`id`, `orderId`, `userId`, `addressId`, `status`, `totalAmount`, `payMethod`, `payStatus`, `remark`, `cancelReason`, `cancelTime`, `rejectionReason`, `estimatedDeliveryTime`, `deliveryStatus`, `deliveryTime`, `packAmount`, `createTime`, `updateTime`, `isDelete`, `riderId`, `restaurant_lat`, `restaurant_lng`, `customer_lat`, `customer_lng`, `rider_earning`, `earning_multiplier`, `actual_delivery_time`, `estimated_delivery_duration`, `preparation_status`, `pickup_time`) VALUES
(1, 'ORD20260130001', 24, 501, 5, '299.99', 'Credit Card', 0, 'Leave at the door', NULL, NULL, NULL, '2026-01-30 12:30:00', 0, NULL, 10, '2026-01-30 09:45:00', '2026-02-02 22:33:48', 0, 301, NULL, NULL, NULL, NULL, '0.00', '1.00', NULL, NULL, 0, NULL),
(2, 'ORD1770043336571-53027d', 27, 9, 6, '90.00', '線上支付', 0, NULL, NULL, NULL, NULL, NULL, 1, '2026-04-15 02:58:23', 0, '2026-02-02 22:42:16', '2026-04-15 02:58:23', 0, 1, NULL, NULL, NULL, NULL, '0.00', '1.00', NULL, NULL, 0, NULL),
(4, 'ORD20231116002', 1004, 204, 5, '65.40', '1', 1, 'Leave at doorstep', NULL, NULL, NULL, '2023-11-16 13:30:00', 1, NULL, 1, '2023-11-16 13:00:00', '2026-02-03 17:08:07', 0, 503, NULL, NULL, NULL, NULL, '0.00', '1.00', NULL, NULL, 0, NULL),
(5, 'ORD20231116003', 1005, 205, 1, '320.00', '2', 1, 'Birthday cake - handle with care', NULL, NULL, NULL, '2023-11-16 20:15:00', 3, '2023-11-16 20:10:00', 5, '2023-11-16 19:30:00', '2023-11-16 20:10:00', 0, 504, NULL, NULL, NULL, NULL, '0.00', '1.00', NULL, NULL, 0, NULL),
(6, 'ORD20231117001', 1006, 206, 7, '42.50', '1', 0, 'Call before arrival', NULL, NULL, 'Out of stock items', '2023-11-17 11:45:00', 0, NULL, 1, '2023-11-17 11:20:00', '2026-02-03 14:43:37', 0, NULL, NULL, NULL, NULL, NULL, '0.00', '1.00', NULL, NULL, 0, NULL),
(7, 'ORD20231117002', 1007, 207, 1, '189.90', '3', 1, NULL, NULL, NULL, NULL, '2023-11-17 19:00:00', 2, NULL, 3, '2023-11-17 18:30:00', '2023-11-17 18:30:00', 0, 505, NULL, NULL, NULL, NULL, '0.00', '1.00', NULL, NULL, 0, NULL),
(8, 'ORD20231118001', 1008, 208, 1, '78.60', '1', 1, 'Vegetarian meal only', NULL, NULL, NULL, '2023-11-18 12:30:00', 3, '2023-11-18 12:25:00', 1, '2023-11-18 12:00:00', '2023-11-18 12:25:00', 0, 506, NULL, NULL, NULL, NULL, '0.00', '1.00', NULL, NULL, 0, NULL),
(10, 'ORD20231118003', 1009, 210, 1, '95.80', '1', 1, 'Office delivery - 5th floor', NULL, NULL, NULL, '2023-11-18 18:45:00', 1, NULL, 2, '2023-11-18 18:15:00', '2023-11-18 18:15:00', 0, 507, NULL, NULL, NULL, NULL, '0.00', '1.00', NULL, NULL, 0, NULL),
(23, 'ORD20231115002', 1002, 202, 7, '89.90', '2', 1, NULL, 'Changed my mind', '2023-11-15 18:30:00', NULL, '2023-11-15 20:00:00', 0, NULL, 2, '2023-11-15 18:15:00', '2026-02-03 14:43:46', 0, NULL, NULL, NULL, NULL, NULL, '0.00', '1.00', NULL, NULL, 0, NULL),
(32, 'ORD20231116001', 1003, 203, 1, '245.80', '3', 2, 'No onions, extra spicy', NULL, NULL, 'Restaurant busy', '2023-11-16 12:45:00', 2, NULL, 3, '2023-11-16 12:00:00', '2023-11-16 12:10:00', 0, 502, NULL, NULL, NULL, NULL, '0.00', '1.00', NULL, NULL, 0, NULL),
(50, 'ORD20231115001', 1001, 201, 1, '158.50', '1', 1, 'Please deliver after 6 PM', NULL, NULL, NULL, '2023-11-15 19:30:00', 3, '2023-11-15 19:25:00', 2, '2023-11-15 18:00:00', '2023-11-15 19:25:00', 0, 501, NULL, NULL, NULL, NULL, '0.00', '1.00', NULL, NULL, 0, NULL),
(51, 'ORD1770100146890-de3f37', 27, 9, 6, '345.00', '線上支付', 0, NULL, NULL, NULL, NULL, NULL, 1, NULL, 0, '2026-02-03 14:29:06', '2026-02-03 14:31:16', 0, NULL, NULL, NULL, NULL, NULL, '0.00', '1.00', NULL, NULL, 0, NULL),
(52, 'ORD1770109606016-233284', 27, 9, 6, '1035.00', '線上支付', 0, NULL, NULL, NULL, NULL, NULL, 1, NULL, 0, '2026-02-03 17:06:46', '2026-02-03 17:27:03', 0, NULL, NULL, NULL, NULL, NULL, '0.00', '1.00', NULL, NULL, 0, NULL),
(53, 'ORD1770111120443-ce228a', 27, 9, 6, '90.00', '線上支付', 0, NULL, NULL, NULL, NULL, NULL, 1, NULL, 0, '2026-02-03 17:32:00', '2026-02-03 17:36:19', 0, NULL, NULL, NULL, NULL, NULL, '0.00', '1.00', NULL, NULL, 0, NULL),
(54, 'ORD1771230665655-be25d6', 28, 10, 5, '78.00', '線上支付', 0, NULL, NULL, NULL, NULL, NULL, 1, NULL, 0, '2026-02-16 16:31:05', '2026-04-16 14:31:03', 0, 1, '22.31930000', '114.16940000', '22.28150000', '114.15820000', '3.90', '1.00', NULL, 858, 2, '2026-04-16 14:31:03'),
(55, 'ORD20260408001', 28, 10, 6, '128.00', '線上支付', 0, '测试订单1', NULL, NULL, NULL, NULL, 1, NULL, NULL, '2026-04-08 20:15:33', '2026-04-16 16:13:28', 0, 1, '22.31930000', '114.16940000', '22.29300000', '114.17200000', '5.76', '0.90', '2026-04-16 16:13:28', 581, 2, '2026-04-16 14:31:32'),
(56, 'TEST_NEW_001', 28, 10, 6, '100.00', '線上支付', 0, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2026-04-16 13:33:53', '2026-04-16 14:26:16', 0, 1, '22.31930000', '114.16940000', '22.28150000', '114.15820000', '6.00', '1.20', '2026-04-16 08:26:16', 858, 2, '2026-04-16 08:25:01'),
(57, 'ORD1776430923488-941f80', 29, 16, 2, '58.00', '線上支付', 0, NULL, NULL, NULL, NULL, NULL, 1, NULL, 0, '2026-04-17 21:02:03', '2026-04-17 21:02:03', 0, NULL, NULL, NULL, NULL, NULL, '0.00', '1.00', NULL, NULL, 0, NULL),
(58, 'ORD1776431159531-5efd0a', 29, 11, 2, '58.00', '線上支付', 0, NULL, NULL, NULL, NULL, NULL, 1, NULL, 0, '2026-04-17 21:05:59', '2026-04-17 21:05:59', 0, NULL, NULL, NULL, NULL, NULL, '0.00', '1.00', NULL, NULL, 0, NULL);

-- --------------------------------------------------------

--
-- 資料表結構 `order_items`
--

CREATE TABLE `order_items` (
  `id` int(11) NOT NULL,
  `orderId` int(11) NOT NULL COMMENT '訂單id',
  `dishId` int(11) NOT NULL COMMENT '菜品id',
  `dishName` varchar(100) DEFAULT NULL COMMENT '菜品名稱快照',
  `dishFlavor` varchar(128) DEFAULT NULL COMMENT '菜品口味 JSON格式',
  `quantity` int(11) NOT NULL COMMENT '菜品數量',
  `price` decimal(10,2) NOT NULL COMMENT '價錢快照'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 資料表的匯出資料 `order_items`
--

INSERT INTO `order_items` (`id`, `orderId`, `dishId`, `dishName`, `dishFlavor`, `quantity`, `price`) VALUES
(1, 2, 1, '老版宫保鸡丁', NULL, 1, '35.00'),
(2, 2, 6, '盐酥鸡', NULL, 1, '55.00'),
(3, 51, 5, '珍珠奶茶', NULL, 3, '45.00'),
(4, 51, 1, '老版宫保鸡丁', NULL, 6, '35.00'),
(5, 52, 1, '老版宫保鸡丁', NULL, 3, '35.00'),
(6, 52, 11, '台式卤肉饭', NULL, 14, '65.00'),
(7, 52, 4, '新菜品', NULL, 1, '20.00'),
(8, 53, 1, '老版宫保鸡丁', NULL, 1, '35.00'),
(9, 53, 4, '新菜品', NULL, 1, '20.00'),
(10, 53, 1, '老版宫保鸡丁', NULL, 1, '35.00'),
(11, 54, 4, '新菜品', NULL, 1, '20.00'),
(12, 54, 2, '53535353535', NULL, 1, '58.00'),
(13, 57, 2, '53535353535', NULL, 1, '58.00'),
(14, 58, 2, '53535353535', NULL, 1, '58.00');

-- --------------------------------------------------------

--
-- 資料表結構 `payment_records`
--

CREATE TABLE `payment_records` (
  `id` bigint(20) NOT NULL,
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `amount` decimal(10,2) NOT NULL COMMENT '支付金额',
  `payment_method` varchar(50) NOT NULL COMMENT '支付方式：CREDIT_CARD, CASH, E_WALLET等',
  `status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '支付状态：SUCCESS, FAILED, PENDING',
  `transaction_id` varchar(100) DEFAULT NULL COMMENT '交易ID（模拟）',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';

-- --------------------------------------------------------

--
-- 資料表結構 `review`
--

CREATE TABLE `review` (
  `id` int(11) NOT NULL,
  `userId` int(11) NOT NULL COMMENT '用户id',
  `orderId` int(11) NOT NULL COMMENT '訂單id',
  `rating` tinyint(4) NOT NULL COMMENT '評分1-5，1最低',
  `comment` varchar(1000) DEFAULT NULL COMMENT '評論内容',
  `likes` int(11) DEFAULT '0' COMMENT '點贊數',
  `likesUserId` int(11) DEFAULT NULL COMMENT '点赞用户',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 1-刪除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 資料表結構 `review_dish_ratings`
--

CREATE TABLE `review_dish_ratings` (
  `id` bigint(20) NOT NULL,
  `review_id` bigint(20) NOT NULL,
  `dish_id` bigint(20) NOT NULL,
  `rating` tinyint(4) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 資料表結構 `rider`
--

CREATE TABLE `rider` (
  `employeeId` bigint(20) NOT NULL COMMENT '騎手ID',
  `name` varchar(100) NOT NULL COMMENT '騎手姓名',
  `role` varchar(50) DEFAULT NULL COMMENT '角色，例如配送員',
  `loginCredentials` varchar(255) DEFAULT NULL COMMENT '登入憑證或帳號資訊',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `restaurantId` bigint(20) DEFAULT NULL COMMENT '所屬餐廳ID',
  `phone` varchar(20) NOT NULL COMMENT '騎手電話',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否刪除 1-刪除',
  `current_lat` decimal(10,8) DEFAULT NULL COMMENT '当前纬度',
  `current_lng` decimal(11,8) DEFAULT NULL COMMENT '当前经度',
  `last_location_update` datetime DEFAULT NULL COMMENT '最后位置更新时间',
  `bank_account` varchar(50) DEFAULT NULL COMMENT '银行账号',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '银行名称',
  `account_holder` varchar(100) DEFAULT NULL COMMENT '账户持有人',
  `total_earned` decimal(10,2) DEFAULT '0.00' COMMENT '累计总收入',
  `available_balance` decimal(10,2) DEFAULT '0.00' COMMENT '可提现余额',
  `withdrawn_amount` decimal(10,2) DEFAULT '0.00' COMMENT '已提现金额'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 資料表的匯出資料 `rider`
--

INSERT INTO `rider` (`employeeId`, `name`, `role`, `loginCredentials`, `password`, `restaurantId`, `phone`, `createTime`, `updateTime`, `isDelete`, `current_lat`, `current_lng`, `last_location_update`, `bank_account`, `bank_name`, `account_holder`, `total_earned`, `available_balance`, `withdrawn_amount`) VALUES
(1, 'obo', '??', '12345678', '12345678', NULL, '23434457', '2026-04-05 22:33:10', '2026-04-16 16:13:28', 0, '22.31930000', '114.16940000', '2026-04-08 19:44:22', '3213', '12312', 'asdasda', '48.02', '14.02', '34.00'),
(2, 'OBO', '??', '1231231', '1231313', NULL, '13245234', '2026-04-15 02:18:46', '2026-04-15 02:18:46', 0, NULL, NULL, NULL, NULL, NULL, NULL, '0.00', '0.00', '0.00');

-- --------------------------------------------------------

--
-- 資料表結構 `rider_location_history`
--

CREATE TABLE `rider_location_history` (
  `id` int(11) NOT NULL,
  `rider_id` bigint(20) NOT NULL,
  `order_id` int(11) DEFAULT NULL,
  `latitude` decimal(10,8) NOT NULL,
  `longitude` decimal(11,8) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='骑手位置历史表';

-- --------------------------------------------------------

--
-- 資料表結構 `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL COMMENT 'id',
  `username` varchar(30) DEFAULT NULL COMMENT '用戶昵稱',
  `userPassword` varchar(512) NOT NULL COMMENT '密码',
  `userAccount` varchar(256) DEFAULT NULL COMMENT '用戶賬號',
  `avatarUrl` varchar(1024) DEFAULT NULL COMMENT '用户头像',
  `gender` tinyint(4) DEFAULT NULL COMMENT '性别 0-男 1-女',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `tel` varchar(128) DEFAULT NULL COMMENT '電話',
  `userStatus` int(11) DEFAULT '0' COMMENT '狀態 0-正常 1-禁用',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `userRole` int(11) NOT NULL DEFAULT '0' COMMENT '用户角色 0-普通用户 1-管理员'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 資料表的匯出資料 `user`
--

INSERT INTO `user` (`id`, `username`, `userPassword`, `userAccount`, `avatarUrl`, `gender`, `email`, `tel`, `userStatus`, `createTime`, `updateTime`, `isDelete`, `userRole`) VALUES
(1, NULL, '$2a$10$rM/Yg6QKkyDfC.htNroUzO3gh3u4zyu3FOhlxZWeUGHtwi4QNbCmq', 'leeseww', NULL, NULL, NULL, NULL, 0, '2025-12-31 16:21:13', '2025-12-31 16:21:13', 0, 0),
(2, NULL, '$2a$10$KCzBcR796uxzwnijUOBy..yx9wZSmpngNrdtV6LydO2I2hTsWLgO.', 'leese', NULL, NULL, NULL, NULL, 0, '2025-12-31 16:35:16', '2025-12-31 16:35:16', 0, 0),
(3, NULL, '$2a$10$bhQQMRq0r0Ldi16IEsVOk.nR1v19/nxLjrjsj5XTBlCyJMaGdUsla', 'leese44', NULL, NULL, NULL, NULL, 0, '2025-12-31 16:38:13', '2025-12-31 16:38:13', 0, 0),
(4, NULL, '$2a$10$ZQoEG4w.ZWFUGPXlpkA5GOMr7eUd/znJciw0koTDdqC47TBCKlUvy', 'wdnmd', NULL, NULL, NULL, NULL, 0, '2025-12-31 17:17:28', '2025-12-31 17:17:28', 0, 0),
(5, NULL, '$2a$10$IBdI/.LIa1QHlJSQCsGbTOoA2DJfxhIlnNbFEDW/.tIhuJApYox76', 'wdnm', NULL, NULL, NULL, NULL, 0, '2025-12-31 17:17:50', '2025-12-31 17:17:50', 0, 0),
(6, NULL, '$2a$10$5Aq3sSGlFv08306KEZfSG.R4cwScYz5V2/p4ERCjYlL4cktyBnhKa', 'wdnm55', NULL, NULL, NULL, NULL, 0, '2025-12-31 17:17:54', '2025-12-31 17:17:54', 0, 0),
(7, NULL, '$2a$10$Nv8.aQm1llHDYMg0n/czPu6LhSEw1CCgMSntY9pqc4QyydAUi5kmq', 'leese11', NULL, NULL, NULL, NULL, 0, '2025-12-31 17:19:18', '2025-12-31 17:19:18', 0, 0),
(8, NULL, '$2a$10$H9nObpEaNck3UXLXHgg/MuhRQxm.F1qRmOmHt11qJimDCClUABbV6', 'sdsm', NULL, NULL, NULL, NULL, 0, '2025-12-31 17:22:02', '2025-12-31 17:22:02', 0, 0),
(22, 'leese14514', '$2a$10$tJfks88j3AjMs1ieAX/jpOKaHEA5U/q4vSFGmOBFca3w6twNrGru6', 'leese145', NULL, 0, NULL, NULL, 0, '2026-01-19 13:11:45', '2026-01-19 16:53:13', 0, 0),
(23, 'leese123', '$2a$10$oTrD77x6gMDM2430QSmlsO/Z8pgZ1c525M9Gw5IVZBwXN/LswCgbG', 'leese123', NULL, NULL, NULL, NULL, 0, '2026-01-19 13:12:54', '2026-01-19 13:12:54', 0, 0),
(24, 'Test5', '$2a$10$4JUhBIqby.onhSEDHUuIyeLIRF18gBftzxNd37UGjjzlatziRQQPq', 'Test5', NULL, NULL, NULL, NULL, 0, '2026-01-29 18:46:36', '2026-01-29 18:46:36', 0, 0),
(25, 'Test6', '$2a$10$wl1u9qo4nSCrIeXO4wbju.u//fa.wQNYAgclHyuukdsHtsKecDps2', 'Test6', NULL, NULL, NULL, NULL, 0, '2026-01-30 10:11:12', '2026-01-30 10:11:12', 0, 0),
(26, 'Admin1', '$2a$10$Ys1H/qFvE/OH5RdibWdKu.Khd0Z3X8HYpf7JMQNEEFuhkL03R2lHG', 'Admin1', NULL, NULL, NULL, NULL, 0, '2026-01-30 10:19:35', '2026-01-30 10:19:48', 0, 1),
(27, '123abcdefg', '$2a$10$pJGFqqJVbdbKbQFMrXjDdOdArLcmtZ4fuOALH/d.TY5pTz8tQ22Qy', '123abcdefg', NULL, 1, 'dasdasd@dsad.com', '12345678', 0, '2026-02-02 22:34:26', '2026-02-03 14:26:32', 0, 1),
(28, '12345678', '$2a$10$Eb.kXiulHPs9V3e3Je/W2.d09pQIUmkRLoqpzvFNYJB/GSEzobVkW', '12345678', NULL, NULL, NULL, NULL, 0, '2026-02-16 16:30:04', '2026-02-16 16:30:04', 0, 0),
(29, '123456789', '$2a$10$wxtNCzw4eVzwMD.j8DfxE.Vo6OokfFw5PuyXApBRCce8Sue6Zm5t.', '123456789', NULL, NULL, NULL, NULL, 0, '2026-04-16 17:10:10', '2026-04-16 17:10:10', 0, 0);

-- --------------------------------------------------------

--
-- 資料表結構 `withdrawal_records`
--

CREATE TABLE `withdrawal_records` (
  `id` int(11) NOT NULL,
  `rider_id` bigint(20) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `bank_account` varchar(50) NOT NULL,
  `status` tinyint(4) DEFAULT '0' COMMENT '0-待处理,1-已完成,2-拒绝',
  `request_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `process_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提现记录表';

--
-- 資料表的匯出資料 `withdrawal_records`
--

INSERT INTO `withdrawal_records` (`id`, `rider_id`, `amount`, `bank_account`, `status`, `request_time`, `process_time`) VALUES
(1, 1, '1.00', '3213', 0, '2026-04-16 01:52:06', NULL),
(2, 1, '33.00', '3213', 0, '2026-04-16 15:32:14', NULL);

--
-- 已匯出資料表的索引
--

--
-- 資料表索引 `address`
--
ALTER TABLE `address`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `categoryName` (`categoryName`);

--
-- 資料表索引 `dish`
--
ALTER TABLE `dish`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `dish_flavor`
--
ALTER TABLE `dish_flavor`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `account` (`account`);

--
-- 資料表索引 `feedback`
--
ALTER TABLE `feedback`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_user_id` (`user_id`),
  ADD KEY `idx_status` (`status`),
  ADD KEY `idx_created_at` (`created_at`);

--
-- 資料表索引 `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `orderId` (`orderId`);

--
-- 資料表索引 `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `payment_records`
--
ALTER TABLE `payment_records`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_order_id` (`order_id`),
  ADD KEY `idx_user_id` (`user_id`),
  ADD KEY `idx_payment_order` (`order_id`),
  ADD KEY `idx_payment_user` (`user_id`);

--
-- 資料表索引 `review`
--
ALTER TABLE `review`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `orderId` (`orderId`),
  ADD UNIQUE KEY `likesUserId` (`likesUserId`);

--
-- 資料表索引 `review_dish_ratings`
--
ALTER TABLE `review_dish_ratings`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `rider`
--
ALTER TABLE `rider`
  ADD PRIMARY KEY (`employeeId`);

--
-- 資料表索引 `rider_location_history`
--
ALTER TABLE `rider_location_history`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_rider_id` (`rider_id`),
  ADD KEY `idx_order_id` (`order_id`);

--
-- 資料表索引 `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `withdrawal_records`
--
ALTER TABLE `withdrawal_records`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_rider_id` (`rider_id`);

--
-- 在匯出的資料表使用 AUTO_INCREMENT
--

--
-- 使用資料表 AUTO_INCREMENT `address`
--
ALTER TABLE `address`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- 使用資料表 AUTO_INCREMENT `cart`
--
ALTER TABLE `cart`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2134425603;

--
-- 使用資料表 AUTO_INCREMENT `categories`
--
ALTER TABLE `categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `dish`
--
ALTER TABLE `dish`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'dish id', AUTO_INCREMENT=26;

--
-- 使用資料表 AUTO_INCREMENT `dish_flavor`
--
ALTER TABLE `dish_flavor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1863307268;

--
-- 使用資料表 AUTO_INCREMENT `employee`
--
ALTER TABLE `employee`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `feedback`
--
ALTER TABLE `feedback`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID', AUTO_INCREMENT=8;

--
-- 使用資料表 AUTO_INCREMENT `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- 使用資料表 AUTO_INCREMENT `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- 使用資料表 AUTO_INCREMENT `payment_records`
--
ALTER TABLE `payment_records`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `review`
--
ALTER TABLE `review`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `review_dish_ratings`
--
ALTER TABLE `review_dish_ratings`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `rider`
--
ALTER TABLE `rider`
  MODIFY `employeeId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '騎手ID', AUTO_INCREMENT=3;

--
-- 使用資料表 AUTO_INCREMENT `rider_location_history`
--
ALTER TABLE `rider_location_history`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id', AUTO_INCREMENT=30;

--
-- 使用資料表 AUTO_INCREMENT `withdrawal_records`
--
ALTER TABLE `withdrawal_records`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
