package com.tuongky.flagfan;

import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import static com.tuongky.flagfan.Initialization.*;
import static com.tuongky.flagfan.GameInfor.*;

public class BoardPanel extends JPanel implements MouseListener {
	
	final static String board_path = "resources/pictures/board/";
	final static String piece_path = "resources/pictures/pieces/";
	
	FlagFanMain xqm;
	private Image board_img;
	private Image[] piece_img;
	
	int moving = -1;
	
	public BoardPanel(FlagFanMain xqm) {
		this.xqm = xqm;
		board_img = new ImageIcon(board_path+"board.png").getImage();
		piece_img = new Image[14];
		
		piece_img[0] = new ImageIcon(piece_path+"red_king.png").getImage();		
		piece_img[1] = new ImageIcon(piece_path+"red_bishop.png").getImage();
		piece_img[2] = new ImageIcon(piece_path+"red_elephant.png").getImage();
		piece_img[3] = new ImageIcon(piece_path+"red_rook.png").getImage();
		piece_img[4] = new ImageIcon(piece_path+"red_cannon.png").getImage();
		piece_img[5] = new ImageIcon(piece_path+"red_knight.png").getImage();
		piece_img[6] = new ImageIcon(piece_path+"red_pawn.png").getImage();
		
		piece_img[7] = new ImageIcon(piece_path+"black_king.png").getImage();
		piece_img[8] = new ImageIcon(piece_path+"black_bishop.png").getImage();
		piece_img[9] = new ImageIcon(piece_path+"black_elephant.png").getImage();
		piece_img[10] = new ImageIcon(piece_path+"black_rook.png").getImage();
		piece_img[11] = new ImageIcon(piece_path+"black_cannon.png").getImage();
		piece_img[12] = new ImageIcon(piece_path+"black_knight.png").getImage();
		piece_img[13] = new ImageIcon(piece_path+"black_pawn.png").getImage();
		
		addMouseListener(this);
	}
	
	int type2index(int type) {
		int index;
		if (type>0) index = type-1; else index = 6-type;
		return index;
	}
	
	public void drawPiece(Graphics g, int pos, int type) {
		if (type==0) return;
		
		int x, y, index;		
		x = 10+17+33*((pos&0xf)-3)-16;
		y = 10+17+33*((pos>>4)-3)-16;

		if (pos==moving) {
			g.setColor(Color.BLUE);
			g.drawArc(x, y-1, 32, 32, 0, 360);
			return;
		}
		
		index = type2index(type);
		
		g.drawImage(piece_img[index], x, y, this);
	}
	
	public void drawBoard(Graphics g) {
		g.drawImage(board_img, 10, 10, this);
		for (int i=0; i<256; i++) drawPiece(g, i, xqm.ginf.state[i]);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawBoard(g);
	}
	
	int getPos(int x, int y) {
		double min = 1E20;
		int res = -1;
		for (int i=0; i<256; i++) 
		if (inBoard[i]) {
			int u, v;
			u = 10+17+33*((i&0xf)-3);
			v = 10+17+33*((i>>4)-3);
			double tmp = 1.0*(x-u)*(x-u)+1.0*(y-v)*(y-v);
			if (tmp<16*16&&tmp<min) {
				min = tmp;
				res = i;
			}
		}
		return res;
	}
	
    void setPieceCursor(int piece) {
		Cursor newCursor = getToolkit().createCustomCursor(piece_img[type2index(piece)], new Point(16,16), "newCursor");
		setCursor(newCursor);    	
    }
    
    void setNormalCursor() {
    	Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    	setCursor(normalCursor);    	
    }
    
    void refreshBoard() {
    	this.update(this.getGraphics());
    }
        
    public void mouseClicked(MouseEvent e) {
    	int pos = getPos(e.getX(),e.getY());
    	if (pos==-1) return;
    	
    	if (moving==-1) {
    		int piece = xqm.ginf.state[pos];
    		if (piece==0) return;
    		
    		if (piece>0&&xqm.ginf.side==BLACK) return;
    		if (piece<0&&xqm.ginf.side==RED) return;
    		
    		if (piece>0&&xqm.p1==COMPUTER) return;
    		if (piece<0&&xqm.p2==COMPUTER) return;    		
			
			setPieceCursor(piece);
			
			moving = pos;
			
			refreshBoard();
			
    	} else {
    		if (pos==moving) {
    			moving = -1;
    			setNormalCursor();
    			refreshBoard();
    			return ;
    		}
    		
    		int move = moving<<8|pos; 
    		
    		if (!xqm.ginf.isValidMove(move)) return;    		
    		xqm.ginf.makeMove(move);
    		
    		moving = -1;
    		
    		setNormalCursor();    		
    		refreshBoard();    		
    	}
    }

	public void mousePressed(MouseEvent e) {		
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

}
