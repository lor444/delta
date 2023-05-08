package it.corsojava.bookstore.persistence;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="web-app")
public class WebApp {

	@XmlElement(name="context-param")
	public List<ContextParam> params=new ArrayList<ContextParam>();
}

class ContextParam{
	private String paramName;
	
	private String paramValue;
	
	@XmlElement(name="param-name")
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	@XmlElement(name="param-value")
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	
}
