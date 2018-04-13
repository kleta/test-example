package com.ediweb.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ediweb.entityes.InputDoc;
import com.ediweb.services.DBService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
public class EDIController {

	@Autowired
	private DBService ds;

	@RequestMapping(path = "/download", method = RequestMethod.GET)
	public ResponseEntity<Resource> download(@RequestParam(value = "orderNum") String param) throws IOException, FileNotFoundException {
		InputDoc doc = ds.getDocument(param);
		if (doc != null) {
			String fName = doc.getOrderNumber() + ".xml";
			HttpHeaders headers = new HttpHeaders();
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			headers.add("Content-Disposition", "attachment; filename=\""+fName+"\"");
			headers.add("Content-Type", "application/octet-stream");
			byte[] bytes = doc.getOutputDoc().getContent().getBytes(Charset.forName("UTF-8"));
			ByteArrayResource resource = new ByteArrayResource(bytes);
			ResponseEntity<Resource> body = ResponseEntity.ok().headers(headers).contentLength(bytes.length)
					.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
			return body;
		}
		throw new FileNotFoundException("Order with number "+param+" not found");	
	}

	@RequestMapping(path = "/docs", method = RequestMethod.GET)
	public List<InputDoc> getDocuments(@RequestParam(value = "orderNum", defaultValue = "") String orderNum) {
		List<InputDoc> inputDocs = ds.getInputDocs(orderNum);
		inputDocs.forEach(doc -> doc.setContent(null));
		inputDocs.forEach(doc -> doc.setOutputDoc(null));
		return inputDocs;
	}

}
