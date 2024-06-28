package com.model.aldasa;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.model.aldasa.general.bean.NavegacionBean;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	//@Inject
	//private NavegacionBean navegacionBean;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		//navegacionBean.onPageLoadInit();
		//navegacionBean.onPageLoad();
		response.sendRedirect(request.getContextPath()+"/secured/view/home.xhtml");
	}

}
