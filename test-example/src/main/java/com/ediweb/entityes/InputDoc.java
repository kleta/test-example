package com.ediweb.entityes;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class InputDoc {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique=true)
	private String orderNumber;
	
	@Column(columnDefinition="CLOB")
	private String content;
	
	private LocalDate date;
	
	@OneToOne(mappedBy = "inputDoc", cascade = CascadeType.ALL, 
    fetch = FetchType.LAZY, optional = false)
	private OutputDoc outputDoc;

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public OutputDoc getOutputDoc() {
		return outputDoc;
	}

	public void setOutputDoc(OutputDoc outputDoc) {
		this.outputDoc = outputDoc;
	}
}
