package com.ediweb.controllers;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import com.ediweb.Application;
import com.ediweb.entityes.InputDoc;
import com.ediweb.entityes.OutputDoc;
import com.ediweb.repositories.InputDocRepository;
import com.ediweb.services.DBService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class EDIControllerTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MediaType fileContentType = new MediaType(MediaType.APPLICATION_OCTET_STREAM.getType(),
			MediaType.APPLICATION_OCTET_STREAM.getSubtype(), Charset.forName("utf8"));
	
	private MockMvc mockMvc;

	private String orderNum = "123";

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private DBService ds;
	
	@Autowired
	private InputDocRepository idr;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	
	

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);

		assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		
		idr.deleteAll();
		
		InputDoc input = new InputDoc();
		input.setDate(LocalDate.now());
		input.setOrderNumber(orderNum);
		String inputContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<Group>\r\n" + "</Group>\r\n";
		String outputContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<Group>\r\n" + "</Group>\r\n";
		input.setContent(inputContent);
		OutputDoc output = new OutputDoc();
		output.setContent(outputContent);
		input.setOutputDoc(output);
		output.setInputDoc(input);
		ds.saveInputDoc(input);
		
		input = new InputDoc();
		input.setDate(LocalDate.now());
		input.setOrderNumber("1234");
		inputContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<Group2>\r\n" + "</Group2>\r\n";
		outputContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<Group2>\r\n" + "</Group2>\r\n";
		input.setContent(inputContent);
		output = new OutputDoc();
		output.setContent(outputContent);
		input.setOutputDoc(output);
		output.setInputDoc(input);
		ds.saveInputDoc(input);
	}

	@Test
	public void testDownload() throws Exception {
		mockMvc.perform(get("/download?123"));
	}

	@Test
	public void testGetDocuments() throws Exception {
		mockMvc.perform(get("/docs?123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)));
	}

}
