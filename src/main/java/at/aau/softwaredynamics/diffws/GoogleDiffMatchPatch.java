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

        String usedSrcText = "";
        String usedDstText = "";

        for (int i = 0; i < fullDiffSize; i++) {

            Result diffInfo;
            diffInfo = new Result();


            switch (fullDiff.get(i).operation) {
                case EQUAL:
                    srcZeile += StringUtils.countMatches(fullDiff.get(i).text, "\n");
                    dstZeile += StringUtils.countMatches(fullDiff.get(i).text, "\n");

                    usedSrcText += fullDiff.get(i).text;
                    usedDstText += fullDiff.get(i).text;
                    break;

                case DELETE:
                    boolean zeileMehr;
                    int lastNewlineDel;

                    if (fullDiff.get(i).text.indexOf("\n") == 0) {
                        dstZeile++;
                        zeileMehr = true;
                        if (usedDstText.length() == usedSrcText.lastIndexOf("\n") + 1) {
                            lastNewlineDel = usedSrcText.lastIndexOf("\n");
                        } else {
                            lastNewlineDel = usedSrcText.length() - 1;
                        }
                    } else {
                        zeileMehr = false;

                        lastNewlineDel = usedSrcText.lastIndexOf("\n");
                    }

                    diffInfo.setSrcStartLine(srcZeile);

                    if (lastNewlineDel == -1) {
                        lastNewlineDel = 0;
                        diffInfo.setSrcStartLineOffset(usedSrcText.length() - lastNewlineDel);
                    } else {
                        diffInfo.setSrcStartLineOffset(usedSrcText.length() - lastNewlineDel - 1);
                    }

//                    srcZeile += StringUtils.countMatches(fullDiff.get(i).text, "\n");

                    diffInfo.setSrcEndLine(srcZeile);

                    diffInfo.setSrcEndLineOffset(usedSrcText.substring(lastNewlineDel + 1).length() + ((fullDiff.get(i).text.length() - StringUtils.countMatches(
                                    fullDiff.get(i).text, "\n")
                            )
                    ) +1 );



                    diffInfo.setActionType("DELETE");

                    diffInfo.setSrcId(id);

                    id++;
                    usedSrcText += fullDiff.get(i).text;

                    diffList.add(diffInfo);
                    break;

                case INSERT:
                    int lastNewlineIns;

                    if (fullDiff.get(i).text.indexOf("\n") == 0) {
                        dstZeile++;
                        zeileMehr = true;

                        if (usedDstText.length() == usedDstText.lastIndexOf("\n") + 1) {
                            lastNewlineIns = usedDstText.lastIndexOf("\n");
                        } else {
                            lastNewlineIns = usedDstText.length() - 1;
                        }
                    } else {
                        zeileMehr = false;

                        lastNewlineIns = usedDstText.lastIndexOf("\n");
                    }

                    diffInfo.setDstStartLine(dstZeile);


                    if (lastNewlineIns == -1) {
                        lastNewlineIns = 0;
                        diffInfo.setDstStartLineOffset(usedDstText.length() - lastNewlineIns);
                    } else {
                        diffInfo.setDstStartLineOffset(usedDstText.length() - lastNewlineIns - 1);
                    }
//                    diffInfo.setDstStartLineOffset(usedDstText.length() - lastNewlineIns - 1);


//                    if (zeileMehr = false) {
//                        dstZeile += StringUtils.countMatches(fullDiff.get(i).text, "\n");
//                    }


                    diffInfo.setDstEndLine(dstZeile);

                    diffInfo.setDstEndLineOffset(diffInfo.getDstStartLineOffset() + (fullDiff.get(i).text.length() - StringUtils.countMatches(fullDiff.get(i).text, "\n")));

                    diffInfo.setActionType("INSERT");

                    diffInfo.setSrcId(id);

                    id++;
                    usedDstText += fullDiff.get(i).text;

                    diffList.add(diffInfo);
                    break;

                default:
                    System.out.println("Failed to setActionType: DmpMain, Line ca. 103-113");
            }

        }

        System.out.println("GoogleDiffMatchPatch wird verwendet");
        return diffList;
    }


}
