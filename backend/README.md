# Ramblin Treks Backend
Our backend application is based around [Docker](https://www.docker.com). This allows for portability and scalability. Docker can be hosted by any device that can run docker, it is the only dependency.

## Installation Instructions Simple

### Dependencies
1. Docker
2. docker-compose
2. ports 9001, 9003, 5432 open

### Installation Steps
1. __[Install Docker](https://docs.docker.com/install/)__ be sure to follow the directions for your server operating system.

2. __[Install docker-compose](https://docs.docker.com/compose/install/)__

3. __Pull the docker images__

  ```docker pull jasongibson274/path-svc```
  ```docker pull jasongibson274/data-svc```

4. __Create a file__ called *docker-compose.yml* and then copy [this](https://github.com/JasonGibson274/Ramblin-Treks/blob/backend/backend/deploy.txt) into it

5. __Start the containers__

```docker-compose up```

## Building From Source
Follow the same steps as above but additionally

### Dependencies
1. Git
2. Maven

### Installation Steps

   1. __[Install Maven](https://maven.apache.org/install.html)__ be sure to follow the directions for your server operating system.

   2. __Move to the directory that will be used to store the code__

   3. __Clone the repository__

   ```git clone https://github.com/JasonGibson274/Ramblin-Treks.git```

   4. __Move to backend folder__

   ```cd backend```

   5. __Build the code__

   ```mvn clean install```

   6. __Build and run the Docker containers__

   ```docker-compose up -d --build```

   7. __Verify that all three continers are running by doing__

   ```docker ps```


## Troubleshooting
