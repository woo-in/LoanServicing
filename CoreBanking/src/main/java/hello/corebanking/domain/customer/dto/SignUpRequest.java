package hello.corebanking.domain.customer.dto;

import lombok.Getter;

@Getter
public class SignUpRequest {

    private final String loginId;
    private final String password;
    private final String passwordConfirm;
    private final String name;

    public SignUpRequest(String loginId, String password, String passwordConfirm, String name) {
        this.loginId = loginId;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
    }
}
