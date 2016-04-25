package Chess;

import java.io.File;import java.io.FileWriter;

import java.util.*;
import java.io.PrintWriter;
import java.io.FileReader;


public class ChessBoardImplementation implements ChessBoard {

	ChessPiece	pieces[] = new ChessPiece[8 * 8];
	       
        public ChessBoardImplementation() {
		for (int i = 0; i < 8; i++) {
			pieces[getPieceIndex(i, 1)] = new ChessPieceImplementation(ChessPiece.Color.WHITE, ChessPiece.Type.PAWN);
			pieces[getPieceIndex(i, 6)] = new ChessPieceImplementation(ChessPiece.Color.BLACK, ChessPiece.Type.PAWN);
		}
		pieces[getPieceIndex(0, 0)] = new ChessPieceImplementation(ChessPiece.Color.WHITE, ChessPiece.Type.ROOK);
		pieces[getPieceIndex(7, 0)] = new ChessPieceImplementation(ChessPiece.Color.WHITE, ChessPiece.Type.ROOK);
		pieces[getPieceIndex(0, 7)] = new ChessPieceImplementation(ChessPiece.Color.BLACK, ChessPiece.Type.ROOK);
		pieces[getPieceIndex(7, 7)] = new ChessPieceImplementation(ChessPiece.Color.BLACK, ChessPiece.Type.ROOK);

		pieces[getPieceIndex(1, 0)] = new ChessPieceImplementation(ChessPiece.Color.WHITE, ChessPiece.Type.KNIGHT);
		pieces[getPieceIndex(6, 0)] = new ChessPieceImplementation(ChessPiece.Color.WHITE, ChessPiece.Type.KNIGHT);
		pieces[getPieceIndex(1, 7)] = new ChessPieceImplementation(ChessPiece.Color.BLACK, ChessPiece.Type.KNIGHT);
		pieces[getPieceIndex(6, 7)] = new ChessPieceImplementation(ChessPiece.Color.BLACK, ChessPiece.Type.KNIGHT);

		pieces[getPieceIndex(2, 0)] = new ChessPieceImplementation(ChessPiece.Color.WHITE, ChessPiece.Type.BISHOP);
		pieces[getPieceIndex(5, 0)] = new ChessPieceImplementation(ChessPiece.Color.WHITE, ChessPiece.Type.BISHOP);
		pieces[getPieceIndex(2, 7)] = new ChessPieceImplementation(ChessPiece.Color.BLACK, ChessPiece.Type.BISHOP);
		pieces[getPieceIndex(5, 7)] = new ChessPieceImplementation(ChessPiece.Color.BLACK, ChessPiece.Type.BISHOP);

		pieces[getPieceIndex(3, 0)] = new ChessPieceImplementation(ChessPiece.Color.WHITE, ChessPiece.Type.KING);
		pieces[getPieceIndex(4, 0)] = new ChessPieceImplementation(ChessPiece.Color.WHITE, ChessPiece.Type.QUEEN);
		pieces[getPieceIndex(4, 7)] = new ChessPieceImplementation(ChessPiece.Color.BLACK, ChessPiece.Type.QUEEN);
		pieces[getPieceIndex(3, 7)] = new ChessPieceImplementation(ChessPiece.Color.BLACK, ChessPiece.Type.KING);
	}
	   
        
	@Override
	public ChessPiece[] getPieces(ChessPiece.Color PieceColor) {
		int count = 0;
		for (ChessPiece piece : pieces)
			if (piece != null && piece.getColor() == PieceColor)
				count++;

		if (count == 0)
			return null;
		
		ChessPiece[] result = new ChessPiece[count];
		count = 0;
		for (ChessPiece piece : pieces)
			if (piece != null && piece.getColor() == PieceColor)
				result[count++] = piece;

		return result;
	}
	
	private	int getPieceIndex(int column, int row) {
		return row * 8 + column;
	}

	private	int getPieceIndex(PiecePosition position) {
		return position.getRow() * 8 + position.getColumn();
	}

	private	ChessPiece getPiece(int column, int row) {
		int index = getPieceIndex(column, row);
                if (PiecePosition.isAvailable(column, row))
                    return pieces[index];
                else
                    return null;
	}

	@Override
	public ChessPiece getPieceAt(PiecePosition position) {
		if (!PiecePosition.isAvailable(position))
			return null;
		return getPiece(position.getColumn(), position.getRow());
	}

	@Override
	public PiecePosition getPiecePosition(ChessPiece APiece) {
		for (int row = 0; row < 8; row++)
			for (int column = 0; column < 8; column++)
				if (getPiece(column, row) == APiece)
					return new PiecePosition(column, row);
		return null;
	}

	@Override
	public void removePieceAt(PiecePosition Position) {
            if (PiecePosition.isAvailable(Position))
		pieces[getPieceIndex(Position.getColumn(), Position.getRow())] = null;
	}
        
	public void convertQueen(ChessPiece Piece, PiecePosition Position) {
            pieces[getPieceIndex(Position.getColumn(), Position.getRow())] = new ChessPieceImplementation (Piece.getColor(), ChessPiece.Type.QUEEN);
	}

	@Override
	public boolean movePieceTo(ChessPiece Piece, PiecePosition Position) {
		PiecePosition oldPosition = getPiecePosition(Piece);
		if (oldPosition != null) {
                    if (PiecePosition.isAvailable(oldPosition) && PiecePosition.isAvailable(Position)) {
                            int oldIndex = getPieceIndex(oldPosition);
                            int newIndex = getPieceIndex(Position);
                            pieces[oldIndex] = null;
                            if (PiecePosition.isAvailable(Position)){
                                if ((Position.getRow()==7 && Piece.getType()==ChessPiece.Type.PAWN && Piece.getColor()==ChessPiece.Color.WHITE)||
                                     (Position.getRow()==0 && Piece.getType()==ChessPiece.Type.PAWN && Piece.getColor()==ChessPiece.Color.BLACK)){
                                    convertQueen (Piece, Position);
                                }               
                                else
                                    pieces[newIndex] = Piece;
                            }
                            Piece.notifyMoved();
                            return true;
                    }
		}
		return false;
	}

	@Override
	public boolean containsKing(ChessPiece.Color PieceColor) {
		for (ChessPiece piece : pieces) 
			if (piece != null && piece.getType() == ChessPiece.Type.KING && piece.getColor() == PieceColor)
				return true;
		return false;
	}


	@Override
	public boolean loadFromFile(File location) {
            File fichero = new File(location.toString());
            String [] linea;
            Scanner ruta = null;

            try {
                    ruta = new Scanner(fichero);
                    while (ruta.hasNextLine()) {
			linea = ruta.nextLine().split(",");
                        //System.out.println(linea);      
                    }
                   

		} catch (Exception ex) {
			System.out.println("ERROR AL CARGAR PARTIDA!" + ex.getMessage());
                        return false;
		}
	
                return true;
        }

    @Override
    public boolean saveToFile(File location) {
        FileWriter fichero;
        
            try {
                fichero = new FileWriter(location);
                for(ChessPiece piece: pieces) {
                    if (piece!=null){
                        fichero.write
                        (getPiecePosition(piece).getColumn()+" "+getPiecePosition(piece).getRow()+" "+
                        piece.getColor().toString()+" "+piece.getType().toString()+","                                            
                        +"\n");
                    }
                }
                fichero.close();
                return true;
	} catch (Exception ex) {
            System.out.println("ERROR AL GUARDAR PARTIDA!" + ex.getMessage());
            return false;
	}
    }
    public ChessBoardImplementation(ChessPiece [] partidaguardada){
        ChessPiece pieces[] = new ChessPiece[8 * 8];
        
    }
}
