CREATE TABLE IF NOT EXISTS USERS  (
	USER_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	EMAIL VARCHAR(50),
	LOGIN VARCHAR(50),
	NAME VARCHAR(50),
	BIRTHDAY DATE
);

CREATE TABLE IF NOT EXISTS MPARATING  (
	MPARATING_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	NAME VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS FILMS  (
	FILM_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	NAME VARCHAR(50),
	DESCRIPTION VARCHAR(50),
	RELEASEDATE DATE,
	DURATION INTEGER,
	MPARATING_ID INTEGER
);

CREATE TABLE IF NOT EXISTS FRIENDSHIP  (
	FRIENDSHIP_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	USER_ID INTEGER,
	FRIEND_ID INTEGER
);

CREATE TABLE IF NOT EXISTS LIKES  (
	LIKES_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	USER_ID INTEGER,
	FILM_ID INTEGER
);

CREATE TABLE IF NOT EXISTS GENRES  (
	GENRES_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	NAME VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS FILM_GENRES  (
	FILM_GENRES_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	FILM_ID INTEGER,
	GENRES_ID INTEGER
);