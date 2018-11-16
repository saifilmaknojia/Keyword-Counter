
/**
 * @author Shaifil
 *
 */
import java.io.*;
import java.util.*;

public class KeywordCounter {

	// output file
	// File output = new File("C:\\Users\\Saifil
	// Home\\eclipse-workspace\\ADS\\src\\output_file.txt");
	File output = new File("output_file.txt");
	FileWriter f = null;

	// Hashtable to keep track of the Node corresponding to the keyword
	Hashtable<String, Node> trackerHashtable = new Hashtable<String, Node>();
	// Max Fibonacci heap
	FibonacciHeap fiboHeap = new FibonacciHeap();
	// ArrayList to keep track of removed Nodes so that we can add them back after operations
	ArrayList<Node> trackRemovedNodes = new ArrayList<Node>();

	// driver program
	public static void main(String args[]) throws IOException {
		String fileToRead = args[0];
		//taking file name or path where the file containing the input is kept to read it
		KeywordCounter temp = new KeywordCounter();
		temp.OverwriteOutputFile();
		// calling overwrite method so that we can clear whatever is written in output_file.txt 
		// before we start writing new output to it, otherwise it will keep on appending new output to
		// the end of file
		temp.parseInputFile(fileToRead);
	}

	/**
	 * Generates the Max Fibonacci heap and calls removeMax() for query number of
	 * times.
	 * 
	 * @param totalQueries
	 */
	void handleQueries(int totalQueries) {
		String outputString = "";
		// System.out.println("totalQueries = " + totalQueries);
		for (int i = 0; i < totalQueries; i++) {
			// remove max node from fibonacci heap
			Node maxPointer = fiboHeap.getMax();
		    Node removedNode = new Node(maxPointer.getkeyword(), maxPointer.getKey());
			trackRemovedNodes.add(removedNode);
			//trackRemovedNodes.add(maxPointer);
			String maxPointerkeyword = removedNode.getkeyword();
			// remove the trackerHashtable entry corresponding to the maxPointer
			trackerHashtable.remove(maxPointerkeyword);
			// Append to the output string
			maxPointerkeyword = maxPointerkeyword.concat(",");
			outputString = outputString.concat(maxPointerkeyword);
		}
		// write to the output file
		try {
			outputString = outputString.substring(0, outputString.length() - 1);
			// remove the extra comma from the end of output
			updateOutputFile(outputString);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Add back the removed nodes to the Max Fibonacci heap
		for (int addBackNodes = 0; addBackNodes < trackRemovedNodes.size(); addBackNodes++) {
			fiboHeap.insert(trackRemovedNodes.get(addBackNodes));
			trackerHashtable.put(trackRemovedNodes.get(addBackNodes).getkeyword(), trackRemovedNodes.get(addBackNodes));
		}
		// empty the removeNodes array list
		trackRemovedNodes.clear();
	}

	public void parseInputFile(String InputFilePath) {
		try {
			BufferedReader readFile = new BufferedReader(new FileReader(InputFilePath));
			// System.out.println("parseInputFile");
			try {
				String currentLine = readFile.readLine();
				// = readFile.readLine();

				while (currentLine != null) {
					//System.out.println(currentLine);
					if (currentLine.equalsIgnoreCase("stop")) {
						System.out.println("******* Completed ********");
						// if we encounter stop string, return 
						return;
					} else if (currentLine.charAt(0) == '$') {
						// if its a keyword, we insert it in hashtable and fibonnaci heap
						String[] split_array = currentLine.split(" ");
						//split and take first and second input from it
						String keyword = split_array[0];
						keyword = keyword.substring(1, keyword.length());
						// remove $ from first string of input line
						int frequency = Integer.parseInt(split_array[1]);
						// If the keyword already exists, increment its frequency.
						if (trackerHashtable.containsKey(keyword)) {
							Node tempInsert = trackerHashtable.get(keyword);
							int currFrequency = tempInsert.getKey();
							int updateFrequency = currFrequency + frequency;
							fiboHeap.increaseKey(tempInsert, updateFrequency);
							trackerHashtable.put(keyword, tempInsert);
						}
						// If its the first occurence of the keyword, add a new entry
						// in the Fibonacci heap
						else {
							Node newEntry = new Node(keyword, frequency);
							fiboHeap.insert(newEntry);
							trackerHashtable.put(keyword, newEntry);
						}

					} else {
						// System.out.println("else query");
						// if its just a number, call handle query which will find the max node
						int noOfQueries = Integer.parseInt(currentLine);
						handleQueries(noOfQueries);
					}
					currentLine = readFile.readLine();
				}
			} finally {
				readFile.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * below function writes the n popular keywords to the outputfile
	 * 
	 * @param outputString
	 */
	void updateOutputFile(String outputString) throws IOException {
		BufferedWriter writer = null;
		f = new FileWriter(output, true);
		writer = new BufferedWriter(f);
		// System.out.println("writing output file");
		writer.write(outputString);
		writer.newLine();
		writer.flush();

		if (writer != null) {
			try {
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	void OverwriteOutputFile() throws IOException {
		f = new FileWriter(output, false);
	}
}
