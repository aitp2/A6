package com.hankcs.baidu;

import java.math.BigDecimal;

public class TagItem {

	private BigDecimal score;
	
	private String tag;

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
