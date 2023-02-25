# java-filmorate
Template repository for Filmorate project.
![image](https://user-images.githubusercontent.com/101995680/221371031-7294c030-c163-440d-9336-fbd49523c639.png)

// Creating tables
Table users {
  id int [pk, increment]
  email varchar [not null]
  login varchar [not null]
  name varchar
  birthday timestamp
}

Table films {
  id int [pk, increment]
  name varchar [not null]
  description varchar
  releaseDate timestamp
  duration int
}

Table genres {
  id int [pk, not null]
  name varchar [not null]
}

Table mpa {
  id int [pk, increment]
  name varchar [not null]
}

Table film_genre {
  film_id int [not null]
  genre_id int [not null]    
}

Table film_mpa {
  film_id int [not null]
  mpa_id int [not null]
}

Table film_likes {
  film_id int [not null]
  user_id int [not null]
}

Table friendship {
  user_1_id int [not null]
  user_2_id int [not null]
  confirmed boolean [not null]
}

Ref: film_genre.film_id > films.id
Ref: film_genre.genre_id > genres.id
Ref: film_likes.film_id > films.id
Ref: film_likes.user_id > users.id
Ref: friendship.user_1_id > users.id
Ref: friendship.user_2_id > users.id
Ref: film_mpa.mpa_id > mpa.id
Ref: film_mpa.film_id > films.id
