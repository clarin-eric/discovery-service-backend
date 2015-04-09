
************
Dependencies
************

We use Jersey + JAXB to provide the REST api. Since we want to output JSON in
natural notation, a specific dependency on JAXB 2.1.12+ (2.2.3 is
used) and jackson (jackson-all 1.6.7) is required.

********
REST API
********

Expose a rest webservice to convert shibboleth metadata into discojuise json.

exposed urls:
.../rest/metadata/discojuice
    Transform the shibboleth metadata into discojuice json, including geo information.

.../rest/lookup/ip/{ip}
    Resolve a IP address to a geo location
    {ip}    the IP address to resolve

.../rest/lookup/url/{url}
    Resolve a url to a geo location, by translating the url to an ip first
    {url}    the url to resolve