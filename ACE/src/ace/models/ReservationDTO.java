package ace.models;

import java.io.Serializable;
import java.sql.Date;

public class ReservationDTO implements Serializable {
	private String rcode;
	private Date rdateIn;
	private Date rdateOut;
	private String rmcode;
	private int rgnum;
	private int rpay;
	private String remail;
	private String rname;
	private long rcard;
	private String hname;
	private String rmname;
	private String haddr;
	private String hnum;

	public ReservationDTO() {
	}

	public String getHnum() {
		return hnum;
	}

	public void setHnum(String hnum) {
		this.hnum = hnum;
	}

	public String getRcode() {
		return rcode;
	}

	public void setRcode(String rcode) {
		this.rcode = rcode;
	}

	public Date getRdateIn() {
		return rdateIn;
	}

	public void setRdateIn(Date rdateIn) {
		this.rdateIn = rdateIn;
	}

	public Date getRdateOut() {
		return rdateOut;
	}

	public void setRdateOut(Date rdateOut) {
		this.rdateOut = rdateOut;
	}

	public String getRmcode() {
		return rmcode;
	}

	public void setRmcode(String rmcode) {
		this.rmcode = rmcode;
	}

	public int getRgnum() {
		return rgnum;
	}

	public void setRgnum(int rgnum) {
		this.rgnum = rgnum;
	}

	public int getRpay() {
		return rpay;
	}

	public void setRpay(int rpay) {
		this.rpay = rpay;
	}

	public String getRemail() {
		return remail;
	}

	public void setRemail(String remail) {
		this.remail = remail;
	}

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public long getRcard() {
		return rcard;
	}

	public void setRcard(long rcard) {
		this.rcard = rcard;
	}

	public String getHname() {
		return hname;
	}

	public void setHname(String hname) {
		this.hname = hname;
	}

	public ReservationDTO(String rcode, Date rdateIn, Date rdateOut, String rmcode, int rgnum, int rpay, String remail,
			String rname, long rcard, String hname, String hnum) {
		super();
		this.rcode = rcode;
		this.rdateIn = rdateIn;
		this.rdateOut = rdateOut;
		this.rmcode = rmcode;
		this.rgnum = rgnum;
		this.rpay = rpay;
		this.remail = remail;
		this.rname = rname;
		this.rcard = rcard;
		this.hname = hname;
		this.hnum = hnum;
	}

	public String getRmname() {
		return rmname;
	}

	public void setRmname(String rmname) {
		this.rmname = rmname;
	}

	public String getHaddr() {
		return haddr;
	}

	public void setHaddr(String haddr) {
		this.haddr = haddr;
	}

	@Override
	public String toString() {
		return "ReservationDTO [rcode=" + rcode + ", rdateIn=" + rdateIn + ", rdateOut=" + rdateOut + ", rmcode="
				+ rmcode + ", rgnum=" + rgnum + ", rpay=" + rpay + ", remail=" + remail + ", rname=" + rname
				+ ", rcard=" + rcard + ", hname=" + hname + ", rmname=" + rmname + ", haddr=" + haddr + ", hnum=" + hnum
				+ "]";
	}


}
