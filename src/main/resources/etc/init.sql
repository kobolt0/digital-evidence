SELECT * FROM USER_TABLES;

-- 프로젝트 초기화 --

/* -- DROP TABLES --
DROP TABLE tb_task_run;
DROP TABLE tb_dummy;

DROP TABLE ACT_GE_PROPERTY;
DROP TABLE ACT_GE_SCHEMA_LOG;
DROP TABLE ACT_RU_JOB;
DROP TABLE ACT_RE_CAMFORMDEF;
DROP TABLE ACT_RU_IDENTITYLINK;
DROP TABLE ACT_RU_VARIABLE;
DROP TABLE ACT_RU_EVENT_SUBSCR;
DROP TABLE ACT_RU_INCIDENT;
DROP TABLE ACT_RU_AUTHORIZATION;
DROP TABLE ACT_RU_FILTER;
DROP TABLE ACT_RU_METER_LOG;
DROP TABLE ACT_RU_TASK_METER_LOG;
DROP TABLE ACT_RU_EXT_TASK;
DROP TABLE ACT_RU_BATCH;
DROP TABLE ACT_HI_PROCINST;
DROP TABLE ACT_HI_ACTINST;
DROP TABLE ACT_HI_TASKINST;
DROP TABLE ACT_HI_VARINST;
DROP TABLE ACT_HI_DETAIL;
DROP TABLE ACT_HI_IDENTITYLINK;
DROP TABLE ACT_HI_COMMENT;
DROP TABLE ACT_HI_ATTACHMENT;
DROP TABLE ACT_HI_OP_LOG;
DROP TABLE ACT_HI_INCIDENT;
DROP TABLE ACT_HI_JOB_LOG;
DROP TABLE ACT_HI_BATCH;
DROP TABLE ACT_HI_EXT_TASK_LOG;
DROP TABLE ACT_ID_MEMBERSHIP;
DROP TABLE ACT_ID_INFO;
DROP TABLE ACT_ID_TENANT_MEMBER;
DROP TABLE ACT_RU_CASE_SENTRY_PART;
DROP TABLE ACT_HI_CASEINST;
DROP TABLE ACT_HI_CASEACTINST;
DROP TABLE ACT_RE_DECISION_DEF;
DROP TABLE ACT_RE_DECISION_REQ_DEF;
DROP TABLE ACT_HI_DECINST;
DROP TABLE ACT_HI_DEC_IN;
DROP TABLE ACT_HI_DEC_OUT;

DROP TABLE ACT_GE_BYTEARRAY;
DROP TABLE ACT_RE_DEPLOYMENT;
DROP TABLE ACT_RU_JOBDEF;
DROP TABLE ACT_RU_TASK;
DROP TABLE ACT_ID_GROUP;
DROP TABLE ACT_ID_USER;
DROP TABLE ACT_ID_TENANT;
DROP TABLE ACT_RU_CASE_EXECUTION;

DROP TABLE ACT_RU_EXECUTION;
DROP TABLE ACT_RE_PROCDEF;
DROP TABLE ACT_RE_CASE_DEF;


DROP TABLE TB_PROCESS_INSTANCE;
DROP TABLE TB_TASK_RUN;
DROP TABLE TB_TASK_RUN_HST;
DROP TABLE tb_dummy;
*/
--------------
-- 테이블생성
-- CAMUNDA.TB_PROCESS_INSTANCE definition

CREATE TABLE "TB_PROCESS_INSTANCE"
(
    "PROCESS_INSTANCE_ID" VARCHAR2(80) DEFAULT NULL,
    "CASE_ID" VARCHAR2(20) DEFAULT NULL,
    "STATUS" VARCHAR2(20) DEFAULT NULL,
    "START_TIME" VARCHAR2(20) DEFAULT NULL,
    "SUSPEND_TIME" VARCHAR2(20) DEFAULT NULL,
    "END_TIME" VARCHAR2(20) DEFAULT NULL,
    "UPDATE_TIME" VARCHAR2(20) NOT NULL,
    PRIMARY KEY ("PROCESS_INSTANCE_ID")
)
;
-- task 수행상태 테이블 생성
CREATE TABLE "TB_TASK_RUN"
(
    "PROCESS_INSTANCE_ID" VARCHAR2(80) NOT NULL,
    "TASK_ACTIVITY_ID" VARCHAR2(80) NOT NULL,
    "TASK_INSTANCE_ID" VARCHAR2(80) NOT NULL,
    "CASE_ID" VARCHAR2(20) DEFAULT NULL,
    "TASK_STATUS" VARCHAR2(20) DEFAULT NULL,
    "TASK_START_TIME" VARCHAR2(20) DEFAULT NULL,
    "TASK_SUSPEND_TIME" VARCHAR2(20) DEFAULT NULL,
    "TASK_END_TIME" VARCHAR2(20) DEFAULT NULL,
    "UPDATE_TIME" VARCHAR2(20) NOT NULL,
    PRIMARY KEY ("PROCESS_INSTANCE_ID","TASK_ACTIVITY_ID")
)
;
-- task 수행상태 테이블 생성
CREATE TABLE "TB_TASK_RUN_HST"
(
    "SEQ" NUMBER NOT NULL,
    "PROCESS_INSTANCE_ID" VARCHAR2(80) NOT NULL,
    "TASK_ACTIVITY_ID" VARCHAR2(80) NOT NULL,
    "TASK_INSTANCE_ID" VARCHAR2(80) NOT NULL,
    "CASE_ID" VARCHAR2(20) DEFAULT NULL,
    "TASK_STATUS" VARCHAR2(20) DEFAULT NULL,
    "TASK_START_TIME" VARCHAR2(20) DEFAULT NULL,
    "TASK_SUSPEND_TIME" VARCHAR2(20) DEFAULT NULL,
    "TASK_END_TIME" VARCHAR2(20) DEFAULT NULL,
    "UPDATE_TIME" VARCHAR2(20) NOT NULL,
    PRIMARY KEY ("SEQ")
)
;
-- 타스크이력 시퀀스 생성
CREATE SEQUENCE SEQ_TASK_RUN_HST
    INCREMENT BY 1
    START WITH 1
    MINVALUE 1
    MAXVALUE 99999
    NOCYCLE
    NOCACHE
    NOORDER;

;
-- 테스트용 더미 테이블. 테스트용 더미 rest api 리턴값
CREATE TABLE tb_dummy (
                          name varchar(100) NOT NULL,
                          val varchar(100) DEFAULT NULL,
                          PRIMARY KEY (name)
)
;

-- 테스트용 테이블
INSERT INTO tb_dummy (name,val) VALUES ('isProcessEnd', 'Y');
INSERT INTO tb_dummy (name,val) VALUES ('sleepTime', '8');
INSERT INTO tb_dummy (name,val) VALUES ('incidentYn', 'Y');

-- 더미테이블 값변경
UPDATE tb_dummy SET val= 'Y' WHERE name = 'isProcessEnd'; -- 종료
UPDATE tb_dummy SET val= 'N' WHERE name = 'isProcessEnd'; -- 무한
UPDATE tb_dummy SET val= 'N' WHERE name = 'incidentYn'; -- 인시던트생성
;
UPDATE tb_dummy SET val= '2' WHERE name = 'sleepTime'; -- 더미 대기시간
--------------------------------------------------------------------------------------------------
SELECT *
FROM tb_task_run
ORDER BY TASK_START_TIME
;

SELECT * FROM tb_dummy;

;
SELECT * FROM ACT_RU_EXECUTION;
SELECT * FROM ACT_RU_TASK;
SELECT * FROM ACT_HI_OP_LOG ahol ;
SELECT * FROM ACT_RU_EXECUTION;
SELECT * FROM ACT_RU_EXECUTION;



SELECT 'SELECT * FROM ' || TABLE_NAME ||';'
FROM USER_TABLES A
WHERE 1=1
  AND A.TABLE_NAME  LIKE 'ACT_HI%'
;

SELECT * FROM ACT_HI_ACTINST;
SELECT * FROM ACT_HI_ATTACHMENT;
SELECT * FROM ACT_HI_BATCH;
SELECT * FROM ACT_HI_CASEACTINST;
SELECT * FROM ACT_HI_CASEINST;
SELECT * FROM ACT_HI_COMMENT;
SELECT * FROM ACT_HI_DECINST;
SELECT * FROM ACT_HI_DEC_IN;
SELECT * FROM ACT_HI_DEC_OUT;
SELECT * FROM ACT_HI_DETAIL;
SELECT * FROM ACT_HI_EXT_TASK_LOG;
SELECT * FROM ACT_HI_IDENTITYLINK;
SELECT * FROM ACT_HI_INCIDENT;
SELECT * FROM ACT_HI_JOB_LOG;
SELECT * FROM ACT_HI_OP_LOG;
SELECT * FROM ACT_HI_PROCINST;
SELECT * FROM ACT_HI_TASKINST;
SELECT * FROM ACT_HI_VARINST;