package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    public ChessPiece[][] board;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    public ChessBoard() {
        this.board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() -1 ][position.getColumn() -1] = piece;
    }

    /**
     * Set the pointer at the given position to be null
     *
     * @param position which position
     */
    public void removePiece(ChessPosition position) {
        board[position.getRow() -1 ][position.getColumn() -1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (position.posOutOfBounds()){
            return null;
        }
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    public boolean isSquareEmpty(ChessPosition position){
        return getPiece(position) == null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        this.board[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        this.board[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        this.board[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        this.board[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        this.board[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        this.board[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        this.board[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        this.board[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        for (int i = 0; i < board[1].length; i++){
            this.board[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }
        for (int i = 0; i < board[1].length; i++){
            this.board[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
        this.board[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        this.board[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        this.board[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        this.board[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        this.board[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        this.board[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        this.board[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        this.board[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
    }

    /**
     * Allows us to set the board with new pointers to each object
     */
    public void copyBoardState(ChessBoard myBoard){ //This is used for making a deepcopy of a board
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++){
                ChessPiece pieceOnSquare = myBoard.getPiece(new ChessPosition(i+1,j+1));
                if (pieceOnSquare == null){this.board[i][j] = null;}
                else { //it is essential that we create a new pointer here, otherwise input board is changed at the same time
                    this.board[i][j] = new ChessPiece(pieceOnSquare.getTeamColor(), pieceOnSquare.getPieceType());
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = board.length - 1; i >= 0; i--) {
            ChessPiece[] chessPieces = board[i];
            for (ChessPiece chessPiece : chessPieces) {
                if (chessPiece == null) {
                    stringBuilder.append("| |");
                } else {
                    stringBuilder.append(String.format("|%s|", chessPiece));
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
    /** @return Whether a square is Illegal to move on. Takes into account both board size and friendly pieces, but not check*/
    public boolean squareBlocked(ChessPosition position, ChessPiece pieceWantingToMove){
        if (position.getRow() > 8 || position.getColumn() > 8 || position.getRow() < 1 || position.getColumn() < 1) { //IF square Out of Bounds
            return true;
        } else
            if (this.getPiece(position) == null) { //IF Square is empty
            return false;
        } else return (this.getPiece(position)).sameTeam(pieceWantingToMove); //IF Friendly Fire -> True
    }
    public boolean enemyOnSquare(ChessPosition position, ChessPiece pieceWantingToMove){
        if (this.getPiece(position) == null) return false; //if there is no piece
        return !this.getPiece(position).sameTeam(pieceWantingToMove);
    }
}
