package de.faz;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class JsonTestExample {

	public static final String TEST_URL = "http://www.faz.net/json/aktuell/politik/ausland/europa/ostukraine-armee-beginnt-einsatz-gegen-separatisten-12896719.html";
	private JsonSchemaFactory factory;

	@Before
	public void setUp() throws MalformedURLException, URISyntaxException {
		factory = JsonSchemaFactory.byDefault();
	}

	@Test
	public void testSchema() throws IOException, ProcessingException {
		final JsonSchema schema = factory.getJsonSchema(JsonLoader.fromResource("/v1.0/article-schema.json"));
		JsonNode jsonNode = JsonLoader.fromURL(new URL(TEST_URL));
		ProcessingReport report = schema.validate(jsonNode);
		System.out.println(report);
		assertThat(report.isSuccess()).isTrue();
	}

	@Test
	public void testNewVersionAgainstOldSchema() throws IOException, ProcessingException {
		final JsonSchema schema = factory.getJsonSchema(JsonLoader.fromResource("/v1.0/article-schema.json"));
		JsonNode jsonNode = JsonLoader.fromResource("/test-json/test-article-v2.3.json");
		ProcessingReport report = schema.validate(jsonNode);
		System.out.println(report);
		assertThat(report.isSuccess()).isTrue();
	}

	@Test
	public void testOldVersionAgainstNewSchema() throws IOException, ProcessingException {
		final JsonSchema schema = factory.getJsonSchema(JsonLoader.fromResource("/v2.3/article-schema.json"));
		JsonNode jsonNode = JsonLoader.fromURL(new URL(TEST_URL));
		ProcessingReport report = schema.validate(jsonNode);
		System.out.println(report);
		assertThat(report.isSuccess()).isTrue();
	}
}
