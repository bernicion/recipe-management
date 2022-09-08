# recipe-management
REST API sample project to manage Recipes(OpenAPI, Spring, MySQL, Java 17)

# Getting Started
This application is a REST API app that will help manage the recipes.
This API can help with adding/updating/deleting/fetching/filtering of the recipes
Technically all API's are defined using the OpenAPI definition tool. 

# Architecture overview
1. Using the *openapi-generator-maven-plugin* file recipe-openapi.yaml is read and API interfaces and Models
are pre-generated during package in the location "/target/generated-sources/openapi/src/main/com.recipe.management.openapi/api".

   *The API classes need to be overwritten with proper implementation.*

2. On Data layer is used JPA standard to define Entities.

3. Transformation between Data and Dto layer is done with the help of MapStruct library during compile time.

4. Application connects to a MySQL database that runs in a docker container.

### Requirements to run the application on local environment
1. **Start the docker container that with store the MySQL database**
    * From project root directory, locate file **docker-compose.yml**
    * To start MySqL server need to run **docker compose up**

2. **Build the project and start the application**
    * From project root directory, locate file *pom.xml*
    * To build the application run **mvn clean package**
    * To start the application go to /target folder and run **java -jar recipe-management-0.0.1-SNAPSHOT.jar --spring.profiles.active=local-mysql**
   
3. **Check if the application is up**
   Access **http://localhost:8040/actuator/health** and check if status is UP

4. **Access the Swagger UI to study the REST API endpoint**
   Access **http://localhost:8040/swagger-ui/index.html** and check end-points definition
   *CRUD operations are pretty straightforward. Filtering Operation is more special*
   
   **Documentation of the filtering recipes API**
   The filtering endPoint is a POST call that returns a list of Recipe objects. 
   The most important part is the filtering body.

   ### Few examples
   1. All vegetarian recipes<br>
      Access **POST: http://localhost:8040/api/recipes/filter** with body<br>
      **{"isVegan": true}**

   2. Recipes that can serve 4 persons and have �potatoes� as an ingredient
      Access **POST: http://localhost:8040/api/recipes/filter** with body <br>
      **{"servings": 4,"ingredients": [{"name": "potato","include": true}]}**
   
   3. Recipes without �salmon� as an ingredient that has �oven� in the instructions.<br>
      Access **POST: http://localhost:8040/api/recipes/filter** with body<br>
      **{"ingredients": [{"name": "salmon","include": false}],"instruction": "oven"}**

