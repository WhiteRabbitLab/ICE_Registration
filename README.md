#  Ice Works / Artist Registration Application

## Getting started:

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
