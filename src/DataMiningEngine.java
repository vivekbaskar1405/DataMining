

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class DataMiningEngine {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		double userSupport; 
		double userConfidence;
		try {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Welcome to Data Mining NJIT CS634 Summer 2022 : Dev :  Vivek Baskar");

			System.out.println("\nPlease enter Minimum Confidence in Multiples of 10: Max Value : 100 eg: 50 \n");
			String minConf = scanner.nextLine();
			userConfidence = Double.parseDouble(minConf);


			System.out.println("\nPlease enter Minimum Support in Multiples of 10: Max Value : 100 eg: 50\n");
			String minSupport = scanner.nextLine(); 
			userSupport = Double.parseDouble(minSupport);


			File actual = new File(".");
			for( File f : actual.listFiles()){
				if(f.isFile() && f.getName()!=null && f.getName().contains(".csv"))
				{	
					System.out.println("\n---------------------------------------------------------------------------------------");
					System.out.println("\nFile Name to be Processed :: "+f.getName());


					System.out.println("User Support Value: " + userSupport + " User Confidence Value: " + userConfidence );


					LocalDateTime startTimeAP = LocalDateTime.now();
					System.out.println("\n>>>>>>>>>>>>>>>>>>>>Apriori Algorithm START<<<<<<<<<<<<<<<<<<<<"+startTimeAP);

					AprioriAlgorithm aprioriAlgorithm = new AprioriAlgorithm();
					aprioriAlgorithm.executeAprioriAlgorithm(f.getName(),userSupport,userConfidence);

					LocalDateTime endTimeAP = LocalDateTime.now();
					System.out.println(">>>>>>>>>>>>>>>>>>>>Apriori Algorithm END<<<<<<<<<<<<<<<<<<<<"+endTimeAP);
					long aprioriRunTime = ChronoUnit.MILLIS.between(startTimeAP, endTimeAP);
					System.out.println("\nApriori Algorithm Run Time :: " + aprioriRunTime + " ms");


					LocalDateTime startTimeBF = LocalDateTime.now();
					System.out.println("\n>>>>>>>>>>>>>>>>>>>>Brute Force Algorithm START<<<<<<<<<<<<<<<<<<<<"+startTimeBF);

					BruteForceAlgorithm bruteForceAlgorithm = new BruteForceAlgorithm();
					bruteForceAlgorithm.executeBruteForceAlgorithm(f.getName(), userSupport, userConfidence);

					LocalDateTime endTimeBF = LocalDateTime.now();
					System.out.println(">>>>>>>>>>>>>>>>>>>>Brute Force Algorithm END<<<<<<<<<<<<<<<<<<<<"+endTimeBF);



					long bruteRunTime = ChronoUnit.MILLIS.between(startTimeBF, endTimeBF);
					System.out.println("Brute Force Algorithm Run Time :: " + bruteRunTime + " ms");

					long sub = bruteRunTime-aprioriRunTime;
					System.out.println("Apriori is " + sub + " ms faster then BruteForce for Dataset:: "+f.getName());
					System.out.println("---------------------------------------------------------------------------------------\n");
					Thread.sleep(5000);
				}else
				{
					System.out.println("No CSV File Exists in the current Path "+f.getName());
				}
			}
			System.exit(0);
		}
		catch(Exception e)
		{
			System.out.println("Exception ::"+e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}


}
