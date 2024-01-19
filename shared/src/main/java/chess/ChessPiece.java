package chess;

import com.sun.jdi.Value;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private PieceType piece;
    private final ChessGame.TeamColor team;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        team = pieceColor;
        piece = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {return team;}

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {return piece;}

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> moves = new HashSet<ChessMove>();
        ChessPiece piece = board.getPiece(myPosition);
        switch (piece.getPieceType()) {
            case KING:
                break;
            case QUEEN:
                break;
            case ROOK:
                break;
            case BISHOP:
                break;
            case KNIGHT:
                break;
            case PAWN:
                break;
            default:
                System.out.println("Something went wrong");
        }
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return piece == that.piece && team == that.team;
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece, team);
    }

    public ChessGame.TeamColor getOppositeColor(ChessGame.TeamColor color){
        if (color == ChessGame.TeamColor.WHITE) {return ChessGame.TeamColor.BLACK;}
        else {return ChessGame.TeamColor.WHITE;}
    }
    public boolean sameTeam(ChessPiece piece) {return piece.getTeamColor() == this.getTeamColor();}
}
