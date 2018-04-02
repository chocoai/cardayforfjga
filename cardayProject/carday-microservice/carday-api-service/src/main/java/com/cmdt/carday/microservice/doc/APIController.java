/**
 * 
 */
package com.cmdt.carday.microservice.doc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author makun, senli
 *
 */
/**
 * Home redirection to swagger api documentation 
 */
@Controller
public class APIController {
	
	@RequestMapping(value = "/")
	public String index() {
		return "redirect:swagger-ui.html";
	}
	
}
