package ace.models;

import java.io.Serializable;

import org.apache.commons.codec.digest.DigestUtils;

public class MemberDTO implements Serializable{
	private String m_email; //회원 이메일
	private String m_name;  //회원 이름
	private String m_pw;    //회원 비밀번호
	private int check;    //회원 비밀번호
	
	public int getCheck() {
		return check;
	}

	public void setCheck(int check) {
		this.check = check;
	}

	public MemberDTO() {}
	
	public MemberDTO(String m_email,String m_pw) {
		this.m_email = m_email;
		this.m_pw 	 = DigestUtils.sha512Hex(m_pw);
	}
	
	public MemberDTO(String m_email, String m_name, String m_pw) {
		this.m_email = m_email;
		this.m_name = m_name;
		this.m_pw = DigestUtils.sha512Hex(m_pw);
	}

	public String getM_email() {
		return m_email;
	}

	public void setM_email(String m_email) {
		this.m_email = m_email;
	}

	public String getM_name() {
		return m_name;
	}

	public void setM_name(String m_name) {
		this.m_name = m_name;
	}

	public String getM_pw() {
		return m_pw;
	}

	@Override
	public String toString() {
		return "MemberDTO [m_email=" + m_email + ", m_name=" + m_name + ", m_pw=" + m_pw + ", check=" + check + "]";
	}

	public void setM_pw(String m_pw) {
		this.m_pw = DigestUtils.sha512Hex(m_pw);
	}
	
}
