package ace.front.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//slf4j로 아래 두가지 import할 것
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@WebServlet({"/ace/*"}) // ()안에 {"", ""...}의 형태로 경로를 여러개 줄 수 있음
public class DispatcherServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
	private Map<String, AbstractController> controllerMap =
									new HashMap<String, AbstractController>();
	
	@Override
	public void init() throws ServletException {
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(this.getClass().getResource("").getPath() 
												+ "/dispatcherServlet.properties"));
			for(Object okey : prop.keySet()) {
				String key = ((String)okey).trim();
				try {
					Class className =  Class.forName(prop.getProperty(key).trim());
					controllerMap.put(key,(AbstractController)className.newInstance());
					logger.info("loaded : " + key);
				} catch (Exception e) {
					logger.info("ActionClass Error : " + e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			logger.info("dispatcherServlet.properties 파일이 존재하지 않습니다.");
			e.printStackTrace();
		} 
		
		 logger.info("dispatcherServlet 수행");
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.warn("dispatcherServlet.service()");
		
		String requestURI = request.getRequestURI().trim();
		String action = requestURI.substring(request.getContextPath().length()).trim();
		//URI에서 제일 끝 경로 이름 action 변수에 담음
		
		logger.debug("requestURI : "+requestURI);
		logger.debug(action);
		
		//커맨드 패턴을 prop와 init을 활용해서 변경에 대해 열린 구조로 만듬
		AbstractController controller = controllerMap.get(action);
		ModelAndView mav = null;
		
		try { //action이름이 잘못되면 nullpointexception발생
			mav = controller.handleRequestInternal(request,response);
		} catch(NullPointerException e) {
			logger.info(action + "액션이 존재하지 않습니다");
		}
		
		//mav는 null일 수 없다. null이라면, 경로를 잘못적은 것
		if(mav != null) {
			String viewName = mav.getViewName();
			if(viewName.substring(0,9).equals("redirect:")) {
				response.sendRedirect(viewName.substring(9));
				return;
			}
			
			Map<String,Object> model = mav.getModel();
			for(String key:model.keySet()) {
				request.setAttribute(key, model.get(key));
			}
			
			RequestDispatcher dispatcher 
				= request.getRequestDispatcher(viewName);
			dispatcher.forward(request, response);
			return;
		}else {
			logger.info("DispatcherServlet에서 길을 잃었다네 어디로 가야 할까.... :(");
		}
	}

}
