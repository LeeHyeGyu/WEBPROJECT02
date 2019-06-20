package ace.models;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AceDAOImpl implements AceDAO {

	private static final AceDAO aceDAO = new AceDAOImpl();
	private static final Logger logger = LoggerFactory.getLogger(ArticleDAOImpl.class);

	private String url;
	private String driver;
	private String username;
	private String password;

	private AceDAOImpl() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(this.getClass().getResource("").getPath() + "/database.properties"));
			url = prop.getProperty("url").trim();
			driver = prop.getProperty("driver").trim();
			username = prop.getProperty("username").trim();
			password = prop.getProperty("password").trim();

			Class.forName(driver);

		} catch (Exception e) {
			logger.info(e.toString());
		}
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	public void dbClose(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		if (rs != null)
			try {
				rs.close();
			} catch (Exception e) {
			}
		if (pstmt != null)
			try {
				pstmt.close();
			} catch (Exception e) {
			}
		if (conn != null)
			try {
				conn.close();
			} catch (Exception e) {
			}
	}

	public void dbClose(PreparedStatement pstmt, Connection conn) {
		if (pstmt != null)
			try {
				pstmt.close();
			} catch (Exception e) {
			}
		if (conn != null)
			try {
				conn.close();
			} catch (Exception e) {
			}
	}

	public static AceDAO getInstance() {
		return aceDAO;
	}

	@Override
	public int signUp(MemberDTO memberDTO) throws SQLException {
		int result = 0;

		Connection conn = null;
		PreparedStatement pstmt = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" insert into t_member(m_email, m_name, m_pw)");
		sql.append(" values (?,?,?) ");

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, memberDTO.getM_email());
			pstmt.setString(2, memberDTO.getM_name());
			pstmt.setString(3, memberDTO.getM_pw());

			result = pstmt.executeUpdate();
		} finally {
			dbClose(pstmt, conn);
		}

		return result;
	}

	@Override
	 public List<RoomDTO> getRoomList(String hcode, SearchDTO searchDTO, PageDTO pageDTO) throws SQLException {
	      Connection conn = null;
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	   
	      List<RoomDTO> list = new ArrayList<RoomDTO>();
	      StringBuffer sql = new StringBuffer();
	      sql.append("SELECT B.*                                                                                   ");
	      sql.append("FROM(SELECT rownum as rnum, A.*                                                              ");
	      sql.append("     FROM (select *                                                                          ");
	      sql.append("           from rooms a,roomtype b                                                           ");
	      sql.append("           where rmcode in ((SELECT rmcode                                                   ");
	      sql.append("                             from rooms                                                      ");
	      sql.append("                             where hcode = ?)                                                ");
	      sql.append("                             minus                                                           ");
	      sql.append("                             (select rmcode                                                  ");
	      sql.append("                             from   reservation                                              ");
	      sql.append("                             where not(TO_DATE(rdatein,'yyyy/mm/dd') > TO_DATE(?,'yyyy/mm/dd')                   ");
	      sql.append("                                    OR TO_DATE(rdateout,'yyyy/mm/dd') < TO_DATE(?,'yyyy/mm/dd'))))               ");
	      sql.append("           and a.rmtype = b.rtcode                                                           ");
	      sql.append("           and b.rtmaxp >=?                                                                   ");
	      sql.append("           order by TO_NUMBER(rmcode)) A)B                                                   ");
	      sql.append("WHERE  rnum between ? and ?                                                                  ");
	   
	      try {
	         conn = getConnection();
	         pstmt = conn.prepareStatement(sql.toString());
	         
	         pstmt.setString(1, hcode);
	         pstmt.setDate(2, searchDTO.getS_outDate());
	         pstmt.setDate(3, searchDTO.getS_inDate());
	         pstmt.setLong(4, searchDTO.getS_pNum());
	         pstmt.setLong(5, pageDTO.getStartNum());
	         pstmt.setLong(6, pageDTO.getEndNum());
	         
	         rs = pstmt.executeQuery();
	         
	         while (rs.next()) {
	            RoomDTO roomDTO = new RoomDTO();
	            
	            roomDTO.setRmcode(rs.getString("rmcode"));
	            roomDTO.setRmname(rs.getString("rmname"));
	            roomDTO.setRmtype(rs.getString("rmtype"));
	            roomDTO.setRtmaxp(rs.getString("rtmaxp"));
	            roomDTO.setRmpay(rs.getString("rmpay"));
	            
	            list.add(roomDTO);
	            
	            logger.info(roomDTO.toString());
	         }
	         
	      } finally {
	         dbClose(rs, pstmt, conn);
	      }
	      
	      return list;
	   }

	@Override
	public long getTotalCount(SearchDTO searchDTO) throws SQLException {
		StringBuffer sql_totalCount = new StringBuffer();
		sql_totalCount.append("SELECT COUNT(*) AS cnt                                                                                "); 
		sql_totalCount.append(" FROM (select * from hotels                                                                           "); 
		sql_totalCount.append("       where hcode in (select a.hcode                                                                 "); 
		sql_totalCount.append("                       from rooms a,roomtype b                                                        "); 
		sql_totalCount.append("                       where rmcode in ((select rmcode                                                "); 
		sql_totalCount.append("                                         from   rooms)                                                "); 
		sql_totalCount.append("                                         minus                                                        "); 
		sql_totalCount.append("                                        (select rmcode                                                "); 
		sql_totalCount.append("                                         from   reservation                                           "); 
		sql_totalCount.append("                                         where not(TO_DATE(rdatein,'yyyy/mm/dd') > TO_DATE(?,'yyyy/mm/dd')      "); 
		sql_totalCount.append("                                                OR TO_DATE(rdateout,'yyyy/mm/dd') < TO_DATE(?,'yyyy/mm/dd'))))  "); 
		sql_totalCount.append("                       and a.rmtype = b.rtcode                                                        "); 
		sql_totalCount.append("                       and b.rtmaxp >=?                                                                "); 
		sql_totalCount.append("                       group by hcode)                                                                "); 
		sql_totalCount.append("         and (haddr LIKE '%'||?||'%' OR hname LIKE '%'||?||'%'))                                      "); 
		
	

		long result = 0; // return할 변수

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			pstmt = conn.prepareStatement(sql_totalCount.toString());
			pstmt.setDate(1, searchDTO.getS_outDate());
			pstmt.setDate(2, searchDTO.getS_inDate());
			pstmt.setLong(3, searchDTO.getS_pNum());
			pstmt.setString(4, searchDTO.getS_loc());
			pstmt.setString(5, searchDTO.getS_loc());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getLong("cnt"); // 전체 레코드수
			}
		} finally {
			dbClose(rs, pstmt, conn);

		}
		return result;
	}

	@Override
	public List<HotelDTO> getResultList(SearchDTO searchDTO, PageDTO pageDTO) throws SQLException {
		List<HotelDTO> list = new ArrayList<>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.*                                                                                                 ");
		sql.append("FROM(SELECT rownum as rnum, A.*                                                                            ");
		sql.append("     FROM (select * from hotels                                                                            ");
		sql.append("           where hcode in (select a.hcode                                                                  ");
		sql.append("                           from rooms a,roomtype b                                                         ");
		sql.append("                           where rmcode in ((select rmcode                                                 ");
		sql.append("                                             from   rooms)                                                 ");
		sql.append("                                             minus                                                         ");
		sql.append("                                            (select rmcode                                                 ");
		sql.append("                                             from   reservation                                            ");
		sql.append("                                             where not(TO_DATE(rdatein,'yyyy/mm/dd') > TO_DATE(?,'yyyy/mm/dd')                  ");
		sql.append("                                                    OR TO_DATE(rdateout,'yyyy/mm/dd') < TO_DATE(?,'yyyy/mm/dd'))))              ");
		sql.append("                           and a.rmtype = b.rtcode                                                         ");
		sql.append("                           and b.rtmaxp >= ?                                                                ");
		sql.append("                           group by hcode)                                                                 ");
		sql.append("             and (haddr LIKE '%'||?||'%' OR hname LIKE '%'||?||'%')) A)B                                   ");
		sql.append("WHERE  rnum between ? and ?                                                                                ");
		try {
			conn = getConnection();

			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setDate(1, searchDTO.getS_outDate());
			pstmt.setDate(2, searchDTO.getS_inDate());
			pstmt.setLong(3, searchDTO.getS_pNum());
			pstmt.setString(4, searchDTO.getS_loc());
			pstmt.setString(5, searchDTO.getS_loc());
			pstmt.setLong(6, pageDTO.getStartNum());
			pstmt.setLong(7, pageDTO.getEndNum());
			rs = pstmt.executeQuery();

			// rs 안에 다음 레코드가 존재하면 계속해서 반복함
			while (rs.next()) {
				HotelDTO hotelDTO = new HotelDTO();

				hotelDTO.setH_code(rs.getString("hcode"));
				hotelDTO.setH_name(rs.getString("hname"));
				hotelDTO.setH_addr(rs.getString("haddr"));
				hotelDTO.setH_num(rs.getString("hnum"));
				hotelDTO.setH_rooms(rs.getInt("hrooms"));

				list.add(hotelDTO);
				logger.info(hotelDTO.toString());
			}

		} finally {
			dbClose(rs, pstmt, conn);
		}

		return list;
	}


	@Override
	public MemberDTO checkLogin(MemberDTO memberDTO) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" select count(m_name) over() as chlogin,m_name from t_member ");
		sql.append(" where  m_email=? and m_pw=? ");

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, memberDTO.getM_email());
			pstmt.setString(2, memberDTO.getM_pw());

			rs = pstmt.executeQuery();

			if (rs.next()) {
				memberDTO.setCheck(rs.getInt("chlogin"));
				memberDTO.setM_name(rs.getString("m_name"));
			}
		} finally {
			dbClose(pstmt, conn);
		}

		return memberDTO;
	}

	@Override
	public List<ReservationDTO> getReservation(String email) throws SQLException {
		List<ReservationDTO> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM reservation a, rooms b ,hotels c");
		sql.append(" where remail = ? and a.rmcode = b.rmcode and b.hcode = c.hcode");
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, email);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReservationDTO reservationDTO = new ReservationDTO();
				reservationDTO.setRcode(rs.getString("rcode"));
				reservationDTO.setRdateIn(rs.getDate("rdatein"));
				reservationDTO.setRdateOut(rs.getDate("rdateout"));
				reservationDTO.setRmcode(rs.getString("rmcode"));
				reservationDTO.setRgnum(rs.getInt("rgnum"));
				reservationDTO.setRpay(rs.getInt("rpay"));
				reservationDTO.setRemail(rs.getString("remail"));
				reservationDTO.setRname(rs.getString("rname"));
				reservationDTO.setRmname(rs.getString("rmname"));
				reservationDTO.setHname(rs.getString("hname"));
				reservationDTO.setHaddr(rs.getString("haddr"));
				reservationDTO.setHnum(rs.getString("hnum"));
				list.add(reservationDTO);
			}
		} finally {
			dbClose(pstmt, conn);
		}
		System.out.println(list);
		return list;
	}

	@Override
	public MyPageDTO getMemberInfo(String email) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		MyPageDTO myPageDTO = new MyPageDTO();

		List<ReservationDTO> reservationDTO = getReservation(email);
		System.out.println(reservationDTO.toString());
		StringBuffer sql = new StringBuffer();
		sql.append("select * from t_member ");
		sql.append("where m_email = ?");

		int result = 0;

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, email);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				myPageDTO.setM_email(rs.getString("m_email"));
				myPageDTO.setM_name(rs.getString("m_name"));
				myPageDTO.setM_pw(rs.getString("m_pw"));
				myPageDTO.setReservationDTO(reservationDTO);
			}
		} finally {
			dbClose(pstmt, conn);
		}

		return myPageDTO;
	}
	@Override
	public void reservation(ReservationDTO reservationDTO) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" insert into reservation(rcode, rdatein, rdateout, rmcode, rgnum, rpay, remail, rname, rcard)");
		sql.append(" values (?,?,?,?,?,?,?,?,?) ");

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, reservationDTO.getRcode());
			pstmt.setDate(2, reservationDTO.getRdateIn());
			pstmt.setDate(3, reservationDTO.getRdateOut());
			pstmt.setString(4, reservationDTO.getRmcode());
			pstmt.setInt(5, reservationDTO.getRgnum());
			pstmt.setInt(6, reservationDTO.getRpay());
			pstmt.setString(7, reservationDTO.getRemail());
			pstmt.setString(8, reservationDTO.getRname());
			pstmt.setLong(9, reservationDTO.getRcard());

			pstmt.executeUpdate();
		} finally {
			dbClose(pstmt, conn);
		}
		
	}

	@Override
	public int checkEmail(String ch_email) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) as cnt from t_member ");
		sql.append(" where  m_email=? ");

		int result = -1;

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, ch_email);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt("cnt");
			}
		} finally {
			dbClose(rs, pstmt, conn);
		}

		return result;
	}

	@Override
	public void updateMemberInfo(MyPageDTO myPageDTO) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" update t_member set m_name = ?, m_pw = ? where m_email = ?");

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, myPageDTO.getM_name());
			pstmt.setString(2, myPageDTO.getM_pw());
			pstmt.setString(3, myPageDTO.getM_email());
			pstmt.executeUpdate();
		} finally {
			dbClose(pstmt, conn);
		}
	}

	@Override
	public HotelDTO getHotel(String hcode) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" select * from Hotels ");
		sql.append(" where hcode= ? ");
		HotelDTO hotelDTO = new HotelDTO();

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, hcode);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				hotelDTO.setH_addr(rs.getString("haddr"));
				hotelDTO.setH_name(rs.getString("hname"));
				hotelDTO.setH_num(rs.getString("hnum"));
				hotelDTO.setH_rooms(rs.getInt("hrooms"));
			}
		} finally {
			dbClose(rs, pstmt, conn);
		}

		return hotelDTO;
	}
	
	@Override
	public ReservationDTO getReservationByCode(String reserv_code) throws SQLException {
		ReservationDTO reservationDTO = new ReservationDTO();		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT a.rcode,");
		sql.append("        a.rdatein,");
		sql.append("        a.rdateout,");
		sql.append("        a.rmcode,");
		sql.append("        a.rgnum,");
		sql.append("        a.rpay,");
		sql.append("        a.remail,");
		sql.append("        a.rname,");
		sql.append("        a.rcard,");
		sql.append("        b.rmname,");
		sql.append("        c.hname,");
		sql.append("        c.haddr");
		sql.append(" FROM   reservation a,");
		sql.append("        rooms b,");
		sql.append("        hotels c");
		sql.append(" WHERE  a.rcode = ? ");
		sql.append("        AND a.rmcode = b.rmcode ");
		sql.append("        AND b.hcode = c.hcode ");
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, reserv_code);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				reservationDTO.setRcode(rs.getString("rcode"));
				reservationDTO.setRdateIn(rs.getDate("rdatein"));
				reservationDTO.setRdateOut(rs.getDate("rdateout"));
				reservationDTO.setRmcode(rs.getString("rmcode"));
				reservationDTO.setRgnum(rs.getInt("rgnum"));
				reservationDTO.setRpay(rs.getInt("rpay"));
				reservationDTO.setRemail(rs.getString("remail"));
				reservationDTO.setRname(rs.getString("rname"));
				reservationDTO.setRcard(rs.getLong("rcard"));
				reservationDTO.setHname(rs.getString("hname"));
				reservationDTO.setRmname(rs.getString("rmname"));
				reservationDTO.setHaddr(rs.getString("haddr"));			
			}
		} finally {
			dbClose(rs, pstmt, conn);
		}
		logger.info(reservationDTO.toString());
		return reservationDTO;
	}

	@Override
	public int deleteReservation(String rcode) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		int result = -1;
		
		StringBuffer sql = new StringBuffer();
		sql.append("delete from reservation where rcode=?");
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, rcode);			
			result = pstmt.executeUpdate();
		} finally {
			dbClose(pstmt, conn);
		}
		
		return result;
	}
	
	@Override
	public long getRooms(String hcode, SearchDTO searchDTO) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		long result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as cnt                                              ");
		sql.append("from rooms a,roomtype b                                             ");
		sql.append("where rmcode in ((SELECT rmcode                                     ");
		sql.append("				 from rooms                                         ");
		sql.append("				 where hcode = ?)                                   ");
		sql.append("				 minus                                              ");
		sql.append("				 (select rmcode                                     ");
		sql.append("				 from   reservation                                 ");
		sql.append("				 where not(TO_DATE(rdatein,'yyyy/mm/dd') > TO_DATE(?,'yyyy/mm/dd')       ");
		sql.append("						OR TO_DATE(rdateout,'yyyy/mm/dd') < TO_DATE(?,'yyyy/mm/dd'))))   ");
		sql.append("and a.rmtype = b.rtcode                                             ");
		sql.append("and b.rtmaxp >=?                                                     ");
		sql.append("order by TO_NUMBER(rmcode)                                          ");

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, hcode);
			pstmt.setDate(2, searchDTO.getS_outDate());
			pstmt.setDate(3, searchDTO.getS_inDate());
			pstmt.setLong(4, searchDTO.getS_pNum());

			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getLong("cnt");
			}
		} finally {
			dbClose(rs, pstmt, conn);
		}

		return result;
	}
	
	@Override
	public int checkReservExist(String reserv_code) throws SQLException {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) as rcodenum");
		sql.append(" from reservation ");
		sql.append(" where rcode = ? ");

		int result = -1;

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, reserv_code);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt("rcodenum");
			}
		} finally {
			dbClose(rs, pstmt, conn);
		}		
		return result;
	}
	
	@Override
	public int cancelMember(MemberDTO memberDTO) throws SQLException {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		int result = -1;
		
		StringBuffer sql = new StringBuffer();
		sql.append("delete from t_member where m_email=? and m_pw=?");
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, memberDTO.getM_email());			
			pstmt.setString(2, memberDTO.getM_pw());			
			result = pstmt.executeUpdate();
		} finally {
			dbClose(pstmt, conn);
		}
		
		return result;
	}

}
