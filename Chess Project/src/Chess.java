/*
 * Chess Project
 * 
 * Student name : Marquis Cyntia
 * Student number : 2854111
 * Student name : Soufflet Marc
 * Student number : 2854113
 */


/** includes **/
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** classes **/

public class Chess extends JFrame {
	
	/** constructors **/
	public Chess() {

		// set size of the windows
		setSize(655, 675);
		// set the title of the window
		setTitle("Reversi");
		// set the default close operation
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// make a panel and add to the content pane
		widget = new ReversiWidget();
		getContentPane().add(widget);
	}
	
	/** public functions **/
	public static void main(String[] args) 
	{
		Chess  chess = new Chess();
		// Display the windows
		chess.setVisible(true);

	}
	
	/** private fields **/
	ReversiWidget widget;	// where the game is being played
}

class ReversiWidget extends JComponent implements MouseListener {
	
	/** constructors **/
	public ReversiWidget() {
		
		// Set RGB color for black, cyan et white
		black = Color.BLACK;
		cyan = Color.CYAN;
		white = Color.WHITE;
		// Call the initialState method
		initialState();
		// Adding a mouse listener to the widget
		this.addMouseListener(this);
	}
	
	/** public functions **/
	
	// method required by MouseListener. not used
	public void mouseClicked(MouseEvent event) {
		
	}
	
	// method required by MouseListener. not used
	public void mouseEntered(MouseEvent event) {
		
	}
	
	// method required by MouseListener. not used
	public void mouseExited(MouseEvent event) {
		
	}
	
	// will react to mouse press events on the widget
	public void mousePressed(MouseEvent event) 
	{
		
		// Save the x and y position when the left mouse button is pressed and convert the coordinate to indexes of the array
		if (event.getButton() == MouseEvent.BUTTON1) 
		{
			// Calculate which box was clicked from coordinate
			oldx = (event.getX() / (getWidth() / 8) > 7) ? 7 : event.getX() / (getWidth() / 8);
			oldy = (event.getY() / (getHeight() / 8) > 7) ? 7 : event.getY() / (getHeight() / 8);
		}
		
	}
	
	// will react to mouse release events on the widget
	public void mouseReleased(MouseEvent event) {
		
		// Save the x and y position when the left mouse button is released and convert the coordinate to indexes of the array
		if (event.getButton() == MouseEvent.BUTTON1) 
		{
			// Calculate which box was clicked from coordinate
			int newx = (event.getX() / (getWidth() / 8) > 7) ? 7 : event.getX() / (getWidth() / 8);
			int newy = (event.getY() / (getHeight() / 8) > 7) ? 7 : event.getY() / (getHeight() / 8);
			//if newx, newy match oldx, oldy a move should be attempted in that position
			if (newx == oldx && newy == oldy) 
			{
				// Call the attempMove method
				attemptMove(oldx, oldy, current_player);
			}
		}
	}
	
	// repaints the widget when an update of any kind is made
	public void paintComponent(Graphics g) 
	{
		// Creation of 2D graphics object
		Graphics2D g2d = (Graphics2D) g;
		// Set a color of background
		g2d.setColor(cyan);
		// Draw the background
		g2d.fillRect(0, 0, getWidth(), getHeight());
		// Call the drawGrid method
		drawGrid(g2d);
		// Call the drawPieces method
		drawPieces(g2d);
	}
	
	/** private functions **/
	
	// will take in a position (x,y) a player and will attempt to make a move. if successful then it will place the
	// piece and update the game board.
	private void attemptMove(int x, int y, int player) {

	}	
	
	// determines if an end game state has been reached. this will happen if there are zero spaces on the board, if one
	// player has lost all of their pieces, or there are no valid moves left for either player
	private boolean determineEndGame() 
	{
		return (true);
	}
	
	// will draw the grid for the game. this assumes a 640 by 640 grid
	private void drawGrid(Graphics2D g2d) 
	{
		// set the color of the line
		g2d.setColor(this.black);
		// Draw lines for do the edge of the board
		g2d.drawLine(0, 0, 0, getHeight());
		g2d.drawLine(0, 0, getWidth(), 0);
		g2d.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
		g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
		
		// Draw line for do the grid
		for (int x = 0; x < 8; x++)
		{
			for (int y = 0; y < 8; y++)
			{
				//Draw horizontal lines
				g2d.drawLine(0, y * (getHeight() / 8), getWidth(), y * (getHeight() / 8));
			}
			//Draw vertical lines
			g2d.drawLine(x * (getWidth() / 8), 0, x * (getWidth() / 8), getHeight());
		}		
	}
	
	// will draw the pieces that are currently on the board. assumes a widget size of 640 square
	private void drawPieces(Graphics2D g2d) {
		
		// Set a coefficient to determine the size of the piece
		double	coef = 0.9;
		// Computation the size of the cells
		int		sizeCellX = getWidth() / 8;
		int		sizeCellY = getHeight() / 8;
		// Computation the size of the piece
		int		sizeX = (int)(sizeCellX * coef);
		int 	sizeY = (int)(sizeCellY * coef);
		
		for (int x = 0; x < 8; x++)
		{
			for (int y = 0; y < 8; y++)
			{
				
			}
		}
	}
	
	// will initialise the game board to the starting state
	private void initialState() 
	{
		// Initialise all cells in the array to have a value of zero
		board = new Tuple[8][8];
		for (int x = 0; x < 8; x++)
		{
			for (int y = 0; y < 8; y++)
			{
				board[x][y] = new Tuple<Integer, Integer>(0, 0);
			}
		}
		// set the player 1 slot
		board[0][0].player = 1;
		board[0][0].piece = 2;
		board[0][1].player = 1;
		board[0][1].piece = 3;
		board[0][2].player = 1;
		board[0][2].piece = 4;
		board[0][3].player = 1;
		board[0][3].piece = 6;
		board[0][4].player = 1;
		board[0][4].piece = 5;
		board[0][5].player = 1;
		board[0][5].piece = 4;
		board[0][6].player = 1;
		board[0][6].piece = 3;
		board[0][7].player = 1;
		board[0][7].piece = 2;
		
		board[1][0].player = 1;
		board[1][0].piece = 1;
		board[1][1].player = 1;
		board[1][1].piece = 1;
		board[1][2].player = 1;
		board[1][2].piece = 1;
		board[1][3].player = 1;
		board[1][3].piece = 1;
		board[1][4].player = 1;
		board[1][4].piece = 1;
		board[1][5].player = 1;
		board[1][5].piece = 1;
		board[1][6].player = 1;
		board[1][6].piece = 1;
		board[1][7].player = 1;
		board[1][7].piece = 1;

		// PLayer 2
		board[7][0].player = 2;
		board[7][0].piece = 2;
		board[7][1].player = 2;
		board[7][1].piece = 3;
		board[7][2].player = 2;
		board[7][2].piece = 4;
		board[7][3].player = 2;
		board[7][3].piece = 6;
		board[7][4].player = 2;
		board[7][4].piece = 5;
		board[7][5].player = 2;
		board[7][5].piece = 4;
		board[7][6].player = 2;
		board[7][6].piece = 3;
		board[7][7].player = 2;
		board[7][7].piece = 2;
	
		board[6][0].player = 2;
		board[6][0].piece = 1;
		board[6][1].player = 2;
		board[6][1].piece = 1;
		board[6][2].player = 2;
		board[6][2].piece = 1;
		board[6][3].player = 2;
		board[6][3].piece = 1;
		board[6][4].player = 2;
		board[6][4].piece = 1;
		board[6][5].player = 2;
		board[6][5].piece = 1;
		board[6][6].player = 2;
		board[6][6].piece = 1;
		board[6][7].player = 2;
		board[6][7].piece = 1;

		

		//set the scores for both players
		player_1_score = 2;
		player_2_score = 2;
		//set the game to be in play 
		inPlay = true;
		// set the current player
		current_player = 1;
		
		
		
	}
	
	// called at the end of a valid turn this will swap the current players
	private void swapPlayers() 
	{
		// If current player is player 1 then it's player's 2 turn and vice versa
		if (current_player == 1)
			current_player = 2;
		else
			current_player = 1;
	}
	
	// updates the player scores after a piece has been placed
	private void updatePlayerScores() 
	{	

	}
	
	/** private fields **/
	Tuple<Integer, Integer> board[][];						// the state of the game board
	int oldx, oldy;						// denotes where the player clicked when he pressed the mouse button
	int current_player;					// denotes who the current player is
	int player_1_score, player_2_score;	// denotes the score each player has in the game thus far
	boolean inPlay;						// indicates if the game is being played at the moment
	Color black, cyan, white;			// color objects that represent their named colours
}