package Ranker;

import Parse.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BM25  {
    double b = 0.00005;
    double k = 10;
    boolean isSemantic;

    public BM25(boolean isSemantic){
        this.isSemantic = isSemantic;
    }


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

    // tf(i)
    private int termFrequencyInDoc(String term , Document document){
        int val= 0;
        if(term!= null && term.length()>0 && document!= null) {
            if (document.listOfWord.getTermAndTF().containsKey(term))
                val = document.listOfWord.getTermAndTF().get(term);
        }
        return val;
    }


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


    //calculation -> log(N/dfi) //1111111111111111111111111111111111
    private double log_N_Fractional_Dfi(double N , double dfi){
        double toReturn=0;
        if(dfi>0){
            double nFDfi= N/dfi;
            toReturn = Math.log(nFDfi);
        }
        return toReturn;
    }

    //calculation -> (k+1)*tfi //22222222222222222222222222222222222222
    private double k_plus_1_Dual_tfi(double tfi){
        return (k+1)*tfi;
    }

    public double partC(double dSize, double avgD, double tfi) {
        if (avgD != 0) {
            return  ((((1-b)+b*(dSize/avgD))*k)+tfi);
        }
        return 0;
    }


}
