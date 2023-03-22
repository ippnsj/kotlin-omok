package woowacourse.omok

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import woowacourse.omok.db.OmokDBHelper
import woowacourse.omok.db.PlayerContract

class StartActivity : AppCompatActivity() {
    private lateinit var omokDB: SQLiteDatabase
    private lateinit var nicknameView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        omokDB = OmokDBHelper(this).writableDatabase
        nicknameView = findViewById(R.id.nickname_input)

        val startButton = findViewById<Button>(R.id.start_game)
        startButton.setOnClickListener {
            if (validateNickname()) startGame()
        }
    }

    private fun validateNickname(): Boolean {
        if (nicknameView.length() in 1..4) return true
        Toast.makeText(this, "닉네임을 4자 이하로 입력해 주세요", Toast.LENGTH_SHORT)
            .show()
        return false
    }

    private fun startGame() {
        val nickname = nicknameView.text.toString()
        if (!hasNickname(nickname)) return startNewGame(nickname)
        showStartDialog(nickname)
    }

    private fun hasNickname(nickname: String): Boolean {
        val cursor = omokDB.query(
            PlayerContract.TABLE_NAME,
            arrayOf(PlayerContract.COLUMN_NICKNAME),
            "${PlayerContract.COLUMN_NICKNAME} = ?",
            arrayOf(nickname),
            null,
            null,
            null
        )

        val count = cursor.count
        cursor.close()
        return count != 0
    }

    private fun startNewGame(nickname: String) {
        createPlayer(nickname)
        goToGame(nickname)
    }

    private fun createPlayer(nickname: String) {
        val values = ContentValues()
        values.put(PlayerContract.COLUMN_NICKNAME, nickname)

        omokDB.insert(PlayerContract.TABLE_NAME, null, values)
    }

    private fun goToGame(nickname: String) {
        val intent = Intent(this, MainActivity::class.java)
        getSharedPreferences("Omok", MODE_PRIVATE)
            .edit()
            .putString("nickname", nickname)
            .apply()
        startActivity(intent)
    }

    private fun showStartDialog(nickname: String) {
        val builder = AlertDialog.Builder(this)
        builder.setIcon(R.drawable.glo_icon)
        builder.setTitle("이미 게임이 존재합니다")
        builder.setPositiveButton("이어하기") { _, _ -> goToGame(nickname) }
        builder.setNegativeButton(
            "새로하기"
        ) { _, _ ->
            deletePlayer(nickname)
            startNewGame(nickname)
        }
        builder.show()
    }

    private fun deletePlayer(nickname: String) {
        val condition = "${PlayerContract.COLUMN_NICKNAME} = ?"
        omokDB.delete(PlayerContract.TABLE_NAME, condition, arrayOf(nickname))
    }

    override fun onDestroy() {
        super.onDestroy()
        omokDB.close()
    }
}
