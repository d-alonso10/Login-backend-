package ilogin.demo_jwt.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ilogin.demo_jwt.User.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    String firstname;
    String lastname;
    String country;
    String password;
    Role role;
}