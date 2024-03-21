import chess.*;
import ui.EscapeSequences;
public class Main {
    public static void main(String[] args) {

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
        System.out.print(EscapeSequences.WHITE_QUEEN);
        System.out.print(EscapeSequences.EMPTY);

        System.out.print(EscapeSequences.BLACK_BISHOP);
    }
}