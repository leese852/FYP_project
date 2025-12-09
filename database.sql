-- 第一步：先刪除所有子表（從最底層開始）
DROP TABLE IF EXISTS `review`;
DROP TABLE IF EXISTS `order_items`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `dish`;
DROP TABLE IF EXISTS `address`;
DROP TABLE IF EXISTS `categories`;
drop table if exists `dish_flavor`;
drop table if exists 'cart';
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
    imgUrl LONGBLOB NULL COMMENT '圖片',
    isAvailable TINYINT NULL DEFAULT 1 COMMENT '0-禁用/下架 1-正常',
    createUser int null comment '員工創建',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
    isDelete TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 1-刪除',
    FOREIGN KEY (categoryId) REFERENCES categories(id) on update cascade on delete cascade
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
    FOREIGN KEY (userId) REFERENCES user(id) on update cascade on delete cascade
);

-- 5. 創建 orders（依賴 user 和 address）
CREATE TABLE `orders`(
    id INT AUTO_INCREMENT PRIMARY KEY,
    orderId VARCHAR(50) UNIQUE NOT NULL COMMENT '訂單編號',
    userId INT NOT NULL COMMENT '用户id',
    addressId INT NOT NULL COMMENT '地址id',
    status TINYINT NULL DEFAULT 1 COMMENT '1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款',
    totalAmount DECIMAL(10,2) NULL COMMENT '縂價錢',
    payMethod VARCHAR(50) NULL COMMENT '支付方法',
    payStatus tinyint not null default 0 comment '支付状态 0未支付 1已支付 2已退款',
    remark VARCHAR(512) NULL COMMENT '備注',
    cancelReason varchar(255) null comment '取消原因',
    cancelTime datetime default null comment '取消時間',
    rejectionReason varchar(255 ) null comment '拒绝原因',
    estimatedDeliveryTime datetime default null commnet '預計送達時間',
    deliveryStatus tinyint not null default 1 comment '配送狀態 1立即送出  0选择具体时间',
    deliveryTime datetime null comment '送達時間',
    packAmount int default null comment '打包費',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
    isDelete TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 1-刪除',
    FOREIGN KEY (userId) REFERENCES user(id) on update cascade on delete cascade,
    FOREIGN KEY (addressId) REFERENCES address(id) on update cascade on delete cascade
);

-- 6. 創建 order_items（依賴 orders 和 dish）
CREATE TABLE `order_items`(
    id INT AUTO_INCREMENT PRIMARY KEY,
    orderId INT NOT NULL COMMENT '訂單id',
    dishId INT NOT NULL COMMENT '菜品id',
    dishName VARCHAR(100) NULL COMMENT '菜品名稱快照',
    dishFlavor varchar(128) null comment '菜品口味 JSON格式',
    quantity INT NOT NULL COMMENT '菜品數量',  -- 建議改為 NOT NULL
    price DECIMAL(10,2) NOT NULL COMMENT '價錢快照',  -- 建議改為 NOT NULL
    FOREIGN KEY (orderId) REFERENCES orders(id) on update cascade on delete cascade,
    FOREIGN KEY (dishId) REFERENCES dish(id) on update cascade on delete cascade
);

-- 7. 創建 review（依賴 user 和 orders）
CREATE TABLE `review`(
    id INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL COMMENT '用户id',
    orderId INT NOT NULL UNIQUE COMMENT '訂單id',
    rating TINYINT NOT NULL COMMENT '評分1-5，1最低',  -- 建議改為 NOT NULL
    comment VARCHAR(1000) NULL COMMENT '評論内容',
    likes int null default 0 comment '點贊數',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
    isDelete TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 1-刪除',
    FOREIGN KEY (userId) REFERENCES user(id) on update cascade on delete cascade,
    FOREIGN KEY (orderId) REFERENCES orders(id) on update cascade on delete cascade
);

-- 8 菜品口味
create table `dish_flavor`(
    id int auto_increment primary key,
    tag varchar(64) not null comment '标签 eg. 温度'，
    list varchar(255) not null comment '标签子列表 eg[热,温,冷]',
    dishId int not null,
    foreign key (dishId) references dish(id) on update cascade on delete cascade
);

create table `cart` (
    id int not null auto_increment primary key,
    name varchar(32) null comment '商品名称',
    userId int not null comment '主键',
    dishId int null comment '菜品id',
    dishFlavor varchar(50) null comment '口味',
    number int not null default '1' comment '数量',
    amount decimal(10,2) not null comment '金额',
    create_time datetime null comment '创建时间'
);