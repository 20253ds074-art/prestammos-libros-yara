package mx.edu.utez.prestamos.service;

import mx.edu.utez.prestamos.controller.prestamo.dto.RequestSolicitarPrestamo;
import mx.edu.utez.prestamos.exceptions.customExceptions.BadRequestException;
import mx.edu.utez.prestamos.exceptions.customExceptions.NotFoundException;
import mx.edu.utez.prestamos.model.lector.Lector;
import mx.edu.utez.prestamos.model.libro.Libro;
import mx.edu.utez.prestamos.model.prestamo.Prestamo;
import mx.edu.utez.prestamos.repository.lector.LectorRepository;
import mx.edu.utez.prestamos.repository.libro.LibroRepository;
import mx.edu.utez.prestamos.repository.prestamo.PrestamoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private final LectorRepository lectorRepository;
    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    public PrestamoService(LectorRepository lectorRepository,
                           LibroRepository libroRepository,
                           PrestamoRepository prestamoRepository) {
        this.lectorRepository = lectorRepository;
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
    }

    // 1. solicitarPrestamo
    @Transactional
    public Prestamo solicitarPrestamo(RequestSolicitarPrestamo dto) {
        Lector lector = lectorRepository.findById(dto.getIdLector())
                .orElseThrow(() -> new NotFoundException("Lector no encontrado"));

        if (!lector.isActivo()) {
            throw new BadRequestException("El lector no se encuentra activo");
        }

        Libro libro = libroRepository.findById(dto.getIdLibro())
                .orElseThrow(() -> new NotFoundException("Libro no encontrado"));

        if (libro.getEjemplares() < 1) {
            throw new BadRequestException("El libro no cuenta con ejemplares disponibles");
        }

        long prestamosActivos = prestamoRepository.countByLectorAndEstadoIn(
                lector, List.of("PRESTADO", "VENCIDO")
        );
        int limiteMaximo = lector.isMembresia() ? 5 : 3;

        if (prestamosActivos >= limiteMaximo) {
            throw new BadRequestException("El lector ha alcanzado su límite máximo de préstamos activos (" + limiteMaximo + ")");
        }

        libro.setEjemplares(libro.getEjemplares() - 1);
        libroRepository.save(libro);

        LocalDate hoy = LocalDate.now();
        Prestamo prestamo = new Prestamo();
        prestamo.setLector(lector);
        prestamo.setLibro(libro);
        prestamo.setFecha(hoy);
        prestamo.setFechaDevolucionMaxima(hoy.plusDays(14));
        prestamo.setEstado("PRESTADO");

        return prestamoRepository.save(prestamo);
    }

    // 2. devolverLibro
    @Transactional
    public Prestamo devolverLibro(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new NotFoundException("El préstamo no existe"));

        if ("DEVUELTO".equalsIgnoreCase(prestamo.getEstado())) {
            throw new BadRequestException("El préstamo ya se encuentra devuelto");
        }

        prestamo.setEstado("DEVUELTO");

        Libro libro = prestamo.getLibro();
        libro.setEjemplares(libro.getEjemplares() + 1);
        libroRepository.save(libro);

        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(prestamo.getFechaDevolucionMaxima())) {
            Lector lector = prestamo.getLector();
            lector.setActivo(false);
            lectorRepository.save(lector);
        }

        return prestamoRepository.save(prestamo);
    }

    // 3. renovarPrestamo
    @Transactional
    public Prestamo renovarPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new NotFoundException("El préstamo no existe"));

        if ("VENCIDO".equalsIgnoreCase(prestamo.getEstado()) || "DEVUELTO".equalsIgnoreCase(prestamo.getEstado())) {
            throw new BadRequestException("El préstamo no se puede renovar en su estado actual");
        }

        prestamo.setFechaDevolucionMaxima(prestamo.getFechaDevolucionMaxima().plusDays(7));
        return prestamoRepository.save(prestamo);
    }

    // 4. rehabilitarLector
    @Transactional
    public Lector rehabilitarLector(Long lectorId) {
        Lector lector = lectorRepository.findById(lectorId)
                .orElseThrow(() -> new NotFoundException("El lector no existe"));

        lector.setActivo(true);
        return lectorRepository.save(lector);
    }

    // 5. listarPrestamosActivosLector
    public List<Prestamo> listarPrestamosActivosLector(Long lectorId) {
        Lector lector = lectorRepository.findById(lectorId)
                .orElseThrow(() -> new NotFoundException("El lector no existe"));

        return prestamoRepository.findByLectorAndEstadoIn(lector, List.of("PRESTADO", "VENCIDO"));
    }
}