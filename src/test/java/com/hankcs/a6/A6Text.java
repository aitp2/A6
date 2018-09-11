package com.hankcs.a6;

import java.util.ArrayList;
import java.util.List;

public class A6Text {

	public String nickName;
	
	public String text;
	
	public List<String> tag = new ArrayList<String>();

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<String> getTag() {
		return tag;
	}

	public void setTag(List<String> tag) {
		this.tag = tag;
	}
}
