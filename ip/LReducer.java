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
