package chessdesktop;

import Chess.ChessPiece;
import Chess.PiecePosition;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Scanner;

public class ChessBoardRenderer {
	final double LEFT_MARGIN = 50;
	final double TOP_MARGIN = 50;
	final double PIECE_OFFSET = 5;
        public int jugada=0;
        boolean twhite=false, tblack=false;
	
	private final Chess.ChessBoard board = new Chess.ChessBoardImplementation();
	private Chess.ChessPiece movingPiece;

	static private void drawCrown(GraphicsContext aContext, double minX, double minY, double width, double height) {
		double maxX = minX + width;
		double maxY = minY + height;
		double[] x = {minX, minX, maxX, maxX, minX + width * 0.8, (minX + maxX) * 0.5, minX + width * 0.2};
		double[] y = {minY, maxY, maxY, minY, minY + height * 0.5, minY, minY + height * 0.5};
		aContext.fillPolygon(x, y, 7);
	}
	
	static private void drawTriangle(GraphicsContext aContext, double minX, double minY, double width, double height) {
		double maxX = minX + width;
		double maxY = minY + height;
		double[] x = {minX + width * 0.5, minX, maxX};
		double[] y = {minY, maxY, maxY};
		aContext.fillPolygon(x, y, 3);
	}

	
	void drawPiece(Canvas canvas, Chess.ChessPiece piece, double minX, double minY, double width, double height) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
                               
		if (piece == movingPiece)
			gc.setFill(Color.GREY);
		else if (piece.getColor() == Chess.ChessPiece.Color.BLACK)
			gc.setFill(Color.BLACK);
		else
			gc.setFill(Color.WHITE);

		minX += PIECE_OFFSET;
		minY += PIECE_OFFSET;
		width -= PIECE_OFFSET * 2;
		height -= PIECE_OFFSET * 2;
		
		switch(piece.getType()) {
			case KING:
				drawCrown(gc, minX, minY, width, height);
				break;
			case QUEEN:
				drawCrown(gc, minX + width * 0.3, minY, width * 0.4, height * 0.3);
				gc.fillOval(minX, minY + height * 0.3, width, height * 0.7);
				gc.fillRect(minX, minY + height * 0.7, width, height * 0.3);
				break;
			case ROOK: {
				double maxX = minX + width;
				double maxY = minY + height;
				double[] x = {minX, minX, maxX, maxX, 
					minX + width * 0.8, minX + width * 0.8, 
					minX + width * 0.6, minX + width * 0.6, 
					minX + width * 0.4, minX + width * 0.4, 
					minX + width * 0.2, minX + width * 0.2
				};
				double[] y = {minY, maxY, maxY, 
					minY, minY,
					minY + height * 0.3, minY + height * 0.3, 
					minY, minY,
					minY + height * 0.3, minY + height * 0.3,
					minY
				};
				gc.fillPolygon(x, y, 12);
				break;
			}
			case BISHOP:
				gc.fillOval(minX + width * 0.4, minY, width * 0.2, height);
				drawTriangle(gc, minX + width * 0.3, minY + height * 0.2, width * 0.4, height * 0.8);
				break;
			case KNIGHT:
                                gc.fillRect((minX+9), minY + -1, 18, 12);
				drawTriangle(gc, minX + width * 0.3, minY + height * 0.2, width * 0.4, height * 0.8);
				break;
			case PAWN: 
				drawTriangle(gc, minX, minY + height * 0.3, width, height * 0.7);
				break;
		}
	}
	
	Bounds getBoardBounds(Canvas canvas) {
		Bounds bounds = canvas.getBoundsInLocal();		
		return new BoundingBox(bounds.getMinX() + LEFT_MARGIN, bounds.getMinY() + TOP_MARGIN, 
			bounds.getWidth() - LEFT_MARGIN, bounds.getHeight() - TOP_MARGIN);		
	}
	
	void drawPiece(Canvas canvas, Bounds boardBounds, Chess.ChessPiece piece) {
		PiecePosition position = board.getPiecePosition(piece);
		int c = position.getColumn();
		int r = position.getRow();
		double width = boardBounds.getWidth() / 8;
		double height = boardBounds.getHeight() / 8;

		drawPiece(canvas, piece, boardBounds.getMinX() + c * width, 
				boardBounds.getMinY() + r * height, width, height);
	}

	void draw(Canvas canvas) {
		Bounds bounds = canvas.getBoundsInLocal();
		GraphicsContext gc = canvas.getGraphicsContext2D();
     
                Bounds boardBounds = getBoardBounds(canvas);
		double width = boardBounds.getWidth() / 8;
		double height = boardBounds.getHeight() / 8;
	
		gc.setFill(Color.WHITE);
		gc.clearRect(0, 0, bounds.getWidth(), bounds.getHeight());
               
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {							
                        if ((i + j) % 2 == 0){
                            gc.setFill(Color.AQUAMARINE);
                        }
                        else{
                            gc.setFill(Color.DEEPSKYBLUE);
                        }
                        if (movingPiece!=null){
                            PiecePosition [] posibletab;
                            posibletab=movingPiece.getAvailablePositions(board);
                            for (PiecePosition mov: posibletab){
                                if (mov.getColumn()==i && mov.getRow()==j && turno()==movingPiece.getColor()){
                                    gc.setFill(Color.YELLOW);
                                }
                            }                            
                        }
                        gc.fillRect(boardBounds.getMinX() + i * width, boardBounds.getMinY() + j * height, 
                        boardBounds.getMinX() + (i + 1) * width, boardBounds.getMinY() + (j + 1) * height);
                    }
                }	

               		                
		for (ChessPiece piece : board.getPieces())
			drawPiece(canvas, boardBounds, piece);

		gc.setFill(Color.BLACK);
		for (int i = 0; i < 8; i++) {
			gc.fillText("(" + (i + 1) + ")", 10, TOP_MARGIN + (i + 0.5) * height);
		}
		for (int i = 0; i < 8; i++) {
			gc.fillText("(" + (i + 1) + ")", LEFT_MARGIN + (i + 0.5) * width, 10);
		}
	}
	
	ChessPiece getPieceAt(Canvas canvas, double x, double y) {
		Bounds boardBounds = getBoardBounds(canvas);
		double width = boardBounds.getWidth() / 8;
		double height = boardBounds.getHeight() / 8;

		for (ChessPiece piece : board.getPieces()) {
			PiecePosition position = board.getPiecePosition(piece);
			int c = position.getColumn();
			int r = position.getRow();

			double minx = boardBounds.getMinX() + (c + 0.0) * width;
			double maxx = boardBounds.getMinX() + (c + 1.0) * width;
			double miny = boardBounds.getMinY() + (r + 0.0) * height;
			double maxy = boardBounds.getMinY() + (r + 1.0) * height;
			
			if (minx <= x && x <= maxx && miny <= y && y <= maxy)
				return piece;
		}
		
		return null;
	}

	public void setMovingPiece(ChessPiece movingPiece) {
		this.movingPiece = movingPiece;
	}

	public ChessPiece getMovingPiece() {
		return movingPiece;
	}
        
        
        public void sumarjugada(){
            jugada=jugada+1;
        }
        
	
	boolean movePieceTo(Canvas canvas, double x, double y) {
            
                if (movingPiece == null)
			return false;
		Bounds boardBounds = getBoardBounds(canvas);
                         if (boardBounds.contains(x, y) && containsKing(ChessPiece.Color.WHITE) && containsKing(ChessPiece.Color.BLACK) && isTie()==false) {
                                double width = boardBounds.getWidth() / 8;
                                double height = boardBounds.getHeight() / 8;
                                int column = (int)((x - boardBounds.getMinX()) / width);
                                int row = (int)((y - boardBounds.getMinY()) / height);
                                PiecePosition position = new PiecePosition(column, row);
                                if (movingPiece.canMoveToPosition(position, board)
                                    && turno()==movingPiece.getColor()){
                                        sumarjugada();
                                        return board.movePieceTo(movingPiece, position);  
                                }
                        }
		return false;
        }
     
	boolean containsKing(ChessPiece.Color aColor) {
		return board.containsKing(aColor);
	}
        boolean isTie() {
            ChessPiece[] arrayPieces = board.getPieces(ChessPiece.Color.BLACK);
            int bRook, bQueen, bPawn, bBishop=0, bKnight;
            int sumPieces=0,sumBishops=0,sumKnights=0;
            for (int i=0; i<2; i++){
                bRook = 0; bQueen = 0; bPawn = 0; bBishop = 0; bKnight = 0;
                if (arrayPieces != null){
                    for (ChessPiece p: arrayPieces){
                        switch(p.getType()){
                            case ROOK: bRook++;
                                        break;
                            case QUEEN: bQueen++;
                                        break;
                            case PAWN: bPawn++;
                                        break;
                            case BISHOP: bBishop++;
                                        break;
                            case KNIGHT: bKnight++;
                                        break;
                            default: break;
                        }
                    }
                    sumPieces=sumPieces+bRook+bQueen+bPawn+bBishop+bKnight;
                    sumBishops=sumBishops+bBishop;
                    sumKnights=sumKnights+bKnight;
                }
                arrayPieces = board.getPieces(ChessPiece.Color.WHITE);
            }

            return  (sumPieces==0) ||
                    (sumPieces==1 && sumBishops==1) ||
                    (sumPieces==1 && sumKnights==1) ||
                    (sumPieces==2 && sumBishops==2 && bBishop==1);
            } 
        
        boolean guardarpartida(File fichero){
            return(board.saveToFile(fichero));
        }
        
        boolean cargarpartida(File fichero){
            return(board.loadFromFile(fichero));
        }
        
        ChessPiece.Color turno (){
            if (jugada%2==0)
                return ChessPiece.Color.WHITE;
            else{
                return ChessPiece.Color.BLACK;
            }
        }
        
} 
