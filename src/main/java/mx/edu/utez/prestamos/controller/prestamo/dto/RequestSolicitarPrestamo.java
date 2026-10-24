package mx.edu.utez.prestamos.controller.prestamo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestSolicitarPrestamo {

    @NotNull(message = "El id del lector es obligatorio")
    private Long idLector;

    @NotNull(message = "El id del libro es obligatorio")
    private Long idLibro;
}