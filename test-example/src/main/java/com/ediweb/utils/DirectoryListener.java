package com.ediweb.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ediweb.services.FileService;

public class DirectoryListener extends FileAlterationListenerAdaptor {
	
	private Logger log = LoggerFactory.getLogger(DirectoryListener.class);

	@Override
	public void onFileCreate(File file) {
		try {
			log.info("Обнаружен новый файл " + file.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
