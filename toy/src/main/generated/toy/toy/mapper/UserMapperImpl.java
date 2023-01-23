package toy.toy.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import toy.toy.domain.User;
import toy.toy.dto.UserDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-01-24T02:21:37+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.4 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User UserRequestToUser(UserDto.Request dto) {
        if ( dto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( dto.getUsername() );
        user.password( dto.getPassword() );
        user.name( dto.getName() );
        user.email( dto.getEmail() );
        user.address( dto.getAddress() );
        user.phone( dto.getPhone() );

        return user.build();
    }
}
