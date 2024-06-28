package com.model.aldasa.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Usuario;
import com.model.aldasa.repository.UsuarioRepository;

@Service
public class UserDetailService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByUsernameAndEstado(username,true);
		if(usuario==null) {
            throw new UsernameNotFoundException("Credenciales invalidas para "+ username);
		}
		List<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority("ADMIN"));
		UserDetails userDetails = new User(usuario.getUsername(), usuario.getPassword(),roles);
		return userDetails;
		
	} 
	

}
