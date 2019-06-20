package ace.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;
import ace.models.AceDAO;
import ace.models.AceDAOImpl;
import ace.models.MemberDTO;

public class LoginAction extends AbstractController{

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("로그인 액션 옴");
		String m_email = request.getParameter("m_email");
		String m_pw    = request.getParameter("m_pw");
		String check_id = request.getParameter("check_id"); 
		Boolean login = false;
		
		System.out.println("check 안했을때! : " + check_id);
		
		ModelAndView mav = new ModelAndView();
		MemberDTO memberDTO = new MemberDTO(m_email, m_pw);
		AceDAO aceDAO = AceDAOImpl.getInstance();

		try {						
			MemberDTO result = aceDAO.checkLogin(memberDTO);
			if(result.getCheck() != 1) {
				throw new RuntimeException("이메일과 비밀번호를 확인해주세요.");
			}
			HttpSession session = request.getSession();
			session.setAttribute("check_id", check_id);
			session.setAttribute("m_email", m_email);	
			session.setAttribute("m_name", result.getM_name());				
			
			login = true;
			session.setAttribute("login", login);
			System.out.println(m_email + ":" + result.getM_name() + " : " + login);

			mav.setViewName("/WEB-INF/views/result.jsp");
			mav.addObject("msg", m_email + "님 환영합니다 :)");						
			mav.addObject("url", "main");	

			return mav;		
		}catch (Exception e) {
			e.printStackTrace();

			mav.setViewName("/WEB-INF/views/result.jsp");
			mav.addObject("msg", "아이디 또는 비밀번호를 확인해주세요");						
			mav.addObject("url", "main");	
			return mav;
		}
		
	}

}
