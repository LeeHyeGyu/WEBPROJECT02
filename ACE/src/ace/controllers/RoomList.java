package ace.controllers;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;
import ace.models.AceDAO;
import ace.models.AceDAOImpl;
import ace.models.HotelDTO;
import ace.models.PageDTO;
import ace.models.RoomDTO;
import ace.models.SearchDTO;

public class RoomList extends AbstractController {

	@Override
	   public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
        
        String hcode = request.getParameter("hcode");
        String rdateIn = request.getParameter("rdateIn");
        String rdateOut = request.getParameter("rdateOut");
        int numOfGuests = Integer.parseInt(request.getParameter("numOfGuests"));
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setS_inDate(rdateIn);
        searchDTO.setS_outDate(rdateOut);
        searchDTO.setS_pNum(numOfGuests);
       long pg = 1;         // 현재 페이지
       int pageSize = 5;      // 페이지 당 출력할 게시물 수
       int blockSize= 10;      // 하단의 출력 페이지 개수         
        
        try {      
           pg = Long.parseLong(request.getParameter("pg"));
        } catch(NumberFormatException e) {}   
        
        long startNum = (pg - 1) * pageSize + 1;   // 페이지의 게시물 rownum 시작번호
        long endNum = pg * pageSize;            // 페이지의 게시물 rownum 끝번호
        long totalCount = 0;                  // 전체 게시물 수   
        long totalPage = 0;                     // 전체 페이지 수
        long startBlock = 0;                  // 페이지 블럭의 시작
        long endBlock = 0;                     // 페이지 블럭의 끝
        
        PageDTO pageDTO = new PageDTO(startNum, endNum);
        
        AceDAO aceDAO = AceDAOImpl.getInstance();
        
        
        
	      try {
	    	 HotelDTO hotel = aceDAO.getHotel(hcode);
	         
	    	 totalCount = aceDAO.getRooms(hcode,searchDTO);
	    	 System.out.println(totalCount);
	         totalPage = totalCount / pageSize;                     // 전체 페이지 수
	         if (totalCount % pageSize != 0) totalPage++;
	         startBlock = (pg - 1) / blockSize * blockSize + 1;         // 페이지 블럭의 시작번호
	         endBlock   = (pg - 1) / blockSize * blockSize + blockSize;   // 페이지 블럭의 끝번호
	         if (endBlock > totalPage) endBlock = totalPage;
	    	 
	    	 
	    	 List<RoomDTO> list = aceDAO.getRoomList(hcode,searchDTO,pageDTO);
	         
	         ModelAndView mav = new ModelAndView("/WEB-INF/views/roomList.jsp");
	         	mav.addObject("list", list);
				mav.addObject("hname", hotel.getH_name());
				mav.addObject("haddr", hotel.getH_addr());
				mav.addObject("hnum", hotel.getH_num());
				mav.addObject("rdateIn", rdateIn);
	            mav.addObject("rdateOut", rdateOut);
	            mav.addObject("rmpay", list.get(0).getRmpay());
	            mav.addObject("searchDTO", searchDTO);
	            mav.addObject("pg", pg);
	            mav.addObject("totalPage", totalPage);
	            mav.addObject("totalCount", totalCount);
	            mav.addObject("startBlock", startBlock);
	            mav.addObject("endBlock", endBlock);
	            mav.addObject("rdateIn", rdateIn);
	            mav.addObject("rdateOut", rdateOut);
	            mav.addObject("hcode", hcode);
			return mav;
		} catch (SQLException e) {
			e.printStackTrace();
			ModelAndView mav = new ModelAndView("/WEB-INF/views/roomList.jsp");
			return mav;
		}
	}

}
