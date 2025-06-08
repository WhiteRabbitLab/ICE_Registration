CREATE TABLE artist (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    picture TEXT,
    description TEXT
);

CREATE TABLE genre (
   id SERIAL PRIMARY KEY,
   description VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE track (
   id SERIAL PRIMARY KEY,
   title VARCHAR(255) NOT NULL,
   genre_id INTEGER REFERENCES genre(id) ON DELETE SET NULL,
   length_seconds INTEGER CHECK (length_seconds > 0)
);


CREATE TABLE artist_track (
    artist_id INTEGER NOT NULL REFERENCES artist(id),
    track_id INTEGER NOT NULL REFERENCES track(id),
    PRIMARY KEY (artist_id, track_id)
);