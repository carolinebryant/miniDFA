import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

	
	private static ArrayList<String> lines;
	private static Integer[][] dependencyTable;
	
	public static void main(String[] args){
		
		lines = new ArrayList<String>();
		dependencyTable = new Integer[7][2];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 1; j++) {
				dependencyTable[i][j] = -1;
			}
		} 
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(args[2]));
			
			String grabbedLine = br.readLine();
			while(grabbedLine != null){
				
				// process line
				grabbedLine = br.readLine();
				lines.add(grabbedLine);		
				
				// split up line
				String[] splitLine = grabbedLine.split("\\s*$\\s*");
				
				// build a dependency table 
				ArrayList<Integer> dependencies = new ArrayList<Integer>();
				for (int t = 1; t < splitLine.length; t++) {
					String s = splitLine[t];
					int reg = Character.getNumericValue(s.charAt(0));
					
					// splitLine[0] is our instruction type
					// dependencyTable[reg][// read or write] = when was I last accessed;
					// dfjbva;dkbv;akbva
					
				}
				
			}
			
			br.close();
			
		} catch(IOException e) {
			// you fucked it up
		}
		
 
		
		
		
	}

}
