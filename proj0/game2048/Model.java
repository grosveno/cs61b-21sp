package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author zyan
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    private int moveAtOneColumn(int col) {
        int n = board.size();
        int pos = n - 1;
        int score = 0;
        for (int row = n - 2; row >= 0; row--) {
            Tile topTile = board.tile(col, pos);
            Tile tile = board.tile(col, row);
            if (tile == null) continue;
            if (topTile == null) {
                board.move(col, pos, tile);
            } else if (topTile.value() == tile.value()) {
                // 如果与可移动到的最顶端的方块合并
                board.move(col, pos, tile);
                score += tile.value() * 2;
                pos--;
            } else {
                // 如果不是null，那么就需要往下调整一格。
                pos--;
                board.move(col, pos, tile);
            }
        }
        return score;
    }

    private int oneColumnCounts(int col) {
        int n = board.size();
        int count = 0;
        for (int row = 0; row < n; row++) {
            Tile tile = board.tile(col, row);
            if (tile != null) {
                count++;
            }
        }
        return count;
    }

    private boolean oneColumnHasChanged(int col) {
        int count = oneColumnCounts(col);
        // 全为空，那么就没有变化
        if (count == 0) {
            return false;
        }

        int n = board.size();
        Tile tile = board.tile(col, n - 1);
        // 最顶上没有方格，且不全为空，说明有移动
        if (tile == null) {
            return true;
        }
        count--;
        // 最顶上有方格
        for (int row = n - 2; row >= 0; row--) {
            // 上面都没法移动，下面没有方块了，故没变化
            if (count == 0) {
                return false;
            }
            Tile adjacentTile = board.tile(col, row);
            // 下面还有方块，故存在移动
            if (adjacentTile == null) {
                return true;
            }
            count--;
            // 存在合并
            if (tile.value() == adjacentTile.value()) {
                return true;
            }
            tile = adjacentTile;
        }
        return false;
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        board.setViewingPerspective(side);

        int n = board.size();
        for (int col = 0; col < n; col++) {
            if (oneColumnHasChanged(col)) {
                changed = true;
            }
            score += moveAtOneColumn(col);
        }
        
        board.setViewingPerspective(Side.NORTH);

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        int n = b.size();
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (b.tile(col, row) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        int n = b.size();
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                Tile tile = b.tile(col, row);
                if (tile != null && tile.value() == MAX_PIECE) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean adjacentEqualExists(Board b, int row, int col) {
        Tile tile = b.tile(row, col);
        if (tile == null) {
            return false;
        }
        int n = b.size();
        int[] rowDirection = {1, 0, 0, -1};
        int[] colDirection = {0, -1, 1, 0};
        for (int i = 0; i < 4; i++) {
            int drow = rowDirection[i];
            int dcol = colDirection[i];
            int newRow = row + drow;
            int newCol = col + dcol;
            if (newRow >= n || newRow < 0 || newCol >= n || newCol < 0) continue;
            Tile adjacentTile = b.tile(newCol, newRow);
            if (adjacentTile != null && adjacentTile.value() == tile.value()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        if (emptySpaceExists(b)) {
            return true;
        }
        int n = b.size();
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (adjacentEqualExists(b, row, col)) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
