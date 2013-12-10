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
	
	public int getOtherPlayer()
	{
		if (current_player == 1)
			return 2;
		else
			return 1;
	}
	
	public boolean checkAttackPion(int x, int y) {
		int depX = x - selected.player;
		int depY = y - selected.piece;

		//System.out.println("Check pion attack from " + selected.player + "/" + selected.piece + " to " + x + "/" + y);
		if ((depX == 1 || depX == -1) && ((depY == -1 && current_player == 1) || (depY == 1 && current_player == 2)) && board[x][y].player == getOtherPlayer()) {
			return true;
		}
		
		return false;
	}
	
	public boolean checkMovePion(int x, int y) {
		int depX = x - selected.player;
		int depY = y - selected.piece;

		if (depX == 0 && ((depY == -1 && current_player == 1) || (depY == 1 && current_player == 2)) && board[x][y].player == 0)
			return true;
		else if (((current_player == 1 && depX == 0 && depY == -2 && selected.piece == 6) || (current_player == 2 && depX == 0 && depY == 2 && selected.piece == 1)) && board[x][y].player == 0)
			return true;
		return false;
	}
	
	public boolean checkMoveKnight(int x, int y) {
		int depX = x - selected.player;
		int depY = y - selected.piece;

		
		if (((depX == 2 && depY == 1) || (depX == -2 && depY == 1) ||
				(depX == 2 && depY == -1) || (depX == -2 && depY == -1) ||
				(depX == 1 && depY == 2) || (depX == -1 && depY == 2) ||
				(depX == -1 && depY == -2) || (depX == 1 && depY == -2)) && board[x][y].player != current_player) {
			return true;
		}
		return false;
	}
	
	public boolean checkMoveKing(int x, int y) {
		int depX = x - selected.player;
		int depY = y - selected.piece;

		
		if (((depX == 0 && depY == 1) || (depX == 1 && depY == 1) ||
				(depX == -1 && depY == 1) || (depX == -1 && depY == 0) ||
				(depX == 1 && depY == 0) || (depX == -1 && depY == -1) ||
				(depX == 1 && depY == -1) || (depX == 0 && depY == -1)) && board[x][y].player != current_player) {
			return true;
		}
		return false;
	}
	
	public boolean tryMove(int x, int y) {
		int piece = board[this.selected.player][this.selected.piece].piece;
		
		if (piece == 1) {
			if (checkMovePion(x, y) || checkAttackPion(x,y)) {
				changePion(x, y);
				return true;
			}
			return false;
		}
		else if (piece == 2) {
			if (checkMoveRook(x, y) ) {
				return true;
			}
			return false;
		}
		else if (piece == 3) {
			if (checkMoveKnight(x, y)) {
				return true;
			}
			return false;
		}
		else if (piece == 4) {
			if (checkMoveBishop(x, y)) {
				return true;
			}
			return false;
		}
		else if (piece == 5) {
			if (checkMoveKing(x, y)) {
				//System.out.println("King can move in " + x + "/" + y);
				return true;
			}
			//System.out.println("King can NOT move in " + x + "/" + y);
			return false;
		}
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
		Object choices[] = {"Rook", "Knight", "Queen", "Bishop"};
		String input = (String) JOptionPane.showInputDialog(this, "Choose a pion", "Choice",JOptionPane.PLAIN_MESSAGE, null, choices, "Rook");
		return input;
	}
	
	public void changePion(int x, int y)
	{
		String value = null;
		if (y == 7 || y == 0)
		{
			
			while (value == null)
			{
				value = choiceOfPion();
				if (value == null)
				{
					JOptionPane.showMessageDialog(this,"You have to choose a new pion", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			if (value == "Rook")
			{
				board[selected.player][selected.piece].piece = 2;
			}
			else if (value == "Knight")
			{
				board[selected.player][selected.piece].piece = 3;
			}
			else if (value == "Queen")
			{
				board[selected.player][selected.piece].piece = 6;
			}
			else if (value == "Bishop")
			{
				board[selected.player][selected.piece].piece = 4;
			}
		}
	}
	public boolean checkMoveRook(int x, int y)
	{
		int depX = x - selected.player;
		int depY = y - selected.piece ;
	
		if (this.selected.player == x || this.selected.piece == y)
		{			
			int x1 = 0, y1 = 0, dx = 0, dy = 0;
			if (depX < 0)
			{
				// Left
				x1 = this.selected.player - 1;
				y1 =  this.selected.piece;
				dx = -1;
			}
			else if (depX > 0)
			{
				// Right
				x1 = this.selected.player + 1;
				y1 =  this.selected.piece;
				dx = 1;
			}
			else if (depY < 0)
			{
				// Up
				x1 = this.selected.player;
				y1 =  this.selected.piece - 1;
				dy = -1;
			}
			else if (depY > 0)
			{
				// Down
				x1 = this.selected.player;
				y1 = this.selected.piece + 1;
				dy = 1;
			}
			int x2, y2, move = 0;
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
		//System.out.println("Bishop TEST move from " + selected.player + "/" + selected.piece + " to " + x + "/" + y);
		int depX = x - selected.player;
		int depY = y - selected.piece ;
	
		int depXAbs = (depX < 0) ? depX * -1 : depX;
		int depYAbs = (depY < 0) ? depY * -1 : depY;
		
		if (depXAbs == depYAbs)
		{
			int x1 = 0, y1 = 0, dx = 0, dy = 0;
			if (depX < 0 && depY < 0)
			{
				// Diagonal Left/Up
				x1 = this.selected.player - 1;
				y1 =  this.selected.piece - 1;
				dx = -1;
				dy = -1;
			}
			else if (depX > 0 && depY < 0)
			{
				// Diagonal Left/Down
				x1 = this.selected.player + 1;
				y1 =  this.selected.piece - 1;
				dx = 1;
				dy = -1;
			}
			else if (depY > 0 && depX > 0)
			{
				// Diagonal Right/Up
				x1 = this.selected.player + 1;
				y1 =  this.selected.piece + 1;
				dy = 1;
				dx = 1;
			}
			else if (depY > 0 && depX < 0)
			{
				// Diagonal Left/Up
				x1 = this.selected.player - 1;
				y1 = this.selected.piece + 1;
				dy = 1;
				dx = -1;
			}
			int x2, y2, move = 0;
			for (x2 = x1, y2 = y1; move != depXAbs; x2 += dx, y2 += dy) {
				if (x2 == x && y2 == y && board[x2][y2].player != current_player) {
					//System.out.println("1Queen can move from " + selected.player + "/" + selected.piece + " to " + x + "/" + y);
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
		if (checkMoveBishop(x, y) || checkMoveRook(x, y))
			return true;
		return false;
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
				if (selected == null && board[newx][newy].player == current_player)
				{
					redrawClick(getGraphics(), newx, newy);
					selected = new Tuple<Integer, Integer>(newx, newy);
				}
				else if (selected != null && selected.player == newx && selected.piece == newy)
				{
					paintComponent(getGraphics());
					selected = null;
				}
				else if (selected != null)
				{
					if (tryMove(newx, newy) && launchMove(newx, newy)) {
						paintComponent(getGraphics());
						if (determineEndGame())
						{
							System.out.println("Game over");
						}
						selected = null;
						swapPlayers();
					}
				}
				
			}
		}
	}
	
	public boolean canAttack(int fromX, int fromY, int toX, int toY, boolean checkEnnemy) {
		int piece = board[fromX][fromY].piece;
		selected.player = fromX;
		selected.piece = fromY;
		if (!checkEnnemy)
			dangerousPieces.clear();
		//System.out.println("Current piece = " + piece + " // King position: " + toX + "/" + toY + " could be attack by " + fromX + "/" + fromY + "?");
		if (piece == 1) {
			if (checkAttackPion(toX, toY)) {
				if (!checkEnnemy)
					dangerousPieces.add(new Tuple<Integer, Integer>(fromX, fromY));
				//System.out.println("Yes !");
				return true;
			}
		}
		else if (piece == 2) {
			if (checkMoveRook(toX, toY) ) {
				if (!checkEnnemy)
					dangerousPieces.add(new Tuple<Integer, Integer>(fromX, fromY));
				return true;
			}
		}
		else if (piece == 3) {
			if (checkMoveKnight(toX, toY)){
				if (!checkEnnemy)
					dangerousPieces.add(new Tuple<Integer, Integer>(fromX, fromY));
				return true;
			}
		}
		else if (piece == 4) {
			if (checkMoveBishop(toX, toY)){
				if (!checkEnnemy)
					dangerousPieces.add(new Tuple<Integer, Integer>(fromX, fromY));
				return true;
			}
		}
		else if (piece == 5) {
			if (checkMoveKing(toX, toY)){
				if (!checkEnnemy)
					dangerousPieces.add(new Tuple<Integer, Integer>(fromX, fromY));
				return true;
			}
		}
		else if (piece == 6) {
			if (checkMoveQueen(toX, toY)){
				//System.out.println("Queen can move from " + fromX + "/" + fromY + " to " + toX + "/" + toY);
				if (!checkEnnemy)
					dangerousPieces.add(new Tuple<Integer, Integer>(fromX, fromY));
				return true;
			}
		}
		return false;
	}
	
	public boolean pieceInDanger(int px, int py, boolean checkEnnemy) {
		Tuple<Integer, Integer> os = new Tuple<Integer, Integer>(selected.player, selected.piece);
		if (checkEnnemy)
			swapPlayers();
		for (int x = 0; x < 8; ++x) {
			for (int y = 0; y < 8; ++y) {
				if ((px != x || py != y) && board[x][y].player == current_player && canAttack(x, y, px, py, checkEnnemy))
				{
					if (checkEnnemy)
						swapPlayers();
					System.out.println("Piece in danger in " + px + "/" + py + " from " + x + "/" + y);
					selected = os;
					return true;
				}
			}
		}
		if (checkEnnemy)
			swapPlayers();
		selected = os;
		System.out.println("Piece NOT in danger in " + px + "/" + py);
		return false;
	}
	
	public Tuple<Integer, Integer> getKingPosition(int player) {
		for (int x = 0; x < 8; ++x) {
			for (int y = 0; y < 8; ++y) {
				if (board[x][y].player == player && board[x][y].piece == 5)
					return new Tuple<Integer, Integer>(x, y);
			}
		}
		return null;
	}
	
	public boolean cantAvoidDangerousPiece() {
		if (dangerousPieces.size() > 1) {
			return true;
		}
		//Iterator<Tuple<Integer, Integer>> ite = dangerousPieces.iterator();
		Tuple<Integer, Integer> dp = dangerousPieces.elementAt(0);
		boolean test = pieceCantObstructAttack(dp);
		if (pieceInDanger(dp.player, dp.piece, true) || !pieceCantObstructAttack(dp)) {
			System.out.println("We can (false) avoid dangerous piece / COA = " + test);
			return false;
		}
		System.out.println("We CANT (true) avoid dangerous piece");
		return true;
	}
	
	public boolean checkObstructRook(Tuple<Integer, Integer> ennemy, Tuple<Integer, Integer> kingPos) {
		if (kingPos.player == ennemy.player) {
			if (kingPos.piece > ennemy.piece)
			{
				//Attaque de haut
				for (int y = ennemy.piece; y != kingPos.piece; ++y) {
					swapPlayers();
					System.out.println("b1");
					if (pieceInDanger(kingPos.player, y, true)) {
						swapPlayers();
						return true;
					}
					swapPlayers();
				}
			}
			else {
				//Attaque de bas
				for (int y = ennemy.piece; y != kingPos.piece; --y) {
					swapPlayers();
					System.out.println("b2");
					if (pieceInDanger(kingPos.player, y, true)) {
						swapPlayers();
						return true;
					}
					swapPlayers();
				}
			}
		}
		else {
			if (kingPos.player > ennemy.player) {
				//Attaque d'en bas
				for (int x = ennemy.player; x != kingPos.player; ++x) {
					swapPlayers();
					System.out.println("b3");
					if (pieceInDanger(x, kingPos.piece, true)) {
						swapPlayers();
						return true;
					}
					swapPlayers();
				}
			}
			else {
				//Attaque d'en haut
				for (int x = ennemy.player; x != kingPos.player; --x) {
					swapPlayers();
					System.out.println("b4 ");
					if (pieceInDanger(x, kingPos.piece, true)) {
						swapPlayers();
						return true;
					}
					swapPlayers();
				}
			}
		}
		return false;
	}
	
	public boolean checkObstructBishop(Tuple<Integer, Integer> ennemy, Tuple<Integer, Integer> kingPos) {
		if (kingPos.piece < ennemy.piece) { //haut
			if (kingPos.player < ennemy.player) //gauche
			{
				//Attaque de haut gauche
				for (int y = ennemy.piece, x = ennemy.player; y != kingPos.piece; --y, --x) {
					swapPlayers();
					//System.out.println("b5" + x + "/" + kingPos.player + " and " + y + "/" + kingPos.piece);
					if (pieceInDanger(x, y, true)) {
						swapPlayers();
						return true;
					}
					swapPlayers();
//break;
				}
			}
			else { //droite
				//Attaque de haut droite
				for (int y = ennemy.piece, x = ennemy.player; y != kingPos.piece; ++y, --x) {
					swapPlayers();
					System.out.println("b6");
					if (pieceInDanger(x, y, true)) {
						swapPlayers();
						return true;
					}
					swapPlayers();
				}
			}
		}
		else { //bas
			if (kingPos.player < ennemy.player) //gauche
			{
				//Attaque de bas gauche
				for (int y = ennemy.piece, x = ennemy.player; y != kingPos.piece; --y, ++x) {
					swapPlayers();
					System.out.println("b7");
					if (pieceInDanger(x, y, true)) {
						swapPlayers();
						return true;
					}
					swapPlayers();
				}
			}
			else { //droite
				//Attaque de bas droite
				for (int y = ennemy.piece, x = ennemy.player; y != kingPos.piece; --y, --x) {
					swapPlayers();
					System.out.println("b8");
					if (pieceInDanger(x, y, true)) {
						swapPlayers();
						return true;
					}
					swapPlayers();
				}
			}
		}
		return false;
	}
	
	public boolean pieceCantObstructAttack(Tuple<Integer, Integer> ennemy) {
		int piece = board[ennemy.player][ennemy.piece].piece;
		Tuple<Integer, Integer> kingPos = getKingPosition(getOtherPlayer());
		if (piece == 1 || piece == 3 || piece == 5)
			return true;
		if (piece == 2) {
			return checkObstructRook(ennemy, kingPos);
		}
		else if (piece == 4) {
			return checkObstructBishop(ennemy, kingPos);
		}
		else if (piece == 6) {
			if (checkObstructRook(ennemy, kingPos) || checkObstructBishop(ennemy, kingPos))
				return false;
			else
				return true;
		}
		return false;
	}
	
	public boolean kingNoAvailableMove() {
		Tuple<Integer, Integer> kingPos = getKingPosition(current_player);
		int beginX = kingPos.player - 1;
		int beginY = kingPos.piece -1;
		for (int x = 0; x < 3; ++x) {
			for (int y = 0; y < 3; ++y) {
				if (((beginX + x) >= 0 && (beginX + x) <= 7) && ((beginY + y) >= 0 && (beginY + y) <= 7)) {
					//System.out.println("King No Available Move on " + (beginX + x) + "/" + (beginY + y) + "?");
					if (!pieceInDanger(beginX + x, beginY + y, true) && tryMove(beginX + x, beginY + y))
					{
						System.out.println("CAN MOVE");
						return false;
					}
				}
			}
		}
		System.out.println("no move available");

		return true;
	}
	
	public boolean determineEndGame() {
		Tuple<Integer, Integer> kingPos = getKingPosition(current_player);
		Tuple<Integer, Integer> kingPosOth = getKingPosition(getOtherPlayer());

		if (pieceInDanger(kingPosOth.player, kingPosOth.piece, false))
			System.out.println("Check for the " + (getOtherPlayer() == 1 ? "white" : "black") + " player.");
		if (pieceInDanger(kingPosOth.player, kingPosOth.piece, false) && kingNoAvailableMove() && cantAvoidDangerousPiece()/* && pieceCantObstructAttack()*/) {
			System.out.println("Checkmate");
			return true;
		}
		return false;
	}
	
	public boolean launchMove(int x, int y) {
		//System.out.println(selected.player + "/" + selected.piece + " to " + x + "/" + y);
		Tuple <Integer, Integer> oldPiecePos = new Tuple<Integer, Integer>(selected.player, selected.piece);
		Tuple <Integer, Integer> oldNewPiece = new Tuple<Integer, Integer>(board[x][y].player, board[x][y].piece);
		 board[x][y] = new Tuple<Integer, Integer>(board[selected.player][selected.piece].player, board[selected.player][selected.piece].piece);
		 board[selected.player][selected.piece].piece = 0;
		 board[selected.player][selected.piece].player = 0;
	    Tuple <Integer, Integer> kingPos = getKingPosition(current_player);
		     if (!pieceInDanger(kingPos.player, kingPos.piece, true))
				 return true;
			 else {
				 board[oldPiecePos.player][oldPiecePos.piece] = new Tuple<Integer, Integer>(board[x][y].player, board[x][y].piece);
				 board[x][y] = oldNewPiece;
				 return false;
			 }
	    }
	
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
		int		sizeCellX = getWidth() / 8;
		int		sizeCellY = getHeight() / 8;
		for (int x = 0; x < 8; x++)
		{
			
			for (int y = 0; y < 8; y++)
			{
				if (x % 2 == 0 && y % 2 == 0)
				{
					g2d.setColor(lightBrown);
					g2d.fillRect(x * sizeCellX, y * sizeCellY, sizeCellX, sizeCellY);
				}
				else if (x % 2 == 0 && y != 0)
				{
					g2d.setColor(brown);
					g2d.fillRect(x * sizeCellX, y * sizeCellY, sizeCellX, sizeCellY);
				}
				else if (x % 2 != 0 && y % 2 == 0)
				{
					g2d.setColor(brown);
					g2d.fillRect(x * sizeCellX, y * sizeCellY, sizeCellX, sizeCellY);
				}
				else
				{
					g2d.setColor(lightBrown);
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
	private void drawPieces(Graphics2D g2d) {
		// Computation the size of the cells
		int		sizeCellX = (getWidth() / 8);
		int		sizeCellY = (getHeight() / 8);

		
		for (int x = 0; x < 8; x++)
		{
			for (int y = 0; y < 8; y++)
			{
				g2d.drawImage(getPieceImage(board[x][y]), x * sizeCellX + 8, y * sizeCellY + 8, null);
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
		dangerousPieces = new Vector<Tuple<Integer, Integer>>();
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
		board[0][7].player = 1;
		board[0][7].piece = 2;
		board[1][7].player = 1;
		board[1][7].piece = 3;
		board[2][7].player = 1;
		board[2][7].piece = 4;
		board[3][7].player = 1;
		board[3][7].piece = 5;
		board[4][7].player = 1;
		board[4][7].piece = 6;
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
		board[3][0].piece = 5;
		board[4][0].player = 2;
		board[4][0].piece = 6;
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
		board[7][1].piece = 1;

		

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
	Vector<Tuple<Integer, Integer>> dangerousPieces;
	Hashtable<Tuple<Integer, Integer>, BufferedImage> images;
	Tuple<Integer, Integer> board[][];						// the state of the game board
	int oldx, oldy;						// denotes where the player clicked when he pressed the mouse button
	int current_player;					// denotes who the current player is
	int player_1_score, player_2_score;	// denotes the score each player has in the game thus far
	boolean inPlay;						// indicates if the game is being played at the moment
	Color brown, lightBrown, black, colorSelected;			// color objects that represent their named colours
	Tuple<Integer, Integer> selected;
	
}