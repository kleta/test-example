package com.ediweb.entityes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class OutputDoc {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(columnDefinition="CLOB")
	private String content;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idInputDoc")
	private InputDoc inputDoc;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public InputDoc getInputDoc() {
		return inputDoc;
	}

	public void setInputDoc(InputDoc inputDoc) {
		this.inputDoc = inputDoc;
	}
}
