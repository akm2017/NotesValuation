CREATE TABLE AREA (
  AID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  AREA_ID VARCHAR(15) NOT NULL UNIQUE,
  AREA_NAME VARCHAR(45) NULL,
  DESCRIPTION VARCHAR(45) NULL,
  UPDATED_DATE_TIME DATE NULL,
  UPDATED_BY  VARCHAR(45) NULL,
  CREATED_DATE_TIME DATE NULL,
  CREATED_BY  VARCHAR(45) NULL
  );
