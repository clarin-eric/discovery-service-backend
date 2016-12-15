#!/bin/sh

VERSION="1.9.2"

rm *.war
cp discovery-metadata-api/target/metadata-api-${VERSION}.war discovery-service/target/discovery-service-${VERSION}.war .
tar -pczvf release-${VERSION}.tar.gz *.war
