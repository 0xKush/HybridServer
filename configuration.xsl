<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:hb="http://www.esei.uvigo.es/dai/hybridserver">
                <xsl:output method="html" indent="yes"/>
<xsl:template match="/">
        <html>
            <head>
                <title>Configuration</title>
            </head>
            <body>
                <div id="configuration">
                    <h2>Configuration</h2>
                    <div id="connections">
                        <h3>Connections</h3>
                        <xsl:apply-templates select="hb:configuration/hb:connections"/>
                    </div>
                    <div id="database">
                        <h3>Database</h3>
                        <xsl:apply-templates select="hb:configuration/hb:database"/>
                    </div>
                    <div id="servers">
                        <h3>Servers</h3>
                        <xsl:apply-templates select="hb:configuration/hb:servers"/>
                    </div>
                </div>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="hb:connections">
            <li>
                <b>HTTP: </b><xsl:value-of select="hb:http"/>
            </li>
            <li>
                <b> WebService: </b><xsl:value-of select="hb:webservice"/>
            </li>
            <li>
                <b>Clients NO: </b><xsl:value-of select="hb:numClients"/>
            </li>
    </xsl:template>

    <xsl:template match="hb:database">
            <li>
                <b>User: </b><xsl:value-of select="hb:user"/>
            </li>
            <li>
                <b>Password: </b><xsl:value-of select="hb:password"/>
            </li>
            <li>
                <b>URL: </b><xsl:value-of select="hb:url"/>
            </li>
    </xsl:template>

    <xsl:template match="hb:servers">
        <xsl:for-each select="hb:server">
                <li><b>Server: </b><xsl:value-of select="@name"/> </li>
                <li><b>WSDL: </b><xsl:value-of select="@wsdl"/> </li>
                <li><b>Service: </b><xsl:value-of select="@service"/> </li>
                <li><b>HttpAddress: </b><xsl:value-of select="@httpAddress"/> </li><br/>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>