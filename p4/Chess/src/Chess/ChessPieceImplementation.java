package Chess;

public class ChessPieceImplementation implements ChessPiece{

Color colorPieza;
Type tipoPieza;
boolean movida;

    public ChessPieceImplementation() {
    }

    public ChessPieceImplementation(Color colorPieza, Type tipoPieza) {
        this.colorPieza = colorPieza;
        this.tipoPieza = tipoPieza;
    }
     
    @Override
    public Color getColor() {
        return this.colorPieza;
    }

    @Override
    public Type getType() {
        return this.tipoPieza;
    }    
    
    @Override
    public void notifyMoved() {
        this.movida=true;
    }

    @Override
    public boolean wasMoved() {
        return this.movida;
    }

    @Override
    public PiecePosition[] getAvailablePositions(ChessBoard aBoard) {
        PiecePosition [] posibletab;
    
        switch (this.tipoPieza){
            case PAWN: {
                posibletab=ChessMovementManager.getAvailablePositionsOfPawn(this, aBoard);
                return posibletab;
            }
            case KNIGHT: {
                posibletab=ChessMovementManager.getAvailablePositionsOfKnight(this, aBoard);
                return posibletab;
            }
            case BISHOP:{
                posibletab=ChessMovementManager.getAvailablePositionsOfBishop(this, aBoard);
                return posibletab;
            }
            case ROOK: {
                posibletab=ChessMovementManager.getAvailablePositionsOfRook(this, aBoard);
                return posibletab;
            }
            case KING: {
                posibletab=ChessMovementManager.getAvailablePositionsOfKing(this, aBoard);
                return posibletab;
            }
            case QUEEN: {
                posibletab=ChessMovementManager.getAvailablePositionsOfQueen(this, aBoard);
                return posibletab;
            }
            default:{
                return null;
            }
        }
    }
}