package ace.models;

import java.io.Serializable;

public class PageDTO implements Serializable{
	
	private long startNum;
	private long endNum;
	
	public PageDTO() {}
	
	public PageDTO(long startNum, long endNum) {
		this.startNum = startNum;
		this.endNum = endNum;
	}
	
	public long getStartNum() {
		return startNum;
	}
	public void setStartNum(long startNum) {
		this.startNum = startNum;
	}
	public long getEndNum() {
		return endNum;
	}
	public void setEndNum(long endNum) {
		this.endNum = endNum;
	}
	
	@Override
	public String toString() {
		return "PageDTO [startNum=" + startNum + ", endNum=" + endNum + "]";
	}	
	
}
