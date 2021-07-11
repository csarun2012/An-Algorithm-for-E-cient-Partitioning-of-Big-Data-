import java.util.*;

public class Attribute {
	public Map<Integer, Integer> dataSet;   //every data item is mapped to a value in a row
	public boolean target = false;      // target value of that row
	
	public Attribute(Map<Integer, Integer> dataSet, boolean target) { //create object with the given values
		this.dataSet = dataSet;
		this.target = target;
	}
	
	public Attribute(Attribute attr) {  //copy one attribute to another
		this.dataSet = attr.getDataSet();
	}
	
	public Attribute getPartition(List<Integer> partitionList) {    //pass partition list and generate the attribute set from those partitions
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();
		for(int i=0; i<partitionList.size(); i++) { // take each item in that list of partitions
			data.put(partitionList.get(i), dataSet.get(partitionList.get(i))); //add the partition and its corresponding value in the dataSet to a new hashmap
		}
		return new Attribute(data, false);  //based on the new hashmap, build a new attribute and return it
	}
	
	public double getEntropy() {    //find overall entropy
		Map<Integer, Integer> dataCount = new HashMap<Integer, Integer>(); // to count the number of items in each attribute
		int totalexamples = 0;
		for(Map.Entry<Integer, Integer> entry: dataSet.entrySet()) {    //take each entry in the dataSet
			if(!dataCount.containsKey(entry.getValue())) { //if the item occurs for the first time, set its count as 1 in dataCount
				dataCount.put(entry.getValue(), 1);
			} 
			else {  //get the latest entry in the dataCount set for that item, incremenet it, and add the updated value to dataCount
				Integer count = dataCount.get(entry.getValue());
				count++;
				dataCount.put(entry.getValue(), count);
			}
			totalexamples++;
		}
		
		double entropy = 0.0;
		
		for(Map.Entry<Integer, Integer> entry: dataCount.entrySet()) { // for each item in the dataCount set
			int val = entry.getValue();                             // get number of occurrences of that item
			double pr = (double)val/(double)totalexamples;          // find pr = number of occurrences / total number of items (find fraction of occurrence of that item)
			entropy += pr*((Math.log(1/pr))/(Math.log(2)));         // use standard entropy equation using log probability
		}
		return entropy;
	}
	
	public double getEntropy(Attribute conditionalAttribute) {      //find entropy of a particular attribute
		Map<Integer, List<Integer>> dataPartition = new HashMap<Integer, List<Integer>>(); //map a value to a set of numbers
		int totalexamples = 0;
		for(Map.Entry<Integer, Integer> entry: conditionalAttribute.getDataSet().entrySet()) { //iterate over the set of items in that attribute
			if(dataSet.containsKey(entry.getKey())) {   //if dataSet contains the item, proceed. Otherwise, ignore this item
				if(!dataPartition.containsKey(entry.getValue())) {  //if dataPartition doesnt contain this item, create a new list and add it to the partition set
					List<Integer> partitionList = new ArrayList<Integer>();
					partitionList.add(entry.getKey());
					dataPartition.put(entry.getValue(), partitionList);
				} else {    // if dataPartition contains the item, take the attached list and add this item to that list
					List<Integer> partitionList = dataPartition.get(entry.getValue());
					partitionList.add(entry.getKey());
					dataPartition.put(entry.getValue(), partitionList);
				}
				totalexamples++;
			}
		}
		
		double entropy = 0.0;
		for(Map.Entry<Integer, List<Integer>> entry: dataPartition.entrySet()) { //iterate over the partition set
			Attribute a = this.getPartition(entry.getValue());  //pick the attribute for that partition
			double _entropy = a.getEntropy();   //find the entropy of that attribute
			double pr = (double)(entry.getValue().size())/totalexamples; //find pr=size/total, and add scaled value to the entropy
			_entropy = _entropy*pr;
			
			entropy += _entropy;
		}
		
		return entropy;
	}
	
	public Map<Integer, Integer> getDataSet() { //return associated dataSet
		return dataSet;
	}

	public void setDataSet(Map<Integer, Integer> dataSet) { //set the dataSet
		this.dataSet = dataSet;
	}
    
    public void print()
    {
        System.out.print(dataSet);
        System.out.println(target);
    }
}
