package com.ediweb.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ediweb.utils.DirectoryListener;

@Service
@Scope("singleton")
public class FileService {
	
	private Logger log=LoggerFactory.getLogger(FileService.class);
	
	@Value("${inputDir}")
	private String inputDir;
	
	@Value("${outputDir1}")
	private String outputDir1;

	@Value("${outputDirZip}")
	private String outputDir2;
	
	@PostConstruct
	public void init() throws Exception {
		File folder = new File(inputDir);
		 
        if (!folder.exists()) {
            // Test to see if monitored folder exists
            throw new RuntimeException("Directory not found: " + inputDir);
        }
 
        FileAlterationObserver observer = new FileAlterationObserver(folder);
        FileAlterationMonitor monitor =
                new FileAlterationMonitor(5000);
        FileAlterationListener listener = new DirectoryListener();
        observer.addListener(listener);
        monitor.addObserver(observer);
        monitor.start();
	}
	
	public List<File> getFiles(){
		return null;		
	}
}
