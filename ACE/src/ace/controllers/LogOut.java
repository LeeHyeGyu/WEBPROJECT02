package ace.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;

public class LogOut extends AbstractController{

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("redirect:main");
		HttpSession session = request.getSession();
		
		if(session.getAttribute("check_id") == null) {
			session.removeAttribute("m_email");
		}		
		
		session.setAttribute("login", false);
		
		return mav;
	}

}
