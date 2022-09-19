SELECT
    a.PROCESS_INSTANCE_ID
     , a.TASK_ACTIVITY_ID
     , a.TASK_INSTANCE_ID
     , a.CASE_ID
     , a.TASK_STATUS
     , a.TASK_START_TIME
     , a.TASK_SUSPEND_TIME
     , a.TASK_END_TIME
     , a.UPDATE_TIME
     , b.STATUS
FROM TB_TASK_RUN A
   , TB_PROCESS_INSTANCE B
WHERE 1=1
  AND A.PROCESS_INSTANCE_ID = B.PROCESS_INSTANCE_ID
--AND A.UPDATE_TIME > '202207150'
--AND A.CASE_ID >  'A'
--AND A.CASE_ID <= 'A99'
--AND B.STATUS = '오류'
ORDER BY
    A.TASK_ACTIVITY_ID,
--PROCESS_INSTANCE_ID,
    A.UPDATE_TIME
        desc
;
SELECT *
FROM TB_TASK_RUN_HST A
WHERE 1=1
--AND A.TASK_INSTANCE_ID LIKE 'Activity_damage%'
ORDER BY
--5,
--3,
1
;

--AND PROCESS_INSTANCE_ID = ${processInstanceId}
--AND TASK_INSTANCE_ID = ${taskInstanceId}
;

-- DELETE ----------------------
/*
DELETE FROM TB_PROCESS_INSTANCE;
DELETE FROM TB_TASK_RUN;
DELETE FROM TB_TASK_RUN_HST;

**/
-- SELECT -------------------------
SELECT * FROM TB_PROCESS_INSTANCE ORDER BY START_TIME ;
SELECT * FROM TB_PROCESS_INSTANCE ORDER BY END_TIME  ;
SELECT * FROM TB_TASK_RUN ORDER BY UPDATE_TIME DESC;
SELECT * FROM TB_TASK_RUN_HST ORDER BY 1;

SELECT sysdate
FROM dual;


Activity_damage:850e6c3c-01b0-11ed-a347-2acdc420f0a3
;

SELECT * FROM TB_TASK_RUN_HST A ORDER BY A.UPDATE_TIME ;
SELECT * FROM TB_TASK_RUN;

-- 프로세스가 진행중인데, 타스크가 모두 대기중인애를 뽑아내보자

SELECT A.PROCESS_INSTANCE_ID
FROM   TB_PROCESS_INSTANCE A
WHERE  1=1
  AND    A.STATUS = '재시작'
  AND    (
             SELECT COUNT(1)
             FROM TB_TASK_RUN T
             WHERE A.PROCESS_INSTANCE_ID  = T.PROCESS_INSTANCE_ID
               AND T.TASK_STATUS IN ('시작')
         ) = 0
;


SELECT B.PROCESS_INSTANCE_ID, B.TASK_ACTIVITY_ID
     , COUNT(DECODE(B.TASK_STATUS, '대기', 1)) AS C2
     , COUNT(DECODE(B.TASK_STATUS, '시작', 1)) C1
     , COUNT(1)
FROM   TB_PROCESS_INSTANCE A
   , TB_TASK_RUN B
WHERE  1=1
  AND    A.PROCESS_INSTANCE_ID = B.PROCESS_INSTANCE_ID
  AND    A.STATUS = '재시작'
  AND    B.TASK_STATUS != '정상종료'
GROUP BY B.PROCESS_INSTANCE_ID, B.TASK_ACTIVITY_ID
;
SELECT * FROM   TB_PROCESS_INSTANCE A;


--------------------------------
-- 프로세스 재시작 배치
SELECT T.TASK_ACTIVITY_ID
     , TASK_STATUS
     , PROCESS_INSTANCE_ID
--     , START_TIME
FROM (
         SELECT B.TASK_ACTIVITY_ID
              , A.START_TIME
              , A.PROCESS_INSTANCE_ID
              , ROW_NUMBER () OVER (PARTITION BY B.TASK_ACTIVITY_ID ORDER BY A.START_TIME) AS RN
		     , B.TASK_STATUS
         FROM   TB_PROCESS_INSTANCE A
            , TB_TASK_RUN B
         WHERE  1=1
           AND    A.PROCESS_INSTANCE_ID = B.PROCESS_INSTANCE_ID
           AND    A.STATUS = '재시작'
           AND    B.TASK_STATUS != '정상종료'
		AND    B.TASK_STATUS != '시작'
     ) T
WHERE  1 = 1
  AND    T.RN = 1
;
-- 프로세스별로 현재 타스크 상태 표시
SELECT T.TASK_ACTIVITY_ID
     , TASK_STATUS
     , PROCESS_INSTANCE_ID
--     , START_TIME
     , CASE_ID
     , STATUS
FROM (
         SELECT B.TASK_ACTIVITY_ID
              , A.START_TIME
              , A.PROCESS_INSTANCE_ID
              , ROW_NUMBER () OVER (PARTITION BY B.PROCESS_INSTANCE_ID ORDER BY B.UPDATE_TIME DESC) AS RN
		     , B.TASK_STATUS
              , A.CASE_ID
              , A.STATUS
         FROM   TB_PROCESS_INSTANCE A
            , TB_TASK_RUN B
         WHERE  1=1
           AND    A.PROCESS_INSTANCE_ID = B.PROCESS_INSTANCE_ID
--		AND    A.STATUS = '재시작'
--		AND    B.TASK_STATUS != '정상종료'
     ) T
WHERE  1 = 1
  AND    T.RN = 1
  AND T.CASE_ID LIKE 'C%'
;

-- 타스크별로 대기프로세스, 진행중프로세스 갯수
SELECT B.TASK_ACTIVITY_ID
     , COUNT(DECODE(B.TASK_STATUS, '대기', 1)) AS CNT_WAIT
     , COUNT(DECODE(B.TASK_STATUS, '시작', 1)) AS CNT_ING
     , COUNT(DECODE(B.TASK_STATUS, '오류', 1)) AS CNT_ERR
--     , B.TASK_STATUS
--     , A.PROCESS_INSTANCE_ID
--     , A.CASE_ID
FROM   TB_PROCESS_INSTANCE A
   , TB_TASK_RUN B
WHERE  1=1
  AND    A.PROCESS_INSTANCE_ID = B.PROCESS_INSTANCE_ID
  AND    A.CASE_ID LIKE 'C%'
GROUP BY B.TASK_ACTIVITY_ID


;