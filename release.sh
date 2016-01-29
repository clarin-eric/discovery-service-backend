#!/bin/sh

VERSION="1.8.3-SNAPSHOT"

cp discovery-metadata-api/target/metadata-api-${VERSION}.war discovery-service/target/discovery-service-${VERSION}.war .
tar -pczvf release-${VERSION}.tar.gz *.war
