package ace.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;
import ace.models.AceDAO;
import ace.models.AceDAOImpl;
import ace.models.MyPageDTO;

public class MyPageUpdate extends AbstractController{
	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String email = (String) session.getAttribute("m_email");
		boolean login = (boolean) session.getAttribute("login");
		System.out.println(email);
		AceDAO aceDAO = AceDAOImpl.getInstance();
		try {
			System.out.println(email);
			System.out.println(login);
			if(!login) {
				throw new Exception();
			}
			MyPageDTO myPageDTO= aceDAO.getMemberInfo(email);
			ModelAndView mav = new ModelAndView("/WEB-INF/views/MyPage/mypageUpdate.jsp");
			System.out.println(myPageDTO);
			mav.addObject("myPageDTO", myPageDTO);
			return mav;
		} catch (Exception e) {
			ModelAndView mav = new ModelAndView("/WEB-INF/views/result.jsp");
			mav.addObject("msg", "로그인을 먼저 해주세요");
			mav.addObject("url", "main");
			return mav;
		}
		
		
		
	}

}
