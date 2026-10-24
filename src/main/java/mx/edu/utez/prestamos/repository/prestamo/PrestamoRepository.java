package mx.edu.utez.prestamos.repository.prestamo;

import mx.edu.utez.prestamos.model.lector.Lector;
import mx.edu.utez.prestamos.model.prestamo.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

   List<Prestamo> findByLectorAndEstadoIn(Lector lector, List<String> estados);

   long countByLectorAndEstadoIn(Lector lector, List<String> estados);
}