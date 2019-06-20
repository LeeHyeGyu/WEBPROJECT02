package ace.controllers;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;
import ace.models.AceDAO;
import ace.models.AceDAOImpl;

public class CheckReservExist extends AbstractController {

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		String reserv_code = request.getParameter("reserv_code");		
		System.out.println("reserv_code : " + reserv_code);
		
		AceDAO aceDAO = AceDAOImpl.getInstance();
		ModelAndView mav = new ModelAndView();
		
		try {
			int result = aceDAO.checkReservExist(reserv_code);
			
			JSONObject obj = new JSONObject(); 
		    obj.put("result", result); 
									
			mav.setViewName("/WEB-INF/views/checkReservExist.jsp");
			mav.addObject("obj", obj);
			return mav;				
		} catch (SQLException e) {
			e.printStackTrace();
			mav.setViewName("/WEB-INF/views/result.jsp");
			mav.addObject("msg", "예약 확인 도중 시스템 오류가 발생했습니다. 고객센터를 통해 문의해주시기 바랍니다.");
			mav.addObject("url", "main");
			return mav;
		}
	}
	
}
