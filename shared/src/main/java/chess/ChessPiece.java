package chess;

import com.sun.jdi.Value;

import javax.swing.text.Position;
import java.util.*;

import static chess.ChessGame.TeamColor.WHITE;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final PieceType piece;
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
        PAWN;
        @Override
        public String toString() {
            if (this == KNIGHT) {return "N";}
            else {return name().substring(0, 1);}
        }
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
        List<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);
        switch (piece.getPieceType()) {
            case KING:
                return getKingMoves(board, myPosition);
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

    public static Collection<ChessPosition> posFromIntArray(int[][] coords){
        List<ChessPosition> positions = new ArrayList<>();
        for (int[] coord : coords) {
            positions.add(new ChessPosition(coord[0], coord[1]));
        }
        return positions;
    }
    public static Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition kingPosition) {
        List<ChessMove> moves = new ArrayList<>();
        List<ChessPosition> positions = new ArrayList<>();
        List<ChessPosition> newPositions = new ArrayList<>();
        ChessPiece theKing = board.getPiece(kingPosition);
        positions.add(new ChessPosition(1, 1));
        positions.add(new ChessPosition(1, -1));
        positions.add(new ChessPosition(-1, 0));
        positions.add(new ChessPosition(-1, 1));
        positions.add(new ChessPosition(0, -1));
        positions.add(new ChessPosition(1, 0));
        positions.add(new ChessPosition(-1, -1));
        positions.add(new ChessPosition(0, 1));
        for (ChessPosition position : positions) {
            newPositions.add(kingPosition.newRelativeChessPosition(position.getRow(), position.getColumn()));
        }
        for (ChessPosition position : newPositions){
            if (!board.squareBlocked(position, theKing)){
                moves.add(new ChessMove(kingPosition, position));
            }
        }
        return moves;
    }

    public static Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition queenPosition) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    public static Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition bishopPosition) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    public static Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition knightPosition) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    public static Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition rookPosition) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    public static Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition pawnPosition) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }


    @Override
    public String toString() {
        return switch (this.getTeamColor()) {
            case ChessGame.TeamColor.BLACK -> this.getPieceType().toString().toLowerCase();
            case ChessGame.TeamColor.WHITE -> this.getPieceType().toString();
            default -> "?";
        };
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

    public boolean sameTeam(ChessPiece piece) {return piece.getTeamColor() == this.getTeamColor();}

}
