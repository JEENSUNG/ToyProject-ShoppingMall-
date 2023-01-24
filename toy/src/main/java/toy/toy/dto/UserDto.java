package toy.toy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import toy.toy.domain.User;

@Getter
@Setter
public class UserDto {
    private String username;
    private String password;
    private String email;
    private String name;
    private String address;
    private String phone;
    private String role;
    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .name(name)
                .address(address)
                .phone(phone)
                .role(role)
                .build();
    }
}
