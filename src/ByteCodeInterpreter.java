import sun.jvm.hotspot.interpreter.Bytecode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * For this project, you’ll create a class “ByteCodeInterpreter”.
 * You’ll also modify the Parser class extensively for this project
 * (it will call the “generate” method of ByteCodeInterpreter).
 * Your program, including the classes from the Lexer-Parser,
 * and each class should provide appropriate toString and equals methods.
 */

public class ByteCodeInterpreter {

    ArrayList<Integer> bytecode;
    private ArrayList<Integer> memory;
    public static final int LOAD = 0;
    public static final int LOADI = 1;
    public static final int STORE = 2;
    private int accumulator;    // temporary storage, CPU register.
    private int memorySize;

    public ByteCodeInterpreter(int size){
        this.memorySize = size;
        this.bytecode = new ArrayList<>();
        this.accumulator = 0;
        // initialize memory slot with default values
        this.memory = new ArrayList<>(this.memorySize);
        for (int i = 0; i < this.memorySize; i++) {
            this.memory.add(0);
        }
    }

    /**
     * takes in a command and an operand as parameters and adds the command to the bytecode being generated.
     */
    public void generate(int command, int operand){
        this.bytecode.add(command);
        this.bytecode.add(operand);
    }

    /**
     * runs the code in bytecode, modifying memory
     */
    public void run(){
        for (int i = 0; i < this.bytecode.size(); i += 2) {
            if (this.bytecode.get(i) == LOAD){
                this.Load(this.bytecode.get(i + 1));
            }
            else if (this.bytecode.get(i) == LOADI) {
                this.Loadi(this.bytecode.get(i + 1));
            }
            else if (this.bytecode.get(i) == STORE) {
                int operandAddress = this.bytecode.get(i+1);
                // Check size limitation, stop adding to bytecode.
                if (operandAddress >= this.memorySize){
                    System.out.println("Error: Address out of range");
                    return;
                }
                this.Store(operandAddress);
            }
        }
    }

    /**
     * get value from memory address, add it to Accumulator.
     */
    private void Load(int operandAddress){
        this.accumulator += this.memory.get(operandAddress);
    }

    /**
     * add value of operand to Accumulator
     */
    private void Loadi(int valueOfOperand){
        this.accumulator += valueOfOperand;
    }

    /**
     * Store value in Accumulator to address of operand
     * Set Accumulator to 0
     */
    private void Store(int operandAddress){
        this.memory.set(operandAddress, this.accumulator);
        this.accumulator = 0;
    }

    /**
     * - get methods providing public access to appropriate data members.
     */
    public ArrayList<Integer> getByteCode(){
        return this.bytecode;
    }

    public ArrayList<Integer> getMemory(){
        return this.memory;
    }

    public int getMemorySize(){
        return this.memorySize;
    }

    /**
     * each class should provide appropriate toString and equals methods.
     */
    @Override
    public String toString() {
        return "ByteCodeInterpreter{" +
                "bytecode=" + bytecode +
                ", memory=" + memory +
                ", accumulator=" + accumulator +
                ", memorySize=" + memorySize +
                '}';
    }
}
