#  Ice Works / Artist Registration Application

## Context
Imagine you are part of the team building a music metadata service for a streaming platform akin to Spotify
or Apple Music, that serves millions of users worldwide. Your task is to design and implement a Music
Metadata Service that stores and provides metadata about different music tracks and artists. Our goal is to
provide a user-friendly interface where customers can effortlessly access this information.

## Task Requirements
Your task is to design and implement a system that provides the following user experiences:
- Add a New Track: As a user, you should be able to add a new track to an artist's catalogue,
capturing attributes such as track title, genre, length, etc.
- Edit Artist Name: As a user, you should be able to edit an artist's name to accommodate instances
where artists have multiple aliases.
- Fetch Artist Tracks: As a user, you should be able to fetch all tracks associated with a specific artist.
- Artist of the Day: As a user, you should be able to see a different "Artist of the Day" in a cyclical
manner on the homepage each day, ensuring a fair rotation through the entire catalogue of artists.
This means if there are n artists, after n days, the cycle restarts with the first artist, ensuring an equal
chance for each artist to be the "Artist of the Day".

These requirements brief provide an outline for the service. However, how you approach these
requirements, how you design and implement the system, and how you anticipate and plan for potential
issues is entirely up to you. We encourage you to make assumptions where necessary, but please ensure
that you document and justify those assumptions.

## Time spent on task
Between four / five hours in total.

## Getting setup locally:

### Startup the database using docker-compose

### `cd ICE_Registration_API`
### `docker-compose up -d`

This will start up a PostgresSQL database container locally
and load in the sample data.

For a live production system, the database would be configured elsewhere 
and environment specific application.properties setup to use the relevant 
connection details (i.e. application-dev.properties). 

### Run Ice Registration API

This is a small Spring Boot Application, which can be found in 

### `ICE_Registration_API.src.main.java.com.ice.registration.IceRegistrationApplication.java`

Within your IDE, you can run this project without any extra configuration.

In a live production system, profiles could be used to for environment specific configuration.

### Run Ice Registration UI

This is a very simple React Application, which can be run using the npm cli.

### `cd ICE_Registration_UI`
### `npm install`
### `npm start`

## Next steps

Given more time, I would add the following

### For the second requirement regarding artist aliases

- An 'alias' table with a one-to-many mapping to artist (including link table) to the database
- Modify the Artist entity to include this Alias mapping
- Modify the REST API repository / service /controller 
layer logic to include the new mapping in the DTO response
- Include a put controller to add or remove an artist alias
- Modify the front-end to include a drop-down of some sort where alias can be viewed, and modified

### For the fourth requirement

- A better approach would be to use database tracked rotation
- This way we are ensuring a fair round-robin cycle of each artist in the database
