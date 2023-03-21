package woowacourse.omok

import android.os.Bundle
import android.util.Log
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import omok.domain.Turn
import omok.domain.board.Board
import omok.domain.board.Position
import omok.domain.player.Black
import omok.domain.player.White

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val domainBoard = Board()
        val domainTurn = Turn(setOf(Black, White))

        val board = findViewById<TableLayout>(R.id.board)
        board
            .children
            .filterIsInstance<TableRow>()
            .flatMap { it.children }
            .filterIsInstance<ImageView>()
            .forEachIndexed { index, view ->
                view.setOnClickListener(takeTurn(index, domainBoard, domainTurn, view))
            }
    }

    private fun takeTurn(index: Int, board: Board, turn: Turn, view: ImageView) = OnClickListener {
        val position = getPosition(index)
        Log.d("test_position", position.toString())
        Log.d("test_turn", turn.now.javaClass.simpleName.toString())
        placeStone(board, turn, position, view)
        turn.changeTurn()
    }

    private fun getPosition(index: Int): Position {
        val columnAxis = index % 15
        val rowAxis = 15 - (index / 15) - 1
        return Position(columnAxis, rowAxis)
    }

    private fun placeStone(board: Board, turn: Turn, position: Position, view: ImageView) {
        when (turn.now) {
            Black -> view.setImageResource(R.drawable.black_stone)
            White -> view.setImageResource(R.drawable.white_stone)
        }
    }
}
