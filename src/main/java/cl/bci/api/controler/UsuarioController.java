package controler;

import modelo.Usuario;
import modelo.Telefono;
import repositorio.UsuarioRepository;
import servicio.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> crearUsuario(@RequestBody @Valid Usuario usuario) {
        try {
            Usuario creado = usuarioService.crearUsuario(usuario);
            return new ResponseEntity<>(creado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(crearError(400, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(crearError(500, "Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestHeader("Authorization") String token) {
        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorToken(token);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(crearError(401, e.getMessage()), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(crearError(500, "Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ErrorResponse crearError(int codigo, String detalle) {
        return new ErrorResponse(codigo, detalle);
    }

    // Clase interna para manejar errores
    public static class ErrorResponse {
        private long timestamp;
        private int codigo;
        private String detail;

        public ErrorResponse(int codigo, String detail) {
            this.timestamp = System.currentTimeMillis();
            this.codigo = codigo;
            this.detail = detail;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public int getCodigo() {
            return codigo;
        }

        public String getDetail() {
            return detail;
        }
    }
}