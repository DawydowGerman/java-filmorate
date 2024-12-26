# java-filmorate
The repository for the Filmorate project.

The data base scheme:

![The link to the data base scheme file](https://github.com/DawydowGerman/java-filmorate/blob/main/dbdiagram.svg)

The database scheme describes the two main models as well as the logic of the application.

The following SQL queries can be applied:

Getting all the films:
SELECT * 
FROM Film;

Getting all the users:
SELECT * 
FROM User;

Getting all the friends of the user with id 1:
SELECT user_id
FROM Friendship
WHERE user_id IN (SELECT friend_id 
      FROM Friendship
      WHERE user_id = 1) AND
      friend_id = 1;

Getting the mutual friends of the users with ids 1 and 3:
SELECT tf1.user_id
FROM 
(SELECT *
FROM Friendship
WHERE user_id IN (SELECT friend_id 
      FROM Friendship
      WHERE user_id = 1) AND
      friend_id = 1) AS tf1
INNER JOIN 
(SELECT *
FROM Friendship
WHERE user_id IN (SELECT friend_id 
      FROM Friendship
      WHERE user_id = 3) AND
      friend_id = 3) AS tf3 ON tf1.user_id = tf3.user_id;

Getting the three most popular films:
SELECT name
FROM Film
WHERE film_id IN (SELECT film_id
                  FROM Likes
                  GROUP BY film_id
                  ORDER BY COUNT(user_id) DESC
                  LIMIT 3);

Getting all the films of genre with id 1:
SELECT * 
FROM Film_genres
WHERE genres_id = 1;
