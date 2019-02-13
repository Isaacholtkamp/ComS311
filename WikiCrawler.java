/*
 * Com S 311 
 * PA1
 * created by: Isaac Holtkamp
 */
package wikiCrawler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

public class WikiCrawler {
	final static String BASE_URL = "https://en.wikipedia.org"; 
	private static PriorityQueue<Tuple> find;
	private static LinkedList<String> BFS;
	private static Graph graph;
	private static String seed;
	private static int max;
	private static String[] topics;
	private static String output;
	private static Boolean focused;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		String s;
		int m;
		String[] t;
		String o;
		if(args.length == 4){
			s = args[0];
			m = Integer.parseInt(args[1]);
			t = args[2].split(" ");
			t[0] = t[t.length - 1];
			t[t.length - 1] = null;
			o = args[3];
			new WikiCrawler(s, m, t, o);
		}
		
	}
	
	public WikiCrawler(String seed, int max, String[] topics, String output) throws IOException, InterruptedException {
		//Seed: related address of seed URL (within wiki domain)
		//max: maximum number of pages to consider
		//topics: array of strings representing keywords in a topic-list
		//output: string representing the filename where the web graph over discovered pages are written
		this.seed = seed;
		this.max = max;
		this.topics = topics;
		long start = System.currentTimeMillis();
		long end;
		StringBuilder ret = new StringBuilder();
		find = new PriorityQueue<Tuple> (max, 
				  new Comparator<Tuple> () {
					@Override
					public int compare(Tuple a, Tuple b) {
						if (b.get_priority() > a.get_priority()) 
		                    return 1; 
		                else if (b.get_priority() < a.get_priority()) 
		                    return -1; 
		                return 0; 
					}
				  }
				);
		BFS = new LinkedList<String>();
		
		graph = new Graph(new ArrayList<String>(), new ArrayList<String>(), new LinkedList<Vertex>());
		
		graph.add_discovered(seed);
		URL url = new URL(BASE_URL + seed);
		InputStream is = url.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder read = new StringBuilder();
		String temp;
		while((temp = br.readLine()) != null) {
			read.append(temp + "\n");
		}
		graph.add_point(new Vertex(seed, read.toString(), null));
		
		if(topics == null) {
			focused = false;
			BFS.add(seed);
			crawl(false);
		}
		else {
			focused = true;
			find.add(new Tuple(seed, topics.length));
			crawl(true);
		}
	
		for(int i = 0; i < max; i++) {
			for(int j = 0; j < graph.get_points().get(i).get_edges().size(); j++) {
				if(graph.get_used_strings().contains(graph.get_points().get(j).get_page()) && graph.get_points().get(i).get_page() != graph.get_points().get(j).get_page()) {
					String a = graph.get_points().get(i).get_page() + " " + graph.get_points().get(j).get_page()  + "\n";
					ret.append(a);
				}
			}
		}
		//BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		//writer.write(ret);
		//writer.close();
		end = System.currentTimeMillis();
		System.out.println("Total Runtime:" + (end-start));
		System.out.println(ret.toString());
		
		
	}
	
	private static ArrayList<String> extractLinks(String document) throws IOException, InterruptedException{
		ArrayList<String> ret = new ArrayList<String>();
		int startLoc = 0, endLoc = 0;
		int startPlace = 0, endPlace = 0;
		long startTime = System.currentTimeMillis();
		long waitTime = 0;
		String add;
		String temp;
		String link;
		
		startPlace = document.indexOf("<p>");
		if(startPlace == -1) {
			return null;
		}
		endPlace = document.indexOf("<!--", startPlace);
		if(endPlace == -1) {
			endPlace = document.length();
		}
		temp = document.substring(startPlace, endPlace);
		
		
		while(temp.indexOf("/wiki/", endLoc) > -1) {
			startLoc = temp.indexOf("<a href", endLoc);
			endLoc = temp.indexOf("a>", startLoc);
			link = temp.substring(startLoc, endLoc);
			if(link.contains("title") && link.contains("=\"/wiki/")) {
				startPlace = link.indexOf("/wiki");
				endPlace = link.indexOf("\"", startPlace);
				add = link.substring(startPlace, endPlace);
				if(add.indexOf('#') == -1 && add.indexOf(':') == -1) {
					//if(curr == (max - 1)) {
						//if(discovered.contains(add) && !ret.contains(add)) {
							//ret.add(add);
						//}
					//}
					//else {
					
						if(graph.get_discovered().contains(add)) {
							if(!ret.contains(add)) {
								ret.add(add);
							}	
						}
						else {
							if(focused == true){
								int collected = graph.get_used_strings().size() + find.size();
								if((collected % 20) == 0) {
									long startTemp = System.currentTimeMillis();
									Thread.sleep(3000);
									long endTemp = System.currentTimeMillis();
									waitTime += (endTemp - startTemp);
								}
								graph.add_discovered(add);
								
								URL url = new URL(BASE_URL + add);
								InputStream is = url.openStream();
								BufferedReader br = new BufferedReader(new InputStreamReader(is));
								StringBuilder read = new StringBuilder();
								String t;
								int relevance = 0;
								
								while((t = br.readLine()) != null) {
									read.append(t + "\n");
								}
								//int s = read.indexOf("<p>");
								t = read.toString();
								if(topics != null) {
									for(int i = 0; i < topics.length; i++) {
										if(t.indexOf(topics[i]) > -1) {
											relevance++;
										}
									}
									find.add(new Tuple(add, relevance));
								}else {
									BFS.add(add);
								}
								graph.add_point(new Vertex(add, read.toString(), null));
								is.close();
								br.close();
								ret.add(add);
							}else{
								int collected = graph.get_used_strings().size() + BFS.size();
								if(collected != max){
									if((collected % 20) == 0) {
										long startTemp = System.currentTimeMillis();
										Thread.sleep(3000);
										long endTemp = System.currentTimeMillis();
										waitTime += (endTemp - startTemp);
									}
									graph.add_discovered(add);
									
									URL url = new URL(BASE_URL + add);
									InputStream is = url.openStream();
									BufferedReader br = new BufferedReader(new InputStreamReader(is));
									StringBuilder read = new StringBuilder();
									String t;
									int relevance = 0;
									
									while((t = br.readLine()) != null) {
										read.append(t + "\n");
									}
									//int s = read.indexOf("<p>");
									t = read.toString();									
									BFS.add(add);									
									graph.add_point(new Vertex(add, read.toString(), null));
									is.close();
									br.close();
									ret.add(add);
								}
								
							}
							
							
							
							
							
						}
						
					//}
				}
			}
		}
		long endTime = System.currentTimeMillis();
		//System.out.println("Time spent in extract: " + (endTime - startTime));
		//System.out.println("Total waitTime: " + waitTime);
		return ret;
	}
	
	private static void crawl(boolean focused) throws IOException, InterruptedException {
		int count = 0;
		int place = 0;
		long start = System.currentTimeMillis();;
		long end;
		ArrayList<String> extract = null;
		if(focused) {
			while(!find.isEmpty() && count != max) {				
				String next = find.poll().get_string();
				graph.add_used(next);
				for(int i = 0; i < graph.get_points().size(); i++) {
					if(graph.get_points().get(i).get_page() == next) {
						place = i;
						
						extract = extractLinks(graph.get_points().get(place).get_contents());
						i = graph.get_points().size();
					}
				}
				graph.get_points().get(place).set_edges(extract);
				count++;
				if((count % 20) == 0) {
					Thread.sleep(3000);
				}
			}
		}
		else {
			while(!BFS.isEmpty() && count != max) {	
				String next = BFS.poll();
				graph.add_used(next);
				for(int i = 0; i < graph.get_points().size(); i++) {
					if(graph.get_points().get(i).get_page() == next) {
						place = i;
						
						extract = extractLinks(graph.get_points().get(place).get_contents());
						i = graph.get_points().size();
					}
				}
				
				graph.get_points().get(place).set_edges(extract);
				count++;
				if((count % 20) == 0) {
					Thread.sleep(3000);
				}
			}
		}
		end = System.currentTimeMillis();
		//System.out.println("Total time in crawl: " + (end - start));
		return;
	}
}
