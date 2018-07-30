import at.aau.softwaredynamics.diffws.MyersDiff;
import at.aau.softwaredynamics.diffws.domain.Result;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;


public class ComplexTest {
    MyersDiff myersDiff;

    @Before
    public void prepare() {

        myersDiff = new MyersDiff("","");
    }

    @Test
    public void simpleUpdateTest() { // Files: testoriginal.txt; testrevised.txt
//        List<Result> myList = diffFiles("complextestfiles/Test.java", "complextestfiles/Test_new.java"

        myersDiff.loadFromFile("complextestfiles/Test.java", "complextestfiles/Test_new.java");
        List<Result> myList = myersDiff.diff().getResults();

        Result updateInfo = myList.get(0);
        Assert.assertEquals("UPDATE", updateInfo.getActionType());

        Assert.assertEquals(Integer.valueOf(5), updateInfo.getSrcStartLine());
        Assert.assertEquals(Integer.valueOf(0), updateInfo.getSrcStartLineOffset());
        Assert.assertEquals(Integer.valueOf(9), updateInfo.getSrcEndLine());
        Assert.assertEquals(Integer.valueOf(5), updateInfo.getSrcEndLineOffset());

        Assert.assertEquals(Integer.valueOf(0), updateInfo.getDstId());
        Assert.assertEquals(Integer.valueOf(5), updateInfo.getDstStartLine());
        Assert.assertEquals(Integer.valueOf(0), updateInfo.getDstStartLineOffset());
        Assert.assertEquals(Integer.valueOf(5), updateInfo.getDstEndLine());
        Assert.assertEquals(Integer.valueOf(36), updateInfo.getDstEndLineOffset());

    }

//    @Test
//    public void insertTest() {
//        myersDiff.loadFromFile("complextestfiles/PromotingEmployees.txt", "complextestfiles/PromotingEmployees1.txt");
//        List<Result> myList = myersDiff.diff().getResults();
//
//        Result updateInfo = myList.get(4);
//
//        Assert.assertEquals("INSERT", updateInfo.getActionType());
//
////        Assert.assertEquals(Integer.valueOf(4), updateInfo.getSrcId());
//
//        Assert.assertEquals(Integer.valueOf(226), updateInfo.getSrcStartLine());
//        Assert.assertEquals(Integer.valueOf(0), updateInfo.getSrcStartLineOffset());
//        Assert.assertEquals(Integer.valueOf(226), updateInfo.getSrcEndLine());
//        Assert.assertEquals(Integer.valueOf(0), updateInfo.getSrcEndLineOffset());
//
//        Assert.assertEquals(Integer.valueOf(4), updateInfo.getDstId());
//        Assert.assertEquals(Integer.valueOf(219), updateInfo.getDstStartLine());
//        Assert.assertEquals(Integer.valueOf(219), updateInfo.getDstEndLine());
//        Assert.assertEquals(Integer.valueOf(1), updateInfo.getDstEndLineOffset());
//
//    }
}
