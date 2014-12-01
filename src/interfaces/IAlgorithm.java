package interfaces;

import java.util.HashMap;
import java.util.LinkedList;

import model.Node;

public interface IAlgorithm {

	public HashMap<Node, LinkedList<Node>> getPath(Node startNode);


}
