CREATE TABLE "etl10t" ("e9_id" INTEGER , "id" INTEGER  NOT NULL, "name" VARCHAR (100), PRIMARY KEY ("id"))
;

CREATE TABLE "etl11t" ("bin" VARCHAR(32) FOR BIT DATA, "id" INTEGER  NOT NULL GENERATED BY DEFAULT AS IDENTITY, PRIMARY KEY ("id"))
;

CREATE TABLE "etl12t" ("MixedCaseId" INTEGER  NOT NULL GENERATED BY DEFAULT AS IDENTITY, "StartsWithUpperCase" VARCHAR (100), PRIMARY KEY ("MixedCaseId"))
;

CREATE TABLE "etl9t" ("id" INTEGER  NOT NULL, "name" VARCHAR (100), PRIMARY KEY ("id"))
;

CREATE TABLE "etl2t" ("address" VARCHAR (255), "id" INTEGER  NOT NULL GENERATED BY DEFAULT AS IDENTITY, "name" VARCHAR (100), PRIMARY KEY ("id"))
;

CREATE TABLE "etl6t" ("date" DATE , "id" BIGINT  NOT NULL, "name" VARCHAR (50), PRIMARY KEY ("id"))
;

CREATE TABLE "etl8t" ("c_decimal1" DECIMAL (10, 9), "c_decimal2" DECIMAL (10, 6), "c_decimal3" DECIMAL (10, 9), "id" INTEGER  NOT NULL, PRIMARY KEY ("id"))
;

CREATE TABLE "etl4t" ("c_boolean" BOOLEAN , "c_date" DATE , "c_decimal" DECIMAL (10, 2), "c_enum" VARCHAR (10), "c_int" INTEGER , "c_time" TIME , "c_timestamp" TIMESTAMP , "c_varchar" VARCHAR (100), "id" INTEGER  NOT NULL, PRIMARY KEY ("id"))
;

CREATE TABLE "etl7t" ("first_name" VARCHAR (50), "id" INTEGER  NOT NULL, "last_name" VARCHAR (50), "sex" VARCHAR (1), PRIMARY KEY ("id"))
;

CREATE TABLE "etl1t" ("age" INTEGER , "description" VARCHAR (100), "id" INTEGER  NOT NULL GENERATED BY DEFAULT AS IDENTITY, "name" VARCHAR (100), PRIMARY KEY ("id"))
;

CREATE TABLE "etl5t" ("date" DATE , "id" INTEGER  NOT NULL, "name" VARCHAR (50), PRIMARY KEY ("id"))
;

CREATE TABLE "etl3t" ("e2_id" INTEGER , "e5_id" INTEGER , "id" INTEGER  NOT NULL GENERATED BY DEFAULT AS IDENTITY, "name" VARCHAR (200), "phone_number" VARCHAR (12), PRIMARY KEY ("id"))
;

ALTER TABLE "etl3t" ADD FOREIGN KEY ("e2_id") REFERENCES "etl2t" ("id")
;

ALTER TABLE "etl3t" ADD FOREIGN KEY ("e5_id") REFERENCES "etl5t" ("id")
;

CREATE SEQUENCE "PK_ETL10T" AS BIGINT START WITH 200 INCREMENT BY 20 NO MAXVALUE NO CYCLE
;

CREATE SEQUENCE "PK_ETL11T" AS BIGINT START WITH 200 INCREMENT BY 20 NO MAXVALUE NO CYCLE
;

CREATE SEQUENCE "PK_ETL12T" AS BIGINT START WITH 200 INCREMENT BY 20 NO MAXVALUE NO CYCLE
;

CREATE SEQUENCE "PK_ETL1T" AS BIGINT START WITH 200 INCREMENT BY 20 NO MAXVALUE NO CYCLE
;

CREATE SEQUENCE "PK_ETL2T" AS BIGINT START WITH 200 INCREMENT BY 20 NO MAXVALUE NO CYCLE
;

CREATE SEQUENCE "PK_ETL3T" AS BIGINT START WITH 200 INCREMENT BY 20 NO MAXVALUE NO CYCLE
;

CREATE SEQUENCE "PK_ETL4T" AS BIGINT START WITH 200 INCREMENT BY 20 NO MAXVALUE NO CYCLE
;

CREATE SEQUENCE "PK_ETL5T" AS BIGINT START WITH 200 INCREMENT BY 20 NO MAXVALUE NO CYCLE
;

CREATE SEQUENCE "PK_ETL6T" AS BIGINT START WITH 200 INCREMENT BY 20 NO MAXVALUE NO CYCLE
;

CREATE SEQUENCE "PK_ETL7T" AS BIGINT START WITH 200 INCREMENT BY 20 NO MAXVALUE NO CYCLE
;

CREATE SEQUENCE "PK_ETL8T" AS BIGINT START WITH 200 INCREMENT BY 20 NO MAXVALUE NO CYCLE
;

CREATE SEQUENCE "PK_ETL9T" AS BIGINT START WITH 200 INCREMENT BY 20 NO MAXVALUE NO CYCLE
;

CREATE TABLE "ti_super" ("id" INTEGER  NOT NULL, "super_key" VARCHAR (100), "type" VARCHAR (100) NOT NULL, PRIMARY KEY ("id"))
;

CREATE TABLE "ti_sub1" ("id" INTEGER  NOT NULL, "sub_key" VARCHAR (100), "subp1" VARCHAR (100), PRIMARY KEY ("id"))
;

ALTER TABLE "ti_sub1" ADD FOREIGN KEY ("id") REFERENCES "ti_super" ("id")
;

CREATE SEQUENCE "PK_TI_SUPER" AS BIGINT START WITH 200 INCREMENT BY 20 NO MAXVALUE NO CYCLE
;

