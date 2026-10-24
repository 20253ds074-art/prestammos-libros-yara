package mx.edu.utez.prestamos.repository.libro;

import mx.edu.utez.prestamos.model.libro.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
}
