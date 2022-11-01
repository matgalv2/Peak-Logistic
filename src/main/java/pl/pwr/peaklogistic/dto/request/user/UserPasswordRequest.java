package pl.pwr.peaklogistic.dto.request.user;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPasswordRequest {
    private String oldPassword;
    private String newPassword;
}
