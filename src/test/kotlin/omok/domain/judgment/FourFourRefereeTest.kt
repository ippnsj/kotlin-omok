package omok.domain.judgment

import omok.domain.board.Board
import omok.domain.board.Column
import omok.domain.board.Position
import omok.domain.board.Row
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class FourFourRefereeTest {
    @ParameterizedTest
    @CsvSource("C, THIRTEEN", "C, EIGHT", "F, TWELVE", "H, FIVE", "I, EIGHT", "J, TEN")
    fun `4-4이면 금지된 수이다`(column: Column, row: Row) {
        val board = Board(FOUR_FOUR_BOARD)
        val referee = FourFourReferee()
        val position = Position(column, row)

        Assertions.assertThat(referee.isForbiddenPlacement(board.positions, position)).isTrue
    }

    @ParameterizedTest
    @CsvSource("E, TWELVE", "H, TWELVE", "J, SEVEN", "H, THREE", "I, SEVEN", "C, NINE")
    fun `4-4가 아니면 금지된 수가 아니다`(column: Column, row: Row) {
        val board = Board(FOUR_FOUR_BOARD)
        val referee = FourFourReferee()
        val position = Position(column, row)

        Assertions.assertThat(referee.isForbiddenPlacement(board.positions, position)).isFalse
    }

    @Test
    fun `4-4-3이면 금지된 수이다`() {
        val board = Board(FOUR_FOUR_THREE_BOARD)
        val referee = FourFourReferee()
        val position = Position(Column.C, Row.THIRTEEN)

        Assertions.assertThat(referee.isForbiddenPlacement(board.positions, position)).isTrue
    }
}
