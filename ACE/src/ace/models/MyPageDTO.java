package ace.models;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

public class MyPageDTO implements Serializable {
	private String m_email; // 회원 이메일
	private String m_name; // 회원 이름
	private String m_pw; // 회원 비밀번호
	private List<ReservationDTO> reservationDTO; // 예약내역

	public MyPageDTO() {
	
	}

	
	public MyPageDTO(String m_email, String m_name, String m_pw, List<ReservationDTO> reservationDTO) {
		this.m_email = m_email;
		this.m_name = m_name;
		this.m_pw = DigestUtils.sha512Hex(m_pw);
		this.reservationDTO = reservationDTO;
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

	public void setM_pw(String m_pw) {
		this.m_pw = DigestUtils.sha512Hex(m_pw);
	}

	public List<ReservationDTO> getReservationDTO() {
		return reservationDTO;
	}

	public void setReservationDTO(List<ReservationDTO> reservationDTO) {
		this.reservationDTO = reservationDTO;
	}

	@Override
	public String toString() {
		return "MyPageDTO [m_email=" + m_email + ", m_name=" + m_name + ", m_pw=" + m_pw + ", reservationDTO="
				+ reservationDTO + "]";
	}
	
	

}
