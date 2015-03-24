package interfaces;

import java.util.HashMap;
import java.util.LinkedList;

import model.Node;

/**
 * Interfece for algorithms.
 * getPath must return a linkedlist with the path between the start and end node
 * @author Thomas
 *
 */
public interface IAlgorithm {

	public LinkedList getPath(Node startNode, Node endNode);


}
