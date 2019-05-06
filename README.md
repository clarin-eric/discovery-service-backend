# CLARIN Discovery Service Backend
License: to be decided

The project contains one module:
 * discovery-metadata-api

## discovery-metadata-api
This module provides a java web application providing a set of REST interfaces
for geo-ip lookup and SAML --> JSON conversion and two additional endpoints.

Endpoints:
 * /rest, the metadata conversion api.
 * /proxy, return the json information with permissive CORS headers.
 * /status, provides some statistics on the available data.
 
 ## Frontend
 The discovery service frontend can be found here: https://github.com/clarin-eric/discovery-service-frontend
