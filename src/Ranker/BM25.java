package Ranker;

import Parse.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class ranks documents according to a known BM25 algorithm
 */
public class BM25  {
    double b = 0.00005;
    double k = 10;
    boolean isSemantic;

    public BM25(boolean isSemantic){
        this.isSemantic = isSemantic;
    }


    /**
     * @param parsedQuery - Document representing the query
     * @param sortedDict - The dictionary
     * @param relevantTerms - The words are relevant if semantics are used
     * @param document - The document that ranks
     * @param termAndSize -
     * @param average - Average document sizes
     * @param numOfDocs - Number of documents
     * @return double
     */
    public double rankDoc(Document parsedQuery, Document document, Map<String, String> sortedDict, ArrayList<String> relevantTerms, HashMap<String, Integer> termAndSize, double average, int numOfDocs) {
        double sum =0;
        if(parsedQuery!= null &&document != null && sortedDict!= null && !sortedDict.isEmpty() ) {
            for (String term : termAndSize.keySet()) {
                int tfInCor= termAndSize.get(term);
                sum = sum + rankBM25perTerm(term, average, document,tfInCor, numOfDocs);
            }
        }
        return sum;
    }

    /**
     * @param term - The term that ranks within the document
     * @param document - The document that ranks
     * @return int - The number of times a word appears in the document
     */
    // tf(i)
    private int termFrequencyInDoc(String term , Document document){
        int val= 0;
        if(term!= null && term.length()>0 && document!= null) {
            if (document.listOfWord.getTermAndTF().containsKey(term))
                val = document.listOfWord.getTermAndTF().get(term);
        }
        return val;
    }


    /**
     * @param term - The term that ranks within the document
     * @param average - Average document sizes
     * @param document - The document that ranks
     * @param tfInDoc - The number of times a word appears in the document
     * @param numOfDocs - Number of documents
     * @return double- Value of the term within the document
     */
    private double rankBM25perTerm(String term , double average, Document document, int tfInDoc, int numOfDocs){
        double toReturn =0;
        if(term != null && term.length()>0  && document!= null ) {
            double dfi = tfInDoc;
            double tfi = termFrequencyInDoc(term, document);
            double dSize = document.getDocumetSize();
            double partA= log_N_Fractional_Dfi(numOfDocs, dfi);
            double partB = k_plus_1_Dual_tfi(tfi);
            double partC = partC(dSize , average , tfi);
            if(partC!=0){
                toReturn = partA*(partB/partC);
            }
        }
        return toReturn;
    }
    /**
     * @param N - Number of documents
     * @param dfi - The amount of documents a word appears on the corpus
     * @return calculation -> log(N/dfi)
     */
    private double log_N_Fractional_Dfi(double N , double dfi){
        double toReturn=0;
        if(dfi>0){
            double nFDfi= N/dfi;
            toReturn = Math.log(nFDfi);
        }
        return toReturn;
    }

    /**
     * calculation -> (k+1)*tfi
     * @param tfi - The number of times a word appears in the document
     * @return double- Normalize the number of times a term
     */
    private double k_plus_1_Dual_tfi(double tfi){
        return (k+1)*tfi;
    }

    /**
     * @param dSize - Document size
     * @param avgD - Average document sizes
     * @param tfi - The number of times a word appears in the document
     * @return double
     */
    private double partC(double dSize, double avgD, double tfi) {
        if (avgD != 0) {
            return  ((((1-b)+b*(dSize/avgD))*k)+tfi);
        }
        return 0;
    }


}
