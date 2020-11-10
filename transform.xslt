<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">

<entries>
	<xsl:for-each select="entries/entry">
		<entry>
			<xsl:attribute name="field">		
                <xsl:value-of select="field"/>
			 </xsl:attribute>
		</entry>
    </xsl:for-each>
</entries>
</xsl:template>
</xsl:stylesheet>