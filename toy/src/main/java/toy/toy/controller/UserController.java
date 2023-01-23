package toy.toy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import toy.toy.domain.User;
import toy.toy.dto.UserDto;
import toy.toy.mapper.UserMapper;
import toy.toy.service.UserService;

@RequiredArgsConstructor
@RestController
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
    public String signup(UserDto.Request dto, MultipartFile file) throws Exception{
        User user = userMapper.UserRequestToUser(dto);
        userService.signup(user, file);
        return "signin";
    }
}
