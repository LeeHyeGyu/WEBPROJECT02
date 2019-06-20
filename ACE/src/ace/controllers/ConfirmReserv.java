package ace.controllers;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;
import ace.models.AceDAO;
import ace.models.AceDAOImpl;
import ace.models.ReservationDTO;

public class ConfirmReserv extends AbstractController{
	private static final Logger logger = LoggerFactory.getLogger(ConfirmReserv.class);

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		
		String reserv_code = request.getParameter("reserv_code");
		logger.info("reserv_code" + reserv_code);
		
		ModelAndView mav 	= new ModelAndView();
		AceDAO 		 aceDAO = AceDAOImpl.getInstance();
		
		try {
			ReservationDTO reservationDTO = aceDAO.getReservationByCode(reserv_code);
			aceDAO.getHotel(reservationDTO.getRcode());
	
			mav.setViewName("/WEB-INF/views/confirmReservation.jsp");
			mav.addObject("ReservationInfo", reservationDTO);
			
			return mav;
		} catch (SQLException e) {
			e.printStackTrace();
			
			mav.setViewName("/WEB-INF/views/result.jsp");
			mav.addObject("msg", "죄송합니다.");
			mav.addObject("url", "javascript:history.back();");
			return mav;
		}
	}

}
