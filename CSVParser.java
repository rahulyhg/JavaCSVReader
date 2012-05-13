import java.util.Vector;

//class that processes the raw data from a CSV file 
public class CSVParser
{
	//searches through a document and collects all header data
	public static String[][] parseHeader(String rawData, String headerDelim, String dataDelim)
	{
		if(rawData == null || rawData.equals(new String())) return new String[0][0];
	
		//first scan through the document to find the location of the headers
		//headers are surrounded in the given header delimiter
		String[] headers = StringUtils.tagSplit(rawData,headerDelim);

		Vector buffer = new Vector();
		int r = headers.length;
		//then go through all of the headers and create sub arrays that contain each header data
		for(int i =0;i<r;i++)
		{
			String[] subdata = StringUtils.split(headers[i],dataDelim);
			buffer.add(subdata);
		}

		//then convert the buffer to the array
		int c = buffer.size();
		String[][] d = new String[r][c];
		for(int j=0;j<c;j++)
		{
			d[j] = (String[])buffer.elementAt(j);
		}
		return d;
	}

	public static  
}
