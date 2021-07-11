import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		
		try {
			if(true) { //open the files
				File f_dataSet = new File("dataset");
                File f_data=new File("input");
				File in_partition = new File("partition");
				File out_partition = new File("output");
				if(!(f_dataSet.exists() && in_partition.exists() && out_partition.exists())) {
					//s.close();
					throw new Exception("One or more files not found");
				}
                
                Scanner s_input = new Scanner(f_data);
                int num_example = s_input.nextInt(); //number of rows
                int num_attr = s_input.nextInt(); //number of attributes
                s_input.close();
                
				Scanner s_dataset = new Scanner(f_dataSet);
				//int num_example = s_dataset.nextInt(); //number of rows
				//int num_attr = s_dataset.nextInt(); //number of attributes
				//List of attributes
				List<Attribute> inputAttr= new ArrayList<Attribute>();				
				
				//Generate attribute data structure for the input dataset
				for(int j=0; j< num_attr; j++) {
					Map<Integer, Integer> data = new HashMap<Integer, Integer>();
					Attribute attr;
					if(j == num_attr-1) { //if it is the prediction value, create attribute with "true". Else create it as "false"
						attr = new Attribute(data, true);
					} else {
						attr = new Attribute(data, false);
					}
					inputAttr.add(attr); //add the attribute list to the set of attributes. It is now empty
				}
				
				for(int i=1; i<= num_example; i++) { //for each row
					for(int j=0; j< num_attr; j++) { // for each column in the row
						Attribute attr = inputAttr.get(j); //take the attribute structure for jth column
						Map<Integer, Integer> data = attr.getDataSet(); // get dataSet for that attribute
						int attr_value = s_dataset.nextInt(); //read next value from the file
						data.put(i,attr_value); //add it to the list for that attribute
					}
				}
                
                System.out.println("Attributes listed as :");
                for(int j=0;j<num_attr;j++)
                    inputAttr.get(j).print();
                
				Scanner s_partition = new Scanner(in_partition);
				List<List<String>> partitions = new ArrayList<List<String>>();
				//Generate partition from List
				while(s_partition.hasNextLine()) { //as long as file hasnt finished
					String partition = s_partition.nextLine(); //read next line from file
					String[] partition_vals = partition.split(" "); //split on space
					List<String> list = new ArrayList<String>();
					for(int j=0; j<partition_vals.length; j++) {
						list.add(partition_vals[j]); //add each partition value to a list
					}
					partitions.add(list); //add the list to set of partitions
				}
				
                System.out.println("Partitions given are : " + partitions);
                
				MOD_ID3 id3 = new MOD_ID3();
				
				List<List<String>> result = id3.getNextPartition(partitions, inputAttr); //get next partition based on set of defined partitions and set of input attributes
				
				//printPartition(result);
				writePartitionToFile(result, out_partition);
				
				s_dataset.close();
				s_partition.close();
				//s.close();
				
			} else {
			//	s.close();
			//	throw new Exception("Invalid Args: Program takes 3 inputs- <datasetfile> <input partition> <out partition>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void printPartition(List<List<String>> result) {
		for(int i=0; i<result.size(); i++) {
			List<String> part = result.get(i);
			for(int j=0; j<part.size(); j++) {
				System.out.print(part.get(j) + " ");
			}
			System.out.println();
		}
	}
	
	public static void writePartitionToFile(List<List<String>> result, File file) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(file);
        int filecount=0;
		for(int i=0; i<result.size(); i++) {
			List<String> part = result.get(i);
            //pw.write("Partition ");
            filecount++;
            pw.write("echo '' > temp"+filecount+".txt");
			for(int j=1; j<part.size(); j++) {
				//pw.write(part.get(j) + " ");
                pw.write("\nsed '"+part.get(j)+"q;d' dataset >> temp"+filecount+".txt");
			}
			pw.write("\n");
		}
		pw.close();
        System.out.println(filecount+" files generated");
	}
	
	public static void printAttribute(Attribute attr) {
		Map<Integer, Integer>map = attr.getDataSet();
		for(Map.Entry<Integer, Integer> entry : map.entrySet()) {
			System.out.println("key: "+entry.getKey()+" val: "+entry.getValue());
		}
	}
}
