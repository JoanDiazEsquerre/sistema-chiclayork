/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model.aldasa.util;



import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.WebAttributes;


/**
 *
 * @author Jhon
 */
@SuppressWarnings({"serial"})
public class LoginErrorPhaseListener implements PhaseListener{

	private static final Logger LOG = LoggerFactory
			.getLogger(LoginErrorPhaseListener.class);
	
	@SuppressWarnings({"unchecked"})
    //@Override
    public void beforePhase(PhaseEvent event) { 
          Exception e = (Exception) FacesContext.getCurrentInstance().
	  getExternalContext().getSessionMap().get(WebAttributes.AUTHENTICATION_EXCEPTION);
          	
            if (e instanceof BadCredentialsException) {
            	e.printStackTrace();
            	LOG.info("exception: " + e.getMessage());
              	
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
                            WebAttributes.AUTHENTICATION_EXCEPTION, null);
                FacesContext.getCurrentInstance().addMessage(null, 
                              new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Datos incorrectos...","Datos incorrectos..."));                             
            }            
           
    }
 
    //@Override
    public void afterPhase(final PhaseEvent arg0){
    }
 
    //@Override
    public PhaseId getPhaseId(){
      return PhaseId.RENDER_RESPONSE;
    }
    
    
    
}
