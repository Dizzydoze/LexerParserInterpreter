import java.util.HashMap;
import java.util.Map;

/**
 * Define the class IdTable with the following data members and methods:
 * - a hashmap data member with String key and Integer value.
 * The keys are the identifiers and the values represent the address in memory in which the identifier will be stored (if an interpreter were built)
 * You can also just think of it as the order the ids appear- the first id will have address 0, the second id will have address 1, and so on.
 * The IdTable is a class that tracks the identifiers within a program.
 * When an identifier appears on the left-hand-side of an assignment statement,
 * add it to the IdTable. When an identifier appears on the right-hand-side of an assignment statement,
 * check the IdTable to see if the identifier has been defined (it is an error if not).
 */
public class IdTable {

    public Map<String, Integer> table = new HashMap<>();
    public int IdAddress = 0;

    /**
     * Add an entry to the map, you need only send the id.
     */
    public void add(String IdValue){
        this.table.put(IdValue, this.IdAddress);    // {IdValue: Address}
        this.IdAddress += 1;                        // Auto increment
    }

    /**
     * - *getAddress- this method returns the address associated with an id, or -1 if not found.*
     */
    public int getAddress(String IdValue){
        try {
            return this.table.get(IdValue);
        }catch (NullPointerException e){
            return -1;
        }
    }

    public String toString(){
        return "";
    }

}
