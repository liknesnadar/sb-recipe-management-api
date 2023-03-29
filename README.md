# sb-recipe-management-api
REST API for managing recipes built using Spring Boot


 1. Application Summary:
  	====================
	
	-> The Recipe Management API is a REST API that provides CRUD operations to manage recipes.

	-> The application is built using sprint boot framework with tiered acrhitecture:
		- The API call is processed from the Controller[Resource] to Service to the Persistence[Repository] layer.
		- It uses below stack:
			-	Java 19
			-	Spring Boot 3.0.5
			-	PostgreSQL -- as persistent DB or H2 as in-memory DB
			-	Docker desktop -- for running application as docker container		
			-	Postman for testing


	-> It includes below resources and operations:

		Users:
			- registerUser		--	/api/users/register
			- loginUser		--	/api/users/login

		Recipes:
			- createRecipe		--	/api/recipes
			- getAllRecipes		--	/api/recipes
			- getRecipeById		--	/api/recipes/{recipeId}
			- updateRecipe		--	/api/recipes/{recipeId}
			- deleteRecipe		--	/api/recipes/{recipeId}



 	-> Generating JWT token for managing recipes using 'Users' resource:
 	   -----------------------------------------------------------------
		- All the 'recipes' resource endpoints are secured using 'JWT token'
		- To start using the application, the user first needs to register using 'registerUser' operation
		- If already registered, then the user needs to login using 'loginUser' operation
		- Both the registerUser and loginUser endpoints will return a valid JWT token that is valid for 1 hour
		- If the token is expired then a new token can be generated using 'loginUser' operation


	-> Managing recipes using 'Recipes' resource:
 	   ------------------------------------------
	 	- The operations in Recipes resouce allows an user to create recipes, see all or specific recipes, update or delete recipes
	 	- To manage recipes user needs to create a recipe using 'createRecipe' operation with a valid JWT token and recipe details
	 	- If the recipe is created successfully the createRecipe operation returns the recipeId in response 
	 	- Once the recipe is created then the recipe details can be viewed using 'getAllRecipes' or 'getRecipeById' operations
	 	- Both the 'getAllRecipes' or 'getRecipeById' operations return recipe details with recipeId that can be used to update or delete recipes
	 	- A recipe can be updated using the 'updateRecipe' operation which requires the recipeId
	 	- Likewise a recipe can be deleted using the 'deleteRecipe' operation which also requires the recipeId



 2. Set-up to run the application:
	==============================

	-> The application can be run and tested with following configurations:
		a. Application running locally in IDE or docker and using in-memory H2 database [least external set-up required]
		b. Application running locally in IDE and Postgres DB running as docker container
		c. Application and Postgres DB both running as docker container


	a. Application running locally in IDE or docker and using in-memory H2 database:
	   -----------------------------------------------------------------------------
	   	1. Check out source code from GitHub repo and open in an IDE

	   	2. Start the application with spring profile 'memory' that uses 'application-memory.properties' file
	   		
	   		- If starting the application from IDE set below property in 'application.properties' file:
	   			-- 'spring.profiles.active=memory'

	   		- If starting the application from terminal use below command:
	   			->> java -jar target/recipe-management-0.0.1-SNAPSHOT.jar  --spring.profiles.active=memory

	   	3. Once the application is started successfully use the postman collection to test the APIs
	   	   [Refer section - 3. Testing the application for testing using postman]

	   	4. If required for verification, use below details to login to the H2 database from browser:
	   	
	   		- Open browser and enter URL	--	http://localhost:8090/h2
	   		- Add below details and click connect:
				- JDBC URL	--	jdbc:h2:mem:recipeapidb
				- Username	--	dbuser
				- Password	--	dbuser



	b. With Application running locally in IDE and Postgres DB running as docker container:
	   ------------------------------------------------------------------------------------
		1. Check out source code from GitHub repo and open in an IDE
		
		[Note: If you have local postgres DB then skip to step - 4.d]
		
		2. Open docker desktop and open command prompt / terminal
		
		3. Start postgres container with command:
			->> docker container run --name postgresdb -e POSTGRES_PASSWORD=dbuser -d -p 5432:5432 postgres
		
		4. Execute DB script [recipe_management_db.sql] in repository to create DB objects:
			a. open command prompt and navigate to the db script directory
			b. copy db scripts to postgres container using command
				->> docker cp recipe_management_db.sql postgresdb:/
			c. login to the db container using command
				->> docker exec -it postgresdb bash
			d. execute db script using command
				->> psql -U postgres --file recipe_management_db.sql

		5. Once the application is started successfully use the postman collection to test the APIs
	   	   [Refer section - 3. Testing the application for testing using postman]

		6. If required for verification, use below commands to login to the postgresdb from terminal:

			->> docker exec -it postgresdb psql -U postgres
			->> \connect recipeapidb

			->> select * from users;		--	to check users table
			->> select * from recipes;		--	to check recipes table



	c. With Application and Postgres DB running as docker container:
	   -------------------------------------------------------------
	   	1. Follow steps 1 to 4 from above

	   	2. To start the application as docker container open terminal and navigate to the 'Dockerfile' location in repo checkout path

	   	3. Execute below command to start the application container:
	   		
			->> docker build --tag sbrecipeapi:latest .
	   		->> docker run --name sbrecipeapi -d -p 8090:8090 sbrecipeapi

	   	4. Check the running containers using Docker Desktop or 'docker ps' command in terminal

	   	5. Once the postgres and docker containers are running use the postman collection to test the APIs
	      		[Refer section - 3. Testing the application for testing using postman]



 3. Testing the application:
	========================

   		a. To test the app open Postman and import 'recipe-management-api.postman_collection.json' collection
		   [ Note: The postman collection is in 'test/postman' path in repository ]

		b. To start testing first step is to register/login a user and get JWT token:
			- Run the registerUser/loginUser request from postman collection
			- Use the 'token' received in response to invoke operations of Recipes resource
			- The token is valid for one hour

		c. Testing recipes resource:
			- Use the token generated in previous step to create a recipe using 'createRecipe' operation
			- Once created use the 'getAllRecipes' operation to see all the recipes
			- Use the 'getRecipeById' operation to see a specific recipe
			- Use the 'updateRecipe' operation to update a recipe
			- Use the 'deleteRecipe' operation to delete a recipe
			- The getRecipeById, updateRecipe and deleteRecipe requires the recipeId
			- The RecipeId can be fetched from response of createRecipe or getAllRecipes operations


