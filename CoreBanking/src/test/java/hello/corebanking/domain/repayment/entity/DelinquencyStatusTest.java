package hello.corebanking.domain.repayment.entity;

import hello.corebanking.global.exception.InvalidEnumIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class DelinquencyStatusTest {

    static Stream<Arguments> validIds() {
        return Stream.of(
                arguments(1, DelinquencyStatus.ACTIVE),
                arguments(2, DelinquencyStatus.CURED)
        );
    }

    @ParameterizedTest(name = "id={0} → {1}")
    @MethodSource("validIds")
    @DisplayName("유효한 id로 fromId() 호출 시 올바른 Enum을 반환한다")
    void fromId_validId(int id, DelinquencyStatus expected) {
        assertThat(DelinquencyStatus.fromId(id)).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{0}.getId() = {1}")
    @MethodSource("validIds")
    @DisplayName("각 Enum 상수의 getId()가 DB id와 일치한다")
    void getId(int expectedId, DelinquencyStatus status) {
        assertThat(status.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("존재하지 않는 id로 fromId() 호출 시 InvalidEnumIdException을 던진다")
    void fromId_invalidId() {
        assertThatThrownBy(() -> DelinquencyStatus.fromId(99))
                .isInstanceOf(InvalidEnumIdException.class)
                .hasMessageContaining("99");
    }
}
