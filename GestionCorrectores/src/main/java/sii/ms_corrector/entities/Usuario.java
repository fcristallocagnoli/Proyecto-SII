package sii.ms_corrector.entities;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@EqualsAndHashCode(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@ToString(doNotUseGetters = true)
@NoArgsConstructor
public class Usuario {
	@Id @GeneratedValue(strategy = GenerationType.AUTO) @EqualsAndHashCode.Include
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;
	
    @JsonIgnore
	@Column(nullable = false)
	private String password;
	 
	public enum Role{
		CORRECTOR,
		VICERRECTORADO
	}
	
	@Column(nullable = false)
	@ElementCollection(fetch = FetchType.EAGER) @Enumerated(EnumType.STRING)
	private Set<Role> roles = new HashSet<Role>();
	
}