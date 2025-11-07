package ilogin.demo_jwt.User;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

     // Obtiene el usuario actualmente autenticado desde el contexto de seguridad.
    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

     //  Actualiza el perfil del usuario autenticado.
    public UserResponseDTO updateUser(UserUpdateRequest request) {
        User user = getAuthenticatedUser();

        // Actualiza los campos solo si se proporcionaron en la solicitud (no son nulos)
        if (StringUtils.hasText(request.getFirstname())) {
            user.setFirstname(request.getFirstname());
        }
        if (StringUtils.hasText(request.getLastname())) {
            user.setLastname(request.getLastname());
        }
        if (StringUtils.hasText(request.getCountry())) {
            user.setCountry(request.getCountry());
        }

        // Actualiza la contraseña solo si se proporcionó una nueva
        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User savedUser = userRepository.save(user);

        return UserResponseDTO.fromUser(savedUser);
    }

     // Elimina la cuenta del usuario autenticado.
    public void deleteUser() {
        User user = getAuthenticatedUser();
        userRepository.delete(user);
    }
}