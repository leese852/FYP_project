-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- 主機: 127.0.0.1
-- 產生時間： 2026-01-26 17:22:33
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
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 1-刪除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 資料表的匯出資料 `address`
--

INSERT INTO `address` (`id`, `userId`, `contactName`, `contactPhone`, `address`, `isDefault`, `createTime`, `updateTime`, `isDelete`) VALUES
(2, 10, '张三', '53000849', '测试地址', 0, '2026-01-16 14:35:46', '2026-01-16 14:35:46', 0),
(3, 17, '张二', '53000849', '测试地址1', 1, '2026-01-16 15:14:57', '2026-01-16 15:14:57', 0),
(4, 17, '李四', '53000850', '地址2-非默认', 0, '2026-01-16 15:14:57', '2026-01-16 15:14:57', 0),
(5, 22, 'leese', '53000849', 'leese', 0, '2026-01-19 13:15:25', '2026-01-19 16:12:42', 0),
(6, 22, 'leese', '53000849', 'leese', 0, '2026-01-19 13:15:27', '2026-01-19 14:09:28', 0),
(7, 22, 'leese', '53000894', 'wdnmd', 1, '2026-01-19 13:16:40', '2026-01-19 14:09:31', 0);

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
(2, 'Leese', '58.00', 2, '新菜品', NULL, 1, NULL, '2025-12-17 16:56:28', '2025-12-17 16:56:28', 0),
(4, '新菜品', '20.00', 1, '很好吃的新菜品', NULL, 1, NULL, '2026-01-23 16:25:49', '2026-01-23 16:25:49', 0);

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
(813101058, '辣度', '不辣,微辣,中辣,重辣', 1),
(825683970, '甜度', '正常,少糖,多糖', 1),
(1010233346, '甜度', '正常,少糖,多糖', 2),
(1010233347, '辣度', '不辣,微辣,中辣,重辣', 2),
(1714831362, '辣度', '微辣，特辣', 4);

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
  `riderId` bigint(20) DEFAULT NULL COMMENT '配送騎手ID，關聯rider表'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
-- 資料表結構 `rider`
--

CREATE TABLE `rider` (
  `employeeId` bigint(20) NOT NULL COMMENT '騎手ID',
  `name` varchar(100) NOT NULL COMMENT '騎手姓名',
  `role` varchar(50) DEFAULT NULL COMMENT '角色，例如配送員',
  `loginCredentials` varchar(255) DEFAULT NULL COMMENT '登入憑證或帳號資訊',
  `restaurantId` bigint(20) DEFAULT NULL COMMENT '所屬餐廳ID',
  `phone` varchar(20) NOT NULL COMMENT '騎手電話',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否刪除 1-刪除'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
(23, 'leese123', '$2a$10$oTrD77x6gMDM2430QSmlsO/Z8pgZ1c525M9Gw5IVZBwXN/LswCgbG', 'leese123', NULL, NULL, NULL, NULL, 0, '2026-01-19 13:12:54', '2026-01-19 13:12:54', 0, 0);

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
-- 資料表索引 `review`
--
ALTER TABLE `review`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `orderId` (`orderId`),
  ADD UNIQUE KEY `likesUserId` (`likesUserId`);

--
-- 資料表索引 `rider`
--
ALTER TABLE `rider`
  ADD PRIMARY KEY (`employeeId`);

--
-- 資料表索引 `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- 在匯出的資料表使用 AUTO_INCREMENT
--

--
-- 使用資料表 AUTO_INCREMENT `address`
--
ALTER TABLE `address`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- 使用資料表 AUTO_INCREMENT `cart`
--
ALTER TABLE `cart`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `categories`
--
ALTER TABLE `categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `dish`
--
ALTER TABLE `dish`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'dish id', AUTO_INCREMENT=5;

--
-- 使用資料表 AUTO_INCREMENT `dish_flavor`
--
ALTER TABLE `dish_flavor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1714831363;

--
-- 使用資料表 AUTO_INCREMENT `employee`
--
ALTER TABLE `employee`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `review`
--
ALTER TABLE `review`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表 AUTO_INCREMENT `rider`
--
ALTER TABLE `rider`
  MODIFY `employeeId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '騎手ID';

--
-- 使用資料表 AUTO_INCREMENT `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id', AUTO_INCREMENT=24;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
