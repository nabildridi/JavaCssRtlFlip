package org.nd.models;

public class Css {
	
	private String original;
	private String converted;
	private String filename;
	

	public Css(String original, String filename) {
		super();
		this.original = original;
		this.filename = filename;
	}
	
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String original) {
		this.original = original;
	}
	public String getConverted() {
		return converted;
	}
	public void setConverted(String converted) {
		this.converted = converted;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	

}
