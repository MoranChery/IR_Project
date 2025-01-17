package Searcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * This class reads all the queries from a file of queries
 */
public class ReadQueries {

    private HashMap<String, String> allQueries;
    private String path;
    private BufferedReader reader;

    public ReadQueries(String path) {
        this.path = path;
        allQueries = new HashMap<>();
    }

    public HashMap<String, String> getQueries() {
        return allQueries;
    }

    /**
     * reset the previous queries stored
     */
    public void resetReader() {
        allQueries.clear();
    }

    /**
     * reads all the queries from a file
     */
    //read all the queries
    public void readQueries(){
        File queries = new File(path);
        try {
            FileReader fileReader = new FileReader(queries);
            reader = new BufferedReader(fileReader);
            String line;
            String num = "";
            String text;
            while((line = reader.readLine()) != null) {
                String[] words = line.split(" ");
                // get query number
                if (words.length > 0 && words[0].equals("<num>")) {
                    num = words[2];
                }
                //get query text (title)
                if (words.length > 0 && words[0].equals("<title>")) {
                    text = line.substring(8) + " ";
                    allQueries.put(num, text);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("problem with reading the queries in ReadQueries class");
        }
    }
}