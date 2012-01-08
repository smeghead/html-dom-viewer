package com.starbug1.android.htmldomviewer;

import java.io.Serializable;

public class Element implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id_ = 0;
	private String content_ = "";
	
	public Element(long id, String content) {
		id_ = id;
		content_ = content;
	}
	public long getId() {
		return id_;
	}
	public void setId_(long id_) {
		this.id_ = id_;
	}
	public String getContent() {
		return content_;
	}
	public void setContent(String content) {
		this.content_ = content;
	}
	
}
