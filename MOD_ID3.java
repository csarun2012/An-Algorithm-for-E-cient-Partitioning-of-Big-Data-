import java.util.*;

public class MOD_ID3 {
	
	public List<List<String>> getNextPartition(List<List<String>> partitions, List<Attribute> attributes) { //get next partition based on defined partition set and set of attributes
        System.out.println("-------------------------IN MOD_ID3 getNextPartition");
		// F = pr*Entropy;
		double f_max = 0;
		int attr_index = 0;
		int partition_index = 0;
		
		for(int i=0; i<partitions.size(); i++) { //iterate over the set of partitions
			List<String> partition = partitions.get(i); //get values under each partition set
			List<Integer> intval_partition = new ArrayList<Integer>();
			
			for(int j=1; j<partition.size(); j++) { //iterate over each value in the partition
				intval_partition.add(Integer.parseInt(partition.get(j))); //add the value to the arraylist
			}
            System.out.println("------NEXT PARTITION\nintval_partition : ");
            System.out.println(intval_partition);
            System.out.println("Partition : ");
            System.out.println(partition);
			
			Attribute a1 = new Attribute(attributes.get(attributes.size()-1)); //create target column attribute using the defined attribute list
			Attribute a = a1.getPartition(intval_partition); //get partition using the list of values
				
            System.out.print("a1:"); a1.print();
            System.out.print("a:"); a.print();

			for(int j=0; j<attributes.size()-1; j++) { //for each attribute
				double entropy = a.getEntropy(); //find entropy of the partition
				double conditional_entropy = a.getEntropy(attributes.get(j));	//find conditional entropy based on the selected attribute
				double gain = entropy-conditional_entropy; //find gain
				double pr = (double)intval_partition.size()/(double)attributes.get(j).getDataSet().size(); //find pr=arraylistsize/dataSetsizeof the attribute
				double f = pr*gain; //find f
                System.out.println("At j="+j+",");
                attributes.get(j).print();
                System.out.println("Entropy = "+entropy);
                System.out.println("Conditional entropy = "+conditional_entropy);
                System.out.println("Gain = "+gain);
                System.out.println("Probability = "+pr);
                System.out.println("f = "+f);
				//System.out.println("parition "+i+" attribute "+j);
				//Main.printAttribute(a);
				//System.out.println(entropy+" "+conditional_entropy+" "+gain+" "+pr+" "+f);
				
				if(f > f_max) { //find partition and attribute combination with maximum value of f
					f_max = f;
					attr_index = j;
					partition_index = i;
                    System.out.println("f_max="+f_max+", attr_index="+attr_index+", partition_index="+partition_index);
				}
				
			}
		}
		
		List<List<String>> result = new ArrayList<List<String>>();
		for(int i=0; i<partitions.size(); i++) { //generate partition using the partitionset and attributes
			if(partition_index!=i) {
				result.add(partitions.get(i));
			} else {				
				List<List<String>> splits = splitPartitionByAttribute(
												partitions.get(partition_index), 
												attributes.get(attr_index)
											);
				
				System.out.print("Partition "+partitions.get(partition_index).get(0)
						+ " replaced with ");
				for(int j=0; j<splits.size(); j++) {
					result.add(splits.get(j));
					System.out.print(splits.get(j).get(0) + " ");
				}
				System.out.println("using feature "+(attr_index+1));
			}
		}
        System.out.println(result);
		return result;
	}
	
	public List<List<String>> splitPartitionByAttribute(List<String> partition, Attribute attr) {
        System.out.println("Split "+partition+" based on ");
        attr.print();
		List<List<String>> splittedPartition = new ArrayList<List<String>>();
		Map<Integer, List<String>> partitionMap = new HashMap<Integer, List<String>>();
		
		int count = 0;
		for(int i=1; i<partition.size(); i++) { //for each partition

			int key = Integer.parseInt(partition.get(i)); //take each value
            System.out.println(partitionMap +" with "+key);
			
			if(attr.getDataSet().containsKey(key)) { //if dataSet contains the key, proceed. Else ignore
				
				int value = attr.getDataSet().get(key); //get the value associated with the key
                System.out.println("Value = "+value);
				if(!partitionMap.containsKey(value)) { //if the map doesnt contain the value, add it to the list
					List<String> arr = new ArrayList<String>();
					count++;
					arr.add(partition.get(0)+""+count);
					arr.add(key+"");
                    System.out.println("PartitionMap doesnt contain value as key. arr="+arr);
					partitionMap.put(value, arr);
				} else { //add it to the partition
					List<String> arr = partitionMap.get(value);
                    System.out.println("PartitionMap contains value as key. arr="+arr);
					arr.add(key+"");
					partitionMap.put(value, arr);
				}
			}	
		}
		
		for(Map.Entry<Integer, List<String>> entry : partitionMap.entrySet()) { //generate the split partition
			splittedPartition.add(entry.getValue());
		}
	
		return splittedPartition;
	}
	
}
