
import at.aau.softwaredynamics.diffws.MyersDiff;
import at.aau.softwaredynamics.diffws.domain.Result;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;


public class SimpleTests {
    MyersDiff myersDiff;

    @Before
    public void prepare() {

        myersDiff = new MyersDiff("","");
    }
    @Test
    public void simpleUpdateTest() { // Files: testoriginal.txt; testrevised.txt
        myersDiff.loadFromFile("TestFiles/testoriginal.txt", "TestFiles/testrevised.txt");
        List<Result> myList = myersDiff.diff().getResults();

        Assert.assertEquals("UPDATE", myList.get(0).getActionType());
        Assert.assertEquals(Integer.valueOf(0), myList.get(0).getSrcId());
        Assert.assertEquals(Integer.valueOf(1), myList.get(0).getSrcStartLine());
        Assert.assertEquals(Integer.valueOf(3), myList.get(0).getSrcEndLine());
        Assert.assertEquals(Integer.valueOf(4), myList.get(0).getSrcEndLineOffset());
        Assert.assertEquals(Integer.valueOf(0), myList.get(0).getDstId());
        Assert.assertEquals(Integer.valueOf(1), myList.get(0).getDstStartLine());
        Assert.assertEquals(Integer.valueOf(3), myList.get(0).getDstEndLine());
        Assert.assertEquals(Integer.valueOf(5), myList.get(0).getDstEndLineOffset());

    }

    @Test
    public void newlineLFTest() {
        myersDiff.loadFromFile("TestFiles/newlineLF.txt", "TestFiles/newlineLFrevised.txt");
        List<Result> myList = myersDiff.diff().getResults();

        Assert.assertEquals("UPDATE", myList.get(0).getActionType());
        Assert.assertEquals(Integer.valueOf(0), myList.get(0).getSrcId());
        Assert.assertEquals(Integer.valueOf(1), myList.get(0).getSrcStartLine());
        Assert.assertEquals(Integer.valueOf(1), myList.get(0).getSrcEndLine());
        Assert.assertEquals(Integer.valueOf(31), myList.get(0).getSrcEndLineOffset());
        Assert.assertEquals(Integer.valueOf(0), myList.get(0).getDstId());
        Assert.assertEquals(Integer.valueOf(1), myList.get(0).getDstStartLine());
        Assert.assertEquals(Integer.valueOf(1), myList.get(0).getDstEndLine());
        Assert.assertEquals(Integer.valueOf(33), myList.get(0).getDstEndLineOffset());
    }

    @Test
    public void newlineCRLFTest() {
        myersDiff.loadFromFile("TestFiles/newlineCRLF.txt", "TestFiles/newlineCRLFrevised.txt");
        List<Result> myList = myersDiff.diff().getResults();

        Assert.assertEquals(0, myList.size());
    }
    @Test
    public void newlineAllTypesTest() {
        myersDiff.loadFromFile("TestFiles/newlineAllTypes.txt", "TestFiles/newlineAllTypesrevised.txt");
        List<Result> myList = myersDiff.diff().getResults();

        Assert.assertEquals(0, myList.size());
    }
    @Test
    public void newlineCRTest() {
        myersDiff.loadFromFile("TestFiles/newlineCR.txt", "TestFiles/newlineCRrevised.txt");
        List<Result> myList = myersDiff.diff().getResults();

        Assert.assertEquals(0, myList.size());
    }
}
