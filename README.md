# Welcome TO MEET & FUN API
This is an API you can use to retrieve drinks and jokes from external APIS, and have a great time!

You can also save your favorites jokes and drinks into your profile.
## Setup
For running the API follow the next steps:

Download Zip and extract content.

## On Windows
### Step 1: Install Docker Desktop for Windows
Go to the Docker Desktop for [Windows Download Page](https://docs.docker.com/desktop/install/windows-install/).
Click on the "Get Docker" button to download the installer.
Run the installer and follow the on-screen instructions. This process installs both Docker and Docker Compose.
After installation, Docker will ask you to log in. You can create a Docker account if you don't have one.
### Step 2: Verify Installation
Open a command prompt or PowerShell.
Run docker --version to check the Docker version.
Run docker-compose --version to check the Docker Compose version.
### Step 3: Use Docker Compose
Navigate to the directory containing your docker-compose.yml file in the command prompt or PowerShell.
#### Run docker-compose up --build.
Might need system restart for docker correct usage.
## On macOS
### Step 1: Install Docker Desktop for Mac
Go to the [MacOs Download Page](https://docs.docker.com/desktop/install/mac-install/).
Click on the "Download from Docker Hub" button to go to the download section.
Click "Download" for the stable version, and the .dmg file will download.
Open the .dmg file and drag the Docker icon to the Applications folder to install.
Launch Docker from the Applications folder. Docker will ask for your system password during the setup process.
### Step 2: Verify Installation
Open a terminal.
Run docker --version to check the Docker version.
Run docker-compose --version to check the Docker Compose version.
### Step 3: Use Docker Compose
Navigate to the directory containing your docker-compose.yml file in the terminal.
#### Run docker-compose up --build.

## API ENDPOINTS
#### You can check all endpoint information by going to http://localhost:8080/swagger-ui/index.html#/ 
You can find the Postman Collection to use this API in the root folder of the proyect. You can import the collection json file
named:
#### Meet_and_Fun_Thortful_Challenge.postman_collection.json

To import, open Postman, click on import (top left) button and select json file.

### Authorization Signup and Login

As you can find in the Postman collection provided, you can hit the SignUp API endpoint with credentials body:
#### http://localhost:8080/auth/signup
example body:
{
"username":"John Wick",
"password":"P@ssWordJohn"
}

Once you SignUp, you can hit the Login endpoint:
#### http://localhost:8080/auth/login
example body:
{
"username":"John Wick",
"password":"P@ssWordJohn"
}

#### The response of the login will include a token. Use this token to access all other endpoints by adding Authorization Header Bearer {token} to the postman collection
#### Check swagger documentation for additional endpoints such as search jokes, search drinks, save drink to profile, save joke to profile, retrieve all jokes from user and retrieve all drinks from user.

### Technologies used in this API
- Java Amazon Corretto 21
- Gradle 8.7
- MongoDB 
- Docker
- JUnit and Mockito for Unit Testing
- APIs used for this project: https://jokeapi.dev/ & https://www.thecocktaildb.com/api.php