package ace.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;
import ace.models.ArticleDAO;
import ace.models.ArticleDAOImpl;
import ace.models.ArticleDTO;
import ace.models.PageDTO;

public class List extends AbstractController {

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		long pg = 1;
		int pageSize = 5;		// 페이지 당 출력할 게시물 수
		int blockSize= 10;		// 하단의 출력 페이지 개수
		
		try {		
			pg = Long.parseLong(request.getParameter("pg"));
		} catch(NumberFormatException e) {}			
		long startNum = (pg - 1) * pageSize + 1;	// 페이지의 게시물 rownum 시작번호
		long endNum = pg * pageSize;				// 페이지의 게시물 rownum 끝번호
		long totalCount = 0;						// 전체 게시물 수	
		long totalPage = 0;							// 전체 페이지 수
		long startBlock = 0;						// 페이지 블럭의 시작
		long endBlock = 0;							// 페이지 블럭의 끝	
		try {
			PageDTO pageDTO = new PageDTO(startNum, endNum);
			
			ArticleDAO articleDAO = ArticleDAOImpl.getInstance();
			
			totalCount = articleDAO.getTotalCount();					// 전체 게시물 수	
			totalPage = totalCount / pageSize;							// 전체 페이지 수
			if (totalCount % pageSize != 0) totalPage++;
			startBlock = (pg - 1) / blockSize * blockSize + 1;			// 페이지 블럭의 시작번호
			endBlock   = (pg - 1) / blockSize * blockSize + blockSize;	// 페이지 블럭의 끝번호
			if (endBlock > totalPage) endBlock = totalPage;
			
			java.util.List<ArticleDTO> list = articleDAO.getArticleList(pageDTO);
			// 성공했을 때
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/WEB-INF/views/result_search.jsp");
			mav.addObject("list", list);
			mav.addObject("pg", pg);
			mav.addObject("totalPage", totalPage);
			mav.addObject("totalCount", totalCount);
			mav.addObject("startBlock", startBlock);
			mav.addObject("endBlock", endBlock);	
			return mav;
		} catch(Exception e) {
			// 실패했을 때
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/WEB-INF/views/result.jsp");
			mav.addObject("msg", "게시물 리스트 에러");
			mav.addObject("url", "../result_search.jsp");
			return mav;
		}
	}

}