package dataAccess;

import Exceptions.*;
import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    /**
     * Key is the gameID (an Integer) and the value is the GameData
     * */
    HashMap<Integer, GameData> fakeGameDatabase = new HashMap<>();
    @Override
    public void clear() {
        fakeGameDatabase.clear();
    }

    @Override
    public GameData insertGame(GameData gameData) throws AlreadyTakenException {
        fakeGameDatabase.put(gameData.gameID(), gameData);
        return gameData;
    }

    @Override
    public GameData getGame(int gameID) throws BadRequestException {
        return fakeGameDatabase.get(gameID);
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return(new ArrayList<>(fakeGameDatabase.values()));
    }

    @Override
    public void joinGame(int gameID, ChessGame.TeamColor clientColor, String clientUsername) throws BadRequestException, AlreadyTakenException {
        GameData myGame = fakeGameDatabase.get(gameID);
        GameData myNewGame;
        //since records are immutable, we will overwrite it with new information
        if (clientColor == ChessGame.TeamColor.WHITE){
            myNewGame = new GameData(gameID, clientUsername, myGame.blackUsername(), myGame.gameName(), myGame.game());
        }
        else if (clientColor == ChessGame.TeamColor.BLACK) {
            myNewGame = new GameData(gameID, myGame.whiteUsername(), clientUsername, myGame.gameName(), myGame.game());
        }
        else {
            return; //do nothing; they are a spectator;
        }
        fakeGameDatabase.put(gameID, myNewGame);
    }

    @Override
    public int generateNewGameID() {
        return fakeGameDatabase.size() + 1;
    }
}
