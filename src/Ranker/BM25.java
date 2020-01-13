package Ranker;

import Parse.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BM25  {
    double b = 0.75;
    double k = 1.2;
    boolean isSemantic;

    public BM25(boolean isSemantic){
        this.isSemantic = isSemantic;
    }


    public double rankDoc(Document parsedQuery, Document document, Map<String, String> sortedDict, ArrayList<String> relevantTerms, HashMap<String, Integer> termAndSize, HashMap<String, Integer> docAndSize) {
        double sum =0;
        if(parsedQuery!= null &&document != null && sortedDict!= null && !sortedDict.isEmpty() && relevantTerms!= null && !relevantTerms.isEmpty() ) {
            ArrayList<String> allTerms = parsedQuery.getAllTerms();
            for (String term : allTerms) {
                int tfInCor= termAndSize.get(term);
                sum = sum + rankBM25perTerm(term, docAndSize, document,tfInCor);
            }
            if(isSemantic){
                double partSum1= sum*0.95;
                for (String term : relevantTerms) {
                    int tfInCor= termAndSize.get(term);
                    sum = sum + rankBM25perTerm(term, docAndSize, document, tfInCor)*0.05;
                }
                sum = partSum1+sum;
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

    private double avgDocumentsSize(HashMap<String, Integer> documentIdAndSize){
        int avgD = 0;
        if( documentIdAndSize!=null &&!documentIdAndSize.isEmpty()){
            int sum=0;
            for (String docId: documentIdAndSize.keySet()) {
                sum = sum +documentIdAndSize.get(docId);
            }
            avgD = sum/(documentIdAndSize.size());
        }
        return avgD;
    }

    private double rankBM25perTerm(String term , HashMap<String, Integer> documentIdAndSize, Document document, int tfInDoc){
        double toReturn =0;

        if(term != null && term.length()>0 && documentIdAndSize!=null &&documentIdAndSize.size()>0 && document!= null ) {

            int N = documentIdAndSize.size();
            int dfi = tfInDoc;
            int tfi = termFrequencyInDoc(term, document);
            int dSize = document.getDocumetSize();
            double avgD = avgDocumentsSize(documentIdAndSize);

            double partA= log_N_Fractional_Dfi(N, dfi);
            double partB = k_plus_1_Dual_tfi(tfi);
            double partC = calculationWithBAndWithKAndWithTfi_d_fra_avgD(dSize , avgD , tfi);

            if(partC!=0){
                toReturn = partA*(partB/partC);
            }
        }

        return toReturn;
    }

    //calculation -> log(N/dfi)
    private double log_N_Fractional_Dfi(int N , int dfi){
        double toReturn=0;
        if(dfi>0){
            double nFDfi= N/dfi;
            toReturn = Math.log(nFDfi);
        }
        return toReturn;
    }

    //calculation -> (k+1)*tfi
    private double k_plus_1_Dual_tfi(int tfi){
        return (k+1)*tfi;
    }

    //calculation -> |d|/avg(d)
    private double documentSize_Fractional_avgD(int dSize, double avgD){
        double toReturn=0;
        if(avgD!= 0){
            toReturn = dSize/avgD;
        }
        return toReturn;
    }

    //calculation -> (1-b)+b*(|d|/avg(d))
    private double calculationWithB_d_fra_avgD(int dSize, double avgD){
        double d_fra_avgD = documentSize_Fractional_avgD(dSize,avgD);
        return (1-b)+b*d_fra_avgD;
    }

    //calculation -> k*((1-b)+b*(|d|/avg(d)))
    private double calculationWithBAndWithK_d_fra_avgD(int dSize, double avgD){
        double b_d_fra_avgD =calculationWithB_d_fra_avgD(dSize, avgD);
        return b_d_fra_avgD*k;
    }

    //calculation -> k*((1-b)+b*(|d|/avg(d)))+tfi
    private double calculationWithBAndWithKAndWithTfi_d_fra_avgD(int dSize, double avgD, double tfi){
        double b_d_fra_avgD_k = calculationWithBAndWithK_d_fra_avgD(dSize,avgD);
        return b_d_fra_avgD_k+tfi;
    }

}
