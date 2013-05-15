package cmongen.plugins.fsm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

//It would probably be slightly faster to use ints
//for lables rather than strings... we only read need
//to lookup the strings for pretty printing
public class FSMEnables {

  static private HashSet<String> copy(HashSet<String> original){
    HashSet<String> output = new HashSet<String>();
	 for(String state : original){
      output.add(state);
	 }
	 return output;
  }

  static private ArrayList<String> copy(ArrayList<String> original){
    ArrayList<String> output = new ArrayList<String>();
	 for(String state : original){
      output.add(state);
	 }
	 return output;
  }

  private String startState;
  private ArrayList<String> events;
  private ArrayList<String> states;
  private ArrayList<String> categories;
  private HashMap<String, HashSet<String>> aliases;
  private HashMap<String, ArrayList<Transition>> stateMap;
  private HashMap<String, HashSet<String>> reachability;
  private String creationEvents = "";
  private HashMap<String, HashMap<String, HashSet<HashSet<String>>>> enables;
  

  FSMEnables(String startState,
      ArrayList<String> events,
      ArrayList<String> states,
      ArrayList<String> categories,
      HashMap<String, HashSet<String>> aliases,
		HashMap<String, ArrayList<Transition>> stateMap)
  {
	 this.startState = startState;
    this.events = events;
	 this.states = states;
	 this.categories = categories;
	 this.aliases = aliases; 
    this.stateMap = stateMap;
   
    reachability = new HashMap<String, HashSet<String>>();
    enables = new HashMap<String, HashMap<String, HashSet<HashSet<String>>>>();
    
    computeReachability();
	 computeEnables();
  }

  private void computeEnables(){
	 for(String category : categories){
		HashMap<String, HashSet<HashSet<String>>> categoryEnable
		  = new HashMap<String, HashSet<HashSet<String>>>();
		enables.put(category, categoryEnable);
      for(String event : events){
	        categoryEnable.put(event, new HashSet<HashSet<String>>());
	   }
	 }
	 HashMap<String, HashSet<HashSet<String>>> eventsSeen 
	   = new HashMap<String, HashSet<HashSet<String>>>();
	 for(String state : states){
      eventsSeen.put(state, new HashSet<HashSet<String>>());
	 }
    computeEnables(startState,
	                new HashSet<String>(),
						 eventsSeen);
	 HashSet<String> nil = new HashSet<String>();
	 HashSet<String> creationEventsSet = new HashSet<String>();
	 out:
	 for(String category : categories){
      for(String event : events){
        if(enables.get(category).get(event).contains(nil)){
		    creationEventsSet.add(event);
			 if(creationEventsSet.size() == events.size()) break out;
		  }
		}
	 }
	 for(String event : creationEventsSet){
      creationEvents += " " + event;
	 }
  }

  private void computeEnables(String state,
                              HashSet<String> eventPath,
										HashMap<String, HashSet<HashSet<String>>> eventsSeen){
    //add this path to the mapping of seen event paths
	 //for this state.  This avoids infinite recursion
	 //because we check to see if a state has seen a given
	 //path before we take the recursive step
    eventsSeen.get(state).add(eventPath);
	 boolean nContainsDefault = true;
	 String defaultDestination = null;
	 HashSet<String> definedEvents = new HashSet<String>();
    for(Transition transition : stateMap.get(state)){
		String event = transition.getEvent();
	   String destination = transition.getDestination();
		if(event == null){
        nContainsDefault = false;
		  defaultDestination = destination;
		  continue;
		}
		definedEvents.add(event);
      for(String category : categories){
		  if(reachability.get(category).contains(destination)){
           enables.get(category).get(event).add(eventPath);
		  }
		}
	   HashSet<String> newPath = copy(eventPath);
      newPath.add(event);
		if(!eventsSeen.get(destination).contains(newPath)) 
        computeEnables(destination, newPath, eventsSeen);
	 }
	 if(nContainsDefault) {
		//We need to add the current event path to the fail enable for all
		//events that do not have transitions out of the current state
		//(or a category that is an alias which contains fail)
		//because there is no default transition
      handleImmediateFailures(definedEvents, eventPath);
	 } else {
	 //handle the default transition, which should compute
	 //for all events not explicitly listed
	   handleDefaultTransition(definedEvents, eventPath, defaultDestination, eventsSeen);
	 }
  }
  
  private void computeReachability(){
	 for(String category : categories){
      reachability.put(category, new HashSet<String>());
	 }
	 HashMap<String, HashSet<HashSet<String>>> seen 
	   = new HashMap<String, HashSet<HashSet<String>>>();
	 for(String state : states){
      seen.put(state, new HashSet<HashSet<String>>());
	 }
    computeReachability(startState, 
	                     new HashSet<String>(), 
								seen
								);
  }
  
  private void computeReachability(String state, 
                                   HashSet<String> path,
											  HashMap<String, HashSet<HashSet<String>>> seen){
	 //add this path to the mapping of seen paths
	 //for this state.  This avoids infinite recursion
	 //because we check to see if a state has seen a given
	 //path before we take the recursive step
    seen.get(state).add(path);
	 HashSet<String> newPath = copy(path);
	 newPath.add(state);
	 for(String category : categories){
      //If this is a state that is in one of our categories
		//put all the states seen on the path to this state in
		//the reachability for said category

		//First check to see if the category is an alias
		if(aliases.containsKey(category)){
		  HashSet<String> aliasedStates = aliases.get(category);
        if(aliasedStates.contains(state)){
          addPath(category, newPath); 
		  }
		  //the alias contains fail, we only need to check this
		  //if the alias does not already contain this state
		  //bcause adding the path to the category a second
		  //time accomplishes nothing
        else if(   aliasedStates.contains("fail") 
			       && nContainsDefault(state) 
					 && (stateMap.get(state).size() < events.size()))
		  {
          addPath(category, newPath);
		  }
      }
		//otherwise it must be a state or "fail", outright.  
		//This is essentially a copy of the above, only the conditions
		//differ
		else{
        if( category.equals(state)){
          addPath(category, newPath);
		  }
        else if(    category.equals("fail")
			        && nContainsDefault(state)
					  && (stateMap.get(state).size() < events.size()))
        {
          addPath("fail", newPath);
		  }
		}
	 }
	 //This is the recursive call.  We call it for every
	 //path out of the current state, after appending this state to
	 //the path.  Note that we must clone the path, because all
	 //Objects are references in Java, we do not want one path
	 //for every recursive call!  I implemented my own copy
	 //because I don't like the unsafe operation warning
	 //clone gives
    for(Transition transition : stateMap.get(state)){
      if(!seen.get(transition.getDestination()).contains(newPath))
        computeReachability(transition.getDestination(), newPath, seen);
	 }
  }

  private void addPath(String category, HashSet<String> path){
    for(String pathState : path) {          
      reachability.get(category).add(pathState);
    }
  }

  private boolean nContainsDefault(String state){
    ArrayList<Transition> transitions = stateMap.get(state);
	 for(Transition transition : transitions){
      if(transition.getEvent() == null) return false;
	 }
	 return true;
  }

  private void handleImmediateFailures(HashSet<String> definedEvents, 
                                       HashSet<String> eventPath)
  {
	 for(String category : categories){
		if(aliases.containsKey(category) && aliases.get(category).contains("fail")
			|| category.equals("fail"))
		{
		   for(String event : events){
			  if(!definedEvents.contains(event)){
             enables.get(category).get(event).add(eventPath);
			  }
		   }
		}	 
	 }
  }

  private void handleDefaultTransition(HashSet<String> definedEvents,
                                       HashSet<String> eventPath,
													String defaultDestination,
													HashMap<String, HashSet<HashSet<String>>> eventsSeen)
  {
    for(String category : categories){
      if(!reachability.get(category).contains(defaultDestination)) continue;
		HashMap<String, HashSet<HashSet<String>>> categoryEnables 
		  = enables.get(category);
	   for(String event : events){
        if(!definedEvents.contains(event)){
          categoryEnables.get(event).add(eventPath);
          HashSet<String> newPath = copy(eventPath);
          newPath.add(event);
		    if(!eventsSeen.get(defaultDestination).contains(newPath)) 
            computeEnables(defaultDestination, newPath, eventsSeen);
		  }
	   }
 	 }
  }

  public HashMap<String, HashMap<String, HashSet<HashSet<String>>>> getEnables(){
	 return enables;  
  }

  public String toString(){
    String output = "";
	 for(String category : categories){
      output += "// " + category + " Enables\n";
		output += enables.get(category).toString() + "\n";
	 }
	 return output;
  }
   
  public String FSMString(){
    String output = startState;
	 output += stringOfTransitions(startState);
	 for(String key : stateMap.keySet()){
      if(key == startState) continue;
      output += key;
	   output += stringOfTransitions(key);
	 }
	 return output;
  }

  public String creationEvents(){
    return creationEvents;
  }

  private String stringOfTransitions(String state){
    String output = "[\n";
	 for(Transition transition : stateMap.get(state)){
      output += "  " + transition.toString() + ",\n";
	 }
	 return output + "]\n";
  } 

}

