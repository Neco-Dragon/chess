package dataAccess;

import Exceptions.AlreadyTakenException;
import Exceptions.BadRequestException;
import Exceptions.DataAccessException;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class MySQLGameDAO implements GameDAO {
    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public GameData insertGame(GameData gameData) throws DataAccessException, AlreadyTakenException {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException, BadRequestException {
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(int gameID, GameData gameData) throws DataAccessException, BadRequestException {

    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException, BadRequestException {

    }

    @Override
    public void joinGame(int gameID, ChessGame.TeamColor clientColor, String clientUsername) throws BadRequestException, DataAccessException, AlreadyTakenException {

    }

    @Override
    public int generateNewGameID() {
        return 0;
    }
}
