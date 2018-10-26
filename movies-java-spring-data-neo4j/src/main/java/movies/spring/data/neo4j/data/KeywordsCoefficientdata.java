package movies.spring.data.neo4j.data;

import java.util.List;

public class KeywordsCoefficientdata {

	private TagData tagData;
	
	private List<KeywordsEmployeedata> keywordsEmployeedatas;
	
	private int publishCount;
	
	private int attendCount;
	
	private int commentCount;

	private int checkinCount;
	
	private int[] publishArray;
	
	private int[] attendArray;
	
	private int[] commentArray;

	private int[] checkinArray;

	public TagData getTagData() {
		return tagData;
	}

	public void setTagData(TagData tagData) {
		this.tagData = tagData;
	}

	public List<KeywordsEmployeedata> getKeywordsEmployeedatas() {
		return keywordsEmployeedatas;
	}

	public void setKeywordsEmployeedatas(List<KeywordsEmployeedata> keywordsEmployeedatas) {
		this.keywordsEmployeedatas = keywordsEmployeedatas;
	}

	public int getPublishCount() {
		return publishCount;
	}

	public void setPublishCount(int publishCount) {
		this.publishCount = publishCount;
	}

	public int getAttendCount() {
		return attendCount;
	}

	public void setAttendCount(int attendCount) {
		this.attendCount = attendCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getCheckinCount() {
		return checkinCount;
	}

	public void setCheckinCount(int checkinCount) {
		this.checkinCount = checkinCount;
	}

	public int[] getPublishArray() {
		return publishArray;
	}

	public void setPublishArray(int[] publishArray) {
		this.publishArray = publishArray;
	}

	public int[] getAttendArray() {
		return attendArray;
	}

	public void setAttendArray(int[] attendArray) {
		this.attendArray = attendArray;
	}

	public int[] getCommentArray() {
		return commentArray;
	}

	public void setCommentArray(int[] commentArray) {
		this.commentArray = commentArray;
	}

	public int[] getCheckinArray() {
		return checkinArray;
	}

	public void setCheckinArray(int[] checkinArray) {
		this.checkinArray = checkinArray;
	}


}
