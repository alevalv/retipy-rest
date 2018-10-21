retipy-rest
===========
[![Build Status](https://travis-ci.org/alevalv/retipy-rest.svg?branch=master)](https://travis-ci.org/alevalv/retipy-rest)
[![Coverage Status](https://codecov.io/gh/alevalv/retipy-rest/branch/master/graph/badge.svg)](https://codecov.io/gh/alevalv/retipy-rest)

retipy-rest is part of the [retipy](https://github.com/alevalv/retipy) project.


retipy-rest is a transactional system that is able to handle medical notes of patients focused on
retinal examinations, with the addition to handle access to image processing endpoints from
[retipy-python](https://github.com/alevalv/retipy-python) to offer extra information for the user.

Made with Kotlin + SpringBoot 2

How to run the project
----------------------

To run the server use the command `./gradlew bootRun`, this deploys a in-memory database for testing
purposes.

To run all code checks (test, etc) run the command `./gradlew build`

How to run the project with MariaDB as database
----------------------

You can configure retipy-rest to connect to a mysql database. If you have docker, you can run the
following commands to have a local mysql database running:
```sh
$ docker pull mariadb:10.2
$ docker run -p 3306:3306 --name retipy-mysql-dev -e MYSQL_RANDOM_ROOT_PASSWORD=yes -e MYSQL_DATABASE=retipydev -e MYSQL_USER=retipy -e MYSQL_PASSWORD=verysecurepassword -d mariadb:10.2
```
In `build.gradle` uncomment mariadb dependency and comment the h2database dependency

In `application.properties` uncomment all spring. properties.

Run it with `./gradlew bootRun`

Don't forget to remove/exclude those changes before committing.

Building the docker image
-------------------------

Run the following command to build the image locally:

```sh
docker build --rm -f "Dockerfile" -t retipy-rest:latest .
```


License
-------

retipy-rest is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
