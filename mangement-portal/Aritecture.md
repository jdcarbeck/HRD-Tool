# mangement-portal directories

Here an explanation is given for the directories for the management portal development

## server

Here are all the code that belongs to the sever including the database and server code. This file links both the routing which is done in `./routes/HRform-router.js` which defines the methods uses for different http requests, such as: `POST, PUT, DELTE, and GET`. This allso links the db which is specified in `./db/index.js`

### /server/index.js

This file is where the networking of the application is done

### /server/db

This directory contains the code for the MongoDB in a index.js file and is referenced in the server code in the directory above

### /server/models

Here are the models for the data that will be stored in the database
