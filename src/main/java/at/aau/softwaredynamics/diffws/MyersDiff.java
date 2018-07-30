package at.aau.softwaredynamics.diffws;

import at.aau.softwaredynamics.diffws.domain.DiffResult;
import at.aau.softwaredynamics.diffws.domain.Differ;
import at.aau.softwaredynamics.diffws.domain.Metrics;
import at.aau.softwaredynamics.diffws.domain.Result;
import difflib.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


public class MyersDiff extends Differ {

    private final String src;
    private final String dst;

    List<String> original;
    List<String> revised;

    public MyersDiff(String src, String dst) {
        super(src, dst);
        this.src = src;
        this.dst = dst;
        original = stringToLines(src);
        revised = stringToLines(dst);
    }

    public void loadFromFile(String srcFile, String dstFile) {
        original = fileToLines(srcFile);
        revised = fileToLines(dstFile);
    }

    public static List<String> fileToLines(String fileName) {
        ArrayList<String> liste = new ArrayList<>();

        Path path = Paths.get(fileName);
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(liste::add);
        }  catch (IOException ex) {
            System.out.println("Can not Read File: " + fileName);
        }
        return liste;
    }

    public static List<String> stringToLines(String source) {
        return Arrays.asList(source.split("(\r?\n)"));
    }



    @Override
    public DiffResult diff() {
        long startTime = System.nanoTime();
//        public static List<Result> results {



        // Compute diff. Get the Patch object. Patch is the container for computed deltas.
        Patch patch = DiffUtils.diff(original, revised);


        ArrayList<Result> diffList = new ArrayList<>();


        int i = 0;


        for (Object o : patch.getDeltas()) {
            Result result = new Result();
            Delta delta = (Delta) o;
            System.out.println(delta.toString());


//            int u = delta.getOriginal().toString().substring(2).indexOf("[") + 3; // start quo. ori.
//            int v = delta.getOriginal().toString().length() - 2; // end quo. ori.
//            result.setSrcEndLineOffset(delta.getOriginal().toString().substring(u,v).length());
//            result.setSrcEndLineOffset(original.get(original.size()-1).length());

//            int s = delta.getRevised().toString().substring(2).indexOf("[") + 3; //start quotation revised content
//            int t = delta.getRevised().toString().length() - 2; //end quotation revised content
//            result.setDstEndLineOffset(delta.getRevised().toString().substring(s,t).length());
//            result.setDstEndLineOffset(revised.get(revised.size()-1).length());


            if (delta.getType().equals(Delta.TYPE.CHANGE)) {
                result.setActionType("UPDATE");
            } else if (delta.getType().equals(Delta.TYPE.INSERT)) {
                result.setActionType("INSERT");
            } else if (delta.getType().equals(Delta.TYPE.DELETE)) {
                result.setActionType("DELETE");
            } else {
                result.setActionType("UNKNOWN");
            }


            result.setSrcId(i);
            result.setSrcStartLine(delta.getOriginal().getPosition()+1);
            result.setSrcEndLine(delta.getOriginal().size() - 1 + delta.getOriginal().getPosition() + 1);


            result.setDstId(i);
            result.setDstStartLine(delta.getRevised().getPosition() + 1);

            List originalLines = delta.getOriginal().getLines();
            if (!originalLines.isEmpty()) {
                result.setSrcEndLineOffset(originalLines.get(originalLines.size() - 1).toString().length());
            }

            List revisedLines = delta.getRevised().getLines();
            if (!revisedLines.isEmpty()) {
                result.setDstEndLineOffset((revisedLines.get(revisedLines.size() - 1).toString().length()));
            }


            if (result.getActionType().equals("DELETE")) {
                result.setDstEndLine(delta.getRevised().size() + delta.getRevised().getPosition() + 1);
            } else {
                result.setDstEndLine(delta.getRevised().size() - 1 + delta.getRevised().getPosition() + 1);
            }


            diffList.add(result);

            System.out.println("ActionType: " + result.getActionType());

            System.out.println("srcID: " + result.getSrcId());
            System.out.println("srcStartLine: " + result.getSrcStartLine());
            System.out.println("srcEndline: " + result.getSrcEndLine());
            System.out.println("srcEndlineOffset: " + result.getSrcEndLineOffset());

            System.out.println("dstStartLine: " + result.getDstStartLine());
            System.out.println("dstEndline: " + result.getDstEndLine());
            System.out.println("dstEndlineOffset: " + result.getDstEndLineOffset());
            System.out.println("dstID: " + result.getDstId());

            i++;

        }

        long endTime = System.nanoTime();

        long time = endTime - startTime;
        System.out.println(time / 1000000);
        Metrics metrics = new Metrics();
        metrics.setMatchingTime(time / 1000000);

        DiffResult diffResult = new DiffResult(metrics,diffList);

        return diffResult;
    }
}