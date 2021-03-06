CREATE TABLE `PURCHASE_REQUESTS` (
  `REQUEST_DATE` DATE NOT NULL,
  `NOTE_ID` INT(11) NOT NULL,
  `USER_ID` INT(11) NOT NULL,
  `NOTE_STORE_STORE_NAME` VARCHAR(10) NOT NULL,
  `AMOUNT` INT(11) NOT NULL,
  `APPROVED_REJECTED` VARCHAR(45) DEFAULT NULL,
  `VENDOR_NOTE_ID` VARCHAR(45) DEFAULT NULL,
  PRIMARY KEY (`REQUEST_DATE`,`NOTE_ID`,`USER_ID`,`NOTE_STORE_STORE_NAME`),
  KEY `FK_PURCHASE_REQUESTS_NOTE1_IDX` (`NOTE_ID`),
  KEY `FK_PURCHASE_REQUESTS_USER1_IDX` (`USER_ID`),
  KEY `FK_PURCHASE_REQUESTS_NOTE_STORE1_IDX` (`NOTE_STORE_STORE_NAME`),
  CONSTRAINT `FK_PURCHASE_REQUESTS_NOTE1` FOREIGN KEY (`NOTE_ID`) REFERENCES `NOTE` (`NOTE_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_PURCHASE_REQUESTS_NOTE_STORE1` FOREIGN KEY (`NOTE_STORE_STORE_NAME`) REFERENCES `NOTE_STORE` (`STORE_NAME`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_PURCHASE_REQUESTS_USER1` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
);