package ace.front.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractController {

	public abstract ModelAndView handleRequestInternal(HttpServletRequest request, 
															HttpServletResponse response);
	// 항상 같은 규격으로 수행하도록 해당 추상 클래스를 상속하게 함

}
