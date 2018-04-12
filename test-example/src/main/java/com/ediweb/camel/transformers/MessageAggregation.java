package com.ediweb.camel.transformers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ediweb.entityes.InputDoc;
import com.ediweb.services.DBService;

@Component
public class MessageAggregation implements AggregationStrategy {
	
	@Value("${outputDirZip}")
	private String zipPath;
	
	@Autowired
	private DBService ds;
	
	private Logger log=LoggerFactory.getLogger(MessageAggregation.class);
	
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		log.info("Aggregate called with oldExchange = "
				+ (oldExchange == null ? "null" : oldExchange.getIn().getBody().toString()) + ", newExchange = "
				+ newExchange.getIn().getBody().toString());
		if(oldExchange!=null && newExchange!=null) {
			File oldFile = ((GenericFile<File>)oldExchange.getIn().getBody()).getFile();
			File newFile = ((GenericFile<File>)newExchange.getIn().getBody()).getFile();
			if(!oldFile.getName().equals(newFile.getName())) {
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(zipPath+"/"+oldFile.getName().replace(".xml", ".zip"));
					ZipOutputStream zos = new ZipOutputStream(fos);
					addToZipFile(newFile, zos);
					addToZipFile(oldFile, zos);
					persistToDB(oldFile, newFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		return newExchange;
	}
	private void persistToDB(File oldFile, File newFile) {
		InputDoc input=new InputDoc();
		input.setDate(LocalDate.now());
		String orderNum = newFile.getName().replaceAll(".xml", "");
		input.setOrderNumber(orderNum);
		
	}
	private void addToZipFile(File file, ZipOutputStream zos) throws FileNotFoundException, IOException {

		log.info("Writing '" + file + "' to zip file");

		
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(file.getName());
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}
}
