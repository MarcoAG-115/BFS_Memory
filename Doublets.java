import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import java.util.stream.Collectors;

/**
 * Provides an implementation of the WordLadderGame interface. 
 *
 * @author Marco Gonzalez (mag0089@auburn.edu)
 * @author Dean Hendrix (dh@auburn.edu)
 * @version 2019-03-29
 */
public class Doublets implements WordLadderGame {

   // The word list used to validate words.
   // Must be instantiated and populated in the constructor.
   /////////////////////////////////////////////////////////////////////////////
   // DECLARE A FIELD NAMED lexicon HERE. THIS FIELD IS USED TO STORE ALL THE //
   // WORDS IN THE WORD LIST. YOU CAN CREATE YOUR OWN COLLECTION FOR THIS     //
   // PURPOSE OF YOU CAN USE ONE OF THE JCF COLLECTIONS. SUGGESTED CHOICES    //
   // ARE TreeSet (a red-black tree) OR HashSet (a closed addressed hash      //
   // table with chaining).                                                   //
   /////////////////////////////////////////////////////////////////////////////
   
   private TreeSet<String> lexicon;

   /**
    * Instantiates a new instance of Doublets with the lexicon populated with
    * the strings in the provided InputStream. The InputStream can be formatted
    * in different ways as long as the first string on each line is a word to be
    * stored in the lexicon.
    */
   public Doublets(InputStream in) {
      try {
         //////////////////////////////////////
         // INSTANTIATE lexicon OBJECT HERE  //
         //////////////////////////////////////
         
         lexicon = new TreeSet<String>();
         
         Scanner s =
            new Scanner(new BufferedReader(new InputStreamReader(in)));
         while (s.hasNext()) {
            String str = s.next();
            /////////////////////////////////////////////////////////////
            // INSERT CODE HERE TO APPROPRIATELY STORE str IN lexicon. //
            /////////////////////////////////////////////////////////////
            
            lexicon.add(str.toLowerCase());
            
            s.nextLine();
         }
         in.close();
      }
      catch (java.io.IOException e) {
         System.err.println("Error reading from InputStream.");
         System.exit(1);
      }
   }


   //////////////////////////////////////////////////////////////
   // ADD IMPLEMENTATIONS FOR ALL WordLadderGame METHODS HERE  //
   //////////////////////////////////////////////////////////////
   
   /**
    * Returns the Hamming distance between two strings, str1 and str2. The
    * Hamming distance between two strings of equal length is defined as the
    * number of positions at which the corresponding symbols are different. The
    * Hamming distance is undefined if the strings have different length, and
    * this method returns -1 in that case. See the following link for
    * reference: https://en.wikipedia.org/wiki/Hamming_distance
    *
    * @param  str1 the first string
    * @param  str2 the second string
    * @return      the Hamming distance between str1 and str2 if they are the
    *                  same length, -1 otherwise
    */
   public int getHammingDistance(String str1, String str2) {
   
      int count = 0;
      str1 = str1.toLowerCase();
      str2 = str2.toLowerCase();
      
      if (!(str1.length() == str2.length())) {
      
         return -1;
      }
      else {
      
         for (int i = 0; i < str1.length(); i++) {
         
            if (!(str1.charAt(i) == str2.charAt(i))) {
            
               count++;
            }
         }
         return count;  
      }
   }


  /**
   * Returns a minimum-length word ladder from start to end. If multiple
   * minimum-length word ladders exist, no guarantee is made regarding which
   * one is returned. If no word ladder exists, this method returns an empty
   * list.
   *
   * Breadth-first search must be used in all implementing classes.
   *
   * @param  start  the starting word
   * @param  end    the ending word
   * @return        a minimum length word ladder from start to end
   */
   public List<String> getMinLadder(String start, String end) {
   
      start = start.toLowerCase();
      end = end.toLowerCase();
      
      List<String> minLadder = new ArrayList<String>();
      
      if (start.equals(end)) {
      
         minLadder.add(start);
         return minLadder;
      }
      
      List<String> emptyLadder = new ArrayList<>();
      
      if (getHammingDistance(start, end) == -1) {
      
         return emptyLadder;
      }
      
      ArrayList<String> list = new ArrayList<String>();
      
      if (isWord(start) && isWord(end)) {
      
         list = breadthFirstSearch(start, end);
      }
      
      if (list.isEmpty()) {
      
         return emptyLadder;
      }
      
      for (int i = list.size() - 1; i >= 0; i--) {
      
         minLadder.add(list.get(i));
      }
      return minLadder;
   }
   
   /**
    * Node Class.
    */
   private class Node {
    
      // Instance Variables
      
      String location;
      Node previous;
      
      // Constructor
      
      public Node(String x, Node y) {
      
         location = x;
         previous = y;
      }
   }
   
   /**
    * Breadth First Search Method.
    */
   private ArrayList<String> breadthFirstSearch(String start, String end) {
    
      HashSet<String> temp = new HashSet<String>();
      Deque<Node> queue = new ArrayDeque<Node>();
      ArrayList<String> list = new ArrayList<String>();
      temp.add(start);
      queue.addLast(new Node(start, null));
      Node last = new Node(end, null);
      
      outerloop:
      while (!(queue.isEmpty())) {
      
         Node n = queue.removeFirst();
         String location = n.location;
         
         for (String nbr : getNeighbors(location)) {
         
            if (!(temp.contains(nbr))) {
            
               temp.add(nbr);
               queue.addLast(new Node(nbr, n));
               
               if (nbr.equals(end)) {
               
                  last.previous = n;
                  break outerloop;
               }
            }
         }
      }
      if (last.previous == null) {
      
         return list;
      }
      
      Node n2 = last;
      
      while (n2 != null) {
      
         list.add(n2.location);
         n2 = n2.previous;
      }
      return list;
   }


   /**
    * Returns all the words that have a Hamming distance of one relative to the
    * given word.
    *
    * @param  word the given word
    * @return      the neighbors of the given word
    */
   public List<String> getNeighbors(String word) {
   
      Iterator<String> iter = lexicon.iterator();
      List<String> list = new ArrayList<String>();
      
      while (iter.hasNext()) {
      
         String check = iter.next();
         
         if (getHammingDistance(word, check) == 1) {
         
            list.add(check);
         }
      }
      return list;
   }


   /**
    * Returns the total number of words in the current lexicon.
    *
    * @return number of words in the lexicon
    */
   public int getWordCount() {
   
      return lexicon.size();
   }


   /**
    * Checks to see if the given string is a word.
    *
    * @param  str the string to check
    * @return     true if str is a word, false otherwise
    */
   public boolean isWord(String str) {
   
      str = str.toLowerCase();
      
      if (lexicon.contains(str)) {
      
         return true;
      }
      else {
      
         return false;
      }
   }


   /**
    * Checks to see if the given sequence of strings is a valid word ladder.
    *
    * @param  sequence the given sequence of strings
    * @return          true if the given sequence is a valid word ladder,
    *                       false otherwise
    */
   public boolean isWordLadder(List<String> sequence) {
   
      if (sequence.isEmpty()) {
      
         return false;
      }
      
      String str1 = "";
      String str2 = "";
      
      for (int i = 0; i < sequence.size() - 1; i++) {
      
         str1 = sequence.get(i);
         str2 = sequence.get(i + 1);
         
         if (!(isWord(str1)) || !(isWord(str2))) {
         
            return false;
         }
         if (getHammingDistance(str1, str2) != 1) {
         
            return false;
         }
      }
      return true;
      
   }

}

