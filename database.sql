-- 第一步：先刪除所有子表（從最底層開始）
DROP TABLE IF EXISTS `review`;
DROP TABLE IF EXISTS `order_items`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `dish`;
DROP TABLE IF EXISTS `address`;
DROP TABLE IF EXISTS `categories`;
DROP TABLE IF EXISTS `user`;

create table user
(
    id           int auto_increment comment 'id'   primary key,
    username     varchar(30)                          null comment '用戶昵稱',
    userPassword varchar(512)                         not null comment '密码',
    userAccount  varchar(256)                         null comment '用戶賬號',
    avatarUrl    varchar(1024)                        null comment '用户头像',
    gender       tinyint                              null comment '性别 0-男 1-女',
    email        varchar(128)                         null comment '邮箱',
    tel          varchar(128)                         null comment '電話',
    userStatus   int      default 0                   null comment '狀態 0-正常 1-禁用',
    createTime   datetime default current_timestamp() null comment '创建时间',
    updateTime   datetime default current_timestamp() null on update current_timestamp() comment '更新時間',
    isDelete     tinyint  default 0                   not null comment '是否删除',
    userRole     int      default 0                   not null comment '用户角色 0-普通用户 1-管理员',
    plantCode   varchar(512)                         null comment '編號',
    tags         varchar(1024)                        null comment '标签列表'
);
-- 2. 創建 categories（不依賴其他業務表）
CREATE TABLE `categories`(
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             categoryName VARCHAR(50) NOT NULL UNIQUE COMMENT '類別名稱',
                             displayOrder INT NULL DEFAULT 0 COMMENT '顯示權重',
                             createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
                             isDelete TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 1-刪除'
);

-- 3. 創建 dish（依賴 categories）
CREATE TABLE `dish`(
                       id INT AUTO_INCREMENT COMMENT 'dish id' PRIMARY KEY,
                       dishName VARCHAR(100) NULL DEFAULT NULL COMMENT '菜品名稱',
                       price DECIMAL(10,2) NOT NULL COMMENT '價錢',
                       categoryId INT NOT NULL COMMENT '類別ID',
                       description VARCHAR(512) NULL COMMENT '菜品介紹',
                       imgUrl VARCHAR(512) NULL COMMENT '圖片地址',
                       isAvailable TINYINT NULL DEFAULT 1 COMMENT '0-禁用/下架 1-正常',
                       createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                       updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
                       isDelete TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 1-刪除',
                       FOREIGN KEY (categoryId) REFERENCES categories(id)
);

-- 4. 創建 address（依賴 user）
CREATE TABLE `address`(
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          userId INT NOT NULL COMMENT '用戶id',
                          contactName VARCHAR(50) NOT NULL COMMENT '收貨人',
                          contactPhone VARCHAR(50) NOT NULL COMMENT '聯係電話',
                          address VARCHAR(512) NOT NULL COMMENT '地址',
                          isDefault TINYINT DEFAULT 0 COMMENT '是否默認 1=default',
                          createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
                          isDelete TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 1-刪除',
                          FOREIGN KEY (userId) REFERENCES user(id)
);

-- 5. 創建 orders（依賴 user 和 address）
CREATE TABLE `orders`(
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         orderId VARCHAR(50) UNIQUE NOT NULL COMMENT '訂單編號',
                         userId INT NOT NULL COMMENT '用户id',
                         addressId INT NOT NULL COMMENT '地址id',
                         status TINYINT NULL DEFAULT 1 COMMENT '1-待处理 2-准备中 3-配送中 4-已送达 5-已取消',
                         totalAmount DECIMAL(10,2) NULL COMMENT '縂價錢',
                         paymentMethod VARCHAR(50) NULL COMMENT '支付方法',
                         remark VARCHAR(512) NULL COMMENT '備注',
                         createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
                         isDelete TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 1-刪除',
                         FOREIGN KEY (userId) REFERENCES user(id),
                         FOREIGN KEY (addressId) REFERENCES address(id)
);

-- 6. 創建 order_items（依賴 orders 和 dish）
CREATE TABLE `order_items`(
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              orderId INT NOT NULL COMMENT '訂單id',
                              dishId INT NOT NULL COMMENT '菜品id',
                              dishName VARCHAR(100) NULL COMMENT '菜品名稱快照',
                              quantity INT NOT NULL COMMENT '菜品數量',  -- 建議改為 NOT NULL
                              price DECIMAL(10,2) NOT NULL COMMENT '價錢快照',  -- 建議改為 NOT NULL
                              FOREIGN KEY (orderId) REFERENCES orders(id),
                              FOREIGN KEY (dishId) REFERENCES dish(id)
);

-- 7. 創建 review（依賴 user 和 orders）
CREATE TABLE `review`(
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         userId INT NOT NULL COMMENT '用户id',
                         orderId INT NOT NULL UNIQUE COMMENT '訂單id',
                         rating TINYINT NOT NULL COMMENT '評分1-5，1最低',  -- 建議改為 NOT NULL
                         comment VARCHAR(1000) NULL COMMENT '評論内容',
                         createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
                         isDelete TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 1-刪除',
                         FOREIGN KEY (userId) REFERENCES user(id),
                         FOREIGN KEY (orderId) REFERENCES orders(id)
);