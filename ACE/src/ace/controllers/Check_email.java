package ace.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;
import ace.models.AceDAO;
import ace.models.AceDAOImpl;

public class Check_email extends AbstractController {

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {

		String ch_email = request.getParameter("ch_email");	

		ModelAndView mav = new ModelAndView();
		AceDAO aceDAO = AceDAOImpl.getInstance();

		try {
			int result = aceDAO.checkEmail(ch_email);

			JSONObject obj = new JSONObject(); 
		    obj.put("result", result); 
						
			mav.setViewName("/WEB-INF/views/check_email_tmp.jsp");
			mav.addObject("obj", obj); 
			
			return mav;

		} catch (Exception e) {
			e.printStackTrace();
			
			mav.setViewName("/WEB-INF/views/check_email_tmp.jsp");
			mav.addObject("obj", -1);

			return mav;
		}

	}

}
