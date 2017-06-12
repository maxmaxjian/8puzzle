import java.util.List;
import java.util.ArrayList;

public class Board {
    private int[][] board;
    
    public Board(int[][] blocks){
	board = new int[blocks.length][blocks[0].length];
	for (int i = 0; i < blocks.length; i++) {
	    for (int j = 0; j < blocks[i].length; j++)
		board[i][j] = blocks[i][j];
	}
    }

    public int dimension() {
	return board.length;
    }
    
    public int hamming() {
	int dist = 0;
	int n = dimension();
	for (int i = 0; i < n; i++) {
	    for (int j = 0; j < n; j++) {
		if (!(i == n-1 && j == n-1) && board[i][j] != i*n+j+1)
		    dist++;
	    }
	}
	return dist;
    }
    
    public int manhattan() {
	int dist = 0;
	int n = dimension();
	for (int i = 0; i < n; i++) {
	    for (int j = 0; j < n; j++) {
		if (board[i][j] != i*n+j+1) {
		    int x = board[i][j]/n, y = board[i][j]%n-1;
		    dist += Math.abs(i-x)+Math.abs(j-y);
		}
	    }
	}
	return dist;
    }
    
    public boolean isGoal() {
	return manhattan() == 0;
    }

    public Board twin() {
	Board res = new Board(this.board);
        int i = 0, j = 0;
        for (; i < dimension(); i++) {
            for (; j < dimension()-1; j++) {
                if (res.board[i][j] != 0 && res.board[i][j+1] != 0)
                    break;
            }
            if (j != dimension()-1)
                break;
        }
        res.swap(i, j, i, j+1);
	return res;
    }

    public boolean equals(Object y) {
	Board other = (Board) y;
	for (int i = 0; i < board.length; i++) {
	    for (int j = 0; j < board[i].length; j++) {
		if (board[i][j] != other.board[i][j])
		    return false;
	    }
	}
	return true;
    }

    public Iterable<Board> neighbors() {
	List<Board> neibs = new ArrayList<Board>();
	int ix1 = 0, ix2 = 0;
	for (ix1 = 0; ix1 < board.length; ix1++) {
	    for (ix2 = 0; ix2 < board[ix1].length; ix2++) {
		if (board[ix1][ix2] == 0)
		    break;
	    }
	    if (board[ix1][ix2] == 0)
		break;
	}

	if (ix1 > 0) {
	    Board next = new Board(board);
	    next.swap(ix1, ix2, ix1-1, ix2);
	    neibs.add(next);
	}
	if (ix1 < board.length-1) {
	    Board next = new Board(board);
	    next.swap(ix1, ix2, ix1+1, ix2);
	    neibs.add(next);
	}
	if (ix2 > 0) {
	    Board next = new Board(board);
	    next.swap(ix1, ix2, ix1, ix2-1);
	    neibs.add(next);
	}
	if (ix2 < board.length-1) {
	    Board next = new Board(board);
	    next.swap(ix1, ix2, ix1, ix2+1);
	    neibs.add(next);
	}
	return neibs;
    }

    private void swap(int i1, int j1, int i2, int j2) {
	int temp = board[i1][j1];
	board[i1][j1] = board[i2][j2];
	board[i2][j2] = temp;
    }

    public String toString() {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < board.length; i++) {
	    for (int j = 0; j < board[i].length; j++) {
		if (j != board.length-1)
		    sb.append(board[i][j]).append(' ');
		else
		    sb.append(board[i][j]).append('\n');
	    }
	}
	return sb.toString();
    }
}
