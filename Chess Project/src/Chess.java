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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
		brown = new Color(140, 83, 46);
		lightBrown = new Color(236, 185, 106);
		colorSelected = new Color(255, 255, 255, 127);
		black = Color.black;
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
	
	// This method will return the ID of the other player (current player's ennemy)
	public int getOtherPlayer()
	{
		if (current_player == 1)
			return 2;
		else
			return 1;
	}
	// This method will check if the unit (pion type) can attack then return a boolean, true if pion can attack, else false 
	public boolean checkAttackPion(int x, int y) {
		int depX = x - selected.player; // Number of move to do on X axes
		int depY = y - selected.piece; // Number of move to do on X axes

		//Check the available square where pion can attack and check for each square if the asked movement is possible
		if ((depX == 1 || depX == -1) && ((depY == -1 && current_player == 1) || (depY == 1 && current_player == 2)) && board[x][y].player == getOtherPlayer()) {
			return true;
		}
		
		return false;
	}
	// This method will check if the unit (pion type) can move then return a boolean, true if pion can move, else false 
	public boolean checkMovePion(int x, int y) {
		int depX = x - selected.player; // Number of move to do on X axes
		int depY = y - selected.piece; // Number of move to do on X axes

		//Check the available square where pion can move and check for each square if the asked movement is possible
		if (depX == 0 && ((depY == -1 && current_player == 1) || (depY == 1 && current_player == 2)) && board[x][y].player == 0)
			return true;
		else if (((current_player == 1 && depX == 0 && depY == -2 && selected.piece == 6) || (current_player == 2 && depX == 0 && depY == 2 && selected.piece == 1)) && board[x][y].player == 0)
			return true;
		return false;
	}
	// This method will check if the unit (knight type) can move then return a boolean, true if knight can move, else false 
	public boolean checkMoveKnight(int x, int y) {
		int depX = x - selected.player;
		int depY = y - selected.piece;

		//Check the available square where knight can move of attack and check for each square if the asked movement is possible
		if (((depX == 2 && depY == 1) || (depX == -2 && depY == 1) ||
				(depX == 2 && depY == -1) || (depX == -2 && depY == -1) ||
				(depX == 1 && depY == 2) || (depX == -1 && depY == 2) ||
				(depX == -1 && depY == -2) || (depX == 1 && depY == -2)) && board[x][y].player != current_player) {
			return true;
		}
		return false;
	}
	// This method will check if the unit (king) can move then return a boolean, true if king can move, else false 
	public boolean checkMoveKing(int x, int y) {
		int depX = x - selected.player;
		int depY = y - selected.piece;

		//Check the available square where knight can move of attack and check for each square if the asked movement is possible
		if (((depX == 0 && depY == 1) || (depX == 1 && depY == 1) ||
				(depX == -1 && depY == 1) || (depX == -1 && depY == 0) ||
				(depX == 1 && depY == 0) || (depX == -1 && depY == -1) ||
				(depX == 1 && depY == -1) || (depX == 0 && depY == -1)) && board[x][y].player != current_player) {
			return true;
		}
		return false;
	}
	
	//This method receive the coordinate where current_player want to move and try the move, depending of the selected piece.
	//It will return true if move is possible, else false
	public boolean tryMove(int x, int y) {
		
		// Get the selected piece kind which player selected
		int piece = board[this.selected.player][this.selected.piece].piece;
		
		// If the piece is a pawn then we check if the pawn can move or can attack
		if (piece == 1) {
			if (checkMovePion(x, y) || checkAttackPion(x,y)) {
				//If the piece can attack or can move the method changePion is called
				changePion(x, y);
				return true;
			}
			return false;
		}
		// If the piece is a rook then we check if the rook can move
		else if (piece == 2) {
			if (checkMoveRook(x, y) ) {
				return true;
			}
			return false;
		}
		// If the piece is a knight then we check if the knight can move
		else if (piece == 3) {
			if (checkMoveKnight(x, y)) {
				return true;
			}
			return false;
		}
		// If the piece is a bishop then we check if the bishop can move
		else if (piece == 4) {
			if (checkMoveBishop(x, y)) {
				return true;
			}
			return false;
		}
		// If the piece is a king then we check if the king can move
		else if (piece == 5) {
			if (checkMoveKing(x, y)) {
				return true;
			}
			return false;
		}
		// If the piece is a queen then we check if the queen can move
		else if (piece == 6) {
			if (checkMoveQueen(x, y)) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	public String choiceOfPion()
	{
		// Initialize the list of choice in the input dialog
		Object choices[] = {"Rook", "Knight", "Queen", "Bishop"};
		// Launch an input dialog with list of choice
		String input = (String) JOptionPane.showInputDialog(this, "Choose a pion", "Choice",JOptionPane.PLAIN_MESSAGE, null, choices, "Rook");
		return input;
	}
	
	public void changePion(int x, int y)
	{
		String value = null;
		// If a pawn reaches the opposite end of the board
		if (y == 7 || y == 0)
		{
			// If the player doesn't select a new piece we display a message "You have to choose a new piece" and we launch the input dialog angain
			while (value == null)
			{
				value = choiceOfPion();
				if (value == null)
				{
					JOptionPane.showMessageDialog(this,"You have to choose a new piece", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			// If the return value of the input dialog is a rook we replace the spawn by rook
			if (value == "Rook")
			{
				board[selected.player][selected.piece].piece = 2;
			}
			// If the return value of the input dialog is a Knight we replace the spawn by rook
			else if (value == "Knight")
			{
				board[selected.player][selected.piece].piece = 3;
			}
			// If the return value of the input dialog is a Queen we replace the spawn by rook
			else if (value == "Queen")
			{
				board[selected.player][selected.piece].piece = 6;
			}
			// If the return value of the input dialog is a Bishop we replace the spawn by rook
			else if (value == "Bishop")
			{
				board[selected.player][selected.piece].piece = 4;
			}
		}
	}

	public boolean checkMoveRook(int x, int y)
	{
		// Initialize the first x and first y 
		int depX = x - selected.player;
		int depY = y - selected.piece ;
		// If it's a horizontal or vertical move
		if (this.selected.player == x || this.selected.piece == y)
		{			
			int x1 = 0, y1 = 0, dx = 0, dy = 0;
			if (depX < 0)
			{
				// Check the left move
				x1 = this.selected.player - 1;
				y1 =  this.selected.piece;
				dx = -1;
			}
			else if (depX > 0)
			{
				// Check the right move
				x1 = this.selected.player + 1;
				y1 =  this.selected.piece;
				dx = 1;
			}
			else if (depY < 0)
			{
				// Check the up move
				x1 = this.selected.player;
				y1 =  this.selected.piece - 1;
				dy = -1;
			}
			else if (depY > 0)
			{
				// Check the down move
				x1 = this.selected.player;
				y1 = this.selected.piece + 1;
				dy = 1;
			}
			int x2, y2, move = 0;
			// Check that a rook can move and that there are no other piece in the way of the move
			for (x2 = x1, y2 = y1; move != ((depX != 0) ? depX : depY); x2 += dx, y2 += dy) {
				if (x2 == x && y2 == y && board[x2][y2].player != current_player)
					return true;
				if (board[x2][y2].player != 0)
					return false;
				move += ((depX != 0) ? dx : dy);
				
			}
				return true;
		}
		
			return false;
	}

	public boolean checkMoveBishop(int x, int y)
	{
		// Initialize the first x and first y 
		int depX = x - selected.player;
		int depY = y - selected.piece ;
	
		// Initialize the coordinates of the first move
		int depXAbs = (depX < 0) ? depX * -1 : depX;
		int depYAbs = (depY < 0) ? depY * -1 : depY;
		
		if (depXAbs == depYAbs)
		{
			int x1 = 0, y1 = 0, dx = 0, dy = 0;
			if (depX < 0 && depY < 0)
			{
				// Diagonal Left/Up move
				x1 = this.selected.player - 1;
				y1 =  this.selected.piece - 1;
				dx = -1;
				dy = -1;
			}
			else if (depX > 0 && depY < 0)
			{
				// Diagonal Left/Down move
				x1 = this.selected.player + 1;
				y1 =  this.selected.piece - 1;
				dx = 1;
				dy = -1;
			}
			else if (depY > 0 && depX > 0)
			{
				// Diagonal Right/Up move
				x1 = this.selected.player + 1;
				y1 =  this.selected.piece + 1;
				dy = 1;
				dx = 1;
			}
			else if (depY > 0 && depX < 0)
			{
				// Check Diagonal Left/Up move
				x1 = this.selected.player - 1;
				y1 = this.selected.piece + 1;
				dy = 1;
				dx = -1;
			}
			int x2, y2, move = 0;
			// Check that a bishop can move and that there is no other piece in the way of the move
			for (x2 = x1, y2 = y1; move != depXAbs; x2 += dx, y2 += dy) {
				if (x2 == x && y2 == y && board[x2][y2].player != current_player) {
					return true;
				}
				if (board[x2][y2].player != 0)
					return false;
				move += 1;
				
			}
			return true;
		}
		else
			return false;
	}
	
	public boolean checkMoveQueen(int x, int y)
	{
		// To check the move of the queen we check the move of the Bishop and the move of the Rook because the queen can move like the bishop and the rook
		if (checkMoveBishop(x, y) || checkMoveRook(x, y))
			return true;
		return false;
	}
	
	// will react to mouse release events on the widget
	public void mouseReleased(MouseEvent event) {
	
		// Save the x and y position when the left mouse button is released and convert the coordinate to indexes of the array
		if (event.getButton() == MouseEvent.BUTTON1 && inPlay) 
		{
			// Calculate which box was clicked from coordinate
			int newx = (event.getX() / (getWidth() / 8) > 7) ? 7 : event.getX() / (getWidth() / 8);
			int newy = (event.getY() / (getHeight() / 8) > 7) ? 7 : event.getY() / (getHeight() / 8);
			//if newx, newy match oldx, oldy a move should be attempted in that position
			if (newx == oldx && newy == oldy) 
			{
				// If the piece selected is a piece of the current player
				if (selected == null && board[newx][newy].player == current_player)
				{
					// we draw the selected piece in grey
					redrawClick(getGraphics(), newx, newy);
					selected = new Tuple<Integer, Integer>(newx, newy);
				}
				// If there is a selected piece and the player click on the selected piece
				else if (selected != null && selected.player == newx && selected.piece == newy)
				{
					// We draw the board at the normal state with no piece draw in grey
					paintComponent(getGraphics());
					// Set the variable selected to null
					selected = null;
				}
				// If there is a selected piece
				else if (selected != null)
				{
					// Try the move and if the function tryMove return true we do the move
					if (tryMove(newx, newy) && launchMove(newx, newy)) {
						// We repaint the piece to its new position
						paintComponent(getGraphics());
						// Check if it's the end of the game
						if (determineEndGame())
						{
							// Set the variable inPlay to false
							inPlay = false;
							// Print Game over
							System.out.println("Game over");
						}
						// Set the selected variable to null
						selected = null;
						// Change the player turn
						swapPlayers();
						System.out.println("Player " + getOtherPlayer() + " played.\nPlayer " + current_player + ", you can now play !");
					}
				}
				
			}
		}
	}
	// This function check if piece can be attacked by another piece, the parameter checkEnnemy 
	//dertermine if we have to check the current player can be attacked or the ennemy can be attacked
	// The parameter fromObstruct check if the piece wich can obstruct an attack is a pawn
	public boolean canAttack(int fromX, int fromY, int toX, int toY, boolean checkEnnemy, boolean fromObstruct) {
		// Set the attacked piece
		int piece = board[fromX][fromY].piece;
		
		// Set the threat of the piece in danger
		selected.player = fromX;
		selected.piece = fromY;
		
		// If we check for the ennemy we clear the array of the dangerous piece
		if (!checkEnnemy)
			dangerousPieces.clear();
		
		// If the piece is a pawn then we check if the pawn can move or can attack 
		if (piece == 1) {
			// If fromObstruct equal to true so we check the move of the piece else we check its attack
			if ((fromObstruct == true ? checkMovePion(toX, toY) : checkAttackPion(toX, toY))) {
				// If the pawn can move or attack and checkEnnemy  equal true we add the pawn of the dangerous piece array
				if (!checkEnnemy)
					dangerousPieces.add(new Tuple<Integer, Integer>(fromX, fromY));
				return true;
			}
		}
		// If the piece is a rook then we check if the rook can move
		else if (piece == 2) {
			if (checkMoveRook(toX, toY) ) {
				// If the rook can move and checkEnnemy equal false we add the rook of the dangerous piece array
				if (!checkEnnemy)
					dangerousPieces.add(new Tuple<Integer, Integer>(fromX, fromY));
				return true;
			}
		}
		// If the piece is a knight then we check if the knight can move
		else if (piece == 3) {
			if (checkMoveKnight(toX, toY)){
				// If the knight can move and checkEnnemy equal false we add the knight of the dangerous piece array
				if (!checkEnnemy)
					dangerousPieces.add(new Tuple<Integer, Integer>(fromX, fromY));
				return true;
			}
		}
		// If the piece is a bishop then we check if the bishop can move
		else if (piece == 4) {
			if (checkMoveBishop(toX, toY)){
				// If the bishop can move and checkEnnemy equal false we add the bishop of the dangerous piece array
				if (!checkEnnemy)
					dangerousPieces.add(new Tuple<Integer, Integer>(fromX, fromY));
				return true;
			}
		}
		// If the piece is a king then we check if the king can move
		else if (piece == 5) {
			// Save the piece in danger
			selected.player = toX;
			selected.piece = toY;
			// Check if the king can move and that after the move the king isn't in danger
			if (checkMoveKing(toX, toY) && !pieceInDanger(toX, toY, false, false, false)){
				// If the king can move and checkEnnemy equal false we add the king of the dangerous piece array
				if (!checkEnnemy)
					dangerousPieces.add(new Tuple<Integer, Integer>(fromX, fromY));
				return true;
			}
		}
		// If the piece is a queen then we check if the queen can move
		else if (piece == 6) {
			if (checkMoveQueen(toX, toY)){
				// If the queen can move and checkEnnemy equal false we add the queen of the dangerous piece array
				if (!checkEnnemy)
					dangerousPieces.add(new Tuple<Integer, Integer>(fromX, fromY));
				return true;
			}
		}
		return false;
	}
	
	//This function check if position of given square (px and py) can be attacked. If checkEnnemy is set to true,
	//we will check if square(px/py) can be attacked by the other player. Else we will check if current player can attack the square.
	public boolean pieceInDanger(int px, int py, boolean checkEnnemy, boolean forgetKing, boolean fromObstruct) {
		Tuple<Integer, Integer> os = new Tuple<Integer, Integer>(selected.player, selected.piece); //Save the selected variable because it will be change for the precessing of the function
		if (checkEnnemy) //If checkEnnemy is set to true, we swap players only for this function to check pieces of ennemy player
			swapPlayers();
		//Double loop to check all square on the board
		for (int x = 0; x < 8; ++x) { 
			for (int y = 0; y < 8; ++y) {
				//If there is a piece on the current square which have a current player piece and which can attack on position (px, py) from position (x,y) we could return true
				if ((px != x || py != y) && board[x][y].player == current_player && canAttack(x, y, px, py, checkEnnemy, fromObstruct) && ((forgetKing == true && board[x][y].piece != 5 /*&& board[x][y].player == current_player*/) || (forgetKing == false)))
				{
					//Before returning true we have to init the modified variable to their first value
					if (checkEnnemy)
						swapPlayers();
					selected = os;
					return true;
				}
			}
		}
		//Before returning false we have to init the modified variable to their first value
		if (checkEnnemy)
			swapPlayers();
		selected = os;
		return false;
	}
	
	// This method return a tuple which contain the king position of the player given in parameter
	public Tuple<Integer, Integer> getKingPosition(int player) {
		//We searching on all square of the board the right king
		for (int x = 0; x < 8; ++x) {
			for (int y = 0; y < 8; ++y) {
				//if currentsquare contain the player's king we return its position in a tuple, else we will return null
				if (board[x][y].player == player && board[x][y].piece == 5)
					return new Tuple<Integer, Integer>(x, y);
			}
		}
		return null;
	}

	
	public boolean cantAvoidDangerousPiece() {
		// If the number of piece wich put in check the king are superior of 1 return false because it's impossible for one piece to obstruct two attack
		if (dangerousPieces.size() > 1) {
			return true;
		}
		// Get the dangerous piece
		Tuple<Integer, Integer> dp = dangerousPieces.elementAt(0);
		// Test if the piece is the piece which put the king in check can be attacked or blocked by another piece 
		if (pieceInDanger(dp.player, dp.piece, true, false, false) || !pieceCantObstructAttack(dp)) {
			return false;
		}
		return true;
	}
	
	//Check if some piece can obstruct ennemy piece movement by going on a square of its way (for movement like rook)
	public boolean checkObstructRook(Tuple<Integer, Integer> ennemy/*dangerous piece*/, Tuple<Integer, Integer> kingPos/*king in check by the ennemy*/) {
		// If it's a vertical move
		if (kingPos.player == ennemy.player) {
			if (kingPos.piece > ennemy.piece)
			{
				// Check if the king is in check by a down attack
				for (int y = ennemy.piece; y != kingPos.piece; ++y) {
					// If the ennemy piece can be obstruct by another piece
					if (pieceInDanger(ennemy.player, y, true, true, true)) {
						return true;
					}
				}
			}
			else {
				// Check if the king is in check by a up attack
				for (int y = ennemy.piece; y != kingPos.piece; --y) {
					// If the ennemy piece can be obstruct by another piece
					if (pieceInDanger(ennemy.player, y, true, true, true)) {
						return true;
					}
				}
			}
		}
		// If it's a horizontal move
		else {
			// Check if the king is in check by a left attack
			if (kingPos.player > ennemy.player) {
				for (int x = ennemy.player; x != kingPos.player; ++x) {
					// If the ennemy piece can be obstruct by another piece
					if (pieceInDanger(x, ennemy.piece, true, true, true)) {
						return true;
					}
				}
			}
			else {
				// Check if the king is in check by a right attack
				for (int x = ennemy.player; x != kingPos.player; --x) {
					// If the ennemy piece can be obstruct by another piece
					if (pieceInDanger(x, ennemy.piece, true, true, true)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	//Check if some piece can obstruct ennemy piece movement by going on a square of its way (for movement like bishops)
	public boolean checkObstructBishop(Tuple<Integer, Integer> ennemy, Tuple<Integer, Integer> kingPos) {
		if (kingPos.piece < ennemy.piece) { //Movement from the left
			if (kingPos.player < ennemy.player) //Movement from the top (so top left)
			{
				//Check in the correct direction
				for (int y = ennemy.piece, x = ennemy.player; y != kingPos.piece; --y, --x) {
					// If the player can move some piece to the current piece (to avoid check/checkmate)
					if (pieceInDanger(x, y, true, true, true)) {
						return true;
					}
				}
			}
			else { //Movement from the right (so top right)
				//Check in the correct direction
				for (int y = ennemy.piece, x = ennemy.player; y != kingPos.piece; --y, ++x) {
					// If the player can move some piece to the current piece (to avoid check/checkmate)
					if (pieceInDanger(x, y, true, true, true)) {
						return true;
					}
				}
			}
		}
		else { //Movement from the bottom
			if (kingPos.player < ennemy.player) //Movement from the left (bottom left)
			{
				//Check in the correct direction
				for (int y = ennemy.piece, x = ennemy.player; y != kingPos.piece; ++y, --x) {
					// If the player can move some piece to the current piece (to avoid check/checkmate)
					if (pieceInDanger(x, y, true, true, true)) {
						return true;
					}
				}
			}
			else { //Movement from the right (bottom right)
				//Check in the correct direction
				for (int y = ennemy.piece, x = ennemy.player; y != kingPos.piece; ++y, ++x) {
					// If the player can move some piece to the current piece (to avoid check/checkmate)
					if (pieceInDanger(x, y, true, true, true)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean pieceCantObstructAttack(Tuple<Integer, Integer> ennemy) {
		// Save the position of the piece which put in check the king
		int piece = board[ennemy.player][ennemy.piece].piece;
		// Save the king position of the king in check
		Tuple<Integer, Integer> kingPos = getKingPosition(getOtherPlayer());
		//If the piece is a pawn, knight or king 
		if (piece == 1 || piece == 3 || piece == 5)
			return true;
		//If the dangerous piece is a rook we check the obstruction possible to block a rook attack
		if (piece == 2) {
			return checkObstructRook(ennemy, kingPos);
		}
		//If the dangerous piece is a bishop we check the obstruction possible to block a bishop attack
		else if (piece == 4) {
			return checkObstructBishop(ennemy, kingPos);
		}
		//If the dangerous piece is a queen
		else if (piece == 6) {
			
			if (kingPos.player == ennemy.player || kingPos.piece == ennemy.piece )
			{
				// If the dangerous queen put in check the king by attacking like the rook we check the obstruction possible to block a rook attack
				if (checkObstructRook(ennemy, kingPos))
					return false;
			}
			else
			{
				// If the dangerous queen put in check the king by attacking like the bishop we check the obstruction possible to block a bishop attack
				if (checkObstructBishop(ennemy, kingPos))
					return false;
			}
			return true;
		}
		return false;
	}
	
	// Will check if the king can move somewhere (were it will not be attacked)
	public boolean kingNoAvailableMove() {
		Tuple<Integer, Integer> kingPos = getKingPosition(getOtherPlayer()); //get king position
		Tuple<Integer, Integer> os = new Tuple<Integer, Integer>(selected.player, selected.piece); //save initial selected piece before change it
		Tuple<Integer, Integer> saveCase = new Tuple<Integer, Integer>(0, 0); // used later to save case
		int beginX = kingPos.player - 1; //set the initial value x where loops will begin 
		int beginY = kingPos.piece -1; //set the initial value x where loops will begin 
		swapPlayers(); //need to swap player to check right pieces
		//Check all movement in a 3x3 square (so all around the king)
		for (int x = 0; x < 3; ++x) { 
			for (int y = 0; y < 3; ++y) {
				//Check if we are in the board
				if (((beginX + x) >= 0 && (beginX + x) <= 7) && ((beginY + y) >= 0 && (beginY + y) <= 7) && ((board[beginX + x][beginY + y].player == 0) || (board[beginX + x][beginY + y].player == getOtherPlayer()))) {
					//Below, we simulate the move and if it is possible,
					//and if it will be not possible to attack it on the new square (with method pieceindanger below) we return false because King have a move available.
					saveCase.player = board[beginX + x][beginY + y].player;
					saveCase.piece = board[beginX + x][beginY + y].piece;
					board[beginX + x][beginY + y].player = current_player;
					board[beginX + x][beginY + y].piece = 5;
					if (!pieceInDanger(beginX + x, beginY + y, true, false, false)/* && tryMove(beginX + x, beginY + y)*/)
					{
						//Set modified variable to their own values before all the calculs
						board[beginX + x][beginY + y].player = saveCase.player;
						board[beginX + x][beginY + y].piece = saveCase.piece;
						swapPlayers();
						selected = os;
					}
					board[beginX + x][beginY + y].player = saveCase.player;
					board[beginX + x][beginY + y].piece = saveCase.piece;
				}
			}
		}
		//Set modified variable to their own values before all the calculs. Then we will return true because king can't move.
		swapPlayers();
		selected = os;
		return true;
	}
	
	// Determine if is the end of the game, if there is a stalemate or a checkmate
	public boolean determineEndGame() {
		// Save the king position of the current player
		Tuple<Integer, Integer> kingPos = getKingPosition(current_player);
		// Save the king position of the other player
		Tuple<Integer, Integer> kingPosOth = getKingPosition(getOtherPlayer());
		// Check if there is a Stalemate
		if (CheckStalemate())
		{
			System.out.println("Stalemate");
			return true;
		}
		// Check if there is a check
		if (pieceInDanger(kingPosOth.player, kingPosOth.piece, false, false, false))
			System.out.println("Check for the " + (getOtherPlayer() == 1 ? "white" : "black") + " player.");
		// Check if there is a checkmate
		if (pieceInDanger(kingPosOth.player, kingPosOth.piece, false, false, false) && kingNoAvailableMove() && cantAvoidDangerousPiece()) {
			System.out.println("Checkmate");
			return true;
		}
		return false;
	}
	
	// Try a move to position x y from selected piece.
	//For example, if the move of a piece make our king in chess, the move will be impossible because this function will return false
	public boolean launchMove(int x, int y) {
		Tuple <Integer, Integer> oldPiecePos = new Tuple<Integer, Integer>(selected.player, selected.piece);
		Tuple <Integer, Integer> oldNewPiece = new Tuple<Integer, Integer>(board[x][y].player, board[x][y].piece);
		//Simulate the move
		 board[x][y] = new Tuple<Integer, Integer>(board[selected.player][selected.piece].player, board[selected.player][selected.piece].piece);
		 board[selected.player][selected.piece].piece = 0;
		 board[selected.player][selected.piece].player = 0;
	    Tuple <Integer, Integer> kingPos = getKingPosition(current_player);
	    	//Check if king will be in danger, if not, we return true because move can be launched
		     if (!pieceInDanger(kingPos.player, kingPos.piece, true, false, false))
				 return true;
			 else {
				 //Set originals values if move is not possible
				 board[oldPiecePos.player][oldPiecePos.piece] = new Tuple<Integer, Integer>(board[x][y].player, board[x][y].piece);
				 board[x][y] = oldNewPiece;
				 return false;
			 }
	    }
	// Will redraw clicked square background for underline the selected piece (grey)
	public void redrawClick(Graphics g2d, int x, int y)
	{
		int		sizeCellX = (getWidth() / 8);
		int		sizeCellY = (getHeight() / 8);
		g2d.setColor(colorSelected);
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
	
	private void drawBoard(Graphics2D g2d) 
	{
		// Set the size of each cells
		int		sizeCellX = getWidth() / 8;
		int		sizeCellY = getHeight() / 8;
		
		// For each cells of the board
		for (int x = 0; x < 8; x++)
		{
			
			for (int y = 0; y < 8; y++)
			{
				// Draw the ligthBrown cells of the board
				if (y % 2 == x % 2)
				{
					g2d.setColor(lightBrown);
					g2d.fillRect(x * sizeCellX, y * sizeCellY, sizeCellX, sizeCellY);
				}
				// Draw the brown cells of the board
				else
				{
					g2d.setColor(brown);
					g2d.fillRect(x * sizeCellX, y * sizeCellY, sizeCellX, sizeCellY);
				}
			}
		}
	}
	
	// will draw the grid for the game. this assumes a 640 by 640 grid
	private void drawGrid(Graphics2D g2d) 
	{
		// set the color of the line
		g2d.setColor(black);
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
	
	// Method which draw the pieces on the board
	private void drawPieces(Graphics2D g2d) {
		// Computation the size of the cells
		int		sizeCellX = (getWidth() / 8);
		int		sizeCellY = (getHeight() / 8);

		//Loop on all of the board
		for (int x = 0; x < 8; x++)
		{
			for (int y = 0; y < 8; y++)
			{
				//Drawing the image given by getPieceImage which return the current piece's image
				g2d.drawImage(getPieceImage(board[x][y]), x * sizeCellX + 8, y * sizeCellY + 8, null);
			}
		}
	}
	
	public boolean CheckStalemate()
	{
		// Initialize the number of piece of each player to 0
		int kingW = 0;
		int queenW = 0;
		int pawnW = 0;
		int rookW = 0;
		int bishopW = 0;
		int knightW = 0;
		int nbplayer1 = 0;
		int kingB = 0;
		int queenB = 0;
		int pawnB = 0;
		int rookB = 0;
		int bishopB = 0;
		int knightB = 0; 
		int nbplayer2 = 0;
		
		for (int x = 0; x < 8; x++)
		{
			for (int y = 0; y < 8; y++)
			{
				if (board[x][y].player == 1)
				{
					// For each white piece we count and save the number of the pawn, rook, knight, bishop, king and queen
					if (board[x][y].piece == 1)
						pawnW++;
					else if(board[x][y].piece == 2)
						rookW++;
					else if(board[x][y].piece == 3)
						knightW++;
					else if(board[x][y].piece == 4)
						bishopW++;
					else if(board[x][y].piece == 5)
						kingW++;
					else if(board[x][y].piece == 6)
						queenW++;
					nbplayer1++;
				}
				else
				{
					// For each white piece we count and save the number of the pawn, rook, knight, bishop, king and queen
					if (board[x][y].piece == 1)
						pawnB++;
					else if(board[x][y].piece == 2)
						rookB++;
					else if(board[x][y].piece == 3)
						knightB++;
					else if(board[x][y].piece == 4)
						bishopB++;
					else if(board[x][y].piece == 5)
						kingB++;
					else if(board[x][y].piece == 6)
						queenB++;
					nbplayer2++;
				}
			}
		}
		//king and a queen
		if (nbplayer1 >= 2 && nbplayer2 >= 2)
		{ //System.out.println("STALEMATE");
			if (queenW >= 1 && queenB >= 1)
				return false;
			/*else if (rookW <= 1 && rookB <= 1)
				return false;
			if (bishopB <= 1 && bishopW <= 1 && knightB <= 1 && knightW <= 1)
				return false;
				else if (bishopB == 2 && bishopW == 2)
				{
					//check color case et dans ce cas la 
					// y % 2 == x % 2 case claire
					return false;
				}
				else if (pawnB >= 1 && pawnW >= 1)
				{
					return false;
				}*/
		}
	//System.out.println("PawnW "+pawnW+ " KnightW "+knightW+ " RookW "+rookW+ " BishopW "+bishopW+ " kingW "+kingW+ " queenW "+queenW);
	//System.out.println("PawnB "+pawnB+ " KnightB "+knightB+ " RookB "+rookB+ " BishopB "+bishopB+ " kingB "+kingB+ " queenB "+queenB);
	return true;
	}
	
	// This method will return the image the correspond of the piece given in parameter
	private BufferedImage getPieceImage(Tuple<Integer, Integer> t) {	    
		//We make a loop on the map of the images
	    for (Entry<Tuple<Integer, Integer>, BufferedImage> entry : images.entrySet())
	    {
	    	//If the current image of the map match, we return it
	    	if (entry.getKey().player == t.player && entry.getKey().piece == t.piece) {
	    		return entry.getValue();
	    	}
	    }
	    //else we return null
	    return null;
	}
	
	// will initialise the game board to the starting state
	private void initialState() throws IOException 
	{
		// Initialise all cells in the array to have a value of zero
		board = new Tuple[8][8];
		dangerousPieces = new Vector<Tuple<Integer, Integer>>();
		for (int x = 0; x < 8; x++)
		{
			for (int y = 0; y < 8; y++)
			{
				board[x][y] = new Tuple<Integer, Integer>(0, 0);
			}
		}
		//set images in a hashtable
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
		/*board[0][7].player = 1;
		board[0][7].piece = 2;
		board[1][7].player = 1;
		board[1][7].piece = 3;
		board[2][7].player = 1;
		board[2][7].piece = 4;
		board[3][7].player = 1;
		board[3][7].piece = 6;
		board[4][7].player = 1;
		board[4][7].piece = 5;
		board[5][7].player = 1;
		board[5][7].piece = 4;
		board[6][7].player = 1;
		board[6][7].piece = 3;
		board[7][7].player = 1;
		board[7][7].piece = 2;
		
		board[0][6].player = 1;
		board[0][6].piece = 1;
		board[1][6].player = 1;
		board[1][6].piece = 1;
		board[2][6].player = 1;
		board[2][6].piece = 1;
		board[3][6].player = 1;
		board[3][6].piece = 1;
		board[4][6].player = 1;
		board[4][6].piece = 1;
		board[5][6].player = 1;
		board[5][6].piece = 1;
		board[6][6].player = 1;
		board[6][6].piece = 1;
		board[7][6].player = 1;
		board[7][6].piece = 1;

		// PLayer 2
		
		board[0][0].player = 2;
		board[0][0].piece = 2;
		board[1][0].player = 2;
		board[1][0].piece = 3;
		board[2][0].player = 2;
		board[2][0].piece = 4;
		board[3][0].player = 2;
		board[3][0].piece = 6;
		board[4][0].player = 2;
		board[4][0].piece = 5;
		board[5][0].player = 2;
		board[5][0].piece = 4;
		board[6][0].player = 2;
		board[6][0].piece = 3;
		board[7][0].player = 2;
		board[7][0].piece = 2;
		
		board[0][1].player = 2;
		board[0][1].piece = 1;
		board[1][1].player = 2;
		board[1][1].piece = 1;
		board[2][1].player = 2;
		board[2][1].piece = 1;
		board[3][1].player = 2;
		board[3][1].piece = 1;
		board[4][1].player = 2;
		board[4][1].piece = 1;
		board[5][1].player = 2;
		board[5][1].piece = 1;
		board[6][1].player = 2;
		board[6][1].piece = 1;
		board[7][1].player = 2;
		board[7][1].piece = 1;*/
		
		/*board[0][7].player = 1;
		board[0][7].piece = 2;
		board[1][7].player = 1;
		board[1][7].piece = 3;
		board[2][7].player = 1;
		board[2][7].piece = 4;*/
		board[3][7].player = 1;
		board[3][7].piece = 6;
		board[4][7].player = 1;
		board[4][7].piece = 5;
		/*
		board[5][7].player = 1;
		board[5][7].piece = 4;
		board[6][7].player = 1;
		board[6][7].piece = 3;
		board[7][7].player = 1;
		board[7][7].piece = 2;*/
		
		/*board[0][6].player = 1;
		board[0][6].piece = 1;
		board[1][6].player = 1;
		board[1][6].piece = 1;
		board[2][6].player = 1;
		board[2][6].piece = 1;
		board[3][6].player = 1;
		board[3][6].piece = 1;
		board[4][6].player = 1;
		board[4][6].piece = 1;
		board[5][6].player = 1;
		board[5][6].piece = 1;
		board[6][6].player = 1;
		board[6][6].piece = 1;*/
		board[7][6].player = 1;
		board[7][6].piece = 1;

		// PLayer 2
		
		/*board[0][0].player = 2;
		board[0][0].piece = 2;
		board[1][0].player = 2;
		board[1][0].piece = 3;
		board[2][0].player = 2;
		board[2][0].piece = 4;*/
		board[3][0].player = 2;
		board[3][0].piece = 6;
		board[4][0].player = 2;
		board[4][0].piece = 5;
		/*board[5][0].player = 2;
		/*board[5][0].piece = 4;
		board[6][0].player = 2;
		board[6][0].piece = 3;
		board[7][0].player = 2;
		board[7][0].piece = 2;*/
		
		/*board[0][1].player = 2;
		board[0][1].piece = 1;
		board[1][1].player = 2;
		board[1][1].piece = 1;
		board[2][1].player = 2;
		board[2][1].piece = 1;
		board[3][1].player = 2;
		board[3][1].piece = 1;
		board[4][1].player = 2;
		board[4][1].piece = 1;
		board[5][1].player = 2;
		board[5][1].piece = 1;*/
		board[6][1].player = 2;
		board[6][1].piece = 1;
		//board[7][1].player = 2;
		//board[7][1].piece = 1;

		

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
	Vector<Tuple<Integer, Integer>> dangerousPieces; // stock the pieces which put in check a king
	Hashtable<Tuple<Integer, Integer>, BufferedImage> images; // stock the piece pictures
	Tuple<Integer, Integer> board[][];						// the state of the game board
	int oldx, oldy;						// denotes where the player clicked when he pressed the mouse button
	int current_player;					// denotes who the current player is
	int player_1_score, player_2_score;	// denotes the score each player has in the game thus far
	boolean inPlay;						// indicates if the game is being played at the moment
	Color brown, lightBrown, black, colorSelected;			// color objects that represent their named colours
	Tuple<Integer, Integer> selected; // save the selected piece
	
}