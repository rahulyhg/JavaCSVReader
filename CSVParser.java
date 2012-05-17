import java.util.Vector;

//class that processes the raw data from a CSV file 
public class CSVParser
{

	//parser for the main header of a CSV file
	public static String[][] parseMainHeader(String rawData)
	{
		//pull the data from the first line since all main header data should be located there
		rawData = rawData.substring(rawData.indexOf(1,"\n"),0);
		String headerDelim = "!";
		String dataDelim = StringUtils.tagSplit(getRawData(),"\"")[0];
		return parseHeader(rawData,headerDelim,dataDelim);
	}

	public static String[][] parseData(String rawData,String headerDelim, String lineDelim)
	{
		//grab data in form "d1,d2,...,dn;"
		String[] dataSets = StringUtils.invertTagSplit(rawData,headerDelim);
		
		Vector buffer = new Vector();
		for(int i=0;i<dataSets.length;i++)
		{
			
		}
	}

	//searches through a document and collects all header data
	//returns from "#Head1,Head2#...#Head3,Head4#" to ["Head1","Head2"]["Head3","Head4"]
	public static String[][] parseHeader(String rawData,String headerDelim,String dataDelim)
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

}
