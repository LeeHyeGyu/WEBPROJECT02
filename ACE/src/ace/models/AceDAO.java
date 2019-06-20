package ace.models;

import java.sql.SQLException;
import java.util.List;

public interface AceDAO {

	int signUp(MemberDTO memberDTO) throws SQLException;

	MemberDTO checkLogin(MemberDTO memberDTO) throws SQLException;

	int checkEmail(String ch_email) throws SQLException;

	long getTotalCount(SearchDTO searchDTO) throws SQLException;

	MyPageDTO getMemberInfo(String email) throws SQLException;

	void updateMemberInfo(MyPageDTO myPageDTO) throws SQLException;

	List<HotelDTO> getResultList(SearchDTO searchDTO, PageDTO pageDTO) throws SQLException;

	List<RoomDTO> getRoomList(String hcode, SearchDTO searchDTO, PageDTO pageDTO) throws SQLException;

	List<ReservationDTO> getReservation(String email) throws SQLException;

	void reservation(ReservationDTO reservationDTO) throws SQLException;

	HotelDTO getHotel(String hcode) throws SQLException;

	ReservationDTO getReservationByCode(String reserv_code) throws SQLException;

	int deleteReservation(String rcode) throws SQLException;

	long getRooms(String hcode, SearchDTO searchDTO) throws SQLException;

	int checkReservExist(String reserv_code) throws SQLException;

	int cancelMember(MemberDTO memberDTO) throws SQLException;

}