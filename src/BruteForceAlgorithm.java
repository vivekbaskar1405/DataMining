
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class BruteForceAlgorithm {

	private List<Set<String>> userTransactions = new ArrayList<>();

	public void executeBruteForceAlgorithm(String fileName, double userSupport, double userConfidence) throws Exception {

		try {
			Set<String> rawSet = new HashSet<>();
			File input = new File(fileName);
			Scanner transactions = new Scanner(input);
			System.out.println("\n-------Input Data START------\n");
			while (transactions.hasNextLine()) {
				String line = transactions.nextLine();
				System.out.println(line);
				String[] items = line.split(",");
				HashSet<String> set = new HashSet<>();
				for (String item : items) {
					set.add(item);
					rawSet.add(item);
				}
				userTransactions.add(set);
			}
			transactions.close();
			System.out.println("\n-------Input Data START------\n");


			List<List<String>> subsetList = generateSubsets(new ArrayList<>(rawSet),userConfidence);


	
			Map<Set<String>, Integer> supportMap = new HashMap<>();
			for(List<String> list : subsetList) {
				Set<String> s = new HashSet<>(list);
				supportMap.put(s, 0);
			}

			for(Set<String> set : supportMap.keySet()) {
				for(Set<String> transaction: userTransactions) {
					if(transaction.containsAll(set)) {
						supportMap.put(set, supportMap.get(set)+1);
					}
				}
			}

			Map<Integer, List<Set<String>>> filteredList = new HashMap<>();
			for(Set<String> s : supportMap.keySet()) {
				double calculatedSupport = ((double) supportMap.get(s) / userTransactions.size()) * 100;
				if(calculatedSupport > userSupport) {
					List<Set<String>> list = filteredList.computeIfAbsent(s.size(), k -> new ArrayList<>());
					list.add(s);
					filteredList.put(s.size(), list);
				}
			}



			System.out.println("\n-------Brute Force Association Output Data START------\n");
			AssociationRule associationRule = new AssociationRule();
			List<FrequentItemset> rules = associationRule.calculateAssociation(filteredList, supportMap, userConfidence);
			for(FrequentItemset r : rules) {
				System.out.println(r.getAntecedent() + " -> " + r.getConsequent() + " Support Value: " + r.getUserSupport() + " Confidence value: " + r.getUserConfidence());
			}
			System.out.println("\n-------Brute Force Association Output Data END------\n");


		} catch (Exception e) {
			System.out.println("Exception in executeAprioriAlgorithm : "+e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	private List<List<String>> generateSubsets(List<String> firstCandidateList, double userConfidence) {
		List<List<String>> subset = new ArrayList<>();
		generateAllSubsets(subset, new LinkedList<>(), firstCandidateList, 0,userConfidence);
		return subset;
	}

	private void generateAllSubsets(List<List<String>> subset, LinkedList<String> currentSubset, List<String> subsetList, int s,double userConfidence) {
		if(currentSubset.size()!=0 && currentSubset.size()!=subsetList.size()) {
			subset.add(new ArrayList<>(currentSubset));
		}
		for(int i=s; i<subsetList.size(); i++) {
			currentSubset.addLast(subsetList.get(i));
			generateAllSubsets(subset, currentSubset, subsetList, i+1,userConfidence);
			currentSubset.removeLast();
		}
	}
}
