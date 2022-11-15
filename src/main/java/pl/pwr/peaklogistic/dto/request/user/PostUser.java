package pl.pwr.peaklogistic.dto.request.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.pwr.peaklogistic.common.UserType;
import pl.pwr.peaklogistic.model.User;

@Getter
@AllArgsConstructor
public class PostUser {
    private String email;
    private String password;
//    private UserType userType;

    public static PostUser copy(PostUser postUser, String password) {
        return new PostUser(postUser.email, password);
    }

//    public static User toDomain(PostUser postUser){
//        User transformed = new User();
//        transformed.setUsername(postUser.email);
//        transformed.setPassword(postUser.password);
//        transformed.setUserType(postUser.userType);
//
//        return transformed;
//    }

}
