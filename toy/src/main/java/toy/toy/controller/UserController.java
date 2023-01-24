package toy.toy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import toy.toy.domain.User;
import toy.toy.dto.UserDto;
import toy.toy.mapper.UserMapper;
import toy.toy.service.UserService;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/signin")
    public String signin(){
        return "signin";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(UserDto dto,
                         MultipartFile file) throws Exception {
//        User user = userMapper.UserRequestToUser(dto);
        User user = dto.toEntity();
        userService.signup(user, file);
        return "signin";
    }
}
