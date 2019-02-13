/*
 * Com S 311 
 * PA1
 * created by: Isaac Holtkamp
 */
package priorityQ;

public class Entry {
	private String value;
	private int priority;
	
	public Entry(String s, int p) {
		this.value = s;
		this.priority = p;
	}
	public String get_value() {return value;}
	public int get_priority() {return priority;}
	public void set_priority(int i) {priority = i;}
}
