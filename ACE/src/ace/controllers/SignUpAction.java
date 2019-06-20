package ace.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;
import ace.models.AceDAO;
import ace.models.AceDAOImpl;
import ace.models.MemberDTO;

public class SignUpAction extends AbstractController {

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		String m_name 	= request.getParameter("m_name");
		String m_email 	= request.getParameter("m_email");
		String m_pw 	= request.getParameter("m_pw");
		
		HttpSession session = request.getSession();
		session.setAttribute("login", true);		
		session.setAttribute("m_email", m_email);
		
		MemberDTO memberDTO = new MemberDTO(m_email,m_name,m_pw);
		AceDAO aceDAO = AceDAOImpl.getInstance();
		try {
			ModelAndView mav = new ModelAndView();
			int result = aceDAO.signUp(memberDTO);			
			if(result != 1) {
				throw new RuntimeException();
			}
			mav.setViewName("/WEB-INF/views/main.jsp");
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			ModelAndView mav = new ModelAndView("/WEB-INF/views/result.jsp");
			mav.addObject("msg", e.getMessage().replace("\n", "\\n"));
			// 코드값으로 개행(/)이가 들어가는데 js에서는 이것을 역슬러시 n으로 들어간다???
			mav.addObject("url", "javascript:history.back();");
			return mav;
		}
	}
}
