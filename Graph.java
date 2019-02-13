/*
 * Com S 311 
 * PA1
 * created by: Isaac Holtkamp
 */
package wikiCrawler;

import java.util.ArrayList;
import java.util.LinkedList;

public class Graph {

	private static ArrayList<String> discovered;
	private static ArrayList<String> used;
	private static LinkedList<Vertex> graph;
		
	public Graph(ArrayList<String> discovered, ArrayList<String> used, LinkedList<Vertex> graph) {
		this.discovered = discovered;
		this.used = used;
		this.graph = graph;
	}
	public ArrayList<String> get_discovered() {return discovered;}
	public ArrayList<String> get_used_strings() {return used;}
	public LinkedList<Vertex> get_points() {return graph;}
	public void add_discovered(String discovered) {this.discovered.add(discovered);}
	public void add_used(String used) {this.used.add(used);}
	public void add_point(Vertex point) {this.graph.add(point);}
}
