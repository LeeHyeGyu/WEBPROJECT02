package ace.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;
import ace.models.ArticleDAO;
import ace.models.ArticleDAOImpl;
import ace.models.ArticleDTO;

public class Detail extends AbstractController{

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		long no = Long.parseLong(request.getParameter("no"));
		
		try {			
			ArticleDAO articleDAO = ArticleDAOImpl.getInstance();
			ArticleDTO articleDTO = articleDAO.getArticle(no);
			
			if(articleDTO == null) {throw new RuntimeException();}
			
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/WEB-INF/views/detail.jsp");
			mav.addObject("articleDTO", articleDTO);
			return mav;
		} catch (Exception e) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/WEB-INF/views/result.jsp");
			mav.addObject("msg", no + "번 게시물이 존재하지 않거나 DB연결 오류입니다.");
			mav.addObject("url", "list");
			return mav;
		}
	}

}
