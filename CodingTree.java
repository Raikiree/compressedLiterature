package compressedLiterature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
//*******************
//compress process
//1.Statistics
//2.Construct the tree
//3.Encryption
//4.output the compressed data
//**********************
//get the input string, calculate the frequency: put the result to a hashmap
//create List<TreeNode> treenodes for all the charactors
//build huffman tree: TreeNode root = huffmanTree(List<TreeNode> nodes)
//get/output the codes: using dfs to get the codes
//compress the original file using the code dic

public class CodingTree {
	//data member
	String message;
	String bits;
	private List<TreeNode> nodes = new ArrayList<TreeNode>();
	private Map<Character, Integer> freq = new HashMap<Character, Integer>();
	 Map<Object, String> codes = new HashMap<Object, String>();
	
	private class TreeNode<E>{
		int weight;
		E data;
		TreeNode left;
		TreeNode right;
		public TreeNode(E data, int weight){
			this.weight = weight;
			this.data = data;
		}
	}
	//constructor
	//responsible for calling all private methods that carry out the Huffman coding algorithm
	//based on the requirement of the constructor, need to use readline() and pass the txt
	//one line by one line
	public CodingTree(String message) throws IOException{
		this.message = message;
		countFrequency();
		getCodes(huffmanTree(nodes));
	}
	
	//count frequency & initialize the code dict
	private void countFrequency() throws IOException{
		char[] msg = message.toCharArray();
		for(int i = 0; i < msg.length; i++){
			if(freq.containsKey(msg[i])){
				freq.put(msg[i], freq.get(msg[i]) + 1);
			}else{
				freq.put(msg[i], 1);
			}
		}
		//dict stored in List<> nodes
		Iterator<Character> it = freq.keySet().iterator();
		while(it.hasNext()){
			Character tmp = (Character) it.next();
			nodes.add(new TreeNode(tmp, freq.get(tmp)));
		}
	}
	//build the huffman tree
	private TreeNode huffmanTree(List<TreeNode> nodes){
		Comparator<TreeNode> comparator = new Comparator<TreeNode>(){
			@Override
			public int compare(TreeNode node1, TreeNode node2){
				if(node1.weight - node2.weight < 0)
					return -1;
				else if(node1.weight - node2.weight > 0)
					return 1;
				else
					return 0;
					
			}
		};
		//tree using the list
		while(nodes.size() > 1){
			Collections.sort(nodes,comparator);
			TreeNode node = new TreeNode(null, nodes.get(0).weight + nodes.get(1).weight );
			node.left = nodes.get(0);
			node.right = nodes.get(1);
			nodes.remove(0);
			nodes.remove(0);
			nodes.add(node);
		}
		return nodes.get(0);
	}
	
	//generate the code dict: generate a map of codes
	private void getCodes(TreeNode root){
		StringBuilder code = new StringBuilder();
		dfs(root, code);
		
	}
	private void dfs(TreeNode root, StringBuilder code){
		if(root.left == null){
			codes.put(root.data, code.toString());
			return;
		}else if(root.left != null){
			code.append('0');
			dfs(root.left, code);
			code.deleteCharAt(code.length() - 1);
			
			code.append('1');
			dfs(root.right, code);
			code.deleteCharAt(code.length() - 1);
		}
	}

	//compress
	public void compress(){
		char[] msg = message.toCharArray();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < msg.length; i++){
			sb.append(codes.get(msg[i]));
		}
		bits = sb.toString();
	}
	
	//decode helper
	public String decodeHelper() throws IOException{
		File fin = new File("test.txt");
		BufferedReader br = new BufferedReader(new FileReader(fin));
		StringBuilder converter = new StringBuilder();
		int n;
		while((n = br.read()) != -1){
			
			String s = Integer.toBinaryString(n);
			//check whether s.length == 7
			while(s.length() < 7){
				s = "0" + s;
			}
			converter.append(s);
			
		}
		return converter.toString();
	}
	//(optional) decoder
	public String decode(String bits){
		Map<String, Character> decoder = new HashMap<String, Character>();
		Iterator it = codes.keySet().iterator();
		while(it.hasNext()){
			Character letter = (Character)it.next();
			String val = codes.get(letter);
			decoder.put(val, letter);
		}
		StringBuilder sb = new StringBuilder();
		int start = 0, end = 1;
		while(start < bits.length() && end < bits.length()){
			String s = bits.substring(start,end);
			if(decoder.containsKey(s)){
				sb.append(decoder.get(s));
				start = end; 
				end = start + 1;
			}else{
				end++;
			}
		}
		if(start < bits.length()){
			if(decoder.containsKey(bits.substring(start))){
				sb.append(decoder.get(bits.substring(start)));
			}
		}
		return sb.toString();
	}
}
