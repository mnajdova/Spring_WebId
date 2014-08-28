package mk.ukim.finki.mp.webid.store;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WebIdContentStore {

	public void store(String webIdContent, HttpServletRequest request,
			HttpServletResponse response);

	public void storeWebId(String webIdUrl, HttpServletRequest request,
			HttpServletResponse response);
	
	public String load(HttpServletRequest request);
}
