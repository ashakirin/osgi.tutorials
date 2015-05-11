package org.travelex.poc.talend.camel;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class FileCamelRoute extends RouteBuilder {
    public static final String ENDPOINT = "direct:FileCamelRoute";

	@Override
	public void configure() throws Exception {
        from(ENDPOINT)
        .log(LoggingLevel.INFO, "Body: ${body}")
        .to("file://c:/1/camel");
	}
}
