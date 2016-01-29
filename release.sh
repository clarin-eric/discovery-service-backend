#!/bin/sh

cp discovery-metadata-api/target/metadata-api-1.8.2.1.war discovery-service/target/discovery-service-1.8.2.1.war .
tar -pczvf release-1.8.2.1.tar.gz *.war
