
import at.aau.softwaredynamics.diffws.GoogleDiffMatchPatch;
import at.aau.softwaredynamics.diffws.domain.Result;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class DmpTests {
    GoogleDiffMatchPatch googleDiffMatchPatch;

    @Before
    public void prepare() {
        googleDiffMatchPatch = new GoogleDiffMatchPatch("", "");
    }

    @Test
    public void singleCharacterDelTest() {
        googleDiffMatchPatch.loadFromFile("dmptestfiles/ori.txt", "dmptestfiles/rev.txt");

        List<Result> myList = googleDiffMatchPatch.diff().getResults();

        Result updateInfo = myList.get(0);

        Assert.assertEquals(Integer.valueOf(2), updateInfo.getSrcStartLine());
        Assert.assertEquals(Integer.valueOf(3), updateInfo.getSrcStartLineOffset());
        Assert.assertEquals(Integer.valueOf(3), updateInfo.getSrcEndLine());
        Assert.assertEquals(Integer.valueOf(4), updateInfo.getSrcEndLineOffset());
    }

    @Test
    public void delInsInOneLineTest() {
        googleDiffMatchPatch.loadFromFile("dmptestfiles/ori.txt", "dmptestfiles/rev.txt");

        List<Result> myList = googleDiffMatchPatch.diff().getResults();

        Result updateInfo = myList.get(2);

        Assert.assertEquals(Integer.valueOf(5), updateInfo.getSrcStartLine());
        Assert.assertEquals(Integer.valueOf(4), updateInfo.getSrcStartLineOffset());
        Assert.assertEquals(Integer.valueOf(5), updateInfo.getSrcEndLine());
        Assert.assertEquals(Integer.valueOf(5), updateInfo.getSrcEndLineOffset());
    }
}
