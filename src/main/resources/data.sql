INSERT INTO authority (AUTHORITY_NAME) VALUES ( 'ROLE_USER' );
INSERT INTO authority (AUTHORITY_NAME) VALUES ( 'ROLE_ADMIN' );

INSERT INTO user (USER_ID, EMAIL, ENABLE, PASSWORD, USERNAME) VALUES (1, 'dygks8557@naver.com', true , '$2a$10$3THNdU4D6i3SQjNaQZ4myOGmqPVpJiJKZ8oK9O8qtQCJ1Qalny4MG' , 'jupaka');
INSERT INTO verification_token (ID, UUID, EXPIRY_DATE, USER_ID ) VALUES ( 1, '3b9bc0c4-f9cb-423b-b3b2-13adb6ed5584', null, 1 );

INSERT INTO user_authority (USER_ID, AUTHORITY_NAME) VALUES ( 1, 'ROLE_USER' );

INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT, CREATE_DATE) VALUES ( 1, 'TITLE 1', 'DESC 1', 1, 10, now());
INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT) VALUES ( 2, 'TITLE 2', 'DESC 2', 1, 20 );
INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT) VALUES ( 3, 'TITLE 3', 'DESC 3', 1, 10 );
INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT) VALUES ( 4, 'TITLE 4', 'DESC 4', 1, 30 );
INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT) VALUES ( 5, 'TITLE 5', 'DESC 5', 1, 10 );
INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT) VALUES ( 6, 'TITLE 6', 'DESC 6', 1, 40 );
INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT) VALUES ( 7, 'TITLE 7', 'DESC 7', 1, 10 );
INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT) VALUES ( 8, 'TITLE 8', 'DESC 8', 1, 30 );
INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT) VALUES ( 9, 'TITLE 9', 'DESC 9', 1, 10 );
INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT) VALUES ( 10, 'TITLE 10', 'DESC 10', 1, 20 );
INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT) VALUES ( 11, 'TITLE 11', 'DESC 11', 1, 21 );
INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT) VALUES ( 12, 'TITLE 12', 'DESC 12', 1, 21 );
INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT) VALUES ( 13, 'TITLE 13', 'DESC 13', 1, 24 );
INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT) VALUES ( 14, 'TITLE 14', 'DESC 14', 1, 26 );
INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT) VALUES ( 15, 'TITLE 15', 'DESC 15', 1, 1 );
INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT) VALUES ( 16, 'TITLE 16', 'DESC 16', 1, 10 );
INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT) VALUES ( 17, 'TITLE 17', 'DESC 17', 1, 10 );
INSERT INTO feed (FEED_ID, TITLE, FEED_DESC, USER_ID, LIKE_COUNT) VALUES ( 18, 'TITLE 18', 'DESC 18', 1, 30 );
