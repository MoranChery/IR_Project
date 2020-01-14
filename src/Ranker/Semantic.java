package Ranker;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.impl.WuPalmer;

import java.util.ArrayList;


public class Semantic {
    private static ILexicalDatabase db = new NictWordNet();
    // todo
    //find 2 relevant terms synonyms to term
    public String[] getTheSynonymsForTerm(String term , ArrayList<String> allTermsInCorpus){
        String [] relevantTerms = null;
        if(term!= null && term.length()>0 && allTermsInCorpus!=null && allTermsInCorpus.size()>0){
            relevantTerms = new String[2];
            double word1 = 0;
            //double word2 = 0;
            int i= (int)(allTermsInCorpus.size()*0.1);
            for (; i<allTermsInCorpus.size() ; i++) {
                String termRel = allTermsInCorpus.get(i);
                double myVal =  new WuPalmer(db).calcRelatednessOfWords(term, termRel);
                if(myVal>word1){
                    //word2=word1;
                    word1=myVal;
                    //relevantTerms[1] =relevantTerms[0];
                    relevantTerms[0]= termRel;
                }

                //else if(myVal>word2){
                //    word2=myVal;
//
                //}

                //if (word1 > 0.01 && word2 > 0.01) {
                if (word1 > 0.1) {
                    break;
                }
            }
        }

        return relevantTerms;
    }
    // todo
    public ArrayList<String> getTheSynonymsForAllTerm(ArrayList<String>terms , ArrayList<String> allTermsInCorpus){
        ArrayList<String> toReturn= new ArrayList<>();
        if(terms!= null && terms.size()>0 && allTermsInCorpus!=null && allTermsInCorpus.size()>0) {
            for(String term : terms){
                String [] relevant = getTheSynonymsForTerm(term,allTermsInCorpus);
                if(relevant!= null && relevant.length>0){
                    for (int i = 0; i<relevant.length; i++)
                        toReturn.add(relevant[i]);

                }
            }
        }
        return toReturn;
    }


//    public double rankDoc(Document query ,Document document, HashMap<String, Integer> tf, HashMap<String, Integer> documentIdAndSize) {
//        double toReturn=0;
//        if(query != null && document!= null){
//            ArrayList<String> allQueryTerms = query.getAllTerms();
//            ArrayList<String> allDocTerms = document.getAllTerms();
//            if(allDocTerms != null && allDocTerms.size()>0 && allQueryTerms!= null && allQueryTerms.size()>0) {
//                for (String termInQuery : allQueryTerms) {
//                    toReturn = getWordSimToArrWords(allDocTerms,termInQuery);
//                }
//                toReturn =toReturn/allQueryTerms.size();
//            }
//        }
//
//        return toReturn;
//    }
//
//
//    private  double getSimTooWords(String word1 ,String word2 ){
//        double toReturn =0;
//        if(word1 != null && word2 != null && word1.length()>0 && word2.length()>0) {
//            WS4JConfiguration.getInstance().setMFS(true);
//            toReturn= new WuPalmer(db).calcRelatednessOfWords(word1, word2);
//        }
//        return toReturn;
//    }
//
//    private double getWordSimToArrWords(ArrayList<String> words , String word){
//        double sumSim = 0;
//        if(words!= null && words.size()>0 && word != null && word.length()>0){
//            for (String wordCompered: words) {
//                sumSim = sumSim +getSimTooWords(word, wordCompered);
//            }
//            sumSim= sumSim/words.size();
//        }
//        return sumSim;
//    }


}
