package toy.toy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import toy.toy.domain.User;
import toy.toy.repository.UserRepository;

import java.io.File;
import java.lang.module.FindException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserPageService {

    private final UserRepository userRepository;

    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UsernameNotFoundException("찾을 수 없는 사용자입니다."));
    }

    public void userPageModify(User user, MultipartFile file) throws Exception{
        String path = System.getProperty("user.dir") + "/src/main/resources/static/files/";
        UUID uuid = UUID.randomUUID();
        String filename = uuid + "_" + file.getOriginalFilename();
        File save = new File(path, filename);
        file.transferTo(save);

        User old = userRepository.findById(user.getId()).orElseThrow(() ->
                new UsernameNotFoundException("찾을 수 없는 사용자입니다."));
        old.setFilename(filename);
        old.setFilepath("/files/" + filename);
        old.setEmail(user.getEmail());
        old.setAddress(user.getAddress());
        old.setPhone(user.getPhone());
        userRepository.save(old);
    }

    public void chargeMoney(User user, int money){
        user.setMoney(user.getMoney() + money);
        userRepository.save(user);
    }
}
