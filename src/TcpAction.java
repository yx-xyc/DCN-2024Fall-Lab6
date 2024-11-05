import Fsm.*;

public class TcpAction extends Action {
    private int dataCounter = 0;
    private String actionName;

    public TcpAction(String name) {
        this.actionName = name;
    }

    @Override
    public void execute(FSM fsm, Event evt) {
        String currentState = fsm.currentState().getName();

        if (evt.getName().equals("SDATA") || evt.getName().equals("RDATA")) {
            dataCounter++;
            if (evt.getName().equals("SDATA")) {
                System.out.println("DATA sent " + dataCounter);
            } else {
                System.out.println("DATA received " + dataCounter);
            }
        } else {
            System.out.println("Event " + evt.getName() + " received, current State is " + currentState);
        }
    }
}