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

	private String[] mainHeader;
	private Vector data;
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
		setHeaders();
	}

	//set the line and data delimiters to tell the parser
	//how to go through the CSV file
	public void setLineDelimiter(String d){lineDelimiter = d;}
	public void setDataDelimiter(String d){dataDelimiter = d;}
	public void setHeaderDelimiter(String d){headerDelimiter = d;}

	//return data duh...
	public String getRawData(){return fileData;}
	public String[][] getData(){return data;}
	public String[] getHeader(){return mainHeader;}

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
		String[][] buffer = CSVParser.parseHeader(getRawData());
		//check to make sure that there is actually data to parse
		if(buffer == new String[0][0]) return;
		mainHeader = buffer[0];
		headerDelimiter=mainHeader[0];
		lineDelimiter=mainHeader[1];
		dataDelimiter=mainHeader[2];
		//then split the delimiter from the rest of the file
		int begin = getRawData().indexOf("!",1);
		fileData = getRawData().substring(begin+1);
	}	

	public void setHeaders()
	{
		//scan through the file for each header list
		String[][] headers = CSVParser.parseHeader(getRawData(),headerDelimiter,dataDelimiter);

		for(int i=0;i<headers.length;i++)
		{
			String[] dataTypes = new String[headers[i].length];
			String name = new String();
			for(int j =0;j<headers[i].length;j++)
			{
				//splits the current header element and returns in format "(Type","Name"
				String temp = StringUtils.split(headers[i][j],")");
				name+=temp[1];
				//if the current element is not the last add a comma to the name
				if(j<headers[i].length-1) name+=",";

				dataTypes[j] = temp[0].substring(1);
			}

			//after parsing the info, create the data object and add it to the list
			CSVDataHeader(dataTypes,name);//add the data to the new data header and store it
		}	  
	}
	
}
