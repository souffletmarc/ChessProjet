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

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** classes **/

public class Chess extends JFrame {
	
	/** constructors **/
	public Chess() {

		// set size of the windows
		setSize(640, 640);
		// set the title of the window
		setTitle("Chess");
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
		// Call the drawBoard method
		drawBoard(g2d);
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
	
	// checks if there is a piece in a given position. returns 0 if empty, -1 if out of bounds, 1 for player 1, and
	// 2 for player 2
	private int checkPiece(int x, int y) 
	{
		if (x < 0 || x > 7 || y < 0 || y > 7)
		{
			return -1;
		}	
		return board[x][y];
	}
	
	
	private boolean determineChain(int x, int y, int dx, int dy, int player) 
	{
		return false;
	}
	
	private boolean determineEndGame() 
	{
		
	
		return true;
	}
	
	private void drawBoard(Graphics2D g2d) 
	{
		int		sizeCellX = getWidth() / 8;
		int		sizeCellY = getHeight() / 8;
		for (int x = 0; x < 8; x++)
		{
			
			for (int y = 0; y < 8; y++)
			{
				if (x % 2 == 0 && y % 2 == 0)
				{
					g2d.setColor(white);
					g2d.fillRect(x * sizeCellX, y * sizeCellY, sizeCellX, sizeCellY);
				}
				else if (x % 2 == 0 && y != 0)
				{
					g2d.setColor(black);
					g2d.fillRect(x * sizeCellX, y * sizeCellY, sizeCellX, sizeCellY);
				}
				else if (x % 2 != 0 && y % 2 == 0)
				{
					g2d.setColor(black);
					g2d.fillRect(x * sizeCellX, y * sizeCellY, sizeCellX, sizeCellY);
				}
				else
				{
					g2d.setColor(white);
					g2d.fillRect(x * sizeCellX, y * sizeCellY, sizeCellX, sizeCellY);
				}
			}
		}
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
				if (checkPiece(x , y) == 1) {
					// Draw the white piece
					g2d.setColor(white);
					g2d.fillOval((sizeCellX * x) + ((sizeCellX - sizeX) / 2), (sizeCellY * y) + ((sizeCellY - sizeY) / 2), sizeX, sizeY);
				}
				else if (checkPiece(x , y) == 2) {
					// Draw the black piece
					g2d.setColor(black);
					g2d.fillOval((sizeCellX * x) + ((sizeCellX - sizeX) / 2), (sizeCellY * y) + ((sizeCellY - sizeY) / 2), sizeX, sizeY);
				}
			}
		}
	}
	
	// will initialise the game board to the starting state
	private void initialState() 
	{
		// Initialise all cells in the array to have a value of zero
		board = new int[8][8];
		for (int x = 0; x < 8; x++)
		{
			for (int y = 0; y < 8; y++)
			{
				board[x][y] = 0;
			}
		}
		
		// set the middle 4 cells
		board[3][3] = 1;
		board[4][3] = 2;
		board[3][4] = 2;
		board[4][4] = 1;
		//set the scores for both players
		player_1_score = 2;
		player_2_score = 2;
		//set the game to be in play 
		inPlay = true;
		// set the current player
		current_player = 1;
		
		
		
	}
	
	
	private boolean reverseChainAvailable(int x, int y, int player) 
	{
		return false;
	}	
	
	private void reversePieces(int x, int y, int dx, int dy, int player) 
	{
		
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
		// Display the score
		System.out.println("Player 1's score  : "+ player_1_score +"    Player 2's score : " + player_2_score);
	}
	
	/** private fields **/
	int board[][];						// the state of the game board
	int oldx, oldy;						// denotes where the player clicked when he pressed the mouse button
	int current_player;					// denotes who the current player is
	int player_1_score, player_2_score;	// denotes the score each player has in the game thus far
	boolean inPlay;						// indicates if the game is being played at the moment
	Color black, cyan, white;			// color objects that represent their named colours
}