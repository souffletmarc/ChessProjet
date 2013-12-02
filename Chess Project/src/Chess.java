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
		// boolean which is equal to true if there is a change in the game
		boolean change = false;
		// Check the value of the variable inPlay
		if (inPlay && checkPiece(x, y) != -1) {
			if (checkPiece(x , y) == 0) {
				// Check all adjacent cells
				for (int x2 = (x - 1); x2 <= (x + 1); x2++) {
					if (checkPiece(x2, 1) != -1) {
						for (int y2 = (y - 1); y2 <= (y + 1); y2++) {
							if (checkPiece(1, y2) != -1) {
								// If the value is different to 0 and different to current_player then the reverseChainAvailable is called
								if ((x2 != x || y2 != y) && checkPiece(x2 , y2) != 0 && checkPiece(x2 , y2) != player) {
									if (reverseChainAvailable(x2 - x, y2 - y, player)) {
										// Update the variable change
										change = true;
										// Set player position
										board[x][y] = player;
										// Call the reversePieces method
										reversePieces(x, y, x2 - x, y2 - y, player);
									}
								}
							}
						}
					}
				}
				//If there is the change in the game, we swap the player, we update the score and we repaint the board
				if (change == true)
				{
					swapPlayers();
					updatePlayerScores();
					paintComponent(getGraphics());
					// Display who must play
					System.out.println("It's player " + current_player + "'s turn");
					// If it's the game is over we set the variable inPlay to false display the score and the winner.
					if (determineEndGame()) 
					{
						inPlay = false;
						System.out.println("Player 1's score  : "+ player_1_score +"    Player 2's score : " + player_2_score);
						if (player_1_score > player_2_score)
							System.out.println("Player 1 win !");
						else
							System.out.println("Player 2 win !");
							
					}
				}
			}
		}
			
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
	
	// determines if a valid reverse chain can be made from the position (x, y) in the given direction (dx, dy) and a
	// given player
	private boolean determineChain(int x, int y, int dx, int dy, int player) 
	{
		x+= dx;
		y+= dy;
		//Check if the value is different to the current_player and to 0
		if ((checkPiece(x , y) != -1) && checkPiece(x , y) != player &&  checkPiece(x , y) != 0)
		{
			// Check the chain until we find a piece of the current player 
			while (checkPiece(x, y) != -1 && checkPiece(x, y) != 0)
			{
				if (checkPiece(x, y) == player)
					return true;
				x +=dx;
				y +=dy;
			}
		}
		return false;
	}
	
	// determines if an end game state has been reached. this will happen if there are zero spaces on the board, if one
	// player has lost all of their pieces, or there are no valid moves left for either player
	private boolean determineEndGame() 
	{
		
		boolean player1 = false;
		boolean player2 = false;
		boolean free = false;
		boolean playerPlay = false;
		
		// Scan the board to check if the game is over
		for (int x = 0; x < 8; x++)
		{
			for (int y = 0; y < 8; y++)
			{
				if (checkPiece(x , y) == 0)
				{
					for (int x1 = x - 1; x1 < x + 2; x1++)
					{
						for (int y1 = y - 1; y1 < y + 2; y1++)
						{
							//If there are moves available for the player then return true
							if ((checkPiece(x1, y1) != -1) && (x1 != x || y1 != y) && (checkPiece(x1 , y1) != 0 && checkPiece(x1 , y1) != current_player))
							{
								oldx = x;
								oldy = y;
								if (reverseChainAvailable(x1 - x, y1 - y, current_player))
									playerPlay = true;
							}
						}
					}
					// If there is a free space in the board we set the variable to true
					free = true;
				}
				//If either player has lost all their pieces then return true
				else if (checkPiece(x , y) == 1)
					player1 = true;
				else if (checkPiece(x , y) == 2)
					player2 = true;
				// If there is free space, black piece, white piece on the board and moves available for the player then is'nt the end of game 
				if (player1 == true && player2 == true && free == true && playerPlay == true)
					return false;
			}
		}
		// In the other case it's the end of game, so we display the game over
		System.out.println("\n-----GAME OVER-----");
		if (player1 == false)
				System.out.println("Player 1 has lost all his piece");
		if (player2 == false)
				System.out.println("Player 2 has lost all his piece");
		if (playerPlay == false)
			System.out.println("The player "+ current_player +" hasn't available move");
		return true;
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
	
	// given a position (x, y) and a player this will determine if there is a valid move to be made at the given
	// position by checking for the availability of a reverse chain in any direction
	private boolean reverseChainAvailable(int x, int y, int player) 
	{
		//Check each of the adjacent 8 cells to see if there is a chain available by using determineChain method
		return determineChain(oldx, oldy, x, y, player);	
	}	
	
	// given a position (x, y), direction (dx, dy) and a player this will reverse all opponents pieces in a given
	// direction. NOTE: this assumes that determineChain has been used first. method does not perform checks
	private void reversePieces(int x, int y, int dx, int dy, int player) 
	{
		x+= dx;
		y+= dy;	
		while (checkPiece(x , y) != player)
		{
				board[x][y] = player;
				x+=dx;
				y+=dy;
		}
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
		player_1_score = 0;
		player_2_score = 0;

		// Computation of all the piece black and white and update the score
		for (int x = 0; x < 8; x++)
		{
			for (int y = 0; y < 8; y++)
			{
				if (checkPiece(x , y) == 1)
					player_1_score++;
				else if (checkPiece(x , y) == 2)
					player_2_score++;
			}
		}
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