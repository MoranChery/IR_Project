import Parse.Document;
import Ranker.Ranker;
import Ranker.BM25;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Test {


   // public static void main(String[] args) {
   //     Ranker r = new Ranker(false);
//
   //     Document query = new Document("query");
   //     query.setNumOfTerms(2);
   //     query.setNumOfWords(2);
   //     query.listOfWord.addTermAndTF("NOY", 1);
   //     query.listOfWord.addTermAndTF("ROI", 1);
   //     Document document1 = new Document("doc1");
   //     Document document2 = new Document("doc2");
   //     Document document3 = new Document("doc3");
   //     document1.setNumOfWords(3);
   //     document2.setNumOfWords(3);
   //     document3.setNumOfWords(5);
//
   //     document1.setNumOfTerms(1);
   //     document2.setNumOfTerms(1);
   //     document3.setNumOfTerms(2);
//
   //     Map<String, String> sortedDic= new HashMap<>();
   //     sortedDic.put("NOY", "5");
   //     sortedDic.put("ROI","7");
   //     ArrayList<String> relevantTerms=null;
//
   //     document1.listOfWord.addTermAndTF("NOY", 1);
   //     document2.listOfWord.addTermAndTF("ROI", 1);
   //     document3.listOfWord.addTermAndTF("NOY", 1);
   //     document3.listOfWord.addTermAndTF("ROI", 1);
//
   //     HashMap<String, Integer> termAndSize=new HashMap<>();
   //     termAndSize.put("NOY",2);
   //     termAndSize.put("ROI",2);
//
   //     HashMap<String, Integer> docAndSize= new HashMap<>();
   //     docAndSize.put("doc1",3);
   //     docAndSize.put("doc2",3);
   //     docAndSize.put("doc3",5);
//
//
   //     BM25 bm25 = new BM25(false);
//
   //     double doc1Rank = bm25.rankDoc(query, document1, sortedDic, relevantTerms , termAndSize , docAndSize);
   //     double doc2Rank = bm25.rankDoc(query, document2, sortedDic, relevantTerms , termAndSize , docAndSize);
   //     double doc3Rank = bm25.rankDoc(query, document3, sortedDic, relevantTerms , termAndSize , docAndSize);
   //     System.out.println(doc1Rank);
   //     System.out.println(doc2Rank);
   //     System.out.println(doc3Rank);

//
//        //של השאילתא מחזיק את המסמכים וכל המונחים שיש בהם
//        HashMap<String , ArrayList<Pair<String,Integer>>> docToQueryTerms = new HashMap<>();
//        //המונחים עם הכמות בקורפוס
//        HashMap<String, Integer> termTodf = new HashMap<>();
//        //הוספה
//        termTodf.put("NOY",5);
//        termTodf.put("ROI",7);
////        termTodf.put("HOME",3);
//        //המונח והכמות במסמך
//        Pair<String,Integer> p1 = new Pair<>("NOY",1);
//        Pair<String,Integer> p2 = new Pair<>("ROI",1);
//        // נוי נמצאת המסמך 1
//        ArrayList<Pair<String,Integer>> l1 = new ArrayList<>();
//        l1.add(p1);
//        docToQueryTerms.put("doc1",l1);
//        // רועי נמצאת המסמך 2
//        ArrayList<Pair<String,Integer>> l2 = new ArrayList<>();
//        l2.add(p2);
//        docToQueryTerms.put("doc2",l2);
//        // נוי ורועי נמצאת המסמך 3
//        ArrayList<Pair<String,Integer>> l3 = new ArrayList<>();
//        l3.add(p1);
//        l3.add(p2);
//        docToQueryTerms.put("doc3",l3);
//        // אורך המסמכים
//        HashMap<String, Integer> docToLength = new HashMap<>();
//        docToLength.put("doc1",3);
//        docToLength.put("doc2",3);
//        docToLength.put("doc3",5);

//        HashMap<String ,ArrayList<Pair<String,Integer>>> semTerms = new HashMap<>();
//        ArrayList<Pair<String,Integer>> l4 = new ArrayList<Pair<String,Integer>>();
//        Pair<String,Integer> p3 = new Pair<>("HOME",1);
//        l4.add(p3);
//        semTerms.put("doc1",l4);


//
//        //מריצה את הרנקר
//        ArrayList<DocScore> rankScores = r.rank(docToQueryTerms, termTodf, docToLength,semTerms,false);
//        results:
//        score - doc 1 : -0.105545559
//        score - doc 2: -0.264694905
//        //score - doc 3: -0.292142866
//        System.out.println(rankScores.get(0));
//        System.out.println(rankScores.get(1));
//        System.out.println(rankScores.get(2));
    }

