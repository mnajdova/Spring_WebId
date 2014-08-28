package mk.ukim.finki.mp.webid.store;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionWebIdContentStore implements WebIdContentStore{

	public static final String WEB_ID_CONTENT = "webIdContent";
	public static final String WEB_ID_URL = "webIdUrl";

	@Override
	public void store(String webIdContent, HttpServletRequest request,
			HttpServletResponse response) {
		request.getSession().setAttribute(WEB_ID_CONTENT, webIdContent);
	}

	@Override
	public String load(HttpServletRequest request) {
		return (String) request.getSession().getAttribute(WEB_ID_CONTENT);
	}

	@Override
	public void storeWebId(String webIdUrl, HttpServletRequest request,
			HttpServletResponse response) {
	
		request.getSession().setAttribute(WEB_ID_URL, webIdUrl);
		
	}


}
