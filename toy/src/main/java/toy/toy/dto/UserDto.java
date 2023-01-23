package toy.toy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{
        private String username;
        private String password;
        private String name;
        private String email;
        private String address;
        private String phone;
    }

    @Getter
    public static class Response{
        private String username;
        private String name;
        private String email;
        private String address;
        private String phone;
    }
}
