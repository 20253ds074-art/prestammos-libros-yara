package mx.edu.utez.prestamos.controller.prestamo;

import jakarta.validation.Valid;
import mx.edu.utez.prestamos.controller.prestamo.dto.RequestSolicitarPrestamo;
import mx.edu.utez.prestamos.model.lector.Lector;
import mx.edu.utez.prestamos.model.prestamo.Prestamo;
import mx.edu.utez.prestamos.service.PrestamoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamo")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<Prestamo> solicitarPrestamo(@Valid @RequestBody RequestSolicitarPrestamo dto) {
        return new ResponseEntity<>(prestamoService.solicitarPrestamo(dto), HttpStatus.CREATED);
    }

    @PutMapping("/devolver/{prestamoId}")
    public ResponseEntity<Prestamo> devolverLibro(@PathVariable Long prestamoId) {
        return ResponseEntity.ok(prestamoService.devolverLibro(prestamoId));
    }

    @PutMapping("/renovar/{prestamoId}")
    public ResponseEntity<Prestamo> renovarPrestamo(@PathVariable Long prestamoId) {
        return ResponseEntity.ok(prestamoService.renovarPrestamo(prestamoId));
    }

    @PatchMapping("/lector/{lectorId}/rehabilitar")
    public ResponseEntity<Lector> rehabilitarLector(@PathVariable Long lectorId) {
        return ResponseEntity.ok(prestamoService.rehabilitarLector(lectorId));
    }

    @GetMapping("/lector/{lectorId}/activos")
    public ResponseEntity<List<Prestamo>> listarPrestamosActivos(@PathVariable Long lectorId) {
        return ResponseEntity.ok(prestamoService.listarPrestamosActivosLector(lectorId));
    }
}