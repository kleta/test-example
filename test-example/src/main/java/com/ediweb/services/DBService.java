package com.ediweb.services;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	Logger log=LoggerFactory.getLogger(DBService.class);
	
	
	public DBService(InputDocRepository repo) {
		ir=repo;
	}

	public InputDoc saveInputDoc(InputDoc doc) {
		ir.save(doc);
		log.info("Сохранен документ {}", doc.getOrderNumber());
		return doc;
	}
	
	public List<InputDoc> getInputDocs(String num){
		List<InputDoc> list = ir.findByOrderNumberContaining(num+"%");
		log.info("Найдено {} документов", list.size());
		return list;
	}

	public InputDoc getDocument(String param) {
		return ir.findByOrderNumber(param);
	}
}
