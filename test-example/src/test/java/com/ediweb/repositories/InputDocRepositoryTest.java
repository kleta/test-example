package com.ediweb.repositories;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.ediweb.entityes.InputDoc;

@RunWith(SpringRunner.class)
@DataJpaTest
public class InputDocRepositoryTest {
	
	@Autowired
    private TestEntityManager entityManager;
	
    @Autowired
    private InputDocRepository inputDocRepository;
    
    @Before
    public void setUp(){
    	InputDoc doc = new InputDoc();
		doc.setOrderNumber("12");
		entityManager.persist(doc);
		
		doc=new InputDoc();
		doc.setOrderNumber("123");
		entityManager.persist(doc);
    }

	@Test
	public void testFindByOrderNumberContaining() {		
		List<InputDoc> docs = inputDocRepository.findByOrderNumberContaining("12");
		assertTrue(docs.size()==2);
		
		docs = inputDocRepository.findByOrderNumberContaining("15");
		assertTrue(docs.size()==0);
	}

	@Test
	public void testFindByOrderNumber() {
		InputDoc doc = inputDocRepository.findByOrderNumber("123");
		assertNotNull(doc);
		assertEquals(doc.getOrderNumber(), "123");
		
		doc = inputDocRepository.findByOrderNumber("12");
		assertNotNull(doc);
		assertEquals(doc.getOrderNumber(), "12");
		
		doc = inputDocRepository.findByOrderNumber("15");
		assertNull(doc);
	}

}
