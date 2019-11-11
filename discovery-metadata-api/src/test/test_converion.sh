#!/bin/sh

curl -kfLsS -X POST -d "@input.xml" -H"Content-Type: application/xml" -H"Accept: application/json" "http://localhost:8080/sma/rest/metadata/discojuice"
