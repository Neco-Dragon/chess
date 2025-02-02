package dataAccess;

import Exceptions.AlreadyTakenException;
import Exceptions.BadRequestException;
import Exceptions.DataAccessException;
import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

public interface GameDAO {

    /**Clears all game data.
     * @throws DataAccessException if an error occurs during data access
     */
    void clear() throws DataAccessException;

    /**Inserts a new game into the database.
     * @param gameData the game data to insert
     * @return the inserted game data
     * @throws DataAccessException if an error occurs during data access
     */
    GameData insertGame(GameData gameData) throws DataAccessException, AlreadyTakenException;

    /**Retrieves a game by its ID.
     * @param gameID the ID of the game to get
     * @return the retrieved game data
     * @throws DataAccessException if an error occurs during data access
     * @throws BadRequestException if the game is not in the Database
     */
    GameData getGameData(int gameID) throws DataAccessException, BadRequestException;

    /**Lists all games stored in the database.
     * @return the list of game data
     * @throws DataAccessException if an error occurs during data access
     */
    ArrayList<GameData> listGames() throws DataAccessException;

    /**Joins a game with the specified ID and assigns the client a color.
     * @param gameID      the ID of the game to join
     * @param clientColor ChessGame.TeamColor {WHITE, BLACK}
     * @param clientUsername username of the client, which will be added into the given color's username parameter
     */

    void joinGame(int gameID, ChessGame.TeamColor clientColor, String clientUsername) throws BadRequestException, DataAccessException, AlreadyTakenException;
    int getSize() throws DataAccessException;
    void updateGame(GameData gameData) throws DataAccessException, SQLException;
}
