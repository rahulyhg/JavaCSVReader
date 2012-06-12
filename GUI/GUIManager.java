import java.util.Vector;
import javax.swing.JTabbedPane;
import javax.swing.JOptionPane;

//class that implements program managing and control
public class GUIManager
{
	private static JTabbedPane tabs = new JTabbedPane(JTabbedPane.BOTTOM, JTabbedPane.WRAP_TAB_LAYOUT);

	//return a tabbed pane that contains all of the gui items the user has
	public static JTabbedPane getGUIItems(){return tabs;}

	public static void open()
	{
		DataFile file = new DataFile("");
		String path = file.openFile();
		if(path.equals("")) return;
		addDataFile(new DataFile(path));
	}

	public static void close()
	{
		DataFile file = getCurrentFile();
		if(file == null) return;
		if(file.getFilePath().equals(""))
		{
			int option = JOptionPane.showConfirmDialog(null,
					"This file is not saved, save it before closing?"); 
			if(option == JOptionPane.YES_OPTION)
				file.saveFile();
			else if(option == JOptionPane.CANCEL_OPTION)
				return;
		}
		removeDataFile(file);
	}

	public static int exit()
	{
		int numTabs = tabs.getTabCount();
		for(int i=0;i<numTabs;i++)
		{
			DataFile temp = (DataFile)tabs.getComponentAt(i);
			if(temp.getFilePath().equals(""))
			{
				int option = JOptionPane.showConfirmDialog(null,
					"You have unsaved files opened. Exit?","Closing Program",
					JOptionPane.YES_NO_OPTION);					
				if(option == JOptionPane.YES_OPTION)
					return 2; 
				else 
					return 0;
			}
		}
		return 1;
	}
	
	public static void exit(boolean manual)
	{
		int opt = exit();
		if(manual && opt >0) System.exit(0);
	} 

	//creates an empty data file
	public static void newFile()
	{
		DataFile newFile = new DataFile("");
		addDataFile(newFile);
	}

	//save the current file that is being edited.
	public static void save()
	{
		DataFile file = getCurrentFile();
		if(file != null) file.saveFile();
		int index = tabs.indexOfComponent(file);
		System.out.println(index);
		tabs.setTitleAt(index,file.getName());
	}

	//save a copy of the file but don't set the copy to edit. 
	public static void saveCopy()
	{
		DataFile file = getCurrentFile();
		if(file == null) return;
		String tempPath = file.getFilePath();
		file.setFilePath("");
		file.saveFile();
		file.setFilePath(tempPath);
		int index = tabs.indexOfComponent(file);
		tabs.setTitleAt(index,file.getName());
	}

	//save a copy of the file and file and set the newly saved file to be edited
	//NOTE: this does not save the work of the current file
	//to do this the user must click save first then save as.
	public static void saveAs()
	{
		DataFile file = getCurrentFile();
		if(file == null) return;
		file.setFilePath("");
		file.saveFile();
		int index = tabs.indexOfComponent(file);
		tabs.setTitleAt(index,file.getName());
	}

	public static void newRobotSheet()
	{}

	public static DataFile getCurrentFile(){return (DataFile)tabs.getSelectedComponent();}

	public static void showHelp()
	{}

	public static void showAbout()
	{}
	
	public static void addDataFile(DataFile newFile)
	{
		tabs.add(newFile);
	}

	public static void removeDataFile(DataFile file)
	{
		tabs.remove(file);
	}
}
