<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>

    <xsl:template match="/">
        <!--<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">-->
            <html>
            <head>
                <title>Shibboleth Metadata REST API</title>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

                <style type="text/css">
                    body { font-family: arial; font-size: 12px; }
                    th { font-size: 12px; font-weight: bold; text-align: left;}
                    td { font-size: 12px; }

                    .left {float: left;}
                    .right {float: right;}
                    .border {border: solid 1px #000000;}
                    .border_top {border-top: solid 1px #000000;}
                    .border_bottom {border-bottom: solid 1px #000000;}
                    .even {background-color: #ffffff;}
                    .odd {background-color: lightgray}

                    .api_container {position: relative;height: 75px; width: 860px; margin: 10px;}
                    .api_method {width: 346px; margin: 2px;}
                    .api_description_container {width: 500px;}
                    .api_description {width: 496px; margin: 2px;}
                    .api_parameter_container {width: 500px;}

                </style>
            </head>
            <body>

            <xsl:apply-templates select="/rest-api/description" />
            <p />
            Exposed APIs:
            <xsl:apply-templates select="/rest-api/api-call"/>

            </body>
        </html>
    </xsl:template>

    <xsl:template match="description">
        <xsl:value-of select="title" />
        <p />
        Full project documentation can be found
        <a href="https://trac.mpi.nl/wiki/SDS">
            <xsl:attribute name="href">
                <xsl:value-of select="doc"/>
            </xsl:attribute>
            here
        </a>.
    </xsl:template>

    <xsl:template match="api-call">
        
        <div class="api_container even border_top border_bottom">
            <div class="left api_method">
                <a target="_new">
                    <xsl:attribute name="href">
                        <xsl:value-of select="name"/>
                    </xsl:attribute>
                    <xsl:value-of select="name"/>
                </a>
                <br />
                Method: <xsl:value-of select="method" />
            </div>
            
            <div class="left api_description_container">
                <div class="api_description">
                    <xsl:value-of select="description"/>
                </div>
                <div class="api_parameter_container border_top">
                        <table>
                            <thead>
                                <tr>
                                    <th width="75">Name</th>
                                    <th>Description</th>
                                </tr>
                            </thead>
                            <tbody>
                            <xsl:for-each select="parameters/parameter">
                                <tr>
                                    <td width="100"><xsl:value-of select="name"/></td>
                                    <td><xsl:value-of select="description"/></td>
                                </tr>
                            </xsl:for-each>
                            </tbody>
                        </table>
                </div>
            </div>
        </div>
        
    </xsl:template>
    
</xsl:stylesheet>
