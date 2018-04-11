package com.ediweb.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ediweb.entityes.InputDoc;
import com.ediweb.repositories.InputDocRepository;

@Service
@Scope("singleton")
public class DBService {
	
	@Autowired
	private InputDocRepository ir;
	
	
	public void saveInputDoc(InputDoc doc) {
		
	}
}
