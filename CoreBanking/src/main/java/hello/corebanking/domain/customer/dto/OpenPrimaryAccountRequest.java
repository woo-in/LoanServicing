package hello.corebanking.domain.customer.dto;

import lombok.Getter;

@Getter
public class OpenPrimaryAccountRequest {

    private final long memberId;

    public OpenPrimaryAccountRequest(long memberId) {
        this.memberId = memberId;
    }
}
