/**
 * Create a for loop to test Parser result
 * Find expected result here:  https://docs.google.com/document/d/1Nfx15sYNPsPS3PS-qK8fjQ8VvEozeijMfCECXlYZx2o/edit#heading=h.d51f4t5t6g61
 */
public class ParserTest {

    public static void main(String[] args) {
        String[] fileArray={"test.txt","testExpectingId2.txt","testExpectingAssignOp.txt","testExpectingIdOrInt2.txt","testMultiplePlus.txt","testWhiteSpace.txt", "testWhiteSpace2.txt", "testOutOfBounds.txt"};
//        String[] fileArray={"testOutOfBounds.txt"};

        for (String file : fileArray) {
            System.out.println(file);
            Parser parser = new Parser(file);
            System.out.println("Parser Output: ");
            parser.parseProgram();
            System.out.println(parser);
            System.out.println("-----------------------------------------------------------------------------------------");
        }
    }
}
