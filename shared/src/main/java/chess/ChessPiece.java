package chess;

import com.sun.jdi.Value;

import java.util.*;

import static chess.ChessGame.TeamColor.BLACK;
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
                return getQueenMoves(board, myPosition);
            case ROOK:
                return getRookMoves(board, myPosition);
            case BISHOP:
                return getBishopMoves(board, myPosition);
            case KNIGHT:
                return getKnightMoves(board, myPosition);
            case PAWN:
                return getPawnMoves(board, myPosition);
            default:
                System.out.println("Imaginary Piece Type");
        }
        return moves;
    }

    public static List<ChessPosition> posFromIntArray(int[][] coords){
        List<ChessPosition> positions = new ArrayList<>();
        for (int[] coord : coords) {
            positions.add(new ChessPosition(coord[0], coord[1]));
        }
        return positions;
    }
    public static Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition kingPosition) {
        List<ChessMove> moves = new ArrayList<>();
        int[][] relativeKingMoves = {
                {1, 1},  {1, 0}, {1, -1}, {0, 1},
                {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}
        };
        List<ChessPosition> positions = ChessPiece.posFromIntArray(relativeKingMoves);
        List<ChessPosition> newPositions = new ArrayList<>();
        ChessPiece theKing = board.getPiece(kingPosition);

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
        List<ChessMove> moves = new ArrayList<>();
        moves.addAll(getBishopMoves(board, queenPosition));
        moves.addAll(getRookMoves(board, queenPosition));
        return moves;
    }

    public static Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition bishopPosition) {
        List<ChessMove> moves = new ArrayList<>();
        int[][] diagonalUpLeftMoves = {
                {-1, 1}, {-2, 2}, {-3, 3}, {-4, 4}, {-5, 5}, {-6, 6}, {-7, 7}
        };
        int[][] diagonalDownLeftMoves = {
                {-1, -1}, {-2, -2}, {-3, -3}, {-4, -4}, {-5, -5}, {-6, -6}, {-7, -7}
        };
        int[][] diagonalDownRightMoves = {
                {1, -1}, {2, -2}, {3, -3}, {4, -4}, {5, -5}, {6, -6}, {7, -7}
        };
        int[][] diagonalUpRightMoves = {
                {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7}
        };
        List<ChessPosition> diagonalUpLeftMovesPositions = ChessPiece.posFromIntArray(diagonalUpLeftMoves);
        List<ChessPosition> diagonalDownRightMovesPositions = ChessPiece.posFromIntArray(diagonalDownRightMoves);
        List<ChessPosition> diagonalDownLeftMovesPositions = ChessPiece.posFromIntArray(diagonalDownLeftMoves);
        List<ChessPosition> diagonalUpRightMovesPositions = ChessPiece.posFromIntArray(diagonalUpRightMoves);
        List<ChessPosition> newDiagonalDownRightMovesPositions = new ArrayList<>();
        List<ChessPosition> newDiagonalDownLeftMovesPositions = new ArrayList<>();
        List<ChessPosition> newDiagonalUpRightMovesPositions = new ArrayList<>();
        List<ChessPosition> newDiagonalUpLeftMovesPositions = new ArrayList<>();
        ChessPiece theBishop = board.getPiece(bishopPosition);
        for (ChessPosition position : diagonalUpLeftMovesPositions) {
            newDiagonalUpLeftMovesPositions.add(bishopPosition.newRelativeChessPosition(position.getRow(), position.getColumn()));
        }
        for (ChessPosition position : diagonalDownRightMovesPositions) {
            newDiagonalDownRightMovesPositions.add(bishopPosition.newRelativeChessPosition(position.getRow(), position.getColumn()));
        }
        for (ChessPosition position : diagonalDownLeftMovesPositions) {
            newDiagonalDownLeftMovesPositions.add(bishopPosition.newRelativeChessPosition(position.getRow(), position.getColumn()));
        }
        for (ChessPosition position : diagonalUpRightMovesPositions) {
            newDiagonalUpRightMovesPositions.add(bishopPosition.newRelativeChessPosition(position.getRow(), position.getColumn()));
        }
        List<List<ChessPosition>> allMoveDirections = new ArrayList<>();
        allMoveDirections.add(newDiagonalUpLeftMovesPositions);
        allMoveDirections.add(newDiagonalDownRightMovesPositions);
        allMoveDirections.add(newDiagonalDownLeftMovesPositions);
        allMoveDirections.add(newDiagonalUpRightMovesPositions);
        for (List<ChessPosition> movesInDirection : allMoveDirections) {
            for (ChessPosition position : movesInDirection) {
                if (board.squareBlocked(position, theBishop)) {
                    break;
                }
                if (board.enemyOnSquare(position, theBishop)) {
                    moves.add(new ChessMove(bishopPosition, position));
                    break;
                }
                else {
                    moves.add(new ChessMove(bishopPosition, position));
                }
            }
        }

        return moves;
    }

    public static Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition knightPosition) {
        List<ChessMove> moves = new ArrayList<>();
        int[][] relativeKnightMoves = {
                {1, 2}, {2, 1},
                {-1, 2}, {2, -1},
                {1, -2}, {-2, 1},
                {-1, -2}, {-2, -1}
        };
        List<ChessPosition> positions = ChessPiece.posFromIntArray(relativeKnightMoves);
        List<ChessPosition> newPositions = new ArrayList<>();
        ChessPiece theKnight = board.getPiece(knightPosition);

        for (ChessPosition position : positions) {
            newPositions.add(knightPosition.newRelativeChessPosition(position.getRow(), position.getColumn()));
        }
        for (ChessPosition position : newPositions){
            if (!board.squareBlocked(position, theKnight)){
                moves.add(new ChessMove(knightPosition, position));
            }
        }
        return moves;
    }

    public static Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition rookPosition) {
        List<ChessMove> moves = new ArrayList<>();
        int[][] downMoves = {
                {-1, 0}, {-2, 0}, {-3, 0}, {-4, 0}, {-5, 0}, {-6, 0}, {-7, 0}
        };
        int[][] upMoves = {
                {1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0}
        };
        int[][] leftMoves = {
                {0, -1}, {0, -2}, {0, -3}, {0, -4}, {0, -5}, {0, -6}, {0, -7}
        };
        int[][] rightMoves = {
                {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {0, 7}
        };
        List<ChessPosition> downPositions = ChessPiece.posFromIntArray(downMoves);
        List<ChessPosition> upPositions = ChessPiece.posFromIntArray(upMoves);
        List<ChessPosition> leftPositions = ChessPiece.posFromIntArray(leftMoves);
        List<ChessPosition> rightPositions = ChessPiece.posFromIntArray(rightMoves);
        List<ChessPosition> newDownPositions = new ArrayList<>();
        List<ChessPosition> newUpPositions = new ArrayList<>();
        List<ChessPosition> newLeftPositions = new ArrayList<>();
        List<ChessPosition> newRightPositions = new ArrayList<>();
        ChessPiece theRook = board.getPiece(rookPosition);
        for (ChessPosition position : downPositions) {
            newDownPositions.add(rookPosition.newRelativeChessPosition(position.getRow(), position.getColumn()));
        }
        for (ChessPosition position : upPositions) {
            newUpPositions.add(rookPosition.newRelativeChessPosition(position.getRow(), position.getColumn()));
        }
        for (ChessPosition position : leftPositions) {
            newLeftPositions.add(rookPosition.newRelativeChessPosition(position.getRow(), position.getColumn()));
        }
        for (ChessPosition position : rightPositions) {
            newRightPositions.add(rookPosition.newRelativeChessPosition(position.getRow(), position.getColumn()));
        }
        List<List<ChessPosition>> allMoveDirections = new ArrayList<>();
        allMoveDirections.add(newDownPositions);
        allMoveDirections.add(newUpPositions);
        allMoveDirections.add(newLeftPositions);
        allMoveDirections.add(newRightPositions);
        for (List<ChessPosition> movesInDirection : allMoveDirections) {
            for (ChessPosition position : movesInDirection) {
                if (board.squareBlocked(position, theRook)) {
                    break;
                }
                if (board.enemyOnSquare(position, theRook)) {
                    moves.add(new ChessMove(rookPosition, position));
                    break;
                }
                else {
                    moves.add(new ChessMove(rookPosition, position));
                }
            }
        }

        return moves;
    }

    public static Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition pawnPosition) {
        List<ChessMove> moves = new ArrayList<>();
        ChessPiece thePawn = board.getPiece(pawnPosition);
        int [][] regularMoves;
        int [][] captureMoves;

        if (thePawn.getTeamColor() == WHITE){
            captureMoves = new int[][]{{1,1}, {1,-1}};
            if (ChessPiece.onStartingSquare(thePawn, pawnPosition)){
                regularMoves = new int[][]{{1, 0}, {2, 0}};
            }
            else{
                regularMoves = new int[][]{{1, 0}};
            }
        }
        else{ //Pawn is Black
            captureMoves = new int[][]{{-1,1}, {-1,-1}};
            if (ChessPiece.onStartingSquare(thePawn, pawnPosition)){
                regularMoves = new int[][]{{-1, 0}, {-2, 0}};
            }
            else{
                regularMoves = new int[][]{{-1, 0}};
            }
        }

        List<ChessPosition> capturePositions = ChessPiece.posFromIntArray(captureMoves);
        List<ChessPosition> regularPositions = ChessPiece.posFromIntArray(regularMoves);
        List<ChessPosition> newCapturePositions = new ArrayList<>();
        List<ChessPosition> newRegularPositions = new ArrayList<>();
        List<ChessPosition> finalPositions = new ArrayList<>();

        for (ChessPosition capturePosition : capturePositions) {
            newCapturePositions.add(capturePosition.newRelativeChessPosition(pawnPosition.getRow(), pawnPosition.getColumn()));
        }
        for (ChessPosition regularPosition : regularPositions) {
            newRegularPositions.add(regularPosition.newRelativeChessPosition(pawnPosition.getRow(), pawnPosition.getColumn()));
        }


        for (ChessPosition capturePosition : newCapturePositions) {
            if (board.enemyOnSquare(capturePosition, thePawn)){
                finalPositions.add(capturePosition);
            }
        }
        for (ChessPosition regularPosition : newRegularPositions) {
            if (board.squareBlocked(regularPosition, thePawn) || board.enemyOnSquare(regularPosition, thePawn)) {
                break;
            }
            else {finalPositions.add(regularPosition);}
        }

        if (onPromotionSquare(thePawn, pawnPosition)){
            for (ChessPosition finalPosition : finalPositions) {
                moves.add(new ChessMove(pawnPosition, finalPosition, PieceType.QUEEN));
                moves.add(new ChessMove(pawnPosition, finalPosition, PieceType.BISHOP));
                moves.add(new ChessMove(pawnPosition, finalPosition, PieceType.KNIGHT));
                moves.add(new ChessMove(pawnPosition, finalPosition, PieceType.ROOK));
            }

        }
        else {
            for (ChessPosition finalPosition : finalPositions) {
                moves.add(new ChessMove(pawnPosition, finalPosition));
            }
        }
        return moves;
    }

    private static boolean onStartingSquare(ChessPiece pawn, ChessPosition pawnPosition){
        return ((pawn.getTeamColor()==WHITE && pawnPosition.getRow() == 2) || (pawn.getTeamColor()==BLACK && pawnPosition.getRow() == 7));
    }
    /**@return returns true if the given pawn at its current given position is on the 7th or 2nd rank, just before promotion*/
    private static boolean onPromotionSquare(ChessPiece pawn, ChessPosition pawnPosition){
        return ((pawn.getTeamColor()==WHITE && pawnPosition.getRow() == 7) || (pawn.getTeamColor()==BLACK && pawnPosition.getRow() == 2));
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
    public boolean sameTeam(ChessGame.TeamColor color) {return this.getTeamColor() == color;}

}
