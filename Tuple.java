/*
 * Com S 311 
 * PA1
 * created by: Isaac Holtkamp
 */
package wikiCrawler;

import java.util.ArrayList;
import java.util.LinkedList;

public class Tuple {
	private String str;
	private int priority;
	
	public Tuple(String str, int priority) {
		this.priority = priority;
		this.str = str;
	}
	public String get_string() {return str;}
	public int get_priority (){return priority;}
}
