package mx.edu.utez.prestamos.controller.lector.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegistarLector {

    private String nombre;
    private boolean activo;
    private boolean membresia;


}
