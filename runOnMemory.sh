#!/bin/sh
sed -i '/spring./ s/^#*/#/g' src/main/resources/application.properties
sed -i '/mariadb.jdbc/ s?^[//]*?//?g' build.gradle
sed -i '/h2database/ s?^//??g' build.gradle
