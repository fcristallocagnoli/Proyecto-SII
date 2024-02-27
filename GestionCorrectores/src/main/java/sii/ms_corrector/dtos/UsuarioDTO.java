package sii.ms_corrector.dtos;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sii.ms_corrector.entities.Usuario;
import sii.ms_corrector.entities.Usuario.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioDTO{

	private String username; 
	private String password;
	
	private Set<Role> roles = new HashSet<Role>();

    public static UsuarioDTO fromUsuario(Usuario usuario) {
        var dto = new UsuarioDTO();
        dto.setUsername(usuario.getUsername());
        dto.setPassword(usuario.getPassword());
        dto.setRoles(usuario.getRoles());
        return dto;
    }

}