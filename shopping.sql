create table BOARD_T
(
    BOARD_NO    int auto_increment
        primary key,
    TITLE       varchar(1000) not null,
    CONTENTS    varchar(1000) null,
    MODIFIED_AT datetime      null,
    CREATED_AT  datetime      null
);

create table User
(
    MEMBER_ID   bigint auto_increment
        primary key,
    USERNAME    varchar(25)  not null,
    PASSWORD    varchar(25)  not null,
    email       varchar(40)  not null,
    nickname    varchar(40)  null,
    address1    varchar(255) null,
    address2    varchar(255) null,
    address3    varchar(255) null,
    ROLE        varchar(20)  not null,
    CREATED_AT  datetime     not null,
    MODIFIED_AT datetime     null
);

create table PRODUCT_T
(
    PRODUCT_ID     bigint auto_increment
        primary key,
    PROD_NAME      varchar(255) not null,
    DESCRIPTION    text         null,
    PROD_PRICE     int          not null,
    STOCK_QUANTITY int          null,
    CATEGORY       varchar(100) null,
    CREATED_AT     datetime     not null,
    MODIFIED_AT    datetime     null
);

create table PRODUCT_IMAGE
(
    IMAGE_ID           bigint auto_increment
        primary key,
    PRODUCT_ID         bigint       null,
    ORIGINAL_FILE_NAME varchar(255) null,
    STORED_FILE_NAME   varchar(255) null,
    STORED_URL         varchar(255) null,
    CREATED_AT         datetime     not null,
    MODIFIED_AT        datetime     null,
    constraint product_image_ibfk_1
        foreign key (PRODUCT_ID) references Product (PRODUCT_ID)
);

create index PRODUCT_ID
    on ProductImage (PRODUCT_ID);


