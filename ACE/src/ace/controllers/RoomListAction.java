package ace.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ace.front.controllers.AbstractController;
import ace.front.controllers.ModelAndView;
import ace.models.RoomDTO;

public class RoomListAction extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(RoomListAction.class);

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		String hname = request.getParameter("hname");
		String haddr = request.getParameter("haddr");
		String hnum = request.getParameter("hnum");
		String rmname = request.getParameter("rmname");
		String rmcode = request.getParameter("rmcode");
		String rmtype = request.getParameter("rmtype");
		String rtmaxp = request.getParameter("rtmaxp");
		String rmpay = request.getParameter("rmpay");

		RoomDTO roomDTO = new RoomDTO();
		roomDTO.setHaddr(haddr);
		roomDTO.setHname(hname);
		roomDTO.setHnum(hnum);
		roomDTO.setRmcode(rmcode);
		roomDTO.setRmname(rmname);
		roomDTO.setRmpay(rmpay);
		roomDTO.setRmtype(rmtype);
		roomDTO.setRtmaxp(rtmaxp);

		logger.info(roomDTO.toString());

		return null;
	}

}
