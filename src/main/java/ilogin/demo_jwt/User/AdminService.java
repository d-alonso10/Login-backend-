package ilogin.demo_jwt.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Obtener todos los usuarios
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::fromUser)
                .collect(Collectors.toList());
    }

    // Actualizar usuario por ID
    public UserResponseDTO updateUser(Integer id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con id: " + id));

        // Actualiza los campos si se proporcionaron
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

        // Actualiza el rol si se proporcionó
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        User savedUser = userRepository.save(user);
        return UserResponseDTO.fromUser(savedUser);
    }

    // Eliminar usuario por ID
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UsernameNotFoundException("Usuario no encontrado con id: " + id);
        }
        userRepository.deleteById(id);
    }
}