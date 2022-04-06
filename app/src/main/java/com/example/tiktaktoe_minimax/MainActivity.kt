package com.example.tiktaktoe_minimax

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
/*init var*/
private lateinit var x: GridLayout
private lateinit var y: Button
private lateinit var z:TextView
class MainActivity : AppCompatActivity() {
    //Creating a 2D Array of ImageViews
    private val boardCells = Array(3) { arrayOfNulls<ImageView>(3) }
    //creating the board instance
    var board = Board()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*init var*/
        x = findViewById(R.id.layout_board)
        y = findViewById(R.id.button_restart)
        z = findViewById(R.id.text_view_result)
        //calling the function to load our tic tac toe board
        loadBoard()
        //restart functionality
        y.setOnClickListener {
            //creating a new board instance
            //it will empty every cell
            board = Board()

            //setting the result to empty
            z.text = ""

            //this function will map the internal board
            //to the visual board
            mapBoardToUi()
        }

    }

    //function is mapping
    //the internal board to the ImageView array board
    private fun mapBoardToUi() {
        for (i in board.board.indices) {
            for (j in board.board.indices) {
                when (board.board[i][j]) {
                    Board.PLAYER -> {
                        boardCells[i][j]?.setImageResource(R.drawable.circle)
                        boardCells[i][j]?.isEnabled = false
                    }
                    Board.COMPUTER -> {
                        boardCells[i][j]?.setImageResource(R.drawable.cross)
                        boardCells[i][j]?.isEnabled = false
                    }
                    else -> {
                        boardCells[i][j]?.setImageResource(0)
                        boardCells[i][j]?.isEnabled = true
                    }
                }
            }
        }
    }


    /*
    * This function is generating the tic tac toe board
    * */
    private fun loadBoard() {

        for (i in boardCells.indices) {
            for (j in boardCells.indices) {
                boardCells[i][j] = ImageView(this)
                boardCells[i][j]?.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = 250
                    height = 230
                    bottomMargin = 5
                    topMargin = 5
                    leftMargin = 5
                    rightMargin = 5
                }
                boardCells[i][j]?.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                //attached a click listener to the board
                boardCells[i][j]?.setOnClickListener(CellClickListener(i, j))
                x.addView(boardCells[i][j])
            }
        }
    }
    /*definie inner class that will impliment the onclicklistner*/
    inner class CellClickListener(
        /*i and j to know index of cell*/
        val i: Int,
        val j: Int
    ) : View.OnClickListener {

        override fun onClick(p0: View?) {
            //here we will code the move
            //checking if the game is not over
            if (!board.isGameOver) {

                //creating a new cell with the clicked index
                val cell = Cell(i, j)

                //placing the move for player
                board.placeMove(cell, Board.PLAYER)

                //calling minimax to calculate the computers move
                board.minimax(0, Board.COMPUTER)

                //performing the move for computer
                board.computersMove?.let {
                    board.placeMove(it, Board.COMPUTER)
                }

                //mapping the internal board to visual board
                mapBoardToUi()
            }

            //Displaying the results
            //according to the game status
            when {
                board.hasComputerWon() -> z.text = "Computer Won"
                board.hasPlayerWon() -> z.text = "Player Won"
                board.isGameOver -> z.text = "Game Tied"
            }

        }

    }


}