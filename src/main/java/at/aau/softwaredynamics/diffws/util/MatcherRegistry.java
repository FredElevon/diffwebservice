package at.aau.softwaredynamics.diffws.util;

import at.aau.softwaredynamics.diffws.GoogleDiffMatchPatch;
import at.aau.softwaredynamics.diffws.MyersDiff;
import at.aau.softwaredynamics.diffws.domain.Differ;

import java.util.HashMap;

public class MatcherRegistry {
    HashMap<Integer, Class<? extends Differ>> matcherMap;

    public MatcherRegistry() {
        this.matcherMap = new HashMap<>();

        // TODO (Thomas): init from config
        // TODO Insert differs here
        matcherMap.put(1, MyersDiff.class);
        matcherMap.put(2, GoogleDiffMatchPatch.class);
//        matcherMap.put(2, OptimizedVersions.MtDiff.class);
//        matcherMap.put(3, JavaMatchers.IterativeJavaMatcher.class);
//        matcherMap.put(4, JavaMatchers.IterativeJavaMatcher_V1.class);
//        matcherMap.put(5, JavaMatchers.IterativeJavaMatcher_V2.class);
    }

    public HashMap<Integer, Class<? extends Differ>> getMatcherMap() {
        return matcherMap;
    }

    public Class<? extends Differ> getMatcherTypeFor(int id) {
        return matcherMap.getOrDefault(id, null);
    }
}
