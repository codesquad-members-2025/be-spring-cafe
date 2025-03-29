INSERT INTO users (user_id, password, name, email)
VALUES ('javajigi', 'test', '자바지기', 'javajigi@slipp.net');

INSERT INTO users (user_id, password, name, email)
VALUES ('sanjigi', 'test', '산지기', 'sanjigi@slipp.net');

INSERT INTO article (user_id, title, content, deleted)
VALUES ('1', '안녕하세요', '첫 게시글입니다.', 'false');

INSERT INTO article (user_id, title, content, deleted)
VALUES ('1', '안녕하세요2', '두번째 게시글입니다.', 'true');

INSERT INTO article (user_id, title, content, deleted)
VALUES ('2', '안녕하세요3', '세번째 게시글입니다.', 'false');

INSERT INTO reply (article_id, user_id, content, deleted)
VALUES ('1', '1', '첫 댓글입니다.', 'false');

INSERT INTO reply (article_id, user_id, content, deleted)
VALUES ('1', '2', '두번째 댓글입니다.', 'false');

INSERT INTO reply (article_id, user_id, content, deleted)
VALUES ('1', '1', '세번째 댓글입니다.', 'true');
