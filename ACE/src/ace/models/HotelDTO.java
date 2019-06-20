package ace.models;

import java.io.Serializable;

public class HotelDTO implements Serializable{
	private String h_code;
	private String h_name;
	private String h_type;
	private String h_addr;
	private String h_num;
	private int	   h_rooms;
	private int    h_grade;
	
	public HotelDTO() {}

	public HotelDTO(String h_name, String h_type, String h_addr, String h_num, int h_rooms) {
		this.h_name = h_name;
		this.h_type = h_type;
		this.h_addr = h_addr;
		this.h_num = h_num;
		this.h_rooms = h_rooms;
	}

	public String getH_code() {
		return h_code;
	}

	public void setH_code(String h_code) {
		this.h_code = h_code;
	}

	public String getH_name() {
		return h_name;
	}

	public void setH_name(String h_name) {
		this.h_name = h_name;
	}

	public String getH_type() {
		return h_type;
	}

	public void setH_type(String h_type) {
		this.h_type = h_type;
	}

	public String getH_addr() {
		return h_addr;
	}

	public void setH_addr(String h_addr) {
		this.h_addr = h_addr;
	}

	public String getH_num() {
		return h_num;
	}

	public void setH_num(String h_num) {
		this.h_num = h_num;
	}

	public int getH_rooms() {
		return h_rooms;
	}

	public void setH_rooms(int h_rooms) {
		this.h_rooms = h_rooms;
	}

	public int getH_grade() {
		return h_grade;
	}

	public void setH_grade(int h_grade) {
		this.h_grade = h_grade;
	}

	@Override
	public String toString() {
		return "HotelDTO [h_code=" + h_code + ", h_name=" + h_name + ", h_type=" + h_type + ", h_addr=" + h_addr
				+ ", h_num=" + h_num + ", h_rooms=" + h_rooms + ", h_grade=" + h_grade + "]";
	}
	
}
