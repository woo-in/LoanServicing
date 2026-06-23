package hello.corebanking.domain.product.service;

import hello.corebanking.domain.product.entity.InterestType;
import hello.corebanking.domain.product.repository.InterestTypeMapper;
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
class InterestTypeServiceTest {

    @Mock
    private InterestTypeMapper interestTypeMapper;

    @InjectMocks
    private InterestTypeService interestTypeService;

    @Test
    @DisplayName("이자종류 등록 시 mapper.insert 가 호출되고 이름이 설정된 엔티티를 반환한다")
    void register() {
        InterestType result = interestTypeService.register("고정금리");

        assertThat(result.getName()).isEqualTo("고정금리");
        verify(interestTypeMapper).insert(any(InterestType.class));
    }

    @Test
    @DisplayName("존재하는 id 조회 시 엔티티를 반환한다")
    void findById_found() {
        when(interestTypeMapper.findById(1)).thenReturn(Optional.of(new InterestType("고정금리")));

        InterestType result = interestTypeService.findById(1);

        assertThat(result.getName()).isEqualTo("고정금리");
    }

    @Test
    @DisplayName("존재하지 않는 id 조회 시 NotFoundException 을 던진다")
    void findById_notFound() {
        when(interestTypeMapper.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> interestTypeService.findById(99))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("전체 조회 시 mapper 에서 받은 목록을 반환한다")
    void findAll() {
        List<InterestType> list = List.of(new InterestType("고정금리"), new InterestType("변동금리"));
        when(interestTypeMapper.findAll()).thenReturn(list);

        List<InterestType> result = interestTypeService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(InterestType::getName)
                .containsExactly("고정금리", "변동금리");
    }
}
