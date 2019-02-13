/*
 * Com S 311 
 * PA1
 * created by: Isaac Holtkamp
 */
package priorityQ;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class PriorityQ {
	private static PriorityQueue<Entry> pq = new PriorityQueue<>(new Comparator<Entry>() {
			@Override
			public int compare(Entry e1, Entry e2) {
				Integer ent1 = e1.get_priority();
				Integer ent2 = e2.get_priority();
				return ent2.compareTo(ent1);
			}
        });;
	
	public static void main(String[] args) {
		
		
		for(int i = 0; i < 20; i++) {
			Random rand = new Random();
			int a = rand.nextInt(50);
			String s = "Value: " + a;
			add(s, a);
		}
		priorityArray();
		remove(2);
		priorityArray();
	}
		
	public static void add(String s, int p) {
		Entry e = new Entry(s, p);
		pq.add(e);
	}
	
	public static String returnMax() {
		return pq.peek().get_value();
	}
	
	public static String extractMax() {
		return pq.poll().get_value();
	}
	
	public static void remove(int i) {
		if(pq.size() > 0) {
			Object arr[] = pq.toArray();
			Entry ent = (Entry) arr[i];
			pq.remove(ent);
		}
	}
	
	public static void decrementPriority(int i, int k){
		if(pq.size() > 0) {
			Object arr[] = pq.toArray();
			Entry ent = (Entry) arr[i];
			ent.set_priority(ent.get_priority() - k);
		}
	}
	
	public static Entry[] priorityArray(){
		Object arr[] = pq.toArray();
		Entry[] ent = new Entry[arr.length];
		
		for(int i = 0; i < arr.length; i++) {
			ent[i] = (Entry) arr[i];
			System.out.println(ent[i].get_value());
		}
		return ent;
		
	}
	
	public static int getKey(int i) {
		PriorityQueue<Entry> temp = pq;
		for(int x = 0; i < temp.size(); i++) {
			temp.remove();
		}
		return temp.poll().get_priority();
		
	}
	
	public static String getValue(int i) {
		PriorityQueue<Entry> temp = pq;
		for(int x = 0; i < temp.size(); i++) {
			temp.remove();
		}
		return temp.poll().get_value();
	}
	
	public static Boolean isEmpty() {
		return pq.isEmpty();
	}
}
