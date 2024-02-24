import chess.*;

public class Main {
    public static void main(String[] args) {
        //TODO: Make a new server obj and call run() with port 8080
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}