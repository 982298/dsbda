import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class LDriver {
	public static void main(String [] args) throws Exception
	{
	Configuration c=new Configuration();
	String[] files=new GenericOptionsParser(c,args).getRemainingArgs();
	Path input=new Path(files[0]);
	Path output=new Path(files[1]);
	Job j=new Job(c,"wordcount");
	j.setJarByClass(LDriver.class);
	j.setMapperClass(LMapper.class);
	j.setReducerClass(LReducer.class);
	j.setOutputKeyClass(Text.class);
	j.setOutputValueClass(IntWritable.class);
	FileInputFormat.addInputPath(j, input);
	FileOutputFormat.setOutputPath(j, output);
	System.exit(j.waitForCompletion(true)?0:1);
	}
}






import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class LMapper extends Mapper<LongWritable, Text, Text, IntWritable>{

	private final static IntWritable one = new IntWritable(1);

	public void map(LongWritable key, Text value, Context con) throws IOException, InterruptedException
	{
		String line = value.toString();
		String[] parts = line.split(" ");

		String ip = parts[0];

		con.write(new Text(ip), one);
	}
}


import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class LReducer extends Reducer<Text, IntWritable, Text, IntWritable>
{
	private Text maxword = new Text();
	private int maxsum = 0;

	public void reduce(Text word, Iterable<IntWritable> values, Context con) throws IOException, InterruptedException
	{
		int sum = 0;

		for(IntWritable value : values)
		{
			sum += value.get();
		}

		// check for maximum
		if(sum > maxsum){
			maxsum = sum;
			maxword.set(word);
		}
	}

	// called once after all reduce calls
	protected void cleanup(Context con) throws IOException, InterruptedException
	{
		con.write(maxword, new IntWritable(maxsum));
	}
}
