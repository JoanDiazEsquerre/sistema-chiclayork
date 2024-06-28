package com.model.aldasa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import com.model.aldasa.service.impl.UserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailService userDetailsService ;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		 http.authorizeRequests()
         .antMatchers("/secured/view/**").fullyAuthenticated()
         .antMatchers("/secured/admin/**", "/secured/view/admin/**").access("hasRole('ROLE_SUPERUSER')")
         .antMatchers("/index.xhtml", "/index.html", "/login2.xhtml", "/javax.faces.resources/**", "/home.xhtml").permitAll()
         .and()
         .formLogin()
				 //.loginPage("/login2.xhtml")
				 //.usernameParameter("username")
				 //.passwordParameter("password")
				 .defaultSuccessUrl("/secured/view/home.xhtml").successForwardUrl("/secured/view/home.xhtml")
				 .failureUrl("/login2.xhtml?error=true")
		 .successHandler(customAuthenticationSuccessHandler)

         .and()
         .logout().logoutSuccessUrl("/login2.xhtml").invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutUrl("/logout")
         .and()
         .csrf().disable();
	}

    
}
