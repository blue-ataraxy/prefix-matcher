//left to do

//1. how to process commands and execute methods accordingly

//2. how to return a boolean for insert method --> if NOT inserting a **designated length** of binary string (only 1s and 0s), then return false.

//3. how to print tree after every insert method


import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrefixMatch{
    public static void main(String args[]){
        
        Trie trie = new Trie();

        String filename = args[0];
        File file = new File(filename); 

        try{
            try(OutputStreamWriter out = new OutputStreamWriter(System.out)){

                BufferedReader reader = new BufferedReader(new FileReader(file));

                String line = reader.readLine();
    
                while(line != null){
                        String[] split = line.split(" ");
                    
                        //reads command string and calls the relevant method
        
                        if(split[0].equals("insert")){
                            String insertstr = split[1];
                            trie.insert(trie, insertstr);
        
                            //visualises the tree in terminal every time an insert operation is executed
                                System.out.println("--------------------------------------------");
                                System.out.println("TRIE VISUALISATION: \n");            
                                trie.getTrie().printTree(out);
                                out.flush();
                                System.out.println("--------------------------------------------");
                                System.out.println();            
                                System.out.println();            
            
                            
                        }
                        if(split[0].equals("search")){
                            String searchstr = split[1];
                            System.out.println(trie.search(trie, searchstr));
                        }
                        if(split[0].equals("print")){
                            System.out.println(trie.trieToList(trie));
                        }
                        if(split[0].equals("largest")){
                            System.out.println(trie.largest(trie));
                        }
                        if(split[0].equals("smallest")){
                            System.out.println(trie.smallest(trie));
                        }
                        if(split[0].equals("height")){
                            System.out.println(trie.height(trie));
                        }
                        if(split[0].equals("size")){
                            System.out.println(trie.size(trie));
                        }
        
                        line = reader.readLine();
    
                }
            }

            catch(Exception e){
                e.getStackTrace();
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

class Trie{
    private Node root;

    //creating Trie with root node
    public Trie(Node n){
        this.root = n;
    }
    //creating empty Trie
    public Trie(){
        this.root = null;
    }

    //getter & setter
    public Node getTrie(){
        return this.root;
    }
    public void setTrie(Node n){
        this.root = n;
    }

    //methods

    //1. INSERT

    public boolean insert(Trie trie, String st){

        //only returns true if string is a binary string

        boolean checkvalid = true;

        if(st == null){ //if null string, return false
            checkvalid = false;
            return checkvalid;
        }
        
        char[] check = st.toCharArray();
        for(int i = 0; i<check.length; i++){
            if((check[i] != '0') && (check[i] != '1')){ //if string is not binary string, return false
                checkvalid = false;
                return checkvalid;
            }
        }

        if(trie.getTrie() == null){
            
            //create new Root Node containing st
            Node newroot = new Node(null, null, null, st);

            trie.setTrie(newroot);

        }
        else{
            //create new Node containing st
            Node newnode = new Node(null, null, null, st);

            //check nodes starting from the root. before each check, if the node is empty, then continue the check. if it contains a string (i.e. is leaf node), then ...
            Node temp = trie.getTrie();

            boolean inserted = false;

            while(temp.getBinaryStr() == null){ //i.e. internal empty node

                //traverse the trie until an empty space / leaf node is reached 
                char[] stchar = st.toCharArray();
                for(int i=0; i<stchar.length; i++){

                    if(temp.getBinaryStr() == null){
                        if(stchar[i] == '0'){

                            //before actually traversing to left, check if there exists a node there
                            if(temp.getLeft() == null){ //empty, so insert st here
                                newnode.setParent(temp);
                                temp.setLeft(newnode);
                                inserted = true;
                                break;
                            }
                            else{
                                temp = temp.getLeft(); //could be either internal empty node or leaf node
                            }
                        }
                        if(stchar[i] == '1'){
                            if(temp.getRight() == null){
                                
                                newnode.setParent(temp);
                                temp.setRight(newnode);
                                inserted = true;
                                break;
                            }
                            else{
                                temp = temp.getRight();
                            }
                        }
                    }

                }

            }
            while(inserted == false){

                //exiting the while loop means a leaf node has been reached

                String x = temp.getBinaryStr(); 
                String y = st;

                char[] charx = x.toCharArray();
                char[] chary = y.toCharArray();
                int index = 0;

                for(int i = 0; i<charx.length; i++){
                    if(charx[i] != chary[i]){
                        index = i;
                        break;
                    }
                }

                //then trace to that index.
                
                Node temp2 = trie.getTrie();

                for(int i = 0; i<= index; i++){

                    temp2.setBinaryStr(null);

                    if(i == index){ //store the strings as leaf nodes (left and right children)
                        if(charx[i] == '0'){
                            temp2.setLeft(new Node(temp2, null, null, x));
                            temp2.setRight(new Node(temp2, null, null, y));
                        }
                        if(charx[i] == '1'){
                            temp2.setRight(new Node(temp2, null, null, x));
                            temp2.setLeft(new Node(temp2, null, null, y));
                        }
                        inserted = true;
                    }
                    else{

                        if(charx[i] == '0'){
                            if(temp2.getLeft() != null){ //i.e. move to this node's left child and set binary string to null.
                                temp2.getLeft().setBinaryStr(null);
                                temp2 = temp2.getLeft();
                            }
                            else{ //i.e. an empty internal node has to be inserted here
                                temp2.setLeft(new Node(temp2, null, null, null));
                                temp2 = temp2.getLeft();
                            }
                        }
                        if(charx[i] == '1'){
                            if(temp2.getRight() != null){
                                temp2.getRight().setBinaryStr(null);
                                temp2 = temp2.getRight();
                            }
                            else{
                                temp2.setRight(new Node(temp2, null, null, null));
                                temp2 = temp2.getRight();
                            }
                        }    
                    }
                }

            }
        }

        return checkvalid;
        
    }


    //2. TRIE TO LIST

    public List<String> trieToList(Trie trie){

        List<String> list = new ArrayList<String>();

        addString(trie.getTrie(), list);

        return list;

    }

        //2a. Recursive function for adding binary strings to a List

        public void addString(Node n, List<String> list){
                        
            if(n != null){
                addString(n.getLeft(), list);
                if(n.getBinaryStr() != null){
                    list.add(n.getBinaryStr());
                }
                addString(n.getRight(), list);
            }

        }


    //3. LARGEST (returns largest string based on lexicographic order)

    public String largest(Trie trie){

        //get last element of the list
        return trie.trieToList(trie).get(trie.trieToList(trie).size() - 1);
    }


    //4. SMALLEST
    
    public String smallest(Trie trie){

        //get first element of the list
        return trie.trieToList(trie).get(0);
    }

    //5. SEARCH

    public String search(Trie trie, String st){
        
        Node temp = trie.getTrie();

        String lpm = null;
        
        char[] stchar = st.toCharArray();

        for(int i = 0; i<stchar.length; i++){
            if(temp.getBinaryStr() == null){ //i.e. still at empty internal node
                if(stchar[i] == '0'){
                    if(temp.getLeft() != null){
                        temp = temp.getLeft();
                    }
                    else{ //i.e. traversed to a space with no nodes. i.e. i have to stay at empty internal node.
                        temp = temp.getRight();

                        while(temp.getLeft() != null){
                            temp = temp.getLeft();
                        }
                        lpm = temp.getBinaryStr();
                        break;
                        
                    }
                }
                else if(stchar[i] == '1'){
                    if(temp.getRight() != null){
                        temp = temp.getRight();
                    }
                    else{ //i.e. traversed to a space with no nodes. i.e. i have to stay at empty internal node.
                        temp = temp.getLeft();
                        while(temp.getRight() != null){
                            temp = temp.getRight();
                        }
                        lpm = temp.getBinaryStr();
                        break;
                    }
                }
            }
            else{ //leaf node was reached
                lpm = temp.getBinaryStr();
                break;
            }
        }
        return lpm;
    }


    //6. SIZE

    public int size(Trie trie){
        
        List<String> list = new ArrayList<String>();

        list = trie.trieToList(trie);

        int size = list.size();
        
        return size;
    }


    //7. HEIGHT 

    public int height(Trie trie){
        
        List<Integer> heightlist = new ArrayList<Integer>();

        findLeafHeight(trie.getTrie(), heightlist, 0);
        Collections.sort(heightlist); //sorts heightlist in ascending order

        return heightlist.get(heightlist.size()-1); //gets the last index (largest height) in heightlist

    }

        //8a. Recursive function for finding leaf nodes and storing their heights in an arraylist.

        public void findLeafHeight(Node n, List<Integer> list, int i){
            
            Node temp = n;
            int leafheight = i;


            if((temp.getLeft() == null) && (temp.getRight() == null)){ //leaf node found
                list.add(leafheight);
                return;
            }
            
            if(temp.getLeft() != null){
                findLeafHeight(n.getLeft(), list, leafheight + 1); //each time you traverse left, height + 1

            }
            if(temp.getRight() != null){
                findLeafHeight(n.getRight(), list, leafheight + 1); //each time you traverse right, height + 1
            }
            
        }


}

class Node{

    private Node parent;
    private Node left;
    private Node right;
    private String binarystr;

    //constructor
    public Node(Node parent, Node left, Node right, String binarystr){
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.binarystr = binarystr;
    }

    //getters & setters
    public Node getParent(){
        return this.parent;
    }
    public void setParent(Node n){
        this.parent = n;
    }
    public Node getLeft(){
        return this.left;
    }
    public void setLeft(Node n){
        this.left = n;
    }
    public Node getRight(){
        return this.right;
    }
    public void setRight(Node n){
        this.right = n;
    }
    public String getBinaryStr(){
        return this.binarystr;
    }
    public void setBinaryStr(String s){
        this.binarystr = s;
    }



    //METHODS TO VISUALISE THE TREE

    //Recursive function to get the left and right subtrees
    public void printTree(OutputStreamWriter out) throws IOException {

        if (this.right != null) {
            this.right.printTree(out, true, "");
        }
        printNodeValue(out);
        if (this.left != null) {
            this.left.printTree(out, false, "");
        }
    }

    //print each node value
    private void printNodeValue(OutputStreamWriter out) throws IOException {
        if (this.binarystr == null) { //print null if empty internal node
            out.write("null");
        } else {
            out.write(this.binarystr);
        }
        out.write('\n');
    }

    //Recursive function to print the nodes of left and right subtrees, and connecting branches 
    private void printTree(OutputStreamWriter out, boolean isRight, String indent) throws IOException {
        if (this.right != null) {
            this.right.printTree(out, true, indent + (isRight ? "        " : " |      "));
        }
        out.write(indent);
        if (isRight) {
            out.write(" /");
        } else {
            out.write(" \\");
        }
        out.write("----- ");
        printNodeValue(out);
        if (this.left != null) {
            this.left.printTree(out, false, indent + (isRight ? " |      " : "        "));
        }
    }




}

