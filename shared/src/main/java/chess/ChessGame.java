package chess;

import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;
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

    private boolean isOver;
    public ChessGame() {
        this.board = new ChessBoard();
        board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
        this.isOver = false; //TODO: Fisnish
    }

    public ChessGame(ChessBoard boardState) {
        this.board = new ChessBoard();
        this.board.copyBoardState(boardState);
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

    public static TeamColor getEnemyTeam(TeamColor teamColor){
        return teamColor.equals(TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        HashSet<ChessMove> myMoves = new HashSet<>();
        ChessPiece myPiece = this.board.getPiece(startPosition);
        if (myPiece == null) {return null;}
        for (ChessMove move : myPiece.pieceMoves(this.board, startPosition)){
            ChessGame hypotheticalGame = new ChessGame(board); //make a new hypothetical chess game
            hypotheticalGame.makeMoveByForce(move); //make a move on our hypothetical board
            if (!hypotheticalGame.isInCheck(myPiece.getTeamColor())){ //if I don't put myself in check, it's valid
                myMoves.add(move);
            }
        }
        return myMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece myPiece = board.getPiece(move.getStartPosition());
        if (move.getStartPosition().posOutOfBounds() || move.getEndPosition().posOutOfBounds()){
            throw new InvalidMoveException("Move is out of bounds");
        }
        else if (myPiece == null){
            throw new InvalidMoveException("No piece on that square");
        }
        else if (myPiece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("It's not your turn!");
        }
        else if (!allValidMoves(myPiece.getTeamColor()).contains(move)){
            throw new InvalidMoveException("Illegal Move");
        }
        else makeMoveByForce(move);
    }

    /**
     * Makes a move in a chess game, regardless of whether it's legal. Will stomp on other piece pointers
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMoveByForce(ChessMove move) {
        this.board.addPiece(move.getEndPosition(), this.board.getPiece(move.getStartPosition()));
        this.board.removePiece(move.getStartPosition()); //after the piece gets added, there's a copy of it, so this deletes the copy from the old square
        //If it's a promoting pawn...
        if (move.getPromotionPiece() != null){
            this.board.getPiece(move.getEndPosition()).setPieceType(move.getPromotionPiece());
        }
        teamTurn = getEnemyTeam(board.getPiece(move.getEndPosition()).getTeamColor()); //switch who's turn it is
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //since you can never capture your own piece, we only need to check if the final destination
        // HAS a piece and if that piece is a king
        for (ChessMove move : allPossibleMoves(getEnemyTeam(teamColor))){
            ChessPiece pieceOnDestinationSquare = this.board.getPiece(move.getEndPosition());
            if (pieceOnDestinationSquare == null) {continue;}
            else if (pieceOnDestinationSquare.getPieceType() == ChessPiece.PieceType.KING){
                return true;
            }
        }
        return false;
//        check all end positions of all valid moves of the other team
//        if the end position and the King's location are the same, return True
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInStalemate(teamColor) && isInCheck(teamColor);}

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
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition thisSquare = new ChessPosition(i, j);
                if (!getBoard().isSquareEmpty(thisSquare)
                        && board.getPiece(thisSquare).sameTeam(teamColor)) {
                    allMoves.addAll(validMoves(thisSquare));
                }
            }
        }
        return allMoves;
    }
    /**
     *
     *
     * @param teamColor which team to check moves for
     * @return All moves, irrespective of whether they cause a king to be left in danger
     */
    public Collection<ChessMove> allPossibleMoves(TeamColor teamColor) {
        HashSet<ChessMove> allMoves = new HashSet<>();
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition thisSquare = new ChessPosition(i, j);
                if (!board.isSquareEmpty(thisSquare)
                        && board.getPiece(thisSquare).sameTeam(teamColor)) {
                    allMoves.addAll(board.getPiece(thisSquare).pieceMoves(board, thisSquare));
                }
            }
        }
        return allMoves;
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
