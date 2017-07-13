import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;



/**
 * Binary-search based guessing player.
 * This player is for task C.
 *
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class BinaryGuessPlayer implements Player
{

	/** storing all attributes **/
	ArrayList<String> attributes = new ArrayList<String>();
	/** storing all attributes and their values (key:attribute,value: value list of an attribute)**/
	HashMap<String,ArrayList<String>> avMap = new HashMap<String,ArrayList<String>>();
	/** storing all persons , use a HashMap<A,V> to represent a person object**/
	LinkedList<HashMap<String,String>> persons = new LinkedList<HashMap<String,String>>();
	/** storing all distinct a-v in a list for all available candidates **/
	LinkedList<AttributeValue> availableAVList = new LinkedList<AttributeValue>();
	/** the person chosen by this player (simply use a HashMap<A,V> to represent a person object) **/
	HashMap<String,String> chosenPerson = null;
	String chosenPersonName = null;
    /**
     * Loads the game configuration from gameFilename, and also store the chosen
     * person.
     *
     * @param gameFilename Filename of game configuration.
     * @param chosenName Name of the chosen person for this player.
     * @throws IOException If there are IO issues with loading of gameFilename.
     *    Note you can handle IOException within the constructor and remove
     *    the "throws IOException" method specification, but make sure your
     *    implementation exits gracefully if an IOException is thrown.
     */
    public BinaryGuessPlayer(String gameFilename, String chosenName)
        throws IOException
    {
       	this.chosenPersonName = chosenName;
    	loadGameData(gameFilename,chosenName);
    } // end of BinaryGuessPlayer()



    /**
     * Each candidate has a set of attribute-value pairs associated with them.
     * Let the union of the attribute-value pairs sets of all candidates be denoted by
     *  S (Here, S should be the list named availableAVList).
     * Then this type of player will random select a pair (a,v) from S, 
     * and then ask whether the chosen person has the value of v for attribute a.
     */
    public Guess guess() {

    	if(persons.size() > 1){
    		AttributeValue bestChoice = this.getBinaryGuess();
            //remove asked av from availableAVList ,
            //which can avoid ask same question more than once
    		this.availableAVList.remove(bestChoice);
            return new Guess(Guess.GuessType.Attribute, bestChoice.attribute, bestChoice.value);
    	}else{
    		return new Guess(Guess.GuessType.Person, "", persons.get(0).get("name"));
    	}

       
    } // end of guess()
    /**
     * a pair that eliminates as close to half the candidates 
     * (might not be possible to get exactly half, but as close
     *  as possible)
     * @return
     */
    private AttributeValue getBinaryGuess(){
    	double hafPersonAmount = persons.size()/2.0;
    	AttributeValue bestChoice = null;
    	int bestAcounter = 0;
    	for(int i = 0 ; i < availableAVList.size() ; i ++){
    		AttributeValue av = availableAVList.get(i);
    		String attribute = av.attribute;
    		String value = av.value;
    		int counter = 0;
    		if(attribute.equals("name"))continue;
    		for(HashMap<String,String> person : persons){
    			if(person.get(attribute).equals(value))counter++;
    		}
    		if(Math.abs(counter-hafPersonAmount) < Math.abs(bestAcounter-hafPersonAmount)){
    			bestAcounter = counter;
    			bestChoice = av;
    		}
    	}
    	if(bestChoice == null) bestChoice = availableAVList.get(0);
    	return bestChoice;
    }

    /**
     * if guess type is attribute:
     * ask whether the chosen person has the value of v for attribute a.
     * if guess type is person:
     * ask yes only if the response name equals chosen person name.
     * @param currGuess
     * @return
     */
    public boolean answer(Guess currGuess) {
    	
    	// guess attribute
        if(currGuess.mType == Guess.GuessType.Attribute){
        	String value = chosenPerson.get(currGuess.mAttribute);
        	return currGuess.mValue.equals(value);
        	
        }else{//guess person
        	
        	return chosenPersonName.equals(currGuess.mValue);
        }
        
    } // end of answer()
    
    /**
     * @param currGuess
     * @param answer
     * @return
     */
	public boolean receiveAnswer(Guess currGuess, boolean answer) {

		if(answer){
			if(currGuess.mType == Guess.GuessType.Person){
				return true;
			}else{	    
				//  If the answer is yes, then eliminate all candidates who don’t have value v for attribute a. 
				AttributeValue av = new AttributeValue(currGuess.mAttribute,currGuess.mValue);
				eliminatePerson(av,true);
				return false;
			}
		}else{
			if(currGuess.mType == Guess.GuessType.Person){
				removePersonByName(currGuess.mValue);
				return false;
			}else{
			    //  If the answer is no, then eliminate all candidates that have the value v for attribute a.
				AttributeValue av = new AttributeValue(currGuess.mAttribute,currGuess.mValue);
				eliminatePerson(av,false);
				return false;
			}
			
		}


    } // end of receiveAnswer()
	
	/** eliminate unavailable persons  **/
	private void eliminatePerson(AttributeValue av, boolean answer){
		//update person list by eliminating unavailable persons
		availableAVList.clear();
		for(int i = persons.size() - 1 ; i >= 0 ; i --){
			HashMap<String,String> person = persons.get(i);
			boolean avEquals = av.value.equals(person.get(av.attribute));
			if(avEquals != answer){
				persons.remove(person);
			}else{
				Iterator keyIterator = person.keySet().iterator();
				int k = 0;
				while(keyIterator.hasNext()){
					String attribute = (String)keyIterator.next();
					AttributeValue newAV = new AttributeValue(attribute,person.get(attribute));
					if(!availableAVList.contains(newAV)){
						availableAVList.add(newAV);
					}
				}
				
			}
		}
	}

	
	/** remove a person from person list by name **/
	private void removePersonByName(String name){
		for(int i = 0 ; i < persons.size() ; i ++){
			HashMap<String,String> person = persons.get(i);
			if(name.equals(person.get("name"))){
				persons.remove(i);
				break;
			}
		}
	}
	
	
	/** Task A: Design and Implement the Data Structures of a Guess Who Game (3 marks) [for random guess player]**/
	/**
	 * 1.To load all game data from the configuration file 'gameFilename'.
	 * 2.To store all data in this player class.//transferring to knowledge
	 * @return only return false when cannot find the file 'gameFilename'.
	 */
	private boolean loadGameData(String gameFilename,String chosenPersonName){
		boolean loadSucessful = true;
        //
        // load game configuration file
        //
		try {
            // load chosen persons for both players
            BufferedReader assignedReader = new BufferedReader(new FileReader(gameFilename));

            String line;
            boolean isLoadingAttributes = true;
            HashMap<String,String> person = null;
            boolean hasPersonLoaded = false;
            
            while((line = assignedReader.readLine()) != null){
            	
            	String[] values = line.split(" ");
            	int length = values.length;

            	if(isLoadingAttributes){
                	//
                	// 1.loading the attribute-value data 
                	//
            		if(length > 1){
            			//store attribute
            			attributes.add(values[0]);
            			//store values of this attribute
            			ArrayList<String> tempvalues = new ArrayList<String>();
            			for(int i = 1 ; i < values.length ; i ++){
            				tempvalues.add(values[i]);
            			}
            			//put them into the avMap
            			avMap.put(values[0], tempvalues);
            			
            		}else{
            			isLoadingAttributes = false;
            		}
            		
            	}else{

//            		this.createPersons();
//            		break;
                	//
                	// 2.loading person data
                	//
            		if(length == 1){// it should be the person name. 
            			if(!hasPersonLoaded && person != null){
            				persons.add(person);
            				hasPersonLoaded = true;
            				//if the current loading person is the chosen person:
            				if(person.get("name").equals(chosenPersonName)){
            					this.chosenPerson = person;
            				}
            				person = null;
            			}
            			if(!values[0].equals("")){
                			person = new HashMap<String,String>();
                			person.put("name", values[0]);
                			hasPersonLoaded = false;
            			}
            			
            		}else if(length == 2){// it should be an attribute of this person.
            			person.put(values[0], values[1]);
            			AttributeValue av = new AttributeValue(values[0], values[1]);
            			if(!availableAVList.contains(av)){
            				availableAVList.add(av);
            			}
            		}
            		
            		
            	}
            }
            // To avoid the last line is not a empty line,
            // check if the person object has been saved 
        	if(!isLoadingAttributes && person != null){
				persons.add(person);
				if(person.get("name").equals(chosenPersonName)){
					this.chosenPerson = person;
				}
				person = null;
			}

		}
		catch(FileNotFoundException ex) {
			System.err.println("Missing file " + ex.getMessage() + ".");
		}
		catch(IOException ex) {
			System.err.println(ex.getMessage());
		}
		return loadSucessful;
	} // end of loadGameData
	
	/************* end **************/

	/**
	 * 
	 * attribute value pair
	 */
	class AttributeValue{
		String attribute;
		String value;
		public AttributeValue(String attribute,String value){
			this.attribute = attribute;
			this.value = value;
		}
		@Override
		public boolean equals(Object obj) {
			if(obj != null && obj instanceof AttributeValue){
				AttributeValue av = (AttributeValue)obj;
				if(this.attribute.equals(av.attribute) && this.value.equals(av.value)){
					return true;
				}else{
					return false;
				}
			}
			return false;
		}
		
		
	}

} // end of class BinaryGuessPlayer
