package ace.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;

public class Main extends AbstractController{
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		
		logger.info("여기얏@!");

		
		if(session.getAttribute("login") == null) {
			session.setAttribute("login", false);
		}
		
		ModelAndView mav = new ModelAndView("/WEB-INF/views/main.jsp");
		return mav;
	}

}
