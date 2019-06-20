package ace.controllers;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;
import ace.models.AceDAO;
import ace.models.AceDAOImpl;
import ace.models.HotelDTO;
import ace.models.PageDTO;
import ace.models.SearchDTO;

public class SearchAction extends AbstractController{
	private static final Logger logger = LoggerFactory.getLogger(SearchAction.class);
	
	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		String searchLoc 	= request.getParameter("search");
		String InDate 		= request.getParameter("InDate");
		String OutDate 		= request.getParameter("OutDate");
		int    numOfGuests 	= Integer.parseInt(request.getParameter("numOfGuests"));

		long pg = 1;			// 현재 페이지
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
		
		SearchDTO searchDTO = new SearchDTO(searchLoc, InDate, OutDate, numOfGuests);
		PageDTO pageDTO = new PageDTO(startNum, endNum);
		
		logger.info(searchDTO.toString());
		
		AceDAO aceDAO = AceDAOImpl.getInstance();
		
		try {
			totalCount = aceDAO.getTotalCount(searchDTO);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}					// 전체 게시물 수	
		totalPage = totalCount / pageSize;							// 전체 페이지 수
		if (totalCount % pageSize != 0) totalPage++;
		startBlock = (pg - 1) / blockSize * blockSize + 1;			// 페이지 블럭의 시작번호
		endBlock   = (pg - 1) / blockSize * blockSize + blockSize;	// 페이지 블럭의 끝번호
		if (endBlock > totalPage) endBlock = totalPage;
		
		java.util.List<HotelDTO> list;
			    
		try {
			list = aceDAO.getResultList(searchDTO, pageDTO);
			// 성공했을 때
			ModelAndView mav 	= new ModelAndView("/WEB-INF/views/result_search.jsp");
			mav.addObject("list", list);
			mav.addObject("searchDTO", searchDTO);
			mav.addObject("pg", pg);
			mav.addObject("totalPage", totalPage);
			mav.addObject("totalCount", totalCount);
			mav.addObject("startBlock", startBlock);
			mav.addObject("endBlock", endBlock);
			mav.addObject("InDate", InDate);
			mav.addObject("OutDate", OutDate);
			mav.addObject("numOfGuests", numOfGuests);

			return mav;
			
		} catch (SQLException e) {
			// 실패했을 때
			ModelAndView mav 	= new ModelAndView("/WEB-INF/views/result_search.jsp");
			mav.addObject("msg", "호텔 리스트 에러");
			mav.addObject("url", "../result_search.jsp");
			return mav;
		}
		
	}
	
		

}
