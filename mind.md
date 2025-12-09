功能表
    登录/注销
    IP定位
    搜索地址
    获取商店(计算当前位置和商店的距离)
    加购物车
    下订单
    支付(支持微信和支付宝的扫码支付和调起app支付)
    评价
    头像上传(用了七牛云存储)
    图片懒加载
    
数据库表
    CREATE TABLE `user`
    (
        id bigint auto_increment comment'id' primary key ,
        username VARCHAR(30) NULL DEFAULT NULL COMMENT '用戶昵稱',
        userPassword varchar(512) not null comment '密码',
        userAccount varchar(256) null comment '用戶賬號',
        avatarUrl varchar(1024) comment '用户头像',
        gender tinyint null comment '性别 0-男 1-女',
        email VARCHAR(128) NULL COMMENT '邮箱',
        tel varchar(128) null comment '電話',
        userStatus int default 0 comment '狀態 0-正常 1-禁用',
        createTime datetime default CURRENT_TIMESTAMP comment '创建时间',
        updateTime datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '更新時間',
        isDelete tinyint default 0 not null comment '是否删除',
        userRole int default 0 not null comment '用户角色 0-普通用户 1-管理员',
        plantCode varchar(512) comment '星球編號'
    );

    drop table if exists `dish`;

    create table `dish`(
        id int auto_increment comment 'dish id' primary key ,
        dishName varchar(100) null default null comment '菜品名稱',
        price decimal(10,2) not null comment '價錢',
        categoryId int not null comment '類別ID',
        description varchar(512) null comment '菜品介紹',
        imgUrl varchar(512) null comment '圖片地址',
        isAvailable tinyint null default 1 comment '0-禁用/下架 1-正常',
        createTime datetime default CURRENT_TIMESTAMP comment '创建时间',
        updateTime datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '更新時間',
        isDelete tinyint not null default 0 comment '是否删除 1-刪除',
        foreign key (categoryId) references categories(id)
    );
    # alter table dish add foreign key (categoryId) references categories(id);

    DROP TABLE IF EXISTS `categories`;
    create table `categories`(
        id int auto_increment primary key ,
        categoryName varchar(50) not null unique comment '類別名稱',
        displayOrder INT NULL DEFAULT 0 COMMENT '顯示權重',
        createTime datetime default CURRENT_TIMESTAMP comment '创建时间',
        updateTime datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '更新時間',
        isDelete tinyint not null default 0 comment '是否删除 1-刪除'
    );

    drop table if exists `orders`;
    create table `orders`(
        id int auto_increment primary key,
        orderId varchar(50) unique not null comment '訂單編號',
        userId int not null comment '用户id',
        addressId int not null comment '地址id',
        status tinyint null default 1 comment '1-待处理 2-准备中 3-配送中 4-已送达 5-已取消',
        totalAmount decimal(10,2) null comment '縂價錢',
        paymentMethod varchar(50) null comment '支付方法',
        remark varchar(512) null comment '備注',
        createTime datetime default CURRENT_TIMESTAMP comment '创建时间',
        updateTime datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '更新時間',
        isDelete tinyint not null default 0 comment '是否删除 1-刪除',
        foreign key (userId) references user(id),
        foreign key (addressId) references address(id)
    );

    drop table if exists`order_items`;
    create table `order_items`(
        id int auto_increment primary key ,
        orderId int not null comment '訂單id',
        dishId int not null comment '菜品di',
        dishName varchar(100) null comment '菜品名稱快照',
        quantity int null comment'菜品數量',
        price decimal(10,2) null comment'價錢快照',
        foreign key (orderId) references orders(id),
        foreign key (dishId) references dish(id)
    );

    drop table if exists `address`;
    create table `address`(
        id int auto_increment primary key,
        userId int not null comment'用戶id',
        contactName varchar(50) not null comment'收貨人',
        contactPhone varchar(50) not null comment'聯係電話',
        address varchar(512) not null comment'地址',
        isDefault tinyint default 0 comment'是否默認 1=default',
        createTime datetime default CURRENT_TIMESTAMP comment '创建时间',
        updateTime datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '更新時間',
        isDelete tinyint not null default 0 comment '是否删除 1-刪除',
        foreign key (userId) references user(id)
    );

    drop table if exists `review`;
    create table `review`(
        id int auto_increment primary key ,
        userId int not null comment '用户id',
        orderId int not null unique comment '訂單id',
        rating tinyint null comment '評分1-5，1最低',
        comment varchar(1000) null comment'評論内容',
        createTime datetime default CURRENT_TIMESTAMP comment '创建时间',
        updateTime datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '更新時間',
        isDelete tinyint not null default 0 comment '是否删除 1-刪除',
        foreign key (userId) references user(id),
        foreign key (orderId) references orders(id)
    );~