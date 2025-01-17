package Searcher;

import Parse.Document;
import Parse.Parse;
import Ranker.Ranker;
import Ranker.Semantic;

import java.io.*;
import java.util.*;

public class Searcher {
    private String query;
    private HashMap<String, String> queries;
    private Ranker ranker;
    private Document parsedQuery;
    private Parse parser;
    private ArrayList<Document> allRelevantDocs;
    private ReadQueries reader;
    private int size;
    private TreeMap<String, ArrayList<String>> results;
    String saveQueryPath;
    boolean isSemantic;
    Semantic semantic;
    private ArrayList<String> semanticTerms;


    /**
     * Constructor for a query from the text box
     * @param query
     * @param parser
     * @param isSemantic
     * @param saveQueryPath
     */
    public Searcher(String query, Parse parser, boolean isSemantic,String saveQueryPath) {
        ranker = new Ranker(isSemantic);
        this.parser = parser;
        this.query = query;
        allRelevantDocs = new ArrayList<>();
        size = 50;
        results = new TreeMap<>();
        this.saveQueryPath = saveQueryPath;
        this.isSemantic= isSemantic;
        if(isSemantic){
            semantic = new Semantic();
        }
    }

    /**
     * Constructor for a file of queries
     * @param path
     * @param isSemantic
     * @param parse
     * @param isFile
     * @param saveQueryPath
     */
    public Searcher(String path, boolean isSemantic, Parse parse, boolean isFile,String saveQueryPath) {
        parser = parse;
        ranker = new Ranker(isSemantic);
        reader = new ReadQueries(path);
        allRelevantDocs = new ArrayList<>();
        size = 50;
        results = new TreeMap<>();
        this.saveQueryPath = saveQueryPath;
        this.isSemantic= isSemantic;
        if(isSemantic){
            semantic = new Semantic();
        }
    }

    /**
     * This is the main method of Searcher class
     * it'S get all the information from the posting files, arrange it, and then send it to the ranker class
     */
    public void start() {
        try {
            if (reader != null) {
                this.reader.resetReader();
                getAllQueries();
            }
            parser.allQueries.clear();
            parseQueries();
            System.out.println("done parse all the queries"); //todo delete
            long totalTime = 0;
            for (Document doc : parser.allQueries) {
                long start = System.currentTimeMillis();
                System.out.println("start parsing " + doc.getId());
                parsedQuery = doc;
                ArrayList<String> allRelevantTerms = parsedQuery.listOfWord.getAllTerms();
                if(isSemantic){
                    ArrayList<String> allTerms = new ArrayList<>();
                    //allTerms.addAll(parser.indexer.getSortedDict().keySet());
                    semanticTerms = semantic.getTheSynonymsForAllTerm(parsedQuery.getAllTerms(), allTerms);
                    allRelevantTerms.addAll(semanticTerms);
                }
                else {
                    semanticTerms = null;
                }
                HashMap<String, ArrayList<Integer>> allPath = getAllPath();
                HashMap<String, Document> allRelevantDocs = getAllRelevantDocs(allPath, allRelevantTerms);
                if (isSemantic) {
                    HashMap<String, ArrayList<Integer>> allPathSemantic = getAllPathForRelevantTerms();
                    addRelevantTerms(allRelevantDocs, allPathSemantic);
                }
                updateAllRelevantDocsDetails(allRelevantDocs);
                ArrayList<Document> allDocs = new ArrayList<Document>();
                allDocs.addAll(allRelevantDocs.values());
                ArrayList<String> relevantDocs = ranker.rank(parsedQuery, allDocs , size, parser.getSortedDict(), semanticTerms);
                results.put(parsedQuery.getId(), relevantDocs);
                long end = System.currentTimeMillis();
                totalTime += (end-start);
                System.out.println("done " + doc.getId() + " in " + (end-start) + " ms");
            }
            System.out.println("Total time is: " + totalTime);
            writeResults(saveQueryPath);
        } catch (Exception e) {
        }
    }

    /**
     *
     * @param allRelevantDocs
     * @param allPathSem
     */
    private void addRelevantTerms(HashMap<String,Document> allRelevantDocs, HashMap<String, ArrayList<Integer>> allPathSem) {
        if (allRelevantDocs != null && allRelevantDocs.size() > 0 && allPathSem != null && allPathSem.size() > 0) {
            for (String file : allPathSem.keySet()) {
                ArrayList<String> allFileLines = read(file, allPathSem.get(file));
                for (String line : allFileLines) {
                    String[] words = line.split(";");
                    String termName = words[0];
                    for (int i = 1; i < words.length; i = i + 2) {
                        Document document;
                        if (allRelevantDocs.containsKey(words[i])) {
                            document = allRelevantDocs.get(words[i]);
                            document.listOfWord.addTermAndTF(termName, Integer.valueOf(words[i + 1]));
                        }
                    }
                }
            }
        }
    }

    /**
     * This method gets all the path to the relevants docs when using semantic algorithm
     * @return
     */
    public HashMap<String, ArrayList<Integer>> getAllPathForRelevantTerms() {
        HashMap<String, ArrayList<Integer>> allPostingFilesAndLines = new HashMap<>();
        if (semanticTerms.size() > 0) {
            HashMap<String, List<String[]>> pointers = parser.indexer.getPointers();
            for (String term : semanticTerms) {
                if (pointers.containsKey(term)) {
                    for (String[] posting : pointers.get(term)) {
                        String file = posting[0];
                        String line = posting[1];
                        if (allPostingFilesAndLines.containsKey(file)) {
                            allPostingFilesAndLines.get(file).add(Integer.valueOf(line));
                        } else {
                            ArrayList<Integer> lines = new ArrayList<>();
                            lines.add(Integer.valueOf(line));
                            allPostingFilesAndLines.put(file, lines);
                        }
                    }
                }
            }
        }
        return allPostingFilesAndLines;
    }


    /**
     * load all the relevant documents terms and tf
     * @param allPath
     * @param allRelevantTerms
     * @return
     */
    private HashMap<String, Document> getAllRelevantDocs(HashMap<String, ArrayList<Integer>> allPath, ArrayList<String> allRelevantTerms) {
        HashMap<String, Document> allDocs = new HashMap();
        for (String file : allPath.keySet()) {
            ArrayList<String> allFileLines = read(file, allPath.get(file));
            for (String line : allFileLines) {
                String[] words = line.split(";");
                String termName = words[0];
                for (int i = 1; i < words.length; i = i+2) {
                    Document document;
                    if (allDocs.containsKey(words[i])) {
                        document = allDocs.get(words[i]);
                    }
                    else {
                        document = new Document(words[i]);
                        allDocs.put(document.getId(), document);
                    }
                    try {
                        document.listOfWord.addTermAndTF(termName,Integer.valueOf(words[i+1]));
                    }
                    catch (Exception e) {
                        System.out.println("error in getAllRelevantDocs function");
                    }
                }
            }

        }
        return allDocs;
    }


    /**
     * load all the relevant documents details
     * @param allRelevantDocs
     */
    private void updateAllRelevantDocsDetails(HashMap<String, Document> allRelevantDocs) {
        HashMap<String, int[]> allDocsDetails = readFile("documentsDetails.txt");
        for (Document doc : allRelevantDocs.values()) {
            int[] allData = allDocsDetails.get(doc.getId());
            doc.setMaxTf(allData[0]);
            doc.setNumOfTerms(allData[1]);
            doc.setNumOfWords(allData[2]);
        }
    }

    /**
     * get all the path of the relevant documents
     * @return
     */
    public HashMap<String, ArrayList<Integer>> getAllPath() {
        HashMap<String, ArrayList<Integer>> allPostingFilesAndLines = new HashMap<>();
        ArrayList<String> allQueryTerms = parsedQuery.getAllTerms();
        if (allQueryTerms.size() > 0) {
            HashMap<String, List<String[]>> pointers = parser.indexer.getPointers();
            for (String term : allQueryTerms) {
                if (pointers.containsKey(term)) {
                    for (String[] posting : pointers.get(term)) {
                        String file = posting[0];
                        String line = posting[1];
                        if (allPostingFilesAndLines.containsKey(file)) {
                            allPostingFilesAndLines.get(file).add(Integer.valueOf(line));
                        } else {
                            ArrayList<Integer> lines = new ArrayList<>();
                            lines.add(Integer.valueOf(line));
                            allPostingFilesAndLines.put(file, lines);
                        }
                    }
                }
            }
        }
        return allPostingFilesAndLines;
    }


    /**
     * Reads the relevant posting files
     * @param file
     * @param lines
     * @return
     */
    private ArrayList<String> read(String file, ArrayList<Integer> lines) {
        ArrayList<String> allLines = new ArrayList<>();
        try {
            File postingFile = new File(parser.getPostingPath() + "\\" + file);
            FileReader reader = new FileReader(postingFile);
            BufferedReader br = new BufferedReader(reader);
            String line;
            int lineNumber = 0; //todo check starting line number
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (lines.contains(lineNumber))
                    allLines.add(line);
            }
            br.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allLines;
    }

    /**
     * This method reads all the documents details from the posting file
     * @param file
     * @return
     */
    private HashMap<String, int[]> readFile(String file) {
        HashMap<String, int[]> allDocsDetails = new HashMap<>();
        try {
            File detailsFile = new File(parser.getPostingPath() + "\\" + file);
            FileReader reader = new FileReader(detailsFile);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(";");
                int[] details = new int[3];
                details[0] = Integer.valueOf(words[1]);
                details[1] = Integer.valueOf(words[2]);
                details[2] = Integer.valueOf(words[3]);
                allDocsDetails.put(words[0], details);
            }
            br.close();
            reader.close();
        } catch (IOException e) {
            System.out.println("error in readFile function");
        }
        return allDocsDetails;
    }


    /**
     * This method write all the results to a file
     * @param saveQueryPath a path to the location where we want to save the results
     * @throws IOException
     */
    private void writeResults(String saveQueryPath) throws IOException {
        File resultsFile = new File(saveQueryPath + "\\results.txt");
        FileWriter writer = new FileWriter(resultsFile);
        for (String query : results.keySet()) {
            for (String docName : results.get(query)) {
                String line = query + " 0 " + docName + " 1 " + 0 + " void";
                writer.write(line + "\n");
            }
        }
        writer.close();
    }

    /**
     * return all the queries
     */
    private void getAllQueries() {
        try {
            reader.readQueries();
            queries = reader.getQueries();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * reset the last results
     */
    public void resetResults() {
        this.results.clear();
    }

    /**
     * parse all the queries as documents
     */
    private void parseQueries() {
        if (query != null) {
            parser.startParseDocument("query", query, true);
        } else if (queries != null) {
            for (Map.Entry<String, String> query : queries.entrySet()) {
                parser.startParseDocument(query.getKey(), query.getValue(), true);
            }
        }
    }

    /**
     * return the results
     * @return
     */
    public TreeMap<String, ArrayList<String>> getResults() {
        return results;
    }
}
