package at.aau.softwaredynamics.diffws;

import at.aau.softwaredynamics.diffws.domain.DiffResult;
import at.aau.softwaredynamics.diffws.domain.Differ;
import at.aau.softwaredynamics.diffws.domain.Metrics;
import at.aau.softwaredynamics.diffws.domain.Result;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class GoogleDiffMatchPatch extends Differ {
    private final String src;
    private final String dst;

    List<String> original;
    List<String> revised;

    public GoogleDiffMatchPatch(String src, String dst) {
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
        } catch (IOException ex) {
            System.out.println("Can not Read File: " + fileName);
        }
        return liste;
    }

    public static List<String> stringToLines(String source) {
        return Arrays.asList(source.split("(\r?\n)"));
    }


    @Override
    public DiffResult diff() {
        //TODO implement!

       return new DiffResult(new Metrics(), diffFiles(1));
    }


    static ArrayList<Integer> newlinePos;

    public ArrayList<Integer> getNewlinePos() {
        return newlinePos;
    }



    public List<Result> diffFiles(int cleanup) {


        diff_match_patch dmp = new diff_match_patch();

        ArrayList<Result> diffList = new ArrayList<>();

        LinkedList<diff_match_patch.Diff> fullDiff = dmp.diff_main(String.join("\n", original), String.join("\n", revised));

        switch (cleanup) {
            case 1:
                dmp.diff_cleanupSemantic(fullDiff);
                break;
            case 2:
                dmp.diff_cleanupEfficiency(fullDiff);
                break;
            default:
                System.out.println("No cleanup. Raw Data!");
                break;
        }


        int fullDiffSize = fullDiff.size();
        newlinePos = new ArrayList<>();

        Integer srcZeile = 1;
        Integer srcOffset = 0;

        Integer dstZeile = 1;
        Integer dstOffset = 0;

        int id = 0;

        for (int i = 0; i < fullDiffSize; i++) {

            Result diffInfo;
            diffInfo = new Result();



            switch (fullDiff.get(i).operation) {
                case EQUAL:
                    srcZeile += StringUtils.countMatches(fullDiff.get(i).text, "\n");
                    dstZeile += StringUtils.countMatches(fullDiff.get(i).text, "\n");
                    break;
                case DELETE:

                    diffInfo.setSrcStartLine(srcZeile);
                    diffInfo.setSrcStartLineOffset(srcOffset);
                    diffInfo.setSrcEndLine(srcZeile);

                    srcZeile += StringUtils.countMatches(fullDiff.get(i).text, "\n");


                    diffInfo.setSrcEndLineOffset(fullDiff.get(i).text.length() - StringUtils.countMatches(fullDiff.get(i).text, "\n"));

                    // Anzahl der Zeichen hinter letztem \n = endlineOffset


                    diffInfo.setActionType("DELETE");

                    diffList.add(diffInfo);
                    break;
                case INSERT:

                    System.out.println("Insert wird gerade verwendet!");

                    diffInfo.setDstStartLine(srcZeile);
                    diffInfo.setDstStartLineOffset(srcOffset);

                    dstZeile += StringUtils.countMatches(fullDiff.get(i).text, "\n");

                    diffInfo.setDstEndLine(dstZeile);
                    diffInfo.setDstEndLineOffset(fullDiff.get(i).text.length() - StringUtils.countMatches(fullDiff.get(i).text, "\n"));

                    diffInfo.setActionType("INSERT");

                    diffList.add(diffInfo);
                    break;
                default:
                    System.out.println("Failed to setActionType: DmpMain, Line ca. 103-113");
            }


            diffInfo.setSrcId(i);


        }


        return diffList;
    }


}
