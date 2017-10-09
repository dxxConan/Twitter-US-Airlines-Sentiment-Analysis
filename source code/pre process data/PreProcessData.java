import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class PreProcessData {
	private static HashSet<String> patternsToSkip;
	private static ArrayList<String> negatives;
    private static HashSet<String> wordBag;
	static {
		wordBag=new HashSet<String>();
		patternsToSkip= new HashSet<String>();
		negatives=new ArrayList<String>();
    	String[] stops={"a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the"};
    	for(String s:stops)
    		patternsToSkip.add(s.toLowerCase()); 
	}
	
	public static void readStopWords(String fileName){
		File file=new File(fileName);
		FileReader fr;
		try {
			fr = new FileReader(file);
			BufferedReader br=new BufferedReader(fr);
			String line="";
			while((line=br.readLine())!=null){
				String[] strs=line.split(" +");
				for(String str:strs){
					if(!patternsToSkip.contains(str))
						patternsToSkip.add(str);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void getWordBag(String fileName){
		File file=new File(fileName);
		try {
			int l=1;
			BufferedReader br=new BufferedReader(new FileReader(file));
			String line="";
			while((line=br.readLine())!= null){
				String[] strs=line.split(",");
				if(strs.length<11) continue;
				l++;
				String cleanWords = strs[10].replaceAll("^[a-zA-Z0-9]+"," "); 
				String negativeReason=strs[3].toLowerCase().trim();
				if(!negatives.contains(negativeReason) && !negativeReason.equals("") && !negativeReason.equals("negativereason"))
						negatives.add(negativeReason);
				String[] words=cleanWords.split(" +");
			    for(String word:words){ 
			    	if(word.matches("[a-zA-Z]+") && !wordBag.contains(word.toLowerCase()) && !patternsToSkip.contains(word.toLowerCase()) && word.length()>1)
			    		wordBag.add(word.toLowerCase());
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String[][] getDataRect(int low,int high,HashSet<String> hs,String fileName){
		String data[][]=new String[high-low+1][hs.size()+1];
		File file=new File(fileName);
		try {
			BufferedReader br=new BufferedReader(new FileReader(file));
			String line="";
			int l=0;
			int location=0;
			while((line=br.readLine())!= null){
				location++;
				if(location<low) continue;
				if(location>high) return data;
				//if(l>=x) return data;
				String[] strs=line.split(",");
				if(strs.length<11) continue;
				//data[l][0]=Integer.toString(l);
				if(strs[1].toLowerCase().trim().equals("positive"))
					data[l][0]=String.valueOf(1);
				else if(strs[1].toLowerCase().trim().equals("neutral"))
					data[l][0]=String.valueOf(0);
				else if(negatives.contains(strs[3].toLowerCase().trim())){
					int index=2;
					//int index=(negatives.indexOf(strs[3].toLowerCase().trim())+2);
					data[l][0]=String.valueOf(index);
				}else{
					System.out.println(strs[3].trim());
				}
				int col=1;
				String cleanWordsLine = strs[10].replaceAll("^[a-zA-Z0-9]+", " ");  
				String[] cleanWords=cleanWordsLine.split(" +");
				for(String s:hs){
					int num=0;
					for(String wordOfText:cleanWords){
						if(s.equals(wordOfText.toLowerCase()))
							num++;
					}

					//if(num>1) System.out.println(num);
					data[l][col]=String.valueOf(num);
					col++;
				}
				l++;
				//System.out.println("location="+location);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}
	public static void writeToCSV(String[][] data,String outFileName){
		File file=new File(outFileName);
	    try {
			if(file.exists()) file.createNewFile();
			FileWriter fw=new FileWriter(file);
			BufferedWriter bw=new BufferedWriter(fw);
			for(int i=0;i<data.length;i++){
				if(data[i][0]==null) continue;
				for(int j=0;j<data[i].length;j++){
					bw.write(data[i][j]+",");
				}
				bw.newLine();
			}
			bw.close();
			fw.close();
		} catch (Exception e) {
			
		}
	}
	
	static String[][] combinedData(String[][] data1,String data2[][],String[][] data3,String[][] data4){
		int l1=data1.length;
		int l2=data2.length;
		int l3=data3.length;
		int l4=data4.length;
		String[][] data=new String[l1+l2+l3+l4][data1[1].length];
		int i=0;
		while(i<data.length){
			for(int j1=0;j1<l1;j1++,i++){
				data[i]=data1[j1];
			}
			for(int j2=0;j2<l2;j2++,i++){
				data[i]=data1[j2];
			}
			for(int j3=0;j3<l3;j3++,i++){
				data[i]=data1[j3];
			}
			for(int j4=0;j4<l4;j4++,i++){
				data[i]=data1[j4];
			}
		}
		return data;
	}
	public static void main(String[] args){
		readStopWords("StopWords");
		getWordBag("Tweets.csv");
		System.out.println(wordBag.size()+" &&&&&&&&&&&&&&&&&&&&");
		String[][] data1=getDataRect(1,4000,wordBag,"Tweets.csv");
		String[][] data2=getDataRect(4001,8000,wordBag,"Tweets.csv");
	    String[][] data4=getDataRect(8001,12000,wordBag,"Tweets.csv");
		String[][] data4=getDataRect(12001,15000,wordBag,"Tweets.csv");
		//System.out.println(wordBag.size()+" &&&&&&&&&&&&&&&&&&&&-2");
		//String[][] data=combinedData(data1,data2,data3,data4);
		writeToCSV(data1,"TweetSample1.csv");
		writeToCSV(data2,"TweetSample1.csv");
		writeToCSV(data3,"TweetSample1.csv");
		writeToCSV(data4,"TweetSample1.csv");
		//System.out.println(wordBag.size()+" &&&&&&&&&&&&&&&&&&&&-3");
		//System.out.println("done 2");
		/*
		for(int i=0;i<1000;i++){
			for(int j=0;j<3000;j++){
				System.out.print(data[i][j]+" ");
			}
			System.out.println();
		}*/
	}
}
