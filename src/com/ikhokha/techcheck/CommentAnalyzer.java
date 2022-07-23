package com.ikhokha.techcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentAnalyzer {

    private File file;
    private List<Matric> matrics;

    public CommentAnalyzer(File file, List<Matric> matrics) {
        this.file = file;
        this.matrics = matrics;
    }

    public List<Matric> getMatrics() {
        return matrics;
    }
    
    

    public Map<String, Integer> analyze()  {
        Map<String, Integer> resultsMap = new HashMap<>();
        try ( BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                for (Matric matric : getMatrics()) {
                    if (matric.trueAnnalysis(line)) {
                        incOccurrence(resultsMap, matric.getName());
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.getAbsolutePath());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO Error processing file: " + file.getAbsolutePath());
            e.printStackTrace();
        }

        return resultsMap;

    }

    /**
     * This method increments a counter by 1 for a match type on the countMap.
     * Uninitialized keys will be set to 1
     *
     * @param countMap the map that keeps track of counts
     * @param key the key for the value to increment
     */
    private void incOccurrence(Map<String, Integer> countMap, String key) {
        countMap.putIfAbsent(key, 0);
        countMap.put(key, countMap.get(key) + 1);
    }
    
    
    

}
