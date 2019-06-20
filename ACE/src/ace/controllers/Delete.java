package ace.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;

public class Delete extends AbstractController{

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		Long no = Long.parseLong(request.getParameter("no"));
		
		ModelAndView mav = new ModelAndView("/WEB-INF/views/delete.jsp","no",no);
	
		return mav;
	}

}
