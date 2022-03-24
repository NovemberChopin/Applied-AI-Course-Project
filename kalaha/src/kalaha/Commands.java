package kalaha;

/**
 * Command strings that can be sent to and from the game server.
 * 
 * @author Johan Hagelbäck
 */
public class Commands 
{
    /**
     * Make a move.
     */
    public static final String MOVE = "MOVE";
    
    /**
     * Retrieves the player number for a client.
     * 检索客户端的玩家编号。
     */
    public static final String HELLO = "HELLO";
    
    /**
     * Retrieve the current board.
     */
    public static final String BOARD = "BOARD";
    
    /**
     * Retrieve the next player to make a move.
     */
    public static final String NEXT_PLAYER = "PLAYER";
    
    /**
     * Start a new game.
     */
    public static final String NEW_GAME = "NEW";
    
    /**
     * Retrieve the winner of this game.
     */
    public static final String WINNER = "WINNER";
}
