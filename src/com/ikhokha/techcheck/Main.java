package com.ikhokha.techcheck;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Map<String, Integer> totalResults = new HashMap<>();

        File docPath = new File("docs");
        File[] commentFiles = docPath.listFiles((d, n) -> n.endsWith(".txt"));

        List<Matric> objects = new ArrayList<>();
        ExecutorService exe = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 2);

        ShorterThan15 obj1 = new ShorterThan15(MatricTypeEnum.SHORTER_THAN_15.name());
        MoverMentions obj2 = new MoverMentions(MatricTypeEnum.MOVER_MENTIONS.name());
        ShakerMentions obj3 = new ShakerMentions(MatricTypeEnum.SHAKER_MENTIONS.name());
        Question obj4 = new Question(MatricTypeEnum.QUESTION.name());
        Spam obj5 = new Spam(MatricTypeEnum.SPAM.name());
        objects.add(obj1);
        objects.add(obj2);
        objects.add(obj3);
        objects.add(obj4);
        objects.add(obj5);

        for (File commentFile : commentFiles) {
            Callable worker = new ReadFile(commentFile, totalResults, objects);
            Future<Map<String, Integer>> message = exe.submit(worker);
            totalResults = message.get();
        }

        exe.shutdown();
        //System.out.println("totalResults main class " + totalResults);
        System.out.println("RESULTS\n=======");
        totalResults.forEach((k, v) -> System.out.println(k + " : " + v));
    }

    /**
     * This method adds the result counts from a source map to the target map
     *
     * @param source the source map
     * @param target the target map
     */
    private static Map<String, Integer> addReportResults(Map<String, Integer> source, Map<String, Integer> target) {
        Date dt = new Date();
        //SimpleDateFormat sdf = new SimpleDateFormat("hh: mm: ss");
        for (Map.Entry<String, Integer> entry : source.entrySet()) {
            if (target.containsKey(entry.getKey())) {
                target.put(entry.getKey(), entry.getValue() + target.get(entry.getKey()));
            } else {
                target.put(entry.getKey(), entry.getValue());
            }
        }
        //System.out.println(Thread.currentThread().getName() + " (End)" + " at time: " + sdf.format(dt));//prints thread name 
        return target;
    }

    static class ReadFile implements Callable<Map<String, Integer>> {

        Map<String, Integer> totalResults;
        File file;
        List<Matric> objects;

        public ReadFile(File file, Map<String, Integer> totalResults, List<Matric> objects) {
            this.totalResults = totalResults;
            this.file = file;
            this.objects = objects;
        }

        public List<Matric> getObjects() {
            return objects;
        }

        public Map<String, Integer> getTotalResults() {
            return totalResults;
        }

        public File getFile() {
            return file;
        }

        @Override
        public Map<String, Integer> call() {
            Date dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh: mm: ss");

            System.out.println(Thread.currentThread().getName() + " (Start) message = " + getFile().getName() + " at time: " + sdf.format(dt));
            CommentAnalyzer commentAnalyzer = new CommentAnalyzer(getFile(), objects);
            Map<String, Integer> fileResults = commentAnalyzer.analyze();
            return addReportResults(fileResults, getTotalResults());

            // System.out.println("Results ++ " + getTotalResults().toString());
        }

    }

}
