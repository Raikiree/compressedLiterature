package compressedLiterature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class Console {
	public static void main(String[] args) throws IOException{
		//**********************************************
				//not necessary to pass the whole "WarAndPeace.txt"
				//only pass string msg
				//**********************main********************
//				1. initiate the huffman's encoding by passing the target msg
//				2. output the codes to a text file
//				3. output the compressed message to a bat file
//				4. display compression and run time statistics
		
		File file = new File("WarAndPeace.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		
		
		File fout = new File("compressed.txt");
		FileWriter fw = new FileWriter(fout);
		BufferedWriter bw = new BufferedWriter(fw);
		
		int ascii;
		while((ascii = br.read()) != -1){
			sb.append((char)ascii);
			
		}
		String sourceMessage = sb.toString();
		
		CodingTree ct = new CodingTree(sourceMessage);
		Iterator it = ct.codes.entrySet().iterator();
		
		System.out.println("The codes book: ");
		System.out.println("******Codes******");
		bw.write("******Codes******" + "\n");
		
		while(it.hasNext()){
			Map.Entry element = (Map.Entry) it.next();
			Object key = element.getKey();
			Object value = element.getValue();
			System.out.println(key + " = " + value);
			bw.write(key + " = " + value + "\n");
		}
		
		long runTime = System.currentTimeMillis();
		ct.compress();
		long runTime2 = System.currentTimeMillis();
		encodeToFile(ct.bits);
		bw.write("****************" + "\n");
		//bw.write("Coded Message: " + "\n" + ct.bits + "\n" +" ## size: " + sourceMessage.length()+" Byte(s)" + "\n");
		String tmp = ct.decode(ct.decodeHelper());
		//System.out.println("The bits: " + "\n" + ct.bits);
		//System.out.println("recovered bits: " + "\n" + ct.decodeHelper());
		bw.write("\n" + "Decoded Message: " + "\n" + tmp + "\n" + " ## size: " + ct.bits.length()/8 + " Byte(s)");
		bw.close();
	
		System.out.println("****************");
//		System.out.println("The size of the message is: " + sourceMessage.length() + " Byte(s)");
//		System.out.println("The size of the compressed message is: " + ct.bits.length()/8 + " Byte(s)");
//		System.out.println("The time cost for the compression is: " + (runTime2 - runTime) + " ms");
		
		//System.out.println("Decoded Message: " + "\n" + tmp);
		
		
//		int nextc = 0;
//		char c;
//		//differece between read() and readline()
//		while((nextc = br.read()) != -1){
//			System.out.println(nextc);
//			c = (char) nextc;
//			System.out.println(c);
//			bw.write(String.valueOf(nextc));//need to use String.valueOf()
//		}
//		bw.close();	
	}
	
	public static void encodeToFile(String binaryString) throws IOException{
		int part = binaryString.length() / 7;
		int remainder = binaryString.length() % 7;
		
		File f = new File("test.txt");
		FileWriter writer;
		writer = new FileWriter(f);
		BufferedWriter bwriter = new BufferedWriter(writer);
		for(int i = 0; i < part; i++) {
			bwriter.write((char)(Byte.parseByte(binaryString.substring(i*7, i* 7 + 7), 2)));
		}
		
		if(remainder != 0) {
			String tmp = binaryString.substring(binaryString.length() - remainder, binaryString.length());
			while(tmp.length() < 7){
				tmp = tmp + "0";
			}
			bwriter.write((char) Byte.parseByte(tmp, 2));
		}
		bwriter.close();
	}
}
