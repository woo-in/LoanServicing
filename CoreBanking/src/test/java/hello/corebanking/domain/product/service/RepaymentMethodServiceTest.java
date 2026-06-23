package hello.corebanking.domain.product.service;

import hello.corebanking.domain.product.entity.RepaymentMethod;
import hello.corebanking.domain.product.repository.RepaymentMethodMapper;
import hello.corebanking.global.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepaymentMethodServiceTest {

    @Mock
    private RepaymentMethodMapper repaymentMethodMapper;

    @InjectMocks
    private RepaymentMethodService repaymentMethodService;

    @Test
    @DisplayName("상환방법 등록 시 mapper.insert 가 호출되고 이름이 설정된 엔티티를 반환한다")
    void register() {
        RepaymentMethod result = repaymentMethodService.register("원리금균등상환");

        assertThat(result.getName()).isEqualTo("원리금균등상환");
        verify(repaymentMethodMapper).insert(any(RepaymentMethod.class));
    }

    @Test
    @DisplayName("존재하는 id 조회 시 엔티티를 반환한다")
    void findById_found() {
        when(repaymentMethodMapper.findById(1)).thenReturn(Optional.of(new RepaymentMethod("원리금균등상환")));

        RepaymentMethod result = repaymentMethodService.findById(1);

        assertThat(result.getName()).isEqualTo("원리금균등상환");
    }

    @Test
    @DisplayName("존재하지 않는 id 조회 시 NotFoundException 을 던진다")
    void findById_notFound() {
        when(repaymentMethodMapper.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repaymentMethodService.findById(99))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("전체 조회 시 mapper 에서 받은 목록을 반환한다")
    void findAll() {
        List<RepaymentMethod> list = List.of(
                new RepaymentMethod("원리금균등상환"),
                new RepaymentMethod("원금균등상환"),
                new RepaymentMethod("만기일시상환")
        );
        when(repaymentMethodMapper.findAll()).thenReturn(list);

        List<RepaymentMethod> result = repaymentMethodService.findAll();

        assertThat(result).hasSize(3);
        assertThat(result).extracting(RepaymentMethod::getName)
                .containsExactly("원리금균등상환", "원금균등상환", "만기일시상환");
    }
}
