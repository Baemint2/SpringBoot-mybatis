# 스프링 공부를 위한 쇼핑몰 개인 프로젝트
## 프로젝트 기간
2024.03.12 ~ 2024.05.12 (약 9주)   
2024.07.04 ~ 진행중
## 개발 환경
- JDK 17
- MySQL 8.x
- Spring Boot 3.2.3
- Mybatis
- Thymeleaf
## 사용한 라이브러리
- Lombok
- Spring mail
- Spring Security
- bcprov-jdk15on:1.69
- Spring validation
- Spring AOP
- Thymeleaf
## 폴더 구조
```
📦 src
├── 📁 main
│   ├── 📁 generated
│   ├── 📁 java
│   │   └── 📁 com/moz1mozi/mybatis
│   │       ├── 📁 cart
│   │       ├── 📁 category
│   │       ├── 📁 common
│   │       │   ├── 📁 aspect
│   │       │   ├── 📁 config
│   │       │   ├── 📁 exception
│   │       │   ├── 📁 logback
│   │       │   ├── 📁 typehandler
│   │       │   ├── 📁 utils
│   │       │   └── 📁 validator
│   │       ├── 📁 delivery
│   │       ├── 📁 email
│   │       ├── 📁 image
│   │       ├── 📁 order
│   │       ├── 📁 product
│   │       ├── 📁 user
│   │       ├── 📁 wishlist
│   │       └── ExceptionController.java
│   │       └── MybatisApplication.java
│   │       └── indexController.java
│   └── 📁 resources
│       ├── application.yml
│       ├── log4jdbc.log4j2.properties
│       ├── logback.xml
│       ├── 📁 mapper
│       ├── 📁 static
│       │   ├── 📁 css
│       │   │   └── style
│       │   ├── 📁 images
│       │   └── 📁 js
│       │   │   └── app
│       └── 📁 templates
│           ├── 📁 admin
│           ├── 📁 cart
│           ├── 📁 common
│           ├── 📁 order
│           ├── 📁 product
│           ├── 📁 user
│           └── 기타 html 파일들
├── 📁 test
│   ├── 📁 java
│   │   └── 📁 com/moz1mozi/mybatis
│   │       ├── 📁 cart
│   │       ├── 📁 category
│   │       ├── 📁 dao
│   │       ├── 📁 delivery
│   │       ├── 📁 image
│   │       ├── 📁 product
│   │       ├── 📁 user
│   │       └── MybatisApplicationTests.java
│   └── 📁 resources
│       └── lockdown.png
└── test.yaml
```
