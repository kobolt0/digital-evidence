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


DROP TABLE tb_task_run;
DROP TABLE tb_dummy;

SELECT *
FROM USER_TABLES



-- 유저 삭제
DROP USER camunda CASCADE;

-- CAMUNDA 계정생성, 권한
GRANT CONNECT,RESOURCE,UNLIMITED TABLESPACE TO camunda IDENTIFIED BY camunda;
ALTER USER camunda DEFAULT TABLESPACE USERS;
ALTER USER camunda TEMPORARY TABLESPACE TEMP;

SELECT * FROM ALL_USERS;

*/

--------------
-- 테이블생성

-- task 수행상태 테이블 생성
CREATE TABLE tb_task_run (
                             process_instance_id varchar(80) DEFAULT NULL,
                             task_instance_id varchar(80) NOT NULL,
                             case_id varchar(20) DEFAULT NULL,
                             task_status varchar(20) DEFAULT NULL,
                             task_start_time varchar(20) DEFAULT NULL,
                             task_suspend_time varchar(20) DEFAULT NULL,
                             task_end_time varchar(20) DEFAULT NULL,
                             PRIMARY KEY (process_instance_id, task_instance_id)
)
;


-- 프로세스인스턴스
CREATE TABLE TB_PROCESS_INSTANCE (
                                     PROCESS_INSTANCE_ID VARCHAR(80) DEFAULT NULL,
                                     CASE_ID VARCHAR(20) DEFAULT NULL,
                                     STATUS VARCHAR(20) DEFAULT NULL,
                                     START_TIME VARCHAR(20) DEFAULT NULL,
                                     SUSPEND_TIME VARCHAR(20) DEFAULT NULL,
                                     END_TIME VARCHAR(20) DEFAULT NULL,
                                     PRIMARY KEY (PROCESS_INSTANCE_ID)
)
;


-- 테스트용 더미 테이블. 테스트용 더미 rest api 리턴값
CREATE TABLE tb_dummy (
                          name varchar(100) NOT NULL,
                          val varchar(100) DEFAULT NULL,
                          PRIMARY KEY (name)
)
;

-- 테스트용 테이블 인서트
INSERT INTO tb_dummy (name,val) VALUES ('isProcessEnd', 'Y');
INSERT INTO tb_dummy (name,val) VALUES ('sleepTime', '10');

-- 더미테이블 값변경
UPDATE tb_dummy SET val= 'Y' WHERE name = 'isProcessEnd'; --종료
UPDATE tb_dummy SET val= 'N' WHERE name = 'isProcessEnd'; --무한
;
UPDATE tb_dummy SET val= '10' WHERE name = 'sleepTime';
;

