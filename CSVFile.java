import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Scanner;
import java.util.Vector;

public class CSVFile
{
	private InputStreamReader in;
	private OutputStreamWriter out;
	private String encoding;
	private String path;

	private String[] header;
	private String[][] data;
	private String fileData;
	private String headerDelimiter="#";
	private String lineDelimiter=";";
	private String dataDelimiter=","; 

	public CSVFile(String path){this(path,"ASCII");}

	//create a new file
	public CSVFile(String path, String encoding)
	{
		this.path = path;
		this.encoding = encoding;
		//go ahead and read the file and set the data structures
		readFileData();
		setMainHeader();
	}

	//set the line and data delimiters to tell the parser
	//how to go through the CSV file
	public void setLineDelimiter(String d){lineDelimiter = d;}
	public void setDataDelimiter(String d){dataDelimiter = d;}
	public void setHeaderDelimiter(String d){headerDelimiter = d;}

	//return data duh...
	public String getRawData(){return fileData;}
	public String[][] getData(){return data;}
	public String[] getHeader(){return header;}

	//returns data from a given file
	private String readFileData()
	{
		String data = new String();
		try{
			FileInputStream file = new FileInputStream(path);
			in = new InputStreamReader(file);
			data = new String(new byte[0], encoding);
		
			//read data and check if it is the EOF character, if not then 
			//add the concatenate the data to the current data string using the 
			//given encoding type
		
			while(true)
			{
				int raw = in.read();
				if(raw == -1) break;
				byte value = (new Integer(raw)).byteValue();
				byte[] temp = {value};
				data = data+new String(temp,encoding);
			}

			//close stream when done
			in.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); return new String();
		}
		fileData = data;
		return data;
	}

	//writes data to a file using the given encoding
	public void writeFileData(String data)
	{
		try
		{
			FileOutputStream file = new FileOutputStream(path);
			out = new OutputStreamWriter(file,encoding);
			out.flush(); //flush the stream just to be sure there is nothing "clogging" it
			out.write(data,0,data.length());
			out.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); return;
		}
	}

	//search for the Main header of the file that tells which type of delimiters are used
	public void setMainHeader()
	{
		//search for the lines for the delimiters and set them
		String[][] buffer = CSVParser.parseHeader(getRawData(),"!","|");
		header = buffer[0];
		headerDelimiter=header[0];
		lineDelimiter=header[1];
		dataDelimiter=header[2];
		//then split the delimiter from the rest of the file
		int begin = fileData.indexOf("!",1);
		fileData = fileData.substring(begin+1);
	}	

	public static void main(String[] args)
	{
		CSVFile file = new CSVFile("/home/boys/Documents/Noah/src/GIT/JavaCSVReader/test.txt");
		
		file.readFileData();
		file.setMainHeader();
		String[] h = file.getHeader();
		System.out.println(h[0]+" "+h[1]+" "+h[2]);
		String[][] data = CSVParser.parseHeader(file.getRawData(),h[0],h[2]);

		for(String[] i : data)
		{
			System.out.println();
			for(String d : i)
				System.out.print(d+",");
		}
		System.out.println("\nend");
	}
}
