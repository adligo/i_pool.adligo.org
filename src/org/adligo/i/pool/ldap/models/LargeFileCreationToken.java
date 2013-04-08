package org.adligo.i.pool.ldap.models;

import java.io.InputStream;

public class LargeFileCreationToken {
	private String fileName;
	private String baseDn;
	private long size;
	private InputStream contentStream;
	private String serverCheckedOn;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getBaseDn() {
		return baseDn;
	}
	public void setBaseDn(String baseDn) {
		this.baseDn = baseDn;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public InputStream getContentStream() {
		return contentStream;
	}
	public void setContentStream(InputStream contentStream) {
		this.contentStream = contentStream;
	}
	public String getServerCheckedOn() {
		return serverCheckedOn;
	}
	public void setServerCheckedOn(String serverCheckedOn) {
		this.serverCheckedOn = serverCheckedOn;
	}
	
}
