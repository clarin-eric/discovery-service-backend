#!/bin/sh

VERSION="2.1.0"

mvn clean install
rm -rf *.war
cp discovery-metadata-api/target/metadata-api-*.war .
tar -pczvf release-${VERSION}.tar.gz *.war
