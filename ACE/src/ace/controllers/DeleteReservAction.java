package ace.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;
import ace.models.AceDAO;
import ace.models.AceDAOImpl;

public class DeleteReservAction extends AbstractController{

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		String rcode = request.getParameter("deleteRcode");
		HttpSession session = request.getSession();
		boolean login = (boolean)session.getAttribute("login");
		AceDAO aceDAO = AceDAOImpl.getInstance();		
		try {
			int result = aceDAO.deleteReservation(rcode);
			if(result != 1) {
				new RuntimeException("불편을 드려 죄송합니다. 시스템에 문제가 있어 예약 취소가 실패했습니다.");
			}
			
			ModelAndView mav = new ModelAndView("/WEB-INF/views/result.jsp", "rcode", rcode);
			mav.addObject("msg", rcode+"번 예약이 삭제되었습니다.");
			if(login) {
				mav.addObject("url", "confirmReser");
			}else {
				mav.addObject("url", "main");	
			}
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			ModelAndView mav = new ModelAndView("/WEB-INF/views/result.jsp", "rcode", rcode);
			mav.addObject("msg", rcode+"번 예약 삭제에 실패했습니다. 다시 한번 시도해주세요");
			mav.addObject("url", "main");	
			
			return mav;
		}

	}

}
