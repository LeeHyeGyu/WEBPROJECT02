package ace.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;
import ace.models.ArticleDAO;
import ace.models.ArticleDAOImpl;
import ace.models.ArticleDTO;

public class InsertAction extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(InsertAction.class);
	//string이 변조가 심하고 메모리 누수가 심하니까 씀
	
	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		String title 	= request.getParameter("title");
		String name 	= request.getParameter("name");
		String password = request.getParameter("password");
		String content 	= request.getParameter("content");
		
		ArticleDTO articleDTO = new ArticleDTO();
		articleDTO.setTitle(title);
		articleDTO.setName(name);
		articleDTO.setPassword(password);
		articleDTO.setContent(content);
		
		//logger.debug(articleDTO.toString());
		
		ArticleDAO articleDAO = ArticleDAOImpl.getInstance();
		
		try {
			articleDAO.insertArticle(articleDTO);
			ModelAndView mav = new ModelAndView("redirect:list");
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			ModelAndView mav = new ModelAndView("/WEB-INF/views/result.jsp");
			mav.addObject("msg", "입력 실패 :(");
			mav.addObject("url", "javascript:history.back();");
			return mav;
		}
	}

}
