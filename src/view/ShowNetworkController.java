package view;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Graphs;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.graph.util.TestGraphs;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationModel;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import model.Network;
import model.Node;
import model.ReachableList;
import model.ReachableNodes;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.stage.Stage;

/**
 *  A sample showing how to use JUNG's layout classes to position vertices
 * in a graph.
 * @author jeffreyguenther
 * @author timheng
 */


class Edge{
	int key;
	Pair<Integer> p;
	
}
public class ShowNetworkController extends Application {
	private Stage primaryStage;
    private static final int CIRCLE_SIZE = 5; // default circle size
    private  boolean debug = false;
    
    Group root;
    Scene scene;
    
    @Override
    public void start(Stage stage) {
        // setup up the scene.
         root = new Group();
         scene = new Scene(root, 800, 800, Color.WHITE);

        Group viz1 = new Group();
    
        // create a sample graph using JUNG's TestGraphs class.
     //   Graph graph1 = new TestGraphs().getSmallGraph();
		//aph graph1 = TestGraphs.getOneComponentGraph();
		//graph1.removeEdge();
        Graph graph1 = new UndirectedSparseMultigraph();	
        
        ArrayList<Node> nodes = Network.getInstance().getAllNodes();
        ArrayList<ReachableList> edges = Network.getInstance().getNodesToReach();
        for (int i = 0; i < nodes.size(); i++) {
         	 int id = nodes.get(i).getId();
        	 graph1.addVertex(id);
		}
        for (int i = 0; i < edges.size(); i++) {
        	LinkedList ll = edges.get(i).getLl();
        	ListIterator li = ll.listIterator();
        	
        	Node first = edges.get(i).getN();
        	for(int j= 0; li.hasNext(); j++){
        		
        		ReachableNodes n = (ReachableNodes) li.next();
        		
        		graph1.addEdge(new Edge(), first.getId(), n.getN().getId());
        		
        		//graph1.addEdge(new Edge(), first, n);
        		//graph1.addEdge(new Edge(), first, n);
        	}
        }
       	   
        // define the layout we want to use for the graph
        // The layout will be modified by the VisualizationModel
        Layout circleLayout = new CircleLayout(graph1);

        /*
         * Define the visualization model. This is how JUNG calculates the layout
         * for the graph. It updates the layout object passed in.
         */
        VisualizationModel vm1 = new DefaultVisualizationModel(circleLayout, new Dimension(800, 800));
        
        // draw the graph
        renderGraph(graph1, circleLayout, viz1);

        root.getChildren().add(viz1);


        stage.setTitle("Visualization of the Network");
        stage.setScene(scene);
        stage.show();

    }
    
    /**
     * Render a graph to a particular Group
     * @param graph
     * @param layout
     * @param viz 
     */
    private void renderGraph(Graph graph, Layout layout, Group viz) {
        // draw the vertices in the graph
        Collection c = graph.getVertices();
     		Iterator it = c.iterator();
     		int i =0;
        for (Object v : graph.getVertices()) {
            // Get the position of the vertex
            Point2D p = (Point2D) layout.transform(v);
     
    		i = (int) it.next();
            // draw the vertex as a circle
            Circle circle = CircleBuilder.create()
                    .centerX(p.getX())
                    .centerY(p.getY())
                    .radius(CIRCLE_SIZE)
                    .build();
            
           
			if(debug)
            System.out.println(i+ " "+ p.getX() +" " + p.getY());
			Label l = new Label(Integer.toString(i));
			if(p.getY() > 400){
				if(p.getX() > 400){
					l.setLayoutX(p.getX()+10);
					l.setLayoutY(p.getY());
				}
					else{
						l.setLayoutX(p.getX()-20);
						l.setLayoutY(p.getY());
					}
			}else{
				if(p.getX() > 400){
					l.setLayoutX(p.getX()+10);
					l.setLayoutY(p.getY()-10);
				} else{
					l.setLayoutX(p.getX()-20);
					l.setLayoutY(p.getY()-10);
				}
			}
			 viz.getChildren().add(l);
            // add it to the group, so it is shown on screen
            viz.getChildren().add(circle);
        }

        // draw the edges
        for (Object n : graph.getEdges()) {
            // get the end points of the edge
            Pair endpoints = graph.getEndpoints(n);
            
            // Get the end points as Point2D objects so we can use them in the 
            // builder
            Point2D pStart = (Point2D) layout.transform(endpoints.getFirst());
            Point2D pEnd = (Point2D) layout.transform(endpoints.getSecond());
            
            // Draw the line
            Line line = LineBuilder.create()
                    .startX(pStart.getX())
                    .startY(pStart.getY())
                    .endX(pEnd.getX())
                    .endY(pEnd.getY())
                    .build();
            // add the edges to the screen
            viz.getChildren().add(line);
        }
    }

	public void setStage(Stage stage) {
		this.primaryStage = stage;
		
	}
}