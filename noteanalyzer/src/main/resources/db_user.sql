CREATE TABLE USER (
    USER_ID INT NOT NULL AUTO_INCREMENT,
    USER_NAME VARCHAR(255) NOT NULL,
    PASSWORD VARCHAR(255) NOT NULL,
    DISPLAY_NAME VARCHAR(100),
    FIRST_NAME VARCHAR(50),
    LAST_NAME VARCHAR(50),
    EMAIL_ID VARCHAR(100) NOT NULL,
    CONTACT_NUMBER VARCHAR(20),
    ADDRESS VARCHAR(255),
    STREET VARCHAR(50) ,
    CITY VARCHAR(50),
    ZIP_CODE VARCHAR(20),
    STATE VARCHAR(20),
    COUNTRY VARCHAR(20),
    RESET_TOKEN VARCHAR(255),
    RESET_TOKEN_CREATION_TIME TIMESTAMP,
    CREATED_BY VARCHAR(50),
    UPDATED_BY VARCHAR(50),
    CREATED_DATE_TIME TIMESTAMP,
    UPDATED_DATE_TIME TIMESTAMP,
    PRIMARY KEY (USER_ID)
);
