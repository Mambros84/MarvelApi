#MarvelApi

##Import MarvelApi project in IntelliJ

- Open intellij and use `Import project` option
- Navigate to microservices repo root and open `build.gradle` file
- In the popup tick `Use auto-import`
- Click next and wait for the project to load
- Open `IntelliJ IDEA -> Preferences`
- `Build, Execution, Deployment -> Compiler -> Annotation Processor` and tick `Enable annotation processing`
- Now you're ready to GO !

##Setup Google translation service Api
- The Google Translation Api needs credentials set directly on the Api request. The steps required are the followings:
    1) Navigate to https://cloud.google.com/translate/docs/setup and follow the whole procedure
    2) Once arrived onto the `Go to the Create Service Account Key page` button, click on it and complete the process downloading the json credentials file
    3) Rename the file as `Credentials.json` and paste it in the following path `/src/main/resources/Credentials.json` (Without this the translation only won't work)

##Run MarvelApi app from IntelliJ
- The project can run in two ways:
    1) Normal mode - Press play;
    2) Debug mode - Press the debugger icon 

##Run MarvelApi app from Terminal through `Gradle`
- Type the following text in terminal:
    `gradle bootRun`
- To stop the application simply press `option` + `c` on the keyboard
    
##Start to use the MarvelApi!
- Open your browser and navigate to `http://localhost/marvelApi`. This will prompt you with the main page
- Start to fetch the characters ids appending `/characters` to the above url. This operation will take a few seconds and cache the results based on the timer set in the `EmbeddedCacheConfig.java` class. 
- To get the full character details, append a character id to the url (e.g. `http://localhost/marvelApi/characters/1009696`). This will return character name, id, description and thumbnail.
- To get a translation (refer to https://cloud.google.com/translate/docs/languages for available languages) simply append a language ISO-639-1 Code to the above url (e.g. `http://localhost/marvelApi/characters/1009696/de`)# MarvelApi


##How to use Swagger
- To view the Swagger UI related to the project, navigate to the following link after starting the application:
    1) http://localhost:8080/swagger-ui/
    
##Dependencies management
- Dependencies are managed through the `build.gradle` file.