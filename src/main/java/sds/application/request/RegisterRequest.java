package sds.application.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class RegisterRequest {

    String username;
    String password;
    String email;
}
