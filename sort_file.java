import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

//sort file line by line using array list sorting and filewriter

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
		

		fw = new FileWriter(newFileName,true);
		bw = new BufferedWriter(fw);
		pw = new PrintWriter(bw);

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
			pw.println(unsortedFile.get(i));
		}
		pw.close();
		bw.close();
		fw.close();
		// sort transfer complete > report file sizes and time taken
		
		infile = new BufferedReader(new FileReader( newFileName ));
	
		while(infile.ready())
			sortedFile.add(infile.readLine());
		infile.close();
		long endTime = System.currentTimeMillis();

		//complete scan
		System.out.println("SORT TRANSFER SCAN COMPLETE >> ERROR CHECK");
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
