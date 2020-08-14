//sort file line by line using array list sorting and filewriter
import java.util.*;
import java.io.*;
public class sort_file
{
	static String fileName = "";//filename
	static String newFileName = "";
	static long startTime = 0;
	//unsorted and sorted unpopulated files
	static ArrayList<String> unsortedFile = new ArrayList<String>();
	static ArrayList<String> sortedFile = new ArrayList<String>();
	static Scanner kb = new Scanner(System.in);
	static FileWriter fw = null;
	static BufferedWriter bw = null;
	static PrintWriter pw = null;
	//error tracking to report at the end
	static int addError = 0;
	static int bwCloseError = 0;
	static int fwCloseError = 0;
	static boolean add_error = false;
	public static void main(String[]args)throws Exception
	{
		//to ensure a file was input.
		System.out.println("Enter the name of the file you would like to sort: ");
		fileName = kb.nextLine();
		setup(0);
		//populate unsorted array
		BufferedReader infile = new BufferedReader(new FileReader(fileName));
		while(infile.ready())
			unsortedFile.add(infile.readLine());
		infile.close();
		Collections.sort(unsortedFile);
		
		/*i have had errors occur at random points in large files so i will be using a recursive 
		method with a lot of try catches to ensure each line is added to the new file*/
		int fileSize = unsortedFile.size();
		int prog = fileSize/10;
		boolean worthTracking = (prog>=1);
		for(int i = 0; i < fileSize; i++){
			if(worthTracking){
				if(i==prog){
					double perc = 100*((double)prog/fileSize);
					System.out.println(perc+"% complete ");
					prog += prog;
				}
			}
			insert_sorted(unsortedFile.get(i), false);
		}
		// sort transfer complete > report of tracked errors and file size check (useful for larger files)
		infile = null;
		try{
			infile = new BufferedReader(new FileReader( newFileName ));
		}catch(FileNotFoundException e){
			System.out.println("ERROR: Your entered file to sort is empty... Exitting");
			System.exit(0);
		}
		while(infile.ready())
			sortedFile.add(infile.readLine());
		infile.close();
		long endTime = System.currentTimeMillis();

		//complete scan
		System.out.println("SORT TRANSFER SCAN COMPLETE >> ERROR CHECK");
		System.out.println("#ADD_ERRORS = "+addError+" \n#BW_CLOSE_ERRORS = "+bwCloseError+"\n#FW_CLOSE_ERROR = "+fwCloseError);
		System.out.println("SIZE_CHECK >>\n"+fileName+".size() = "+unsortedFile.size()+"\n"+newFileName+".size() = "+sortedFile.size());
		System.out.println("Time taken: "+((double)(endTime-startTime)/1000)+"s");

	}

	//setup file names, etc
	static void setup(int attempt)throws Exception
	{
		//file name to write to:
		if(attempt==0)System.out.println("What would you like to name the new sorted file? ex: 'sortedfile.txt'\n(Will overwrite old file if it has the same name):");
		newFileName = kb.nextLine();
		//check if file input has .txt or not
		if(!endsWith(newFileName, ".txt")){
			System.out.println("ERROR: Please end new file name with '.txt.' ... try again: ");
			setup(1);
			return;
		}
		startTime = System.currentTimeMillis();//track time to sort and add new file
		File f = new File(newFileName);
		if(f.exists())f.delete();
		kb.close();
	}

	//transfering 
	static void insert_sorted(String word, boolean add_e)throws Exception
	{
		try{
			fw = new FileWriter(newFileName,true);
			bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);
			pw.println(word);
			pw.close();
			if(add_error){
				System.out.println("ERROR FIXED... addError--");
				addError--;
			}
			add_error = false;
		}catch(IOException e){
			if(add_error)System.out.println("ERROR AGAIN...");
			else{
				System.out.println("Error Adding or creating line = "+word+" > trying again... >> addError++");
				addError++;
			}
			add_error = true;
			insert_sorted(word, add_error);
		}

		try{
			if(bw!=null)bw.close();
		}catch(IOException e){
			System.out.println("Error Closing BufferedWriter");
			bwCloseError++;
		}

		try{
			if(fw!=null)fw.close();
		}catch(IOException e){
			System.out.println("Error Closing FileWriter");
			fwCloseError++;
		}		
	}
	
	//check if a given String input ends with the String end
	static boolean endsWith(String input, String end)throws Exception
	{
		int startPos = input.length()-4;
		if(end.length() >= input.length()){
			System.out.println("ERROR: Invalid Input... Exitting.");
			System.exit(0);
		}
		String ending = "";
		for(int i = startPos; i < input.length(); i++)
		{
			ending +=input.charAt(i);
		}
		return ending.equals(".txt");
	}
}