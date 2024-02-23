package sii.ms_corrector.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import sii.ms_corrector.dtos.JwtRequestDTO;
import sii.ms_corrector.dtos.JwtResponseDTO;
import sii.ms_corrector.dtos.UsuarioDTO;
import sii.ms_corrector.security.JwtAuthenticationManager;
import sii.ms_corrector.security.JwtUtil;
import sii.ms_corrector.services.JwtUserDetailsService;
import sii.ms_corrector.services.exceptions.UsuarioYaExiste;

@RestController
public class LoginController {
	
	@Autowired
	private JwtUserDetailsService userDetailsService;
	@Autowired
	private JwtAuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	// Hará falta devolver el UsuarioDTO en el ResponseEntity ¿?
	// Si no, se puede cambiar el tipo de retorno a solo "created" y devolver un ResponseEntity vacío
	@PostMapping("/register")
	public ResponseEntity<?> saveUser(@RequestBody UsuarioDTO user) throws Exception {
		return ResponseEntity.status(HttpStatus.CREATED.value())
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.body(userDetailsService.save(user));
	}	

	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequestDTO authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.body(new JwtResponseDTO(token));
	}

	private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

	@ExceptionHandler(UsuarioYaExiste.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)		// 409
    public void usuarioYaExiste() {}

}
