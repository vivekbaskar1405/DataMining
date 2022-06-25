import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class AprioriAlgorithm {

	private List<Set<String>> userTransactions = new ArrayList<>();
	public void executeAprioriAlgorithm(String fileName, double userSupport, double userConfidence) throws Exception {
		try {
			Map<Set<String>, Integer> supportMap = new HashMap<>();

			File input = new File(fileName);
			Scanner transactions = new Scanner(input);
			System.out.println("\n-------Input Data START------\n");
			while (transactions.hasNextLine()) {
				String line = transactions.nextLine();
				System.out.println(line);
				String[] items= line.split(",");
				HashSet<String> set = new HashSet<>();
				for (String item : items) {
					set.add(item);
					HashSet<String> temp = new HashSet<>();
					temp.add(item);
					supportMap.put(temp, supportMap.getOrDefault(temp, 0) + 1);
				}
				userTransactions.add(set);
			}	
			transactions.close();
			System.out.println("\n-------Input Data END------\n");

			List<Set<String>> frequentItems = new ArrayList<>();

			for(Map.Entry<Set<String>, Integer> entry : supportMap.entrySet()) {
				double calculatedSupport = (double) entry.getValue()/userTransactions.size();
				calculatedSupport = calculatedSupport* 100;
				if(calculatedSupport >= userSupport) {
					frequentItems.add(entry.getKey());
				}
			}
			Map<Integer, List<Set<String>>> filteredList = new HashMap<>();
			filteredList.put(1, frequentItems);



			int i = 1;
			do {
				i++;


				List<Set<String>> firstCandidateList = calculateCandidateList(filteredList.get(i-1),userSupport);
				for(Set<String> transaction : userTransactions) 
				{

					List<Set<String>> secondCandidateList = new ArrayList<>(firstCandidateList.size());
					for (Set<String> c : firstCandidateList) {
						if (transaction.containsAll(c)) {
							secondCandidateList.add(c);
						}
					}

					for (Set<String> itemSet : secondCandidateList) {
						supportMap.put(itemSet, supportMap.getOrDefault(itemSet, 0) + 1);
					}
				}



				List<Set<String>> res = new ArrayList<>(firstCandidateList.size());
				for (Set<String> item : firstCandidateList) {
					if (supportMap.containsKey(item)) {
						double calculatedSupport = (double)supportMap.get(item)/userTransactions.size();
						calculatedSupport =calculatedSupport* 100;
						if (calculatedSupport >= userSupport) {
							res.add(item);
						}
					}
				}
				filteredList.put(i, res);


			} while (!filteredList.get(i).isEmpty());


			System.out.println("\n-------Apriori Association Output Data START------\n");
			AssociationRule associationRule = new AssociationRule();
			List<FrequentItemset> rules = associationRule.calculateAssociation(filteredList, supportMap, userConfidence);
			for(FrequentItemset r : rules) {
				System.out.println(r.getAntecedent() + " -> " + r.getConsequent() + " Support Value: " + r.getUserSupport() + " Confidence value: " + r.getUserConfidence());
			}
			System.out.println("\n-------Apriori Association Output Data END------\n");

		} catch (Exception e) {
			System.out.println("Exception in executeAprioriAlgorithm : "+e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}
	private List<Set<String>> calculateCandidateList(List<Set<String>> firstCandidateList, double userSupport) {
		try {
			List<List<String>> secondCandidateList = new ArrayList<>(firstCandidateList.size());
			for (Set<String> item : firstCandidateList) {
				List<String> temp = new ArrayList<>(item);
				Collections.sort(temp);
				secondCandidateList.add(temp);
			}

			int listSize = secondCandidateList.size();
			List<Set<String>> res = new ArrayList<>();


			for(int i=0; i< listSize; i++) {
				for(int j=i+1; j< listSize; j++) {
					Set<String> candidate = combineCandidateLists(secondCandidateList.get(i), secondCandidateList.get(j));
					if (candidate != null) {
						res.add(candidate);
					}
				}
			}
			return res;
		} catch (Exception e) {
			System.out.println("Exception in calculateCandidateList : "+e.getMessage());
			throw e;
		}
	}

	private Set<String> combineCandidateLists(List<String> secondCandidateList1, List<String> secondCandidateList2) {
		try {
			int length = secondCandidateList1.size();
			for (int k = 0; k < secondCandidateList1.size() - 1; ++k) {
				if (!secondCandidateList1.get(k).equals(secondCandidateList2.get(k))) {
					return null;
				}
			}
			if (secondCandidateList1.get(length - 1).equals(secondCandidateList2.get(length - 1))) {
				return null;
			}

			Set<String> candidate = new HashSet<>(length + 1);

			for (int k = 0; k < length - 1; ++k) {
				candidate.add(secondCandidateList1.get(k));
			}

			candidate.add(secondCandidateList1.get(length - 1));
			candidate.add(secondCandidateList2.get(length - 1));
			return candidate;
		} catch (Exception e) {
			System.out.println("Exception in combineCandidateLists : "+e.getMessage());
			throw e;
		}
	}
}