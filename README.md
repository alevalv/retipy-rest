retipy-rest
===========
[![Build Status](https://travis-ci.org/alevalv/retipy-rest.svg?branch=master)](https://travis-ci.org/alevalv/retipy-rest)
[![Coverage Status](https://codecov.io/gh/alevalv/retipy-rest/branch/master/graph/badge.svg)](https://codecov.io/gh/alevalv/retipy-rest)

A REST API for a simple transactional system to store retinal evaluations that have been tagged using [retipy](https://github.com/alevalv/retipy).
Made with Kotlin and SpringBoot 2

How to run the project with mysql
----------------------
By default, retipy-rest is configured to connect to a mysql database. If you have docker, you can
run the following commands to have a local mysql database running:
```sh
$ docker pull mariadb:10.2
$ docker run -p 3306:3306 --name retipy-mysql-dev -e MYSQL_RANDOM_ROOT_PASSWORD=yes -e MYSQL_DATABASE=retipydev -e MYSQL_USER=retipy -e MYSQL_PASSWORD=verysecurepassword -d mariadb:10.2

``` 

To run the server use the command `./gradlew bootRun`

To run all code checks (test, etc) run the command `./gradlew build`

How to run the project with memory database
----------------------
Apply the dbmemory.patch file at the root of the project with the following command:
```sh
$ ./runOnMemory.sh
```
Run it with `./gradlew bootRun`

Don't forget to remove/exclude those changes before committing.

License
-------
retipy-rest is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
