package bankapp.loan.frozen.underwriting.web.request;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class RejectedLoanApplicationDto {

    private String messageToCustomer;

}
