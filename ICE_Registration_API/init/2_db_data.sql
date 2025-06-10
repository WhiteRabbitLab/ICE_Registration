INSERT INTO genre (description) VALUES
                                    ('Indie Rock'),
                                    ('Country'),
                                    ('Metal');

INSERT INTO artist (name, picture, description) VALUES
                                                    ('Generic Indie Landfill Band', 'https://singersroom.com/wp-content/uploads/2024/02/15-Best-Indie-Bands-of-All-Time.jpg', 'Moody vocals, jangly guitars, and vintage filters.'),
                                                    ('AutoTuna', 'https://hips.hearstapps.com/hmg-prod/images/best-female-country-singers-mickey-guyton-1652413741.jpg', 'Country-pop fusion with heavy vocal effects.'),
                                                    ('Mild Rage', 'https://www.season-of-mist.com/wp-content/uploads/2020/03/AndOceans_3_FULL_ARTISTIC-300x300.jpg', 'Melodic metal with a hint of existential dread.');

INSERT INTO track (title, genre_id, length_seconds) VALUES
                                                        ('Coffee and Rain', (SELECT id FROM genre WHERE description = 'Indie Rock'), 214),
                                                        ('Vinyl Heart', (SELECT id FROM genre WHERE description = 'Indie Rock'), 198);

INSERT INTO track (title, genre_id, length_seconds) VALUES
                                                        ('Truckin'' Alone', (SELECT id FROM genre WHERE description = 'Country'), 231),
                                                        ('Moonshine Dreams', (SELECT id FROM genre WHERE description = 'Country'), 205);

INSERT INTO track (title, genre_id, length_seconds) VALUES
                                                        ('Silent Screams', (SELECT id FROM genre WHERE description = 'Metal'), 312),
                                                        ('Ashes Reign', (SELECT id FROM genre WHERE description = 'Metal'), 287);


INSERT INTO artist_track (artist_id, track_id)
SELECT a.id, t.id
FROM artist a, track t
WHERE a.name = 'Generic Indie Landfill Band' AND t.title IN ('Coffee and Rain', 'Vinyl Heart');

INSERT INTO artist_track (artist_id, track_id)
SELECT a.id, t.id
FROM artist a, track t
WHERE a.name = 'AutoTuna' AND t.title IN ('Truckin'' Alone', 'Moonshine Dreams');

INSERT INTO artist_track (artist_id, track_id)
SELECT a.id, t.id
FROM artist a, track t
WHERE a.name = 'Mild Rage' AND t.title IN ('Silent Screams', 'Ashes Reign');