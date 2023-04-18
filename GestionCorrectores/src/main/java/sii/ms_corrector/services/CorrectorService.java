package sii.ms_corrector.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import sii.ms_corrector.entities.Corrector;
import sii.ms_corrector.repositories.CorrectorRepository;
import sii.ms_corrector.services.exceptions.CorrectorNoEncontrado;
import sii.ms_corrector.services.exceptions.CorrectorYaExiste;

@Service
@Transactional
public class CorrectorService {

    private CorrectorRepository repository;

    @Autowired
    public CorrectorService(CorrectorRepository repository) {
        this.repository = repository;
    }

    public List<Corrector> getTodosCorrectores() {
        return repository.findAll();
    }

    public List<Corrector> getTodosCorrectoresByConvocatoria(Long idConvocatoria) {
        return repository.findAllByIdConvocatoria(idConvocatoria);
    }

    public Corrector getCorrectorById(Long id) {
        if (!repository.existsById(id)) {
            throw new CorrectorNoEncontrado();
        }
        return repository.findById(id).get();
    }

    public Long añadirCorrector(Corrector nuevoCorrector) {
        if (repository.existsByIdUsuario(nuevoCorrector.getIdUsuario())) {
            throw new CorrectorYaExiste();
        }
        nuevoCorrector.setId(null);
		repository.save(nuevoCorrector);
		return nuevoCorrector.getId();
    }

	public void modificarCorrector(Corrector corrector) {
		if (!repository.existsById(corrector.getId())) {
			throw new CorrectorNoEncontrado();
        }
        Optional<Corrector> correctorRepo = repository.findById(corrector.getId());
        correctorRepo.ifPresent(c->c.setMateriaEspecialista(corrector.getMateriaEspecialista()));
        
        // (Campos sacados del esquema de la API)
        // se permite cambiar el id de la base de datos?
        // o acaso 'id' en corrector es independiente
        // y que es 'identificadorUsuario'
        correctorRepo.ifPresent(c -> c.setId(corrector.getId()));
        correctorRepo.ifPresent(c -> c.setIdUsuario(corrector.getIdUsuario()));
        correctorRepo.ifPresent(c -> c.setTelefono(corrector.getTelefono()));
        correctorRepo.ifPresent(c -> c.setMaximasCorrecciones(corrector.getMaximasCorrecciones()));
        // que es exactamente la lista de materias en convocatoria?
	}

	public void eliminarCorrector(Long id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
		} else {
            // seria AccesoNoAutorizado? cuando salta esta excepcion?
            // gestion de usuarios se encarga de los roles, nos importa en algo a nosotros?
            // como diferencio quien puede acceder y quien no?
			throw new CorrectorNoEncontrado();
		}
	}

}