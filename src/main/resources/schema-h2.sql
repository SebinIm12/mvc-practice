-- @formatter:off
-- ---------------------------------------------------------------------------------------
-- SSP TABLES --
DROP TABLE IF EXISTS SSP_MEMBER_GROUP_MAPPED;
CREATE TABLE SSP_MEMBER_GROUP_MAPPED (
    UID VARCHAR2(200) NOT NULL,
    GROUP_ID VARCHAR2(200) NOT NULL,
    PRIMARY KEY (UID, GROUP_ID)
);

DROP TABLE IF EXISTS SSP_GROUP_ROLE_MAPPED;
CREATE TABLE SSP_GROUP_ROLE_MAPPED (
    GROUP_ID VARCHAR2(200) NOT NULL,
    ROLE_ID VARCHAR2(200) NOT NULL,
    PRIMARY KEY (GROUP_ID, ROLE_ID)
);

DROP TABLE IF EXISTS SSP_ROLE_API_MAPPED;
CREATE TABLE SSP_ROLE_API_MAPPED (
    ROLE_ID VARCHAR2(200) NOT NULL,
    API_ID VARCHAR2(200) NOT NULL,
    PRIMARY KEY (ROLE_ID, API_ID)
);

DROP TABLE IF EXISTS SSP_API_INFO;
CREATE TABLE SSP_API_INFO (
    API_ID VARCHAR2(200) NOT NULL,
    SERVICE_NAME VARCHAR2(500) NOT NULL,
    HOST VARCHAR2(500) NOT NULL,
    API VARCHAR2(1000) NOT NULL,
    METHOD VARCHAR2(20) NOT NULL,
    API_DESC VARCHAR2(500),
    IS_USE VARCHAR2(1) DEFAULT 'Y' NOT NULL,
    CHECK_AUTH_TYPE VARCHAR2(20) DEFAULT 'ALL',
    PRIMARY KEY (API_ID)
);


DROP TABLE IF EXISTS SSP_ROUTING_SERVICE_INFO;
CREATE TABLE SSP_ROUTING_SERVICE_INFO (
    SERVICE_NAME VARCHAR2(200) NOT NULL,
    HOST VARCHAR2(500) NOT NULL,
    SERVICE_DESC VARCHAR2(500),
    PRIMARY KEY (SERVICE_NAME)
);


DROP TABLE IF EXISTS SSP_IO_LOG;
CREATE TABLE SSP_IO_LOG (
    TOKEN VARCHAR2(256),
    CHANNEL VARCHAR2(200),
    DIR VARCHAR2(50),
    SERVICE_UID VARCHAR2(64),
    SERVICE_NAME VARCHAR2(500),
    SERVICE_IP VARCHAR2(200),
    CLIENT_NAME VARCHAR2(500),
    CLIENT_IP VARCHAR2(200),
    UID VARCHAR2(64),
    HOST VARCHAR2(2000),
    API VARCHAR2(2000),
    METHOD VARCHAR2(20),
    QUERY VARCHAR2(2000),
    REQUEST_TIME timestamp,
    REQUEST_BODY MEDIUMTEXT,
    STATUS NUMBER(3),
    RESPONSE_BODY MEDIUMTEXT,
    ELAPSED_TIME NUMBER
);
CREATE INDEX SSP_IO_LOG_IX1 ON SSP_IO_LOG (TOKEN);
CREATE INDEX SSP_IO_LOG_IX2 ON SSP_IO_LOG (TOKEN, REQUEST_TIME DESC);

COMMENT ON TABLE SSP_IO_LOG IS '서비스 I/O 로그';
COMMENT ON COLUMN SSP_IO_LOG.TOKEN IS '사용자 토큰';
COMMENT ON COLUMN SSP_IO_LOG.CHANNEL IS '사용자 채널';
COMMENT ON COLUMN SSP_IO_LOG.DIR IS 'INBOUNT/OUTBOUND - IN | OUT';
COMMENT ON COLUMN SSP_IO_LOG.SERVICE_UID IS '서비스 UUID';
COMMENT ON COLUMN SSP_IO_LOG.SERVICE_NAME IS '서비스 명';
COMMENT ON COLUMN SSP_IO_LOG.SERVICE_IP IS '서비스 IP';
COMMENT ON COLUMN SSP_IO_LOG.CLIENT_NAME IS 'CLIENT 명';
COMMENT ON COLUMN SSP_IO_LOG.CLIENT_IP IS 'CLIENT IP';
COMMENT ON COLUMN SSP_IO_LOG.UID IS 'REQUEST UUID';
COMMENT ON COLUMN SSP_IO_LOG.HOST IS 'REQUEST HOST';
COMMENT ON COLUMN SSP_IO_LOG.API IS 'REQUEST API';
COMMENT ON COLUMN SSP_IO_LOG.METHOD IS 'REQUEST METHOD';
COMMENT ON COLUMN SSP_IO_LOG.QUERY IS 'REQUEST QUERY STRING';
COMMENT ON COLUMN SSP_IO_LOG.REQUEST_TIME IS 'REQUEST TIME';
COMMENT ON COLUMN SSP_IO_LOG.REQUEST_BODY IS 'REQUEST BODY';
COMMENT ON COLUMN SSP_IO_LOG.STATUS IS 'RESPONSE STATUS';
COMMENT ON COLUMN SSP_IO_LOG.RESPONSE_BODY IS 'RESPONSE BODY';
COMMENT ON COLUMN SSP_IO_LOG.ELAPSED_TIME IS '실행시간';


DROP TABLE IF EXISTS SSP_TOKEN_SEED_LOG;
CREATE TABLE SSP_TOKEN_SEED_LOG (
    PRIMARY_ID VARCHAR2(500) NOT NULL,
    SEED VARCHAR2(64) NOT NULL,
    CHANNEL VARCHAR2(200) NOT NULL,
    CLIENT_IP VARCHAR2(200) NOT NULL,
    TIMEOUT NUMBER,
    EXPIRY_TIME NUMBER,
    CHECKIN_TIME TIMESTAMP NOT NULL,
    CHECKOUT_TIME TIMESTAMP,
    CHECKOUT_TYPE VARCHAR2(200),
    CONSTRAINT SSP_TOKEN_SEED_LOG_PK PRIMARY KEY (PRIMARY_ID, SEED)
);
CREATE INDEX SSP_TOKEN_SEED_LOG_IX1 ON SSP_TOKEN_SEED_LOG (PRIMARY_ID, CHANNEL, CHECKOUT_TIME);
CREATE INDEX SSP_TOKEN_SEED_LOG_IX2 ON SSP_TOKEN_SEED_LOG (SEED);
CREATE INDEX SSP_TOKEN_SEED_LOG_IX3 ON SSP_TOKEN_SEED_LOG (SEED, CHECKOUT_TIME);

COMMENT ON TABLE SSP_TOKEN_SEED_LOG IS 'SEED 생성 로그';
COMMENT ON COLUMN SSP_TOKEN_SEED_LOG.CHANNEL IS '토큰 생성 채널';
COMMENT ON COLUMN SSP_TOKEN_SEED_LOG.PRIMARY_ID IS '사용자 UNIQUE KEY';
COMMENT ON COLUMN SSP_TOKEN_SEED_LOG.SEED IS '토큰 생성에 사용 될 SEED';
COMMENT ON COLUMN SSP_TOKEN_SEED_LOG.CLIENT_IP IS '사용자 IP';
COMMENT ON COLUMN SSP_TOKEN_SEED_LOG.TIMEOUT IS 'TIMEOUT(seconds)';
COMMENT ON COLUMN SSP_TOKEN_SEED_LOG.EXPIRY_TIME IS 'TTL(UTC timestamp)';
COMMENT ON COLUMN SSP_TOKEN_SEED_LOG.CHECKIN_TIME IS 'SEED 생성/로그인 시간';
COMMENT ON COLUMN SSP_TOKEN_SEED_LOG.CHECKOUT_TIME IS 'SEED 폐기/로그아웃 시간';
COMMENT ON COLUMN SSP_TOKEN_SEED_LOG.CHECKOUT_TYPE IS 'SEED 폐기 타입 - EXPIRY(시간만료) | DUPLICATE(중복) | CHECKOUT(로그아웃) | DISCARD(강제폐기)';


DROP TABLE IF EXISTS SSP_TOKEN_LOG;
CREATE TABLE SSP_TOKEN_LOG (
    SEED VARCHAR2(64) NOT NULL,
    TOKEN VARCHAR2(256) NOT NULL,
    TIMEOUT NUMBER,
    EXPIRY_TIME NUMBER,
    CLIENT_IP VARCHAR2(200) NOT NULL,
    CREATION_TIME TIMESTAMP NOT NULL,
    CONSTRAINT SSP_TOKEN_LOG_PK PRIMARY KEY (SEED, TOKEN)
);
COMMENT ON TABLE SSP_TOKEN_LOG IS '토큰 생성 로그';
COMMENT ON COLUMN SSP_TOKEN_LOG.SEED IS 'SEED';
COMMENT ON COLUMN SSP_TOKEN_LOG.TOKEN IS '생성 토큰';
COMMENT ON COLUMN SSP_TOKEN_LOG.TIMEOUT IS 'TIMEOUT(seconds)';
COMMENT ON COLUMN SSP_TOKEN_LOG.EXPIRY_TIME IS 'TTL(UTC timestamp)';
COMMENT ON COLUMN SSP_TOKEN_LOG.CLIENT_IP IS '사용자 IP';
COMMENT ON COLUMN SSP_TOKEN_LOG.CREATION_TIME IS '토큰 생성 시간';

DROP TABLE IF EXISTS SSP_ERROR_CODE;
CREATE TABLE SSP_ERROR_CODE (
    ID VARCHAR2(500) NOT NULL,
    CODE VARCHAR2(20) NOT NULL,
    REASON VARCHAR2(500) NOT NULL,
    STATUS NUMBER(3),
    IS_USE VARCHAR2(1) DEFAULT 'Y',
    IS_TRACE VARCHAR2(1) DEFAULT 'Y'
);
COMMENT ON TABLE SSP_ERROR_CODE IS 'EXCEPTION 코드 정의';
COMMENT ON COLUMN SSP_ERROR_CODE.ID IS '코드의 id';
COMMENT ON COLUMN SSP_ERROR_CODE.CODE IS 'error 코드';
COMMENT ON COLUMN SSP_ERROR_CODE.REASON IS 'error 메세지';
COMMENT ON COLUMN SSP_ERROR_CODE.STATUS IS 'HttpStatus code';
COMMENT ON COLUMN SSP_ERROR_CODE.IS_USE IS '사용여부';
COMMENT ON COLUMN SSP_ERROR_CODE.IS_TRACE IS 'trace 사용여부';


DROP TABLE IF EXISTS SSP_REFRESH_TOKEN_LOG;
CREATE TABLE SSP_REFRESH_TOKEN_LOG (
    SEED VARCHAR2(64) NOT NULL,
    REFRESH_TOKEN VARCHAR2(256) NOT NULL,
    TIMEOUT NUMBER,
    EXPIRY_TIME NUMBER,
    CLIENT_IP VARCHAR2(200) NOT NULL,
    CREATION_TIME TIMESTAMP NOT NULL,
    CONSTRAINT SSP_REFRESH_TOKEN_LOG_PK PRIMARY KEY (SEED, REFRESH_TOKEN)
);
COMMENT ON TABLE SSP_REFRESH_TOKEN_LOG IS '리프레시 토큰 생성 로그';
COMMENT ON COLUMN SSP_REFRESH_TOKEN_LOG.SEED IS 'SEED';
COMMENT ON COLUMN SSP_REFRESH_TOKEN_LOG.REFRESH_TOKEN IS ' 리프레시 토큰';
COMMENT ON COLUMN SSP_REFRESH_TOKEN_LOG.TIMEOUT IS 'TIMEOUT(seconds)';
COMMENT ON COLUMN SSP_REFRESH_TOKEN_LOG.EXPIRY_TIME IS 'TTL(UTC timestamp)';
COMMENT ON COLUMN SSP_REFRESH_TOKEN_LOG.CLIENT_IP IS '사용자 IP';
COMMENT ON COLUMN SSP_REFRESH_TOKEN_LOG.CREATION_TIME IS '토큰 생성 시간';

DROP TABLE IF EXISTS TODO;
CREATE TABLE TODO (
    ID IDENTITY NOT NULL,
    MEMO VARCHAR2(4000),
    DUE_DATE DATE,
    MODIFIED_DATETIME TIMESTAMP,
    PRIMARY KEY (ID)
);


DROP TABLE IF EXISTS SSP_MEMBER_ALLOWED_IP_INFO;
CREATE TABLE SSP_MEMBER_ALLOWED_IP_INFO (
    PRIMARY_ID VARCHAR2(200) NOT NULL,
    ALLOWED_IP_RANGE_FROM VARCHAR2(200) NOT NULL,
    ALLOWED_IP_RANGE_TO VARCHAR2(200) NOT NULL
);
