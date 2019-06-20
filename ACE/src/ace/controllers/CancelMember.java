package ace.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;
import ace.models.AceDAO;
import ace.models.AceDAOImpl;
import ace.models.MemberDTO;

public class CancelMember extends AbstractController{

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		String email = request.getParameter("cancelEmail");
		String cancelPW = request.getParameter("cancelPW");
		System.out.println("여기기기기 " + email  + " : " + cancelPW);

		AceDAO aceDAO = AceDAOImpl.getInstance();
		MemberDTO memberDTO = new MemberDTO(email, cancelPW);
		ModelAndView mav = new ModelAndView();
		
		try {
			int result = aceDAO.cancelMember(memberDTO);
			
			if(result != 1) {
				throw new RuntimeException();
			}
			HttpSession session = request.getSession();
			session.invalidate();
			
			mav.setViewName("/WEB-INF/views/result.jsp");
			mav.addObject("msg", email + "님 탈퇴되셨습니다.");
			mav.addObject("url", "main");
			return mav;

		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("/WEB-INF/views/result.jsp");
			mav.addObject("msg", "죄송합니다. 고객센터에 문의하여 주시기 바랍니다 010-****-****");
			mav.addObject("url", "javascript:history.back();");
			return mav;

		}
		
		
	}

}
