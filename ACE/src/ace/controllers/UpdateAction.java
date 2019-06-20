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

public class UpdateAction extends AbstractController{
	private static final Logger logger = LoggerFactory.getLogger(UpdateAction.class);

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		long   no 		= Long.parseLong(request.getParameter("no"));
		String title 	= request.getParameter("title");
		String name 	= request.getParameter("name");
		String password = request.getParameter("password");
		String content 	= request.getParameter("content");
		
		ArticleDTO articleDTO = new ArticleDTO();
		articleDTO.setNo(no);
		articleDTO.setTitle(title);
		articleDTO.setName(name);
		articleDTO.setPassword(password);
		articleDTO.setContent(content);
		
		logger.debug(articleDTO.toString());
		
		try {
			ArticleDAO articleDAO = ArticleDAOImpl.getInstance();

			if(articleDAO.updateArticle(articleDTO) != 1) {
				throw new RuntimeException("비밀번호를 확인해주세요.");  
			}
			
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/WEB-INF/views/result.jsp");
			mav.addObject("msg", articleDTO.getNo() + "번 게시물이 수정되었습니다.");
			mav.addObject("url", "detail?no="+articleDTO.getNo());
			
			return mav;
		} catch (Exception e) {				
			ModelAndView mav = new ModelAndView("/WEB-INF/views/result.jsp");
			mav.addObject("msg", e.getMessage().replace("\n", "\\n"));
			// 코드값으로 개행(/)이가 들어가는데 js에서는 이것을 역슬러시 n으로 들어간다???
			mav.addObject("url", "javascript:history.back();");
			
			return mav;
		}
	}

}