package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board;
    public ChessGame() {
        this.board = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team)  {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (this.board.getPiece(startPosition) == null) {return null;}
        else {
            return this.board.getPiece(startPosition).pieceMoves(this.board, startPosition);
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessBoard hypothetical = this.clone();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (move : allValidMoves(teamColor) {
            ChessPiece pieceOnDestinationSquare = board.
            if (piece)
        }
//        check all end positions of all valid moves of the other team
//        if the end position and the King's location are the same, return True
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) { return isInStalemate(teamColor) && isInCheck(teamColor);}

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return allValidMoves(teamColor).isEmpty();
    }

    public Collection<ChessMove> allValidMoves(TeamColor teamColor) {
        HashSet<ChessMove> allMoves = new HashSet<>();
        for (int i = 0; i < 8; i++){
            for (int j = 0; i < 8; i++){
                ChessPosition thisSquare = new ChessPosition(i, j);
                //If it's that piece's team's turn
                if (board.getPiece(thisSquare).sameTeam(teamColor)) {
                    allMoves.addAll(board.getPiece(thisSquare).pieceMoves(board, thisSquare));
                }
            }
        }
        return allMoves;
        //call validMoves() on each piece of that team color
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {this.board = board;}

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {return this.board;}
}
