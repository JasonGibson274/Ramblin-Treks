# Ramblin Treks Backend
Our backend application is based around [Docker](https://www.docker.com). This allows for portability and scalability. Docker can be hosted by any device that can run docker, it is the only dependency.

## Change Log
[CHANGELOG.md](CHANGELOG.md)

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

4. __Create a file__ called *docker-compose.yml* and then copy [this](https://github.com/JasonGibson274/Ramblin-Treks/blob/backend/master/deploy.txt) into it

5. __Start the containers__ in the same directory type

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

__Docker containers fail to start__

If the service does not seem like it has started type

```docker ps```

You should see output similiar to

```
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES
e74089e8fc70        data-svc            "java -Djava.securit…"   24 hours ago        Up 59 seconds       0.0.0.0:9001->9001/tcp   backend_data-svc_1
4e8c4ca82494        postgres            "docker-entrypoint.s…"   24 hours ago        Up 59 seconds       0.0.0.0:5432->5432/tcp   backend_data-svc-db_1
a06c6b3ad51a        path-svc            "java '-Djava.securi…"   24 hours ago        Up About a minute   0.0.0.0:9003->9003/tcp   backend_pather_1
```

if you do not see all 3 containers determine which is missing and then type

```docker ps -a```

You should see a recently stopped docker container.

```
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS                        PORTS                    NAMES
...
e74089e8fc70        data-svc            "java -Djava.securit…"   24 hours ago        Exited (143) 19 seconds ago                            backend_data-svc_1
...
```

The above shows that the data-svc has failed to start. To open the log use the following command with the correct CONTAINER ID

```docker logs CONTAINER ID```
```docker logs e74089e8fc70```

common error include

1. __Caused by: org.hibernate.HibernateException: Access to DialectResolutionInfo cannot be null when 'hibernate.dialect' not set__

This means that the service cannot connect to the postgres database, ensure that the postgres container is running

2. __Port busy or failed to bind__

This error means that another process is using the port that the container wants to connect to. Stop that process and then restart the container.
