package com.hankcs.baidu;

import java.util.List;

public class BaiduTag {

	private String log_id;
	
	private List<TagItem> items;

	public String getLog_id() {
		return log_id;
	}

	public void setLog_id(String log_id) {
		this.log_id = log_id;
	}

	public List<TagItem> getItems() {
		return items;
	}

	public void setItems(List<TagItem> items) {
		this.items = items;
	}
}
