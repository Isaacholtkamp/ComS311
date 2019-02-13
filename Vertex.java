/*
 * Com S 311 
 * PA1
 * created by: Isaac Holtkamp
 */
package wikiCrawler;

import java.util.ArrayList;
import java.util.LinkedList;

public class Vertex {
	private String page;
	private String contents;
	private ArrayList<String> edges;
	
	public Vertex(String page, String contents, ArrayList <String> edges) {
		this.edges = edges;
		this.page = page;
		this.contents = contents;
	}
	public String get_page() {return page;}
	public String get_contents() {return contents;}
	public ArrayList<String> get_edges() {return edges;}
	public void set_edges(ArrayList<String> edges) {this.edges = edges;}
}
