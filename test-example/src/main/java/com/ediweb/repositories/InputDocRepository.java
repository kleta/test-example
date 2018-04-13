package com.ediweb.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ediweb.entityes.InputDoc;

public interface InputDocRepository extends CrudRepository<InputDoc, Long>{
	
	List<InputDoc> findByOrderNumberContaining(String num);

	InputDoc findByOrderNumber(String param);
}
