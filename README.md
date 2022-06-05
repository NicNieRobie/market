# Book Market RESTful API

A small RESTful API for a bookshop (market) developed in Java Spring Boot.

Version: *1.0-SNAPSHOT*

Developer: *Vladislav Pendishchuk*

[TOC]

## Building the app

To build the application, run the command line in the project root folder 
and run the following command if you have Apache Maven installed on your system:

    mvn clean install

Otherwise, you might use the Maven Wrapper in the project's root folder.
On a UNIX system, run the following command:

    ./mvnw clean install

On Windows:

    mvnw.cmd clean install


Maven will automatically copy the generated `market-1.0-SNAPSHOT.jar` file to the `jar` folder in
the root project folder.

## Running the app

To run the application, you may choose one of the following options:

1. Running via the JAR file

   Before running the application, a local PostgreSQL database must be started
   via the `docker-compose.yml` configuration in the `docker\db` directory.
   To do that, run the following command in the aforementioned directory
   with the Docker running:
   
       docker-compose up

   To run the app, execute the following command in the folder
   that contains the `market-1.0-SNAPSHOT.jar` file (`jar` by default):

        java -jar market-1.0-SNAPSHOT.jar [args]
   
   In the application's arguments you may specify the following parameters:

   1. A JSON file
      
      This file will be used by the application to seed its database with
      data provided in the file. The data must be compliant with the format specified
      in the `ConfigurationModel`, `ConfigurationBookModel` and `ConfigurationAccountModel` classes.
      JSON content example:
      ```json
      {
        "account": {
          "money": 20000
        },
        "books": [
          {
            "author": "Steven C. McConnell",
            "name": "Code Complete",
            "price": 1000,
            "amount": 7
          },
          {
            "author": "Bruce Eckel",
            "name": "Thinking in Java",
            "price": 1500,
            "amount": 15
          },
          {
            "author": "Joshua Bloch",
            "name": "Effective Java",
            "price": 2500,
            "amount": 10
          }
        ]
      }
      ```
      If multiple JSON files are specified in the command line arguments, only data from
      the last specified existing file will be loaded into the database.
      
      If no JSON file is specified on application launch, the data that was previously
      persisted in the database will be used.
   2. A .txt or .log file
   
      The last specified existing file of such extension will be used
      by the application for logging, in addition to the standard output logging, 
      enabled by default.
   3. `staticSeed`
      
      Specifying this parameter on startup will make the application seed the database
      with the following static data:
      ```json
      {
        "account": {
          "money": 20000
        },
        "books": [
          {
            "author": "Steven C. McConnell",
            "name": "Code Complete",
            "price": 1000,
            "amount": 7
          },
          {
            "author": "Bruce Eckel",
            "name": "Thinking in Java",
            "price": 1500,
            "amount": 15
          },
          {
            "author": "Joshua Bloch",
            "name": "Effective Java",
            "price": 2500,
            "amount": 10
          }
        ]
      }
      ```
2. Running in Docker

   To run the application in Docker, open the command line in the `docker\app-db`
   directory and, with the Docker running, execute the following command:
   
       docker-compose up

   After executing the command, a PostgreSQL database and the application itself 
   will start, and the database will be seeded with the data used when specifying
   the `staticSeed` parameter.

In both cases, the server will run and accept requests at `http://localhost:8080`.

## Core functionality

The application handles requests on three endpoints:

1. `GET /account`
   
   Returns account data in the format, similar to the JSON below:
   ```json
   {
    "balance": 17500,
    "books": [
        {
            "book": {
                "name": "Effective Java",
                "author": "Joshua Bloch"
            },
            "amount": 1
        }
    ]
   }
   ```
2. `GET /market`
   
   Returns market data in the format, similar to the JSON below:
   ```json
   {
    "products": [
        {
            "id": 1,
            "book": {
                "name": "Code Complete",
                "author": "Steven C. McConnell"
            },
            "price": 1000,
            "amount": 7
        },
        {
            "id": 2,
            "book": {
                "name": "Thinking in Java",
                "author": "Bruce Eckel"
            },
            "price": 1500,
            "amount": 15
        },
        {
            "id": 3,
            "book": {
                "name": "Effective Java",
                "author": "Joshua Bloch"
            },
            "price": 2500,
            "amount": 9
        }
    ]
   }
   ```
3. `POST /market/deal`
   
   Is used to perform a books purchase deal between the client and the shop.
   The request body must have the following format:

   ```json
   {
    "id": <product ID>,
    "amount": <amount of books to be purchased>
   }
   ```

   Returns HTTP code `200`, if the deal was performed successfully.
   Returns HTTP code `400`, if the request body was invalid
   (product ID invalid, amount too big, not enough money in the account).
   Returns HTTP code `500` on internal server errors.
## Additional functionality

### Additional endpoints

In addition to the required functionality, several new endpoints were added
to make it possible to interact with the market more:

1. `GET /market/{id}`
   
   Returns data on the product with the specified ID in the format, 
   similar to the JSON below:

   ```json
   {
    "id": 3,
    "book": {
        "name": "Effective Java",
        "author": "Joshua Bloch"
    },
    "price": 2500,
    "amount": 9
   }
   ```
2. `POST /market`
   
   Creates new product on the market. The request body must have the following format:

   ```json
   {
    "name": "<book name>",
    "author": "<book author>",
    "price": "<book price>",
    "amount": "<the amount of books available>"
   }
   ```
3. `PATCH /market/{id}`

   Updates data on the product with the specified ID. The request body must 
   have the following format (all fields are optional):

   ```json
   {
    "book": {
        "name": "<new book name>",
        "author": "<new book author>"
    },
    "price": "<new book price>",
    "amount": "<new amount of books available>"
   }
   ```

### Data persistence

To implement data persistence, Spring Data JPA was used to save and update entities in the PostgreSQL database.

On launch, the administrator may choose to either seed the database with new data, or use the data previously
persisted in the database, by specifying or choosing not to specify the seeding arguments.

### Tests

To test the application functionality, unit and integration tests are used, utilizing JUnit 5, Mockito and Hamcrest libraries.

### Swagger

The springdoc-openapi library is used to generate Swagger OpenAPI endpoints documentation.
The Swagger UI can be accessed on `http://localhost:8080/swagger`.

### Docker

A Docker image, `nicnierobie/spring-market-svc:1.0-docker`, was built from the application.
A container build from this image, unlike the local-connection-based JAR version of the API, will
utilize internal Docker URI of the database for connection.

By default, the containerized version of the application will seed the database each time the container
is created. This behaviour may be later disabled (on production deployment) by removing the `staticSeed`
argument in the Dockerfile in the `docker\app-db` directory.

## Implementation notes

Several aspects of the implementation may be improved, but were deliberately made the way they currently
are not to violate the API specification provided in the technical requirements. All of them can be later
implemented. These aspects include:

* Lack of HATEOAS
  
  To become 'truly' RESTful, the implemented API has to additionally feature HAL structure in its
  response DTOs to be compliant with the HATEOAS constraint.

  HAL structure was deliberately not included in the response DTOs not to violate the requirements,
  but they can be easily added as the application uses instances that implement the `RepresentationModelAssembler`
  interface from the Spring HATEOAS framework to contruct DTOs, and all endpoints wrap the result
  in the `ResponseEntity` wrapper, compatible with String HATEOAS APIs.

* Account management
  
  In the current implementation, some of the `AccountService`'s functionality is redundand or is,
  de-facto, a workaround. The idea behind using a separate repository and a service to store
  a single account is based on following considerations:

  * This allows to persist user account data, making it possible to save account state
    after each deal;
  * Storing the only account in the same database as for the rest of data makes it more easily
    accessible, removing the need to read data from other databases or resource files;
  * This allows to expand the functionality later, allowing users to sign up and sign in,
    as the repository for user accounts already exists.
  
  The sign-in and sign-up mechanisms were not implemented due to the requirements to
  the request signature. In the requirements, no IDs, access tokens or cookies are used
  to access account data, making it impossible to accurately authenticate and
  authorize the user via HTTP. Thus, to make it possible to access account data and the deal endpoint
  without arbitrary authentication and authorization mechanisms, the workaround was implemented,
  leaving room for further improvement.

  Additionally, an issue exists of the `GET /account` endpoint slightly violating the REST
  style - its route implies that only one resource of type `Account` exists, as otherwise
  the individual account data endpoint would have to be a 'subdirectory' of the `/accounts`
  directory (accounts controller).

* Lack of the product deletion endpoint
  
  This endpoint is lacking due to the aforementioned issue with authorization - DELETE endpoints
  are usually made accessible only to the administator due to the potential significant impact
  of entity deletions on the database integrity.

  As there is currently no way of authorizing users, lack of the deletion endpoint makes API more secure.

* Kubernetes

  Initially, it was planned to make the project ready for Kubernetes deployment, but this idea was rejected
  due to lack of dedicated cloud-based managed database (hosting a database on the local Kubernetes cluster
  is both challenging due to many limitations of local Kubernetes clusters and is, technically, pointless).
