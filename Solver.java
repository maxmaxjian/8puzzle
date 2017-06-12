import edu.princeton.cs.algs4.MinPQ;
import java.lang.NullPointerException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Solver {
    private class SearchNode implements Comparable<SearchNode> {
	public Board bd;
	public int moves;

	public SearchNode(Board brd, int mvs) {
	    bd = brd;
	    moves = mvs;
	}

	public SearchNode(Board board) {
	    this(board, 0);
	}

	public SearchNode(SearchNode node) {
	    bd = node.bd;
	    moves = node.moves;
	}

	public int compareTo(SearchNode other) {
	    return bd.manhattan()+moves-other.bd.manhattan()+other.moves;
	}
    }
    
    private MinPQ<SearchNode> pq;

    private class TreeNode {
	public SearchNode currNode;
	public List<TreeNode> children;

	public TreeNode(SearchNode node) {
	    currNode = new SearchNode(node);
	    children = new ArrayList<TreeNode>();
	}
    }

    private boolean exist(TreeNode root, Board node) {
	if (root != null) {
	    if (root.currNode.bd.equals(node))
		return true;
	    for (int i = 0; i < root.children.size(); i++) {
		if (exist(root.children.get(i), node))
		    return true;
	    }
	    return false;
	}
	return false;
    }
    
    private TreeNode find(TreeNode root, Board node) {
	if (root != null) {
	    if (root.currNode.bd.equals(node))
		return root;
	    for (int i = 0; i < root.children.size(); i++) {
	        TreeNode res = find(root.children.get(i), node);
		if (res != null)
		    return res;
	    }
	}
	return null;
    }

    private List<SearchNode> pathToSoln(TreeNode root, SearchNode soln) {
	if (root != null) {
	    List<SearchNode> list = new ArrayList<SearchNode>();
	    if (root.currNode.compareTo(soln) == 0) {
		list.add(root.currNode);
		return list;
	    }
	    else {
		for (int i = 0; i < root.children.size(); i++) {
		    List<SearchNode> res = pathToSoln(root.children.get(i), soln);
		    if (res != null) {
			list.add(0, root.currNode);
			return list;
		    }
		}
	    }
	}
	return null;
    }

    private TreeNode tree;
    
    public Solver(Board initial) {
	if (initial == null)
	    throw new NullPointerException();
	pq = new MinPQ<SearchNode>();
	SearchNode node = new SearchNode(initial, 0);
	pq.insert(node);
	tree = new TreeNode(node);
	while (!pq.isEmpty()) {
	    SearchNode curr = pq.delMin();
	    TreeNode currnode = find(tree, curr.bd);
	    Iterable<Board> list = curr.bd.neighbors();
	    Iterator<Board> it = list.iterator();
	    while (it.hasNext()) {
		Board temp = it.next();
		SearchNode newnode = new SearchNode(temp, curr.moves+1);
		if (!exist(tree, newnode.bd)) {
		    currnode.children.add(new TreeNode(newnode));
		    pq.insert(newnode);
		}
	    }
	}
    }

    public boolean isSolvable() {
	return pq.min().bd.isGoal();
    }

    public int moves() {
	return pq.min().moves;
    }

    public Iterable<Board> solution() {
        List<SearchNode> list = pathToSoln(tree, pq.min());
	Iterator<SearchNode> it = list.iterator();
	List<Board> res = new ArrayList<Board>();
	while (it.hasNext())
	    res.add(it.next().bd);
	return res;
    }
}
