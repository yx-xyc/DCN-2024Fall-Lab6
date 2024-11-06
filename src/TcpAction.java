import Fsm.Action;
import Fsm.Event;
import Fsm.FSM;

public class TcpAction extends Action {
    private int receiveDataCounter = 0;
    private int sendDataCounter = 0;
    private final String actionType;

    public TcpAction(String type) {
        this.actionType = type;
    }

    @Override
    public void execute(FSM fsm, Event evt) {
        String currentState = fsm.currentState().getName();

        if (evt.getName().equals("SDATA")) {
            sendDataCounter++;
            System.out.println("DATA sent " + sendDataCounter);
        } else if (evt.getName().equals("RDATA")) {
            receiveDataCounter++;
            System.out.println("DATA received " + receiveDataCounter);
        } else {
            System.out.println("Event " + evt.getName() + " received, current State is " + currentState);
        }
    }
}