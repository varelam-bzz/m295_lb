-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema steam
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema steam
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `steam` DEFAULT CHARACTER SET utf8 ;
USE `steam` ;

-- -----------------------------------------------------
-- Table `steam`.`Publisher`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `steam`.`Publisher` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `steam`.`Game`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `steam`.`Game` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `Release_Date` DATE NOT NULL,
  `Price` DECIMAL(10,2) NOT NULL,
  `Purchases` INT NOT NULL,
  `Released` TINYINT NOT NULL,
  `Publisher_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `Publisher_idx` (`Publisher_ID` ASC) VISIBLE,
  CONSTRAINT `Publisher_ID`
    FOREIGN KEY (`Publisher_ID`)
    REFERENCES `steam`.`Publisher` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
