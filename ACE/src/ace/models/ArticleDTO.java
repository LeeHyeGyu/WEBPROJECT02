package ace.models;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;

public class ArticleDTO implements Serializable {
	private long no;
	private String title;
	private String name;
	private String password;
	private int readcount;
	private Date regdate;
	private String content;
	
	
	public ArticleDTO() {}

	public ArticleDTO(long no, String title, String name, String password, int readcount, Date regdate,
			String content) {
		this.no = no;
		this.title = title;
		this.name = name;
		this.password = password;
		this.readcount = readcount;
		this.regdate = regdate;
		this.content = content;
	}
	
	public long getNo() {
		return no;
	}
	public void setNo(long no) {
		this.no = no;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = DigestUtils.sha512Hex(password);
	}
	public int getReadcount() {
		return readcount;
	}
	public void setReadcount(int readcount) {
		this.readcount = readcount;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "ArticleDTO [no=" + no + ", title=" + title + ", name=" + name + ", password=" + password
				+ ", readcount=" + readcount + ", regdate=" + regdate + ", content=" + content + "]";
	}
	
	
}
