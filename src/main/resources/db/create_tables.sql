CREATE TABLE USERS
(
  ID       INT          NOT NULL             GENERATED ALWAYS AS IDENTITY,
  CREATED  TIMESTAMP    NOT NULL             DEFAULT CURRENT_TIMESTAMP,
  MODIFIED TIMESTAMP                         DEFAULT CURRENT_TIMESTAMP,
  LOGIN    VARCHAR(255) NOT NULL,
  PASSWORD VARCHAR(255) NOT NULL,
  CONSTRAINT users_id_primary_key PRIMARY KEY (ID),
  CONSTRAINT users_login_unique UNIQUE (LOGIN)
);

CREATE TABLE ROLES
(
  LOGIN    VARCHAR(255) NOT NULL,
  ROLE    VARCHAR(255) NOT NULL,
  CONSTRAINT role_login_foreign_key FOREIGN KEY (LOGIN)
  REFERENCES USERS (LOGIN)
);

CREATE TABLE CATEGORIES
(
  ID   INT          NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 10),
  NAME VARCHAR(255) NOT NULL,
  FILTER VARCHAR(255) NOT NULL DEFAULT 'category',
  CONSTRAINT categories_id_primary_key PRIMARY KEY (ID),
  CONSTRAINT categories_name_unique UNIQUE (NAME)
);
INSERT INTO CATEGORIES (NAME, FILTER) VALUES ('All', 'all');
INSERT INTO CATEGORIES (NAME, FILTER) VALUES ('Popular', 'popular');

CREATE TABLE TASKS
(
  ID           INT          NOT NULL             GENERATED ALWAYS AS IDENTITY,
  PARENT_ID    INT,
  ORDER_NUMBER INT,
  NAME         VARCHAR(255) NOT NULL,
  DESCRIPTION  VARCHAR(1024),
  IS_TEMPLATE  INT,
  CATEGORY_ID  INT,
  IS_COMPLETED BOOLEAN      NOT NULL             DEFAULT FALSE,
  USER_ID      INT          NOT NULL,
  ORIGIN_ID    INT,
  CREATED      TIMESTAMP    NOT NULL             DEFAULT CURRENT_TIMESTAMP,
  MODIFIED     TIMESTAMP                         DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT tasks_id_primary_key PRIMARY KEY (ID),
  CONSTRAINT tasks_category_id_foreign_key FOREIGN KEY (CATEGORY_ID)
  REFERENCES CATEGORIES (ID),
  CONSTRAINT tasks_parent_id_foreign_key FOREIGN KEY (PARENT_ID)
  REFERENCES TASKS (ID),
  CONSTRAINT tasks_user_id_foreign_key FOREIGN KEY (USER_ID)
  REFERENCES USERS (ID),
  CONSTRAINT tasks_template_id_foreign_key FOREIGN KEY (ORIGIN_ID)
  REFERENCES TASKS (ID)
);