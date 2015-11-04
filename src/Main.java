import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
Run Instructions:

 */
public class Main {
	
	private static ArrayList<Integer> lastReads;  
	private static ArrayList<Integer> lastWrites;
	private static Integer[] registerNames;
	
	
	private static ArrayList<String> lines;
	private static Integer[][] dependencyTable;
	private static ArrayList<Integer> cycles;
	private static Integer loadDelay;
	private static ArrayList<String> flags;
	private static boolean wasRead = false;
	private static boolean wasWritten = false;
	private static boolean wasLoaded = false;
	private static boolean rename = false;
	
	public static void main(String[] args){
		
		lastReads = new ArrayList<Integer>();
		lastWrites = new ArrayList<Integer>();
		registerNames = new Integer[8];
		for (int i = 0; i < 8; i++) {
			lastReads.add(-1);
			lastWrites.add(-1);
			registerNames[i] = i + 1;
		}
		
		loadDelay = 1;
		flags = new ArrayList<String>();
		lines = new ArrayList<String>();
		cycles = new ArrayList<Integer>();
		dependencyTable = new Integer[7][2];
		for (int i = 0; i <= 6; i++) {
			for (int j = 0; j <= 1; j++) {
				dependencyTable[i][j] = -1;
			}
		} 
		
		try {
			
			BufferedReader br;
			if (args.length == 2) {
				
				flags.add(args[0]);				
				br = new BufferedReader(new FileReader(args[1]));
			} else if (args.length == 3) {
				
				flags.add(args[0]);
				flags.add(args[1]);				
				br = new BufferedReader(new FileReader(args[2]));
			} else {
				
				br = new BufferedReader(new FileReader(args[0]));
			}					

			// process flags
			for (String s: flags) {
				if (s.charAt(1) == 'l') {
					loadDelay = Integer.parseInt(s.split("l")[1]);
				} else {
					rename = true; 
				}
			}
			
			String grabbedLine = br.readLine();
			int lineNum = 0;
			while(grabbedLine != null) {
				
				// split up line
				String[] splitLine = grabbedLine.split("\\$");

				// re-setting our flags
				wasWritten = false;
				wasLoaded = false;
				wasRead = false;
				
				// splitLine[0] is our instruction type
				//Changed from switch to if else, as switch on strings doesn't work with Java 7
				if(splitLine[0].trim().equals("lw")) {
					wasLoaded = true;
					wasRead = true;
				} else if(splitLine[0].trim().equals("sw")){
					wasRead = true;
				} else {
					wasWritten = true;
				}
				
				// build a dependency table 
				int reg = -1;
				ArrayList<Integer> dependencies = new ArrayList<Integer>();
				ArrayList<Integer> regs = new ArrayList<Integer>();
				for (int t = 1; t < splitLine.length; t++) {
					String s = splitLine[t];
					
					// grab the three registers
					reg = Character.getNumericValue(s.charAt(0)) - 1;
					regs.add(reg);

					// handle dependencies 
					// instruction is store word - process differently
					if (wasRead == true) {

						if (wasLoaded){
							if(rename && t==1)
								dependencies.add(0);
							else
								dependencies.add(dependencyTable[reg][0]+1);
						}
						else
							dependencies.add(dependencyTable[reg][1]+1);
					} 
					// all other instructions
					else if (wasWritten) {
				
						// our destination register - write
						if (t == 1) {
							if(rename)
								dependencies.add(0);
							else
								dependencies.add(dependencyTable[reg][0]+1);	
						} 
						// all other registers - read
						else {
							dependencies.add(dependencyTable[reg][1]+1);
						}	
					}
				}
				
				// determine which level current instruction can be executed on 
				int largestVal = -1;
				for (Integer i: dependencies) {
					if (largestVal < i) {
						largestVal = i;
					}
				}

				// set level current instruction should be executed on
				cycles.add(largestVal);
				
				for (int t = 0; t < dependencies.size(); t++) {
					if (wasRead) {
						
						if (wasLoaded && t == 0) {
	
							if(loadDelay > 1) dependencyTable[regs.get(t)][1] = (largestVal+loadDelay-1);
							else dependencyTable[regs.get(t)][1] = (largestVal);
							
						} else {
							dependencyTable[regs.get(t)][0] = largestVal;
						}
					} 
					// all other instructions
					else if (wasWritten) {
				
						// our destination register - write
						if (t == 0) {
							dependencyTable[regs.get(t)][1]	= largestVal;
						} 
						// all other registers - read
						else {
							dependencyTable[regs.get(t)][0] = largestVal;
						}	
					}
				}
				
				if(rename){
					Integer writeReg = regs.get(0) + 1;
					
					if(wasWritten){
						String a = new Integer(10 + lineNum).toString();
						String b = new Integer(registerNames[regs.get(1)]).toString();
						String c;
						
						grabbedLine += " with renaming d/s1/s2 regs to " + a + " " + b;
						
						if(regs.size() == 3){
							c = new Integer(registerNames[regs.get(2)]).toString();
							grabbedLine += " " + c;
						}
						else
							grabbedLine += " -";
					}
					
					if(wasLoaded || wasWritten){
						registerNames[regs.get(0)] = 10 + lineNum;
					} 				
				}

						lineNum++;
						lines.add(grabbedLine);
				grabbedLine = br.readLine();
			}
			
			br.close();
			
			
			// find max cycle
			int largestVal = -1;
			for (Integer i: cycles) {
				
				if (largestVal < i) {
					largestVal = i;
				}
			}
			
			// print out final data flow levels
			System.out.println("load delay set to " + (loadDelay));
			for (int i = 0; i <= largestVal; i++) {
				
				boolean hasInst = false;
			
				for (int j = 0; j < cycles.size(); j++) {
					if (cycles.get(j) == i) {
						if(!hasInst) System.out.println("level " + i + " instructions:");
						hasInst = true;
	
						System.out.println("\t" + j + " " + lines.get(j));
					}
				}
			}	
			System.out.println("the data flow can be executed in " +
								largestVal + " cycles");
			
		} catch(IOException e) {
			// you fucked it up
			e.printStackTrace();
		}	
		
	}

}
