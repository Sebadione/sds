package sds.application.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class LoginRequest {

    String username;
    String password;
}
