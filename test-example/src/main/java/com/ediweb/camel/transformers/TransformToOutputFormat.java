package com.ediweb.camel.transformers;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Component
public class TransformToOutputFormat implements Processor {
	@Value("${xslFileName}")
	private String xslName;

	@Value("${outputDir}")
	private String outputDir;
	
	@Value("${xpathOrderNumber}")
	private String xpathString;

	@Override
	public void process(Exchange ex) throws Exception {
		URL url =getClass().getClassLoader().getResource(xslName);
		File xsl = new File(url.getPath());
		Message inMessage = ex.getIn();
		GenericFile body = (GenericFile)inMessage.getBody();
		File original = (File)body.getFile();
		TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(xsl);
        Transformer transformer = factory.newTransformer(xslt);
        Source text = new StreamSource(original);
        File outputFile = new File("tmp.xml");
		transformer.transform(text, new StreamResult(outputFile));
		XPath xpath = XPathFactory.newInstance().newXPath();
		FileInputStream fin = new FileInputStream(outputFile);
		InputSource inputSource=new InputSource(fin);
		NodeList nodes = (NodeList) xpath.evaluate(xpathString,
				inputSource, XPathConstants.NODESET);
		if(nodes.getLength()!=1) {
			outputFile.delete();
			throw new IllegalArgumentException("Произошло неправильное преобразование");
		}
		String orderNum=nodes.item(0).getTextContent();
		File file = new File(outputDir+"/"+orderNum+".xml");
		outputFile.renameTo(file);
		GenericFile<File> f=new GenericFile<>();
		f.setFile(file);
		inMessage.setBody(f);
		ex.setOut(inMessage);
	}

}
