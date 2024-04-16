package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage{
    public ChessGame game;
    public LoadGame(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
    }

    @Override
    public String toString() {
        return game.toString();
    }
}
