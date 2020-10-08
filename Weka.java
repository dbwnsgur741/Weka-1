import weka.*;
import weka.classifiers.Evaluation;
import weka.classifiers.rules.OneR;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

public class Weka {

	public static void main(String[] args) throws Exception {
		
		// Parking Area API and creat csv file
		
		/*
	    AirplaneParkingArea apa;   
		ApiExplorer api = new ApiExplorer();
		apa = api.getAirPlaneData();
		
		for(String temp : apa.degree){
    		System.out.println(temp);
    	}
		
		creatCSV(apa);
		
		*/
		
		// Use Weka API and visualizing
		
		DataSource source = new DataSource("C:\\Users\\USER\\Desktop\\test.csv");
		Instances trainset = source.getDataSet();
		trainset.setClassIndex(trainset.numAttributes() - 1);
		
		OneR or = new OneR();
		or.buildClassifier(trainset);
		System.out.println(or.toString());
		
		Evaluation eval = new Evaluation(trainset);
		eval.evaluateModel(or, trainset);
		System.out.println(eval.toSummaryString());
		
		
		eval.crossValidateModel(or, trainset, 10, new Random(1));
		System.out.println(eval.toSummaryString());
		System.out.println(eval.toMatrixString());


    	
		
		// if you want test dataset use this !!
		
		/*
		ArrayList<String> attr1 = new ArrayList<String>(3);
		attr1.add("sunny");
		attr1.add("overcast");
		attr1.add("rainy");
		
		Attribute a1 = new Attribute("outlook",attr1);
		Attribute a2 = new Attribute("temperature");
		Attribute a3 = new Attribute("humidty");
		
		ArrayList<String> attr4 = new ArrayList<String>(2);
		attr4.add("TRUE");
		attr4.add("FALSE");
		
		Attribute a4 = new Attribute("windy", attr4);
		
		ArrayList<String> cls = new ArrayList<String>(2);
		cls.add("yes");
		cls.add("no");
		Attribute a5 = new Attribute("class", cls);
		
		ArrayList<Attribute> instanceAttributes = new ArrayList<Attribute>(5);
		instanceAttributes.add(a1);
		instanceAttributes.add(a2);
		instanceAttributes.add(a3);
		instanceAttributes.add(a4);
		instanceAttributes.add(a5);
		*/
		
		/*
		//Test
		Instances testSet = new Instances("testset", instanceAttributes,0);
		testSet.setClassIndex(testSet.numAttributes() - 1);
		
		double[] testData = new double[] {
				0.0, 85.0, 85.0, 1.0
		};
		
		Instance testInstance = new DenseInstance(1.0, testData);
		testSet.add(testInstance);
		double result = tree.classifyInstance(testSet.instance(0));
		System.out.println(result);
		*/
	}
	
	
	public static int creatCSV(AirplaneParkingArea apa) {
		int resultCount = 0;
		
		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter("C:\\Users\\USER\\Desktop\\test.csv",true));
			
			for(int i = 0 ; i < apa.congestion.size(); i++) {
				fw.write(apa.congestion.get(i)+","+apa.degree.get(i)+","+apa.occupiedSpace.get(i)+","+apa.totalSpace.get(i));
				fw.newLine();
				resultCount++;
			}
			
			fw.flush();
			fw.close();
		}catch(Exception e) {
			
			e.printStackTrace();
		}
		
		return 0;
	}	
}




