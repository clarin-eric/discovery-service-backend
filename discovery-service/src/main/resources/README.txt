
The Shibboleth Metadata Discovery Service (sds) is a java webapplication allowing
easy deployment of a discojuice based where are you from portal page.

The package provides two servlets:
    .../discojuice
        The actual discojuice portal page

    .../proxy
        A proxy streaming the discojuice json metadata to the discojuice portal page.
        This is required the meet the same host requirement in json ajax request while
        allowing for the discojuice metadata to be hosted on a remote server.

As deployed on the lux17 this would result in:
    http://lux17.mpi.nl/ds/sds/discojuice
    http://lux17.mpi.nl/ds/sds/proxy


changed in disccojuice javascript
jsonp -> json
acl=true; --> acl=false;


CLARIN SPs currently point to catalog.clarin.eu/discojuice/idp.html, this is now forwarded
by an apache rewrite rule to catalog.clarin.eu/mw/sds/discojuice

RewriteRule ^/discojuice/idp.html /mw/sds/discojuice [R,NE]


** cronjobs **

export user accounts from clarin.eu website to catalog.clarin.eu IDP:
*/15 * * * *  webuser  /lat/tools/shibboleth-idp/bin/getaai.sh

convert shibboleth xml metadata to discojuice json metadata:
5 * * * *   webuser /lat/webapps/shibboleth/conversion/convert_metadata.sh 2>&1 | tr -d '\a\b\f\r\t\v\0' | mail -s clarin_discojuice willem.elbers@mpi.nl