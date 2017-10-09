package Final6350.Final6350;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

class pair{
	String key;
	String value;
	public pair(String k, String v){
		this.key=k;
		this.value=v;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value =value;
	}
}

class Sorter implements Comparator<Object>{

	public int compare(Object o1, Object o2) {
		pair p1=(pair)o1;
		pair p2=(pair)o2;
		Integer n1=Integer.parseInt(p1.getValue());
		Integer n2=Integer.parseInt(p2.getValue());
		return n2.compareTo(n1);
	}
	
}
class myMapWritable extends MapWritable{
	 public  String toString(){
		 ArrayList<pair> al=new ArrayList<pair>();
		 Set<Writable> keys = this.keySet();
	     for (Writable k : keys) {
	         al.add(new pair(k.toString(),this.get(k).toString()));
	     }
	     int count=0;
	     Collections.sort(al,new Sorter());
		  String s="[";
	        for (pair k : al) {
	        	count++;
	        	if(count>100) break;
	        	s+=k.getKey()+"="+k.getValue()+" ";
	        }
	        s+="]"+"\n";
		  return s;
	  }
}
public class Co1 {
	 private static Set<String> target;
	 private static int[] count;
	 private static Set<String> initTarget(){
	    	if(target==null){
	    		target= new HashSet<String>();
	    		String[] words={"thank", "thanks", "united", "great", "http", "just", "service", "love", "customer", "best"};
	    		for(String s:words)
	    			target.add(s.toLowerCase()); 
	    		return target;
	    	}
	    	return target;
	    }
	private static int len;
    
	public static class TokenizerMapper extends Mapper<Object, Text, Text, myMapWritable>{
		private String tokens = "[_|$#<>\\^=\\[\\]\\*/\\\\,;,.\\-:()?!\"']";
		private myMapWritable occurrenceMap = new myMapWritable();
		private Text word = new Text();

		protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			Set<String> target=initTarget();
    		String delims = ",";
			String[] datas= StringUtils.split(value.toString(),delims);	
			if(datas.length>11){
				String cleanLine = datas[10].replaceAll(tokens, " ");
				for(String t:target){
					occurrenceMap.clear();
					if(cleanLine.contains(t)){
						String[] words=cleanLine.split(" +");
						for(String w:words){
							if(occurrenceMap.containsKey(w)){
	  	                   		IntWritable count = (IntWritable)occurrenceMap.get(w);
	  	                    	count.set(count.get()+1);
							}else
	  	                	occurrenceMap.put(new Text(w),new IntWritable(1));
						}
					}
					context.write(new Text(t),occurrenceMap);
				}
			}
		}
	}

    public static class IntSumReducer extends Reducer<Text,myMapWritable,Text,myMapWritable> {
	  private myMapWritable iMap = new myMapWritable();
	  private Map<String,Integer> hm = new HashMap<String,Integer>();
	  private HashMap<Text,myMapWritable> countMap=new HashMap<Text,myMapWritable>();
	    @Override
	    protected void reduce(Text key, Iterable<myMapWritable> values, Context context) throws IOException, InterruptedException {
	    	iMap.clear();
	    	
	    	iMap.clear();
	    	hm.clear();
	        for (MapWritable val : values) {	            
	            Set<Writable> keys = val.keySet();
		        for (Writable k : keys) {
		            IntWritable fromCount = (IntWritable) val.get(k);
		            if (iMap.containsKey(k)) {
		                IntWritable count = (IntWritable) iMap.get(k);
		                count.set(count.get() + fromCount.get());
		            } else {
		                iMap.put(k, fromCount);
		            }
		        }           
	        }
	        context.write(key, iMap);
	    }
	  
    }
   
    private static <K extends Comparable, V extends Comparable> Map<K, V> sortByValues(Map<K, V> map) {
        List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(map.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {

            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        Map<K, V> sortedMap = new LinkedHashMap<K, V>();

        for (Map.Entry<K, V> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    conf.set("mapred.job.tracker", "hdfs://cshadoop1:61120");
    conf.set("yarn.resourcemanager.address", "cshadoop1.utdallas.edu:8032");
    conf.set("mapreduce.framework.name", "yarn");
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(Co1.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(myMapWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
