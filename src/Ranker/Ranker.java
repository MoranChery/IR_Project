package Ranker;

import Parse.Document;

import java.util.*;

/**
 * This class ranks documents according to a particular document or query
 */
public class Ranker {

    boolean isSemantic;

    BM25 bm25;
    Semantic semantic;


    public Ranker(boolean isSemantic) {
        this.isSemantic = isSemantic;
        bm25 = new BM25(isSemantic);
    }

    /**
     * @param parsedQuery - Document representing the query
     * @param sortedDict - The dictionary
     * @param relevantTerms - The words are relevant if semantics are used
     * @param allRelevantDocs - All relevant documents
     * @param size -The amount of documents we want to retrieve
     * @return ArrayList<String> - Relevant documents
     */
    public ArrayList<String> rank(Document parsedQuery, ArrayList<Document> allRelevantDocs, int size, Map<String, String> sortedDict, ArrayList<String> relevantTerms) {
        ArrayList<String> toReturn = null;
        if (parsedQuery != null && allRelevantDocs != null && allRelevantDocs.size() > 0) {
            HashMap<String, Double> docAndValRank = new HashMap<>();
            double average = getDocAvg(allRelevantDocs);
            HashMap<String, Integer> termAndFrequency = getTermAndFrequency(allRelevantDocs);
            for (Document document : allRelevantDocs) {
                    double myRank = rankDoc(parsedQuery, document, sortedDict, relevantTerms, average, termAndFrequency, allRelevantDocs.size());
                    docAndValRank.put(document.getId(), myRank);
            }
            toReturn = merge(docAndValRank, size);
        }
        return toReturn;
    }

    /**
     * @param allRelevantDocs - Term and its rating
     * @return HashMap<String, Integer>
     */
    private HashMap<String, Integer> getTermAndFrequency(ArrayList<Document> allRelevantDocs) {
        HashMap<String, Integer> termAndFrequency = null;
        if (allRelevantDocs != null && !allRelevantDocs.isEmpty()) {
            termAndFrequency= new HashMap<>();
            for (Document document : allRelevantDocs) {

                    ArrayList<String> termInThisDoc=document.getAllTerms();
                    for (String term : termInThisDoc) {
                        if(termAndFrequency.containsKey(term)){
                            int tf = termAndFrequency.get(term)+1;
                            termAndFrequency.replace(term, tf);
                        }
                        else {
                            termAndFrequency.put(term, 1);
                        }
                    }

            }
        }
        return termAndFrequency;
    }

    /**
     * @param documents - all the document and there rank
     * @return double- Average document size
     */
    private double getDocAvg(List < Document > documents) {
        double avg = 0;
        double sum = 0;
        for (Document doc : documents) {
            sum += doc.getDocumetSize();
        }
        avg = sum/documents.size();
        return avg;
    }

    /**
     * @param parsedQuery - Document representing the query
     * @param document - The document we rank
     * @param sortedDict - The dictionary
     * @param relevantTerms - The words are relevant if semantics are used
     * @param average- Average document size
     * @param termAndFrequency- All their terms and frequency in corpus
     * @param numOfDocs - The number of documents that rank them
     * @return double- the rank
     */
    private double rankDoc (Document parsedQuery, Document document, Map < String, String > sortedDict,
                            ArrayList < String > relevantTerms, double average,
                            HashMap < String, Integer > termAndFrequency, int numOfDocs)
    {
        double toRet = 0;
        if ( sortedDict != null && !sortedDict.isEmpty()&& termAndFrequency != null && !termAndFrequency.isEmpty()) {
            toRet = bm25.rankDoc(parsedQuery, document, sortedDict, relevantTerms,  termAndFrequency, average, numOfDocs);
        }
        return toRet;
    }


    /**
     * @param docAndValRank - all the document and there rank
     * @param size - the number of the documents we want to return
     * @return sorted document by max rank
     */
    private ArrayList<String> merge (HashMap < String, Double > docAndValRank ,int size ){
        ArrayList<String> toReturn = null;
        if (docAndValRank != null) {
            toReturn = new ArrayList<>();
            int addStr = 0;
            while (!docAndValRank.isEmpty()) {
                ArrayList<String> getDocId = getStringsWithMaxVal(docAndValRank);
                if (getDocId != null && !getDocId.isEmpty()) {
                    if (getDocId.size() + addStr < size) {
                        toReturn.addAll(getDocId);
                        addStr = addStr + getDocId.size();
                    } else {
                        while (!getDocId.isEmpty() && addStr < size) {
                            toReturn.add(getDocId.remove(0));
                            addStr++;
                        }
                    }
                }

            }
        }
        return toReturn;
    }

    /**
     * @param docAndValRank - all the document and there rank
     * @return ArrayList<String> - term that as mas rank
     */
    private ArrayList<String> getStringsWithMaxVal (HashMap < String, Double > docAndValRank){
        ArrayList<String> toReturn = null;
        if (docAndValRank != null && !docAndValRank.isEmpty()) {
            toReturn = new ArrayList<>();
            ArrayList<String> toRemove = new ArrayList<>();
            double maxVal = findMaxVal(docAndValRank);
            for (String docId : docAndValRank.keySet()) {
                if (maxVal == docAndValRank.get(docId)) {
                    toReturn.add(docId);
                    toRemove.add(docId);
                }
            }
            for (int i = 0; i < toRemove.size(); i++) {
                docAndValRank.remove(toReturn.get(i));
            }
        }
        return toReturn;
    }

    /**
     * @param docAndValRank  - all the document and the rank
     * @return double - the max value that term rank
     */
    private double findMaxVal (HashMap < String, Double > docAndValRank){
        double toReturn = 0;
        if (docAndValRank != null && !docAndValRank.isEmpty()) {
            Collection<Double> allVal = docAndValRank.values();
            toReturn = Collections.max(allVal);
        }
        return toReturn;
    }



}
