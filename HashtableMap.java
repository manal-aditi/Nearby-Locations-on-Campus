// == CS400 Fall 2024 File Header Information ==
// Name: Manal Aditi
// Email: aditi@wisc.edu
// Group: P2.2713
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {
  protected class Pair {
    public KeyType key;
    public ValueType value;

    public Pair(KeyType key, ValueType value) {
      this.key = key;
      this.value = value;
    }
  }
  protected LinkedList<Pair>[] table;
  private int size;

  public HashtableMap() {
    //first constructor does not contain arguments
    this(64);
  }

  @SuppressWarnings("unchecked")
  public HashtableMap(int capacity) {
    //checks to ensure the capacity held is correct
    if (capacity <= 0) {
      throw new IllegalArgumentException("Capacity must be greater than 0.");
    }
    //sets the table object to a size of zero
    table = (LinkedList<Pair>[]) new LinkedList[capacity];
    size = 0;
  }

  /**
   * Adds a new key,value pair/mapping to this collection.
   * @param key the key of the key,value pair
   * @param value the value that key maps to
   * @throws IllegalArgumentException if key already maps to a value
   * @throws NullPointerException if key is null
   */
  @SuppressWarnings("unchecked")
  @Override
  public void put(KeyType key, ValueType value) throws IllegalArgumentException {
    //checks to make sure the key isn't null or repeated
    if (key == null) {
      throw new NullPointerException("The key value can't be null.");
    }
    if (containsKey(key)) {
      throw new IllegalArgumentException("The key already exists in the program.");
    }
    //finds the index of the value
    int indexVal = Math.abs(key.hashCode()) % table.length;
    //if it's null, then it's set to a new LinkedList
    if (table[indexVal] == null) {
      table[indexVal] = new LinkedList<>();
    }
    //at this index, they add a new pair with a key and the value at the key
    table[indexVal].add(new Pair(key, value));
    //increases size with new code addition
    size++;
    //checks to see if the load factor becomes greater than or equal to 80%
    if ((double) size / table.length >= 0.8) {
      //if so, the code is doubled in its capacity and rehashed
      LinkedList<Pair>[] prevTable = table;
      //doubles the capacity
      table = (LinkedList<Pair>[]) new LinkedList[prevTable.length * 2];
      //sets size to 0 before rehashing
      size = 0;
      //enhanced for loop rehashes the values
      for (LinkedList<Pair> values : prevTable) {
        if (values != null) {
          for (Pair pair : values) {
            put(pair.key, pair.value);
          }
        }
      }
    }
  }

  /**
   * Checks whether a key maps to a value in this collection.
   * @param key the key to check
   * @return true if the key maps to a value, and false is the
   *         key doesn't map to a value
   */
  @Override
  public boolean containsKey(KeyType key) {
    //checks to see if the key is null, and if so returns false
    if (key == null) {
      return false;
    }
    //finds the index val of the key
    int index = Math.abs(key.hashCode()) % table.length;
    //checks to make sure that that given index isn't null
    if (table[index] != null) {
      //enhanced for loop goes through the table to find if the key maps to a value here
      for (Pair pair : table[index]) {
        if (pair.key.equals(key)) {
          //if found, then it returns true
          return true;
        }
      }
    }
    //otherwise, return false
    return false;
  }

  /**
   * Retrieves the specific value that a key maps to.
   * @param key the key to look up
   * @return the value that key maps to
   * @throws NoSuchElementException when key is not stored in this
   *         collection
   */
  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    //checks to see if the key is null, and if so returns exception
    if (key == null) {
      throw new NoSuchElementException("The key value can't be null.");
    }
    //finds the index of the key val
    int indexVal = Math.abs(key.hashCode()) % table.length;
    //as long as the table has the key val is not null it will go to the for loop
    if (table[indexVal] != null) {
      //enhanced for loop will check this index to see if the val exists in the code
      for (Pair pair : table[indexVal]) {
        if (pair.key.equals(key)) {
          //if so, returns the specific val that the key maps to
          return pair.value;
        }
      }
    }
    //if the key isn't present in the collection, then an exception is thrown
    throw new NoSuchElementException("The key can't be found.");
  }

  /**
   * Remove the mapping for a key from this collection.
   * @param key the key whose mapping to remove
   * @return the value that the removed key mapped to
   * @throws NoSuchElementException when key is not stored in this
   *         collection
   */
  @Override
  public ValueType remove(KeyType key) throws NoSuchElementException {
    //checks to see if the key is null, and if so returns exception
    if (key == null) {
      throw new NoSuchElementException("The key value can't be null.");
    }
    //finds the index of the key val
    int indexVal = Math.abs(key.hashCode()) % table.length;
    //as long as the table has the key val is not null it will go to the for loop
    if (table[indexVal] != null) {
      //enhanced for loop traverses through the collection to find the val
      for (Pair pair : table[indexVal]) {
        //if the pair key is equivalent to the key in the param, then it removes the pair
        if (pair.key.equals(key)) {
          table[indexVal].remove(pair);
          //decrements size
          size--;
          //returns the value that the removed key was mapped to
          return pair.value;
        }
      }
    }
    //if the key isn't present in the collection, then an exception is thrown
    throw new NoSuchElementException("The key can't be found.");
  }
  /**
   * Removes all key,value pairs from this collection.
   */
  @Override
  public void clear() {
    //goes through the collection and then at each index, sets it to null
    for (int i = 0; i < table.length; i++) {
      table[i] = null;
    }
    //sets the global var for size to 0
    size = 0;
  }
  /**
   * Retrieves the number of keys stored in this collection.
   * @return the number of keys stored in this collection
   */
  @Override
  public int getSize() {
    //size val is returned for the size of the collection
    return size;
  }

  /**
   * Retrieves this collection's capacity.
   * @return the size of te underlying array for this collection
   */
  @Override
  public int getCapacity() {
    //table's length is returned for the capacity of the collection
    return table.length;
  }


  /**
   * Retrieves this collection's keys.
   *
   * @return a list of keys in the underlying array for this collection.
   */
  public List<KeyType> getKeys() {
    //creates new LinkedList for the key vals
    LinkedList<KeyType> keyList = new LinkedList<>();

    //iterates over the pairs
    for (LinkedList<Pair> bucketVal : table) {
      //check to see if it's null first
      if (bucketVal != null) {
        //if not, iterate over the values
        for (Pair pairs : bucketVal) {
          //add all the values to the LinkedList
          keyList.add(pairs.key);
        }
      }
    }
    //return the final list
    return keyList;
  }

}
