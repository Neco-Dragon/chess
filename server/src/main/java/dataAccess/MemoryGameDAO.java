package dataAccess;

import Exceptions.AlreadyTakenException;
import Exceptions.BadRequestException;
import Exceptions.DataAccessException;
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
    public void clear() throws DataAccessException {
        fakeGameDatabase.clear();
    }

    @Override
    public GameData insertGame(GameData gameData) throws DataAccessException, AlreadyTakenException {
        if (fakeGameDatabase.get(gameData.gameID()) != null){
            throw new AlreadyTakenException();
        }
        fakeGameDatabase.put(gameData.gameID(), gameData);
        return gameData;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException, BadRequestException {
        GameData myGameData = fakeGameDatabase.get(gameID);
        if (myGameData == null){
            throw new BadRequestException();
        }
        return myGameData;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return(new ArrayList<>(fakeGameDatabase.values()));
    }

    @Override
    public void joinGame(int gameID, ChessGame.TeamColor clientColor, String clientUsername) throws BadRequestException, DataAccessException, AlreadyTakenException {
        GameData myGame = fakeGameDatabase.get(gameID);
        GameData myNewGame;
        if (myGame == null){
            throw new BadRequestException();
        }
        if (myGame.whiteUsername() == null && myGame.blackUsername() == null){
            if (clientColor == null){
                return;
            }
        }
        //since records are immutable, we will overwrite it with new information
        if (clientColor == ChessGame.TeamColor.WHITE){
            if (myGame.whiteUsername() != null){
                throw new AlreadyTakenException();
            }
            myNewGame = new GameData(gameID, clientUsername, myGame.blackUsername(), myGame.gameName(), myGame.game());
        }
        else if (clientColor == ChessGame.TeamColor.BLACK) {
            if (myGame.blackUsername() != null){
                throw new AlreadyTakenException();
            }
            myNewGame = new GameData(gameID, myGame.whiteUsername(), clientUsername, myGame.gameName(), myGame.game());
        }
        else {
            myNewGame = new GameData(gameID, myGame.whiteUsername(), myGame.blackUsername(), myGame.gameName(), myGame.game());
        }
        fakeGameDatabase.put(gameID, myNewGame);
    }

    @Override
    public int generateNewGameID() {
        return fakeGameDatabase.size() + 1;
    }
}
