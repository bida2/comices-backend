# ComicES

## What is ComicES?
ComicES is a personal blog and a comic book sharing platform - users can share their favourite comics, comment on their favourite comics, suggest new comics, rate comics and more!

## How do I run this project?
This is the backend part of the project, **built on Spring Boot**. In order to install it and run it, you will need an IDE that supports Java compilation to import the project.
After importing the project, you can simply run the app through the SpringbootVuetifyApplication class.

I use PostgreSQL for this project - in order to setup the database in Spring Boot, you need a `application.properties` file in your `src/main/resources` folder with the following properties:
```
spring.datasource.url=<your database URL>
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.datasource.username=<your database username>
spring.datasource.password=<your database password>
api.sendgrid.key= <your SendGrid API key>
```

After you run both the frontend and backend part of this project, you can access it all at localhost:9000
