package mk.ukim.finki.mp.webid.controller;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mk.ukim.finki.mp.webid.store.SessionWebIdContentStore;
import mk.ukim.finki.mp.webid.store.XAuthWebIdContentStore;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyContoller {
	
	
	@RequestMapping(value = "/*")
	public String invalid() {
		return "redirect:hello";
	}

	@RequestMapping(value = { "hello" })
	@Secured("ROLE_CLIENT")
	public ModelAndView hello(HttpServletRequest request) {
		ModelAndView result = new ModelAndView("index");
	
		return result;
	}
	
	@RequestMapping(value = { "session" })
	public ModelAndView session(HttpServletRequest request) {
		ModelAndView result = new ModelAndView("index");
		
		HttpSession session = request.getSession();
		System.out.println("OD SESSION:" + session.getAttribute(SessionWebIdContentStore.WEB_ID_URL).toString());
		System.out.println("OD SESSION" + session.getAttribute(SessionWebIdContentStore.WEB_ID_CONTENT).toString());
		
		return result;
	}
	
	
	
	
}
