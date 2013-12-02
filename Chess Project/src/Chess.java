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
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;


/** classes **/

public class Chess extends JFrame {
	
	/** constructors 
	 * @throws IOException **/
	public Chess() throws IOException {

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
	
	/** public functions 
	 * @throws IOException **/
	public static void main(String[] args) throws IOException 
	{
		Chess  chess = new Chess();
		// Display the windows
		chess.setVisible(true);

	}
	
	/** private fields **/
	ReversiWidget widget;	// where the game is being played
}

class ReversiWidget extends JComponent implements MouseListener {
	
	/** constructors 
	 * @throws IOException **/
	public ReversiWidget() throws IOException {
		
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
//				attemptMove(oldx, oldy, current_player);
				if (selected == null)
				{
				redrawClick(getGraphics(), newx, newy);
				selected = new Tuple<Integer, Integer>(newx, newy);
				}
				else if (selected.player == newx && selected.piece == newy)
				{
					paintComponent(getGraphics());
					selected = null;
				}
				else
				{
					//Try move
				}
				
			}
		}
	}
	
	public void redrawClick(Graphics g2d, int x, int y)
	{
		int		sizeCellX = (getWidth() / 8);
		int		sizeCellY = (getHeight() / 8);
		g2d.setColor(Color.gray);
		g2d.fillRect(x * sizeCellX, y * sizeCellY, sizeCellX, sizeCellY);
		drawPieces((Graphics2D)g2d);
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
	
	
	// determines if an end game state has been reached. this will happen if there are zero spaces on the board, if one
	// player has lost all of their pieces, or there are no valid moves left for either player
	private boolean determineEndGame() 
	{
		return (true);
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
		// Computation the size of the cells
		int		sizeCellX = (getWidth() / 8) + 2;
		int		sizeCellY = (getHeight() / 8) + 2;

		
		for (int x = 0; x < 8; x++)
		{
			for (int y = 0; y < 8; y++)
			{
				g2d.drawImage(getPieceImage(board[x][y]), x * sizeCellX, y * sizeCellY, null);
			}
		}
	}
	
	
	private BufferedImage getPieceImage(Tuple<Integer, Integer> t) {	    
	    for (Entry<Tuple<Integer, Integer>, BufferedImage> entry : images.entrySet())
	    {
	    	if (entry.getKey().player == t.player && entry.getKey().piece == t.piece) {
	    		return entry.getValue();
	    	}
	    }
	    return null;
	}
	
	// will initialise the game board to the starting state
	private void initialState() throws IOException 
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
		//set images
		images = new Hashtable<Tuple<Integer, Integer>, BufferedImage>();
		images.put(new Tuple<Integer, Integer>(1, 1), ImageIO.read(new File("src/img/pawn_w.png")));
		images.put(new Tuple<Integer, Integer>(1, 2), ImageIO.read(new File("src/img/rook_w.png")));
		images.put(new Tuple<Integer, Integer>(1, 3), ImageIO.read(new File("src/img/knight_w.png")));
		images.put(new Tuple<Integer, Integer>(1, 4), ImageIO.read(new File("src/img/bishop_w.png")));
		images.put(new Tuple<Integer, Integer>(1, 5), ImageIO.read(new File("src/img/king_w.png")));
		images.put(new Tuple<Integer, Integer>(1, 6), ImageIO.read(new File("src/img/queen_w.png")));
		
		images.put(new Tuple<Integer, Integer>(2, 1), ImageIO.read(new File("src/img/pawn_b.png")));
		images.put(new Tuple<Integer, Integer>(2, 2), ImageIO.read(new File("src/img/rook_b.png")));
		images.put(new Tuple<Integer, Integer>(2, 3), ImageIO.read(new File("src/img/knight_b.png")));
		images.put(new Tuple<Integer, Integer>(2, 4), ImageIO.read(new File("src/img/bishop_b.png")));
		images.put(new Tuple<Integer, Integer>(2, 5), ImageIO.read(new File("src/img/king_b.png")));
		images.put(new Tuple<Integer, Integer>(2, 6), ImageIO.read(new File("src/img/queen_b.png")));
		// set the player 1 slot
		board[0][0].player = 1;
		board[0][0].piece = 2;
		board[1][0].player = 1;
		board[1][0].piece = 3;
		board[2][0].player = 1;
		board[2][0].piece = 4;
		board[3][0].player = 1;
		board[3][0].piece = 5;
		board[4][0].player = 1;
		board[4][0].piece = 6;
		board[5][0].player = 1;
		board[5][0].piece = 4;
		board[6][0].player = 1;
		board[6][0].piece = 3;
		board[7][0].player = 1;
		board[7][0].piece = 2;
		
		board[0][1].player = 1;
		board[0][1].piece = 1;
		board[1][1].player = 1;
		board[1][1].piece = 1;
		board[2][1].player = 1;
		board[2][1].piece = 1;
		board[3][1].player = 1;
		board[3][1].piece = 1;
		board[4][1].player = 1;
		board[4][1].piece = 1;
		board[5][1].player = 1;
		board[5][1].piece = 1;
		board[6][1].player = 1;
		board[6][1].piece = 1;
		board[7][1].player = 1;
		board[7][1].piece = 1;

		// PLayer 2
		board[0][7].player = 2;
		board[0][7].piece = 2;
		board[1][7].player = 2;
		board[1][7].piece = 3;
		board[2][7].player = 2;
		board[2][7].piece = 4;
		board[3][7].player = 2;
		board[3][7].piece = 5;
		board[4][7].player = 2;
		board[4][7].piece = 6;
		board[5][7].player = 2;
		board[5][7].piece = 4;
		board[6][7].player = 2;
		board[6][7].piece = 3;
		board[7][7].player = 2;
		board[7][7].piece = 2;
		
		board[0][6].player = 2;
		board[0][6].piece = 1;
		board[1][6].player = 2;
		board[1][6].piece = 1;
		board[2][6].player = 2;
		board[2][6].piece = 1;
		board[3][6].player = 2;
		board[3][6].piece = 1;
		board[4][6].player = 2;
		board[4][6].piece = 1;
		board[5][6].player = 2;
		board[5][6].piece = 1;
		board[6][6].player = 2;
		board[6][6].piece = 1;
		board[7][6].player = 2;
		board[7][6].piece = 1;

		

		//set the scores for both players
		player_1_score = 2;
		player_2_score = 2;
		//set the game to be in play 
		inPlay = true;
		// set the current player
		current_player = 1;
		// set the selected bolean
		selected = null;
		
		
		
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

	/** private fields **/
	Hashtable<Tuple<Integer, Integer>, BufferedImage> images;
	Tuple<Integer, Integer> board[][];						// the state of the game board
	int oldx, oldy;						// denotes where the player clicked when he pressed the mouse button
	int current_player;					// denotes who the current player is
	int player_1_score, player_2_score;	// denotes the score each player has in the game thus far
	boolean inPlay;						// indicates if the game is being played at the moment
	Color black, cyan, white;			// color objects that represent their named colours
	Tuple<Integer, Integer> selected;
	
}