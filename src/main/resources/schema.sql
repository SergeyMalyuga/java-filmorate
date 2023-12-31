DROP ALL OBJECTS;
create table if not exists RATING (
	RATING_ID integer not null,
	RATING_NAME varchar(20),
	constraint CONSTRAINT_8 primary key (RATING_ID)
);
create unique index if not exists PRIMARY_KEY_8 on RATING (RATING_ID);

create table if not exists FILMS(
	FILM_ID integer not null,
	RATING_ID integer references rating on delete cascade on update cascade,
	FILM_NAME varchar(50) not null,
	DESCRIPTION varchar(250),
	RELEASE_DATE date,
	DURATION integer,
	constraint CONSTRAINT_33 primary key (FILM_ID)
);
create unique index if not exists PRIMARY_KEY_3 on FILMS (FILM_ID);
create index if not exists RATING_ID_FOREIGNKEY_INDEX_3 on FILMS (RATING_ID);

create table if not exists GENRE (
	GENRE_ID integer not null,
	GENRE_NAME varchar(20),
	constraint CONSTRAINT_4 primary key (GENRE_ID)
);
create unique index if not exists PRIMARY_KEY_4 on GENRE (GENRE_ID);


CREATE TABLE IF NOT EXISTS FILMS_BY_GENRES (
FILM_ID INTEGER REFERENCES FILMS ON DELETE CASCADE ON UPDATE CASCADE,
GENRE_ID INTEGER REFERENCES GENRE ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS USERS (
	USER_ID INTEGER NOT NULL,
	USER_NAME VARCHAR(30) NOT NULL,
	LOGIN VARCHAR(50),
	EMAIL VARCHAR(50),
	BIRTHDAY DATE,
	CONSTRAINT CONSTRAINT_4D PRIMARY KEY (USER_ID)
);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_4D ON USERS (USER_ID);


CREATE TABLE IF NOT EXISTS USER_LIKES (
	LIKE_ID INTEGER NOT NULL,
	USER_ID INTEGER REFERENCES USERS ON DELETE CASCADE ON UPDATE CASCADE,
	FILM_ID INTEGER REFERENCES FILMS ON DELETE CASCADE ON UPDATE RESTRICT,
	CONSTRAINT CONSTRAINT_3C PRIMARY KEY (LIKE_ID)
);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_C ON USER_LIKES (LIKE_ID);

CREATE TABLE IF NOT EXISTS FRIENDSHIP(
	FRIENDSHIP_ID INTEGER NOT NULL,
	FRIENDSHIP_STATUS VARCHAR(20) NOT NULL,
	CONSTRAINT CONSTRAINT_B PRIMARY KEY (FRIENDSHIP_ID)
);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_B ON FRIENDSHIP (FRIENDSHIP_ID);


CREATE TABLE IF NOT EXISTS FRIENDS(
	FRIENDS_ID INTEGER NOT NULL,
	USER_ID INTEGER REFERENCES USERS ON DELETE CASCADE ON UPDATE CASCADE,
	USER_FRIEND_ID INTEGER REFERENCES USERS ON DELETE CASCADE ON UPDATE CASCADE,
	FRIENDSHIP_ID INTEGER REFERENCES FRIENDSHIP ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT CONSTRAINT_700 PRIMARY KEY (FRIENDS_ID)
);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_7 ON FRIENDS (FRIENDS_ID);



















