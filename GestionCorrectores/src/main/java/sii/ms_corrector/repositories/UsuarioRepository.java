package sii.ms_corrector.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sii.ms_corrector.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	boolean existsByUsername(String username);

	Usuario findByUsername(String username);
}