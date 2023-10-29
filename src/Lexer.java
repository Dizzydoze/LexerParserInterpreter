import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to build an array of Tokens from an input file
 * @author YJ
 * @see Token
 * @see Parser
 */
public class Lexer {
    String buffer;
    int index;
    ArrayList<Token> tokenArrayList;
    public static final String INTTOKEN="INT";
    public static final String IDTOKEN="ID";
    public static final String ASSMTTOKEN="ASSMT";
    public static final String PLUSTOKEN="PLUS";
    public static final String UNKNOWNTOKEN = "UNKNOWN";
    public static final String EOFTOKEN="EOF";

    /**
     * call getInput to get the file data into our buffer
     * @param fileName the file we open
     */
    public Lexer(String fileName) {

        getInput(fileName);
        index = 0;
        tokenArrayList = new ArrayList<>();
    }

    /**
     * Reads given file into the data member buffer
     * @param fileName name of file to parse
    */
    private void getInput(String fileName)  {
        try {
            Path filePath = Paths.get(fileName);
            byte[] allBytes = Files.readAllBytes(filePath);
            buffer = new String (allBytes);
        } catch (IOException e) {
            System.out.println ("You did not enter a valid file name in the run arguments.");
            System.out.println ("Please enter a string to be parsed:");
            Scanner scanner = new Scanner(System.in);
            buffer=scanner.nextLine();
        }
    }

    /**
     * Return all the token in the file
     * @return ArrayList of Token
     */

    public ArrayList<Token> getAllTokens(){

//        ArrayList<Token> tokenArrayList = new ArrayList<>();

        for (int i = 0; i < buffer.length(); i++) {
            if (buffer.charAt(i) == ' ' || buffer.charAt(i) == '\n' || buffer.charAt(i) == '\t'){continue;}
            if (Character.isLetter(buffer.charAt(i))){
                String tmpStr = String.valueOf(buffer.charAt(i));
                while (i + 1 < buffer.length()){
                    if (Character.isLetterOrDigit(buffer.charAt(i + 1))){
                        tmpStr += String.valueOf(buffer.charAt(i + 1));
                        i ++;
                    }else{break;}
                }
                Token token = new Token(IDTOKEN, tmpStr);
                tokenArrayList.add(token);
            } else if (Character.isDigit(buffer.charAt(i))) {
                String tmpStr = String.valueOf(buffer.charAt(i));
                while (i + 1 < buffer.length()){
                    if (Character.isDigit(buffer.charAt(i + 1))){
                        tmpStr += String.valueOf(buffer.charAt(i + 1));
                        i ++;
                    }else{break;}
                }
                Token token = new Token(INTTOKEN, tmpStr);
                tokenArrayList.add(token);
            } else if (buffer.charAt(i) == '=') {
                Token token = new Token(ASSMTTOKEN, String.valueOf(buffer.charAt(i)));
                tokenArrayList.add(token);
            } else if (buffer.charAt(i) == '+') {
                Token token = new Token(PLUSTOKEN, String.valueOf(buffer.charAt(i)));
                tokenArrayList.add(token);
            } else {
                Token token = new Token(UNKNOWNTOKEN, String.valueOf(buffer.charAt(i)));
                tokenArrayList.add(token);
            }
        }
        Token endToken = new Token(EOFTOKEN, "-");
        tokenArrayList.add(endToken);

        return tokenArrayList;
    }

    /**
     * return the whole string of ID at current index
     */
    private String getIdentifier(){
        StringBuilder idSB = new StringBuilder();
        while (index< buffer.length()){
            if (Character.isLetterOrDigit(buffer.charAt(index))){
                idSB.append(buffer.charAt(index));
                index ++;
            }else{break;}
        }
        return idSB.toString();
    }

    /**
     * return the whole string of INT at current index
     */
    private String getInteger(){
        StringBuilder intSB = new StringBuilder();
        while (index < buffer.length()){
            if (Character.isDigit(buffer.charAt(index))){
                intSB.append(buffer.charAt(index));
                index ++;
            }else{break;}
        }
        return intSB.toString();
    }

    /**
     * returns a single Token
     */
    public Token getNextToken() throws Exception {
        if (index < buffer.length()){
            while (buffer.charAt(index) == ' ' || buffer.charAt(index) == '\n' || buffer.charAt(index) == '\t'){
                index++;
                if (index == buffer.length()){return new Token(EOFTOKEN, "-");}
            }
            if (buffer.charAt(index) == '='){
                index ++;
                return new Token(ASSMTTOKEN, "=");
            }
            else if (buffer.charAt(index) == '+'){
                index ++;
                return new Token(PLUSTOKEN, "+");
            }
            else if (Character.isLetter(buffer.charAt(index))){
                String idString = this.getIdentifier();
                return new Token(IDTOKEN, idString);
            }
            else if (Character.isDigit(buffer.charAt(index))){
                String intString = this.getInteger();
                return new Token(INTTOKEN, intString);
            }
            else {
                index ++;
                return new Token(UNKNOWNTOKEN, String.valueOf(buffer.charAt(index)));
            }
        }
        else if (index == buffer.length()){
            index ++;
            return new Token(EOFTOKEN, "-");
        }
        else{
            throw new Exception("Index out of range");
        }
    }


    /**
     * Before your run this starter code
     * Select Run | Edit Configurations from the main menu.
     * In Program arguments add the name of file you want to test (e.g., test.txt)
     * @param args args[0]
     */
    public static void main(String[] args) throws Exception {
        String fileName="";
        if (args.length==0) {
            System.out.println("You can test a different file by adding as an argument");
            System.out.println("See comment above main");
            System.out.println("For this run, test.txt used");
            fileName="test.txt";
        } else {
            fileName=args[0];
        }
        Lexer lexer = new Lexer(fileName);
        // just print out the text from the file
        System.out.println(lexer.buffer);
        // here is where you'll call getAllTokens
        ArrayList<Token> tokenArrayList = lexer.getAllTokens();
        for (Token token: tokenArrayList) {
            System.out.println(token.type + " " + token.value);
        }
        // try getNextToken func
        System.out.println("------------------------------------------------------------------");
        System.out.println("getNextToken Test Output: ");
        for (int i = 0; i < 3; i++) {
            System.out.println(lexer.getNextToken());
        }
    }
}
