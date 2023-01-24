package toy.toy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import toy.toy.domain.User;
import toy.toy.dto.UserDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
//    User UserRequestToUser(UserDto.Request dto);
}
