package com.model.aldasa;

import java.util.Arrays;

import javax.faces.webapp.FacesServlet;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Import({SecurityConfig.class})
@EnableJpaRepositories("com.model.aldasa.repository")
@EntityScan("com.model.aldasa.entity")
@ComponentScan("com.model.aldasa")
@SpringBootApplication
public class AldasaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AldasaApplication.class, args);
	}
	
	@Bean
	  ServletRegistrationBean jsfServletRegistration (ServletContext servletContext) {
	      //spring boot only works if this is set
	      servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
//	      servletContext.setInitParameter ("primefaces.THEME", "bluesky");
//          servletContext.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", "true");
//          servletContext.setInitParameter("com.sun.faces.expressionFactory","com.sun.el.ExpressionFactoryImpl");
//          servletContext.setInitParameter("primefaces.UPLOADER","commons");
	      servletContext.setInitParameter("primefaces.UPLOADER",  "commons");
	      //registration
	      ServletRegistrationBean srb = new ServletRegistrationBean();
	      srb.setServlet(new FacesServlet());
	      srb.setUrlMappings(Arrays.asList("*.xhtml"));
	      srb.setLoadOnStartup(1);
	      return srb;
	  }
	
	
	/*@Bean
	public FilterRegistrationBean FileUploadFilter() {
	    FilterRegistrationBean registration = new FilterRegistrationBean();
	    registration.setFilter(new org.primefaces.webapp.filter.FileUploadFilter());
	    registration.setName("PrimeFaces FileUpload Filter");
	    return registration;
	}*/
	
	@Bean
	  public ServletContextInitializer servletContextInitializer() {
	    return servletContext -> {
	        servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
	        //servletContext.setInitParameter("primefaces.THEME", "blitzer");
	        
	        servletContext.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", Boolean.TRUE.toString());
	        
	        servletContext.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", Boolean.TRUE.toString());
	         
	        servletContext.setInitParameter("primefaces.FONT_AWESOME", Boolean.TRUE.toString());
	        
	        servletContext.setInitParameter("javax.faces.ENABLE_CDI_RESOLVER_CHAIN", Boolean.TRUE.toString());
	        servletContext.setInitParameter("primefaces.UPLOADER", "commons"); 

	      };
	}
	
	
	//for setting fileUploadFilter to in front of filterChain - so uploaded file not consumed by other filter
	  @Bean
	  public FilterRegistrationBean primeFacesFileUploadFilter() {

	     FilterRegistrationBean registration = new FilterRegistrationBean(new org.primefaces.webapp.filter.FileUploadFilter(), facesServletRegistration());
	    
	     registration.addUrlPatterns("/*"); 
	    
	     registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
	     registration.setName("primeFacesFileUploadFilter");
	     registration.setOrder(1);
	     return registration;
	    }
	  

	@Bean
	public ServletRegistrationBean facesServletRegistration() {
	  ServletRegistrationBean registration = new ServletRegistrationBean<>(new FacesServlet(), "*.xhtml");
	  registration.setLoadOnStartup(1);
	  return registration;
	}
}
