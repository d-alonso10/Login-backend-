package ilogin.demo_jwt.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Endpoint para que el usuario autenticado actualice su propia información.
     */
    @PutMapping(value = "update")
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateRequest request) {
        // Retornamos el usuario actualizado (sin la contraseña)
        // Nota: La entidad User (UserDetails) no debería exponer la contraseña
        // al ser devuelta. Considera un UserResponseDTO si es necesario.
        return ResponseEntity.ok(userService.updateUser(request));
    }

    /**
     * Endpoint para que el usuario autenticado elimine su propia cuenta.
     */
    @DeleteMapping(value = "delete")
    public ResponseEntity<String> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.ok("Usuario eliminado exitosamente");
    }
}