package sii.ms_corrector.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sii.ms_corrector.dtos.UsuarioDTO;
import sii.ms_corrector.entities.Usuario;
import sii.ms_corrector.entities.Usuario.Role;
import sii.ms_corrector.repositories.UsuarioRepository;
import sii.ms_corrector.services.exceptions.UsuarioYaExiste;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository userRepo;

	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario user = userRepo.findByUsername(username);

		if (user == null)
			throw new UsernameNotFoundException("User not found with username: " + username);
		
		Set<SimpleGrantedAuthority> auths = new HashSet<SimpleGrantedAuthority>();
		Set<Role> roles = user.getRoles();
		roles.forEach(rol -> auths.add(new SimpleGrantedAuthority("ROLE_" + rol.toString())));
		
		return new User(user.getUsername(), user.getPassword(), auths);
	}
	
	public Usuario save(UsuarioDTO user) {
		if (userRepo.existsByUsername(user.getUsername())) {
            throw new UsuarioYaExiste();
        }
		Usuario newUser = new Usuario();
		newUser.setUsername(user.getUsername());
		newUser.setRoles(user.getRoles());
		
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		return userRepo.save(newUser);
	}
	
}