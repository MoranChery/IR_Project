package Ranker;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import java.util.ArrayList;

public class Semantic {
    private static ILexicalDatabase db = new NictWordNet();

    /**
     * @param term  -the term we want to find relevant words
     * @param allTermsInCorpus - all the terms that need compere to
     * @return  String[] - relevant terms
     */
    //find 1 relevant terms synonyms to term
    private String[] getTheSynonymsForTerm(String term, ArrayList<String> allTermsInCorpus){
        String [] relevantTerms = null;
        if(term!= null && term.length()>0 && allTermsInCorpus!=null && allTermsInCorpus.size()>0){
            relevantTerms = new String[2];
            double word1 = 0;
            int i= (int)(allTermsInCorpus.size()*0.1);
            for (; i<allTermsInCorpus.size() ; i++) {
                String termRel = allTermsInCorpus.get(i);
                double myVal =  new WuPalmer(db).calcRelatednessOfWords(term, termRel);
                if(myVal>word1){
                    word1=myVal;
                    relevantTerms[0]= termRel;
                }
                if (word1 > 0.9) {
                    break;
                }
            }
        }

        return relevantTerms;
    }

    /**
     * @param terms - all the terms that we want to find for them relevant words
     * @param allTermsInCorpus - all the terms that need compere to
     * @return ArrayList<String> all the terms that relevant to set of words
     */
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

}
