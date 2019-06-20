package ace.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;

public class Login extends AbstractController {

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("/WEB-INF/views/login/login.jsp");
		return mav;
	}

}
