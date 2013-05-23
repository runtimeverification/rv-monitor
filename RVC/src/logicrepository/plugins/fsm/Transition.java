package logicrepository.plugins.fsm;

public class Transition{
  private String event;
  private String destination;

  Transition(String event, String destination){
    this.event = event;
    this.destination = destination;
  }

  public String getEvent(){
	 return event;
  }

  public String getDestination(){
	 return destination;
  }

  public String toString(){
    if(event == null)
      return "default " + destination;
    return event + " -> " + destination;
  }
}
