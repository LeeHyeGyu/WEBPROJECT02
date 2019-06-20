package ace.controllers;

import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ace.cmn.SendMail;
import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;
import ace.models.AceDAO;
import ace.models.AceDAOImpl;
import ace.models.ReservationDTO;

public class ReservationAction extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(RoomListAction.class);
	
	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		Date rdateIn    =  Date.valueOf(request.getParameter("rdateIn"));
		Date rdateOut   =  Date.valueOf(request.getParameter("rdateOut"));
		String rmcode 	=  request.getParameter("rmcode");
		String rcode    =  rdateIn.toString()+rmcode;
		Integer rgnum   =  Integer.valueOf(request.getParameter("rgnum"));
		Integer rpay    =  Integer.valueOf(request.getParameter("rpay"));
		String remail	=  request.getParameter("remail");
		String rname    =  request.getParameter("rname");
		Long rcard      =  Long.valueOf(request.getParameter("rcard")); 
		String hname    =  request.getParameter("hname");
		
		HttpSession session = request.getSession();
		String check = (String) session.getAttribute("check");
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setRcode(rcode);
		reservationDTO.setRdateIn(rdateIn);
		reservationDTO.setRdateOut(rdateOut);
		reservationDTO.setRmcode(rmcode);
		reservationDTO.setRgnum(rgnum);
		reservationDTO.setRpay(rpay);
		reservationDTO.setRemail(remail);
		reservationDTO.setRname(rname);
		reservationDTO.setRcard(rcard);
		reservationDTO.setHname(hname);
		session.setAttribute("check", "0");
		ModelAndView mav = new ModelAndView();
		AceDAO aceDAO = AceDAOImpl.getInstance();
		try {
			aceDAO.reservation(reservationDTO);
		} catch (SQLException e) {
			e.printStackTrace();
			mav.setViewName("/WEB-INF/views/result.jsp");
			mav.addObject("msg", "이미 예약 되었습니다.");
			mav.addObject("url", "javascript:history.back();");
			return mav;
		}

		String msg = "안녕하세요, " + rname + " 고객님. ACE HOTEL 입니다.<br>" +
					 "ACE HOTEL 에서 진행하신 예약에 대한 예약코드와 예약 정보 보내드립니다.<br><br>" +
					 "예약코드 : " + rcode + "<br>" +
					 "예약자명 : " + rname + "<br>" +
					 "호텔명 : " + hname + "<br>" +
					 "인원 수 : " + rgnum + "명<br>" +
					 "체크인 날짜 : " + rdateIn + "<br>" +
					 "체크아웃 날짜 : " + rdateOut + "<br>" +
					 "결제 금액 : " + rpay + "원<br><br>" +
					 "저희 ACE HOTEL 을 이용해주셔서 감사드리며, 안전하고 즐거운 여행되시길 기원합니다.";
		if(check == "1") {
			SendMail.getInstance().sendMail("안녕하세요, ACE HOTEL 에서 예약 정보 보내드립니다.", msg, remail);
		}
		logger.info(reservationDTO.toString());
		mav.setViewName("/WEB-INF/views/check_complete_reservation.jsp");
		mav.addObject("dto", reservationDTO);
		mav.addObject("msg", "예약 성공");						
		mav.addObject("url", "success");
		return mav;
	}
}
