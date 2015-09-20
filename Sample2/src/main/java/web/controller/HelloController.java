package web.controller;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import config.MyTokenHandler;
import config.SecurityConfig;
 
@Controller
public class HelloController {
	
	@Autowired
	SecurityConfig sc;
	MyTokenHandler th;
 
	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public ModelAndView welcomePage(@RequestHeader(value="X-AUTH-TOKEN", required=false) String token) {
		th = sc.tokenAuthenticationService().getTokenHandler();
 
		String message = "";
		if(token == null) {
			message = "No token Present";
		} else {
			message = "Token:<br><br>" + token + "<br><br>" + th.toTable(token);
		}
				
		ModelAndView model = new ModelAndView();
		model.addObject("title", "JWT Showcase");
		model.addObject("message", message);
		model.setViewName("hello");
		return model;
 
	}
	
	@RequestMapping(value = { "/welcome**" }, method = RequestMethod.GET)
	public ModelAndView welcomePage() {
		th = sc.tokenAuthenticationService().getTokenHandler();
 
		HashSet<SimpleGrantedAuthority> adminAuths = new HashSet<SimpleGrantedAuthority>();
		adminAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        User admin = new User("admin", "admin", adminAuths);
		String adminToken = th.createTokenForUser(admin);
		
		HashSet<SimpleGrantedAuthority> userAuths = new HashSet<SimpleGrantedAuthority>();
		userAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
		User user = new User("user", "user", userAuths);
		String userToken = th.createTokenForUser(user);
		
		HashSet<SimpleGrantedAuthority> dbaAuths = new HashSet<SimpleGrantedAuthority>();
		dbaAuths.add(new SimpleGrantedAuthority("ROLE_DBA"));
		User dba = new User("dba", "dba", dbaAuths);
		String dbaToken = th.createTokenForUser(dba);
				
		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security Hello World");
		model.addObject("message", "Try to access /admin or /dba with and without this token in the X-AUTH-TOKEN header field (Admin) <br><br>" + adminToken + "<br><br>And now with this one (User)<br><br>" + userToken + "<br><br> The DbA can access /dba, but not admin:<br><br>" + dbaToken);
		model.setViewName("hello");
		return model;
 
	}
	
	@RequestMapping(value = "/admin**", method = RequestMethod.GET)
	public ModelAndView adminPage() {
 
		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security Hello World");
		model.addObject("message", "This is the restricted admin page");
		model.setViewName("admin");
 
		return model;
 
	}
 
	@RequestMapping(value = "/dba**", method = RequestMethod.GET)
	public ModelAndView dbaPage() {
 
		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security Hello World");
		model.addObject("message", "This is a protected page, the Database Page!");
		model.setViewName("admin");
 
		return model;
 
	}
 
}
