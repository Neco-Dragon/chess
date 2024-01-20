package chess;

public class PLAYGROUND {
    public static void main(String[] args){
        ChessBoard myBoard = new ChessBoard();
        myBoard.resetBoard();
        System.out.println(myBoard);
        ChessPosition testPieceStartSquare = new ChessPosition(1,5);
        ChessPiece testPiece = myBoard.getPiece(testPieceStartSquare);
        System.out.println(ChessPiece.getKingMoves(myBoard, testPieceStartSquare));
    }
}
