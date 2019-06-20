package ace.controllers;

import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;
import ace.models.AceDAO;
import ace.models.AceDAOImpl;
import ace.models.MyPageDTO;

public class MyPageAction extends AbstractController{
	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		String name 			= request.getParameter("name");
		String email            = request.getParameter("email");
		String password         = request.getParameter("password");
		String passwordConfirm  = request.getParameter("passwordConfirm");
		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements()) {
			System.out.println(names.nextElement());
		}
		System.out.println(name);
		System.out.println(email);
		System.out.println(password);
		System.out.println(passwordConfirm);
		
		MyPageDTO myPageDTO = new MyPageDTO();
		myPageDTO.setM_email(email);
		myPageDTO.setM_name(name);
		myPageDTO.setM_pw(password);
		AceDAO aceDAO = AceDAOImpl.getInstance();
		
		try {
			aceDAO.updateMemberInfo(myPageDTO);
			ModelAndView mav = new ModelAndView("redirect:updateInfo");
			return mav;
		} catch (SQLException e) {
			ModelAndView mav = new ModelAndView("/WEB-INF/views/result.jsp");
			mav.addObject("msg", "오류");
			mav.addObject("url", "javascript:history.back();");
			return mav;
		}
		
	}

}
