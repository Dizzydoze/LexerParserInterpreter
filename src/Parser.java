import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The Parser’s job is to determine if a given code file is syntactically correct and report either “valid program” or a particular error message.
 * For instance, the code:
 * x12=17+43
 * a = x12+35+1
 * is syntactically correct for the SIMPLE language so the parser should report, “valid program”. The code:
 * 17=x+3
 * is not valid and the parser should report, “Expecting Identifier on line 1”.
 * The Parser should find and report the following errors:
 * - Expecting identifier
 * - Expecting assignment operator
 * - Expecting identifier or integer
 * - Expecting identifier or add operator
 * - Identifier not defined
 * The Parser should not access the input stream directly, but should instead create a Lexer and call getAllTokens, then loop through those Tokens.
 * Use a *recursive descent parsing* scheme: define a *ParseX* method for each particular “part of speech”.
 * For example, parseAssignOp will be called when you are expecting a token of type “AssignOp”.
 */
public class Parser {

    ArrayList<Token> tokenArrayList;
    IdTable idTable;
    private int curIndex;
    private boolean error = false;
    private int errorLine = 0;
    Lexer lexer;
    ByteCodeInterpreter interpreter;
    HashMap<String, Integer> symbolTable;
    private int memoryIndex = 0;

    /**
     * - A constructor which creates a Lexer and places the results of “getAllTokens” into a data member
     */
    public Parser(String file){
        this.lexer = new Lexer(file);
        this.tokenArrayList = this.lexer.getAllTokens();
        this.idTable = new IdTable();
        this.curIndex = 0;
        this.symbolTable = new HashMap<>();     // memory location of ID
        this.interpreter = new ByteCodeInterpreter(10);
    }

    /**
     * Drive the process and parses an entire program.
     * Call parseAssignment within a loop.
     */
    public void parseProgram() {
        while (this.curIndex < this.tokenArrayList.size()){
            this.errorLine += 1;    // each loop is a new line
            if (this.error){
                System.out.println("Invalid Program");
                return;
            }
            this.parseAssignment();
        }
        System.out.println("Valid Program");
        this.interpreter.run();
    }

    /**
     * Parse a single assignment statement.
     * Call parseId, parseAssignmentOp, and parseExpression.
     */
    public void parseAssignment() {
         this.parseId();
         this.parseAssignOp();
         this.parseExpression();
         // store the final value to current ID memory address
         this.interpreter.generate(ByteCodeInterpreter.STORE, this.memoryIndex);
         // each assignment for one ID memory address
         this.memoryIndex ++;
    }

    /**
     * Parse a single identifier
     */
    public void parseId(){
        // Must be on LEFT side, just add to table
        Token curToken = this.nextToken();
        if (!Objects.equals(curToken.type, "ID")){
            System.out.println("Error: Expecting identifier, Line " + this.errorLine);
            this.error = true;
        }
        else{
            // add token value to idTable
            this.idTable.add(curToken.value);
            // add token value to symbolTable
            this.symbolTable.put(curToken.value, this.memoryIndex);
        }
    }

    /**
     * Parses a single assignment operator
     */
    public void parseAssignOp(){
        if (this.error){return;}
        Token nextToken = this.peek();  // call peek first, index not added yet
        Token curToken = this.nextToken();
        if (!Objects.equals(curToken.type, "ASSMT")){
            System.out.println("Error: Expecting assignment operator, Line " + this.errorLine);
            this.error = true;
            return;
        }
        if (!Objects.equals(nextToken.type, "ID") && !Objects.equals(nextToken.type, "INT")){
            System.out.println("Error: Expecting identifier or integer, Line " + this.errorLine);
            this.error = true;
        }
    }

    /**
     * Parse an expression. i.e., the right-hand-side of an assignment.
     * Note that expressions can include an unlimited number of “+” signs, e.g., “Y+3+4”
     */
    public void parseExpression(){
        if (this.error){return;}
        // ID, ASSMT, INT, PLUS, EOF
        Token nextToken = this.peek();
        Token curToken = this.nextToken();
        // if nextToken is ID, do nothing, normally finish this func and let the loop do the rest.
        if (Objects.equals(curToken.type, "ID")){
            // Check IdTable
            if (this.idTable.getAddress(curToken.value) == -1){
                System.out.println("Error: Identifier not defined, Line " + this.errorLine);
                this.error = true;
                return;
            }
            // Interpret into bytecode, add value at ID address's to accumulator
            this.interpreter.generate(ByteCodeInterpreter.LOAD, symbolTable.get(curToken.value));

            if (Objects.equals(nextToken.type, "PLUS")){
                this.parseExpression();     // keep parsing
            }
            else if (Objects.equals(nextToken.type, "INT")){
                System.out.println("Error: Expecting identifier or add operator, Line " + this.errorLine);
                this.error = true;
            }
            else if (Objects.equals(nextToken.type, "EOF")){
                this.curIndex ++;  // force into next loop
            }
        }
        else if (Objects.equals(curToken.type, "INT")){
            // Interpret into bytecode, add value of operand INT to accumulator
            this.interpreter.generate(ByteCodeInterpreter.LOADI, Integer.parseInt(curToken.value));

            if (Objects.equals(nextToken.type, "PLUS")){
                this.parseExpression();     // keep parsing
            }
            else if (Objects.equals(nextToken.type, "INT")){
                System.out.println("Error: Expecting identifier or add operator, Line " + this.errorLine);
                this.error = true;

            }
            else if (Objects.equals(nextToken.type, "EOF")){
                this.curIndex ++;  // force into next loop
            }
        }
        else if (Objects.equals(curToken.type, "PLUS")){
            if (!Objects.equals(nextToken.type, "ID") && !Objects.equals(nextToken.type, "INT")){
                System.out.println("Error: Expecting identifier or integer, Line " + this.errorLine);
                this.error = true;
            }
            else{
                // either ID or INT, keep parsing
                this.parseExpression();
            }
        }
    }


    /**
     * Get the next token in the list and increments the index
     */
    public Token nextToken(){
        return this.tokenArrayList.get(this.curIndex++);
    }

    private Token peek(){
        if (this.curIndex + 1 < this.tokenArrayList.size()){
            return this.tokenArrayList.get(this.curIndex + 1);
        }
        else{
            return new Token("EOF", "-");
        }
    }


    /**
     * Print out the token list and id table
     */
    public String toString(){
        return "Token List: " + this.tokenArrayList + "\n"
                + "Symbol Table: " + this.symbolTable  + "\n"
                + "ByteCode: " + this.interpreter.getByteCode() + "\n"
                + "Memory: " + this.interpreter.getMemory();
    }
}


