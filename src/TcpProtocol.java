import Fsm.FSM;
import Fsm.Transition;
import Fsm.FsmException;

public class TcpProtocol {
    private FSM fsm;

    // States
    private TcpState CLOSED;
    private TcpState LISTEN;
    private TcpState SYN_RCVD;
    private TcpState SYN_SENT;
    private TcpState ESTABLISHED;
    private TcpState FIN_WAIT_1;
    private TcpState FIN_WAIT_2;
    private TcpState CLOSING;
    private TcpState TIME_WAIT;
    private TcpState CLOSE_WAIT;
    private TcpState LAST_ACK;

    // Events
    private TcpEvent PASSIVE;
    private TcpEvent ACTIVE;
    private TcpEvent SYN;
    private TcpEvent SYNACK;
    private TcpEvent ACK;
    private TcpEvent RDATA;
    private TcpEvent SDATA;
    private TcpEvent FIN;
    private TcpEvent CLOSE;
    private TcpEvent TIMEOUT;
    private TcpEvent SEND;

    // Actions
    private TcpAction defaultAction;
    private TcpAction dataAction;

    public TcpProtocol() {
        initializeStates();
        initializeEvents();
        initializeActions();
        initializeFSM();
        addTransitions();
    }

    private void initializeStates() {
        CLOSED = new TcpState("CLOSED");
        LISTEN = new TcpState("LISTEN");
        SYN_RCVD = new TcpState("SYN_RCVD");
        SYN_SENT = new TcpState("SYN_SENT");
        ESTABLISHED = new TcpState("ESTABLISHED");
        FIN_WAIT_1 = new TcpState("FIN_WAIT_1");
        FIN_WAIT_2 = new TcpState("FIN_WAIT_2");
        CLOSING = new TcpState("CLOSING");
        TIME_WAIT = new TcpState("TIME_WAIT");
        CLOSE_WAIT = new TcpState("CLOSE_WAIT");
        LAST_ACK = new TcpState("LAST_ACK");
    }

    private void initializeEvents() {
        PASSIVE = new TcpEvent("PASSIVE");
        ACTIVE = new TcpEvent("ACTIVE");
        SYN = new TcpEvent("SYN");
        SYNACK = new TcpEvent("SYNACK");
        ACK = new TcpEvent("ACK");
        RDATA = new TcpEvent("RDATA");
        SDATA = new TcpEvent("SDATA");
        FIN = new TcpEvent("FIN");
        CLOSE = new TcpEvent("CLOSE");
        TIMEOUT = new TcpEvent("TIMEOUT");
        SEND = new TcpEvent("SEND");
    }

    private void initializeActions() {
        defaultAction = new TcpAction("default");
        dataAction = new TcpAction("data");
    }

    private void initializeFSM() {
        fsm = new FSM("Tcp Protocol", CLOSED);
    }

    @SuppressWarnings("DuplicatedCode")
    private void addTransitions() {
        try {
            // Close State
            fsm.addTransition(new Transition(CLOSED, PASSIVE, LISTEN, defaultAction));
            fsm.addTransition(new Transition(CLOSED, ACTIVE, SYN_SENT, defaultAction));

            // Listen State
            fsm.addTransition(new Transition(LISTEN, CLOSE, CLOSED, defaultAction));
            fsm.addTransition(new Transition(LISTEN, SEND, SYN_SENT, defaultAction));
            fsm.addTransition(new Transition(LISTEN, SYN, SYN_RCVD, defaultAction));

            // SYN_SENT State
            fsm.addTransition(new Transition(SYN_SENT, CLOSE, CLOSED, defaultAction));
            fsm.addTransition(new Transition(SYN_SENT, SYNACK, ESTABLISHED, defaultAction));
            fsm.addTransition(new Transition(SYN_SENT, SYN, SYN_RCVD, defaultAction));

            // SYN_RCVD State
            fsm.addTransition(new Transition(SYN_RCVD, ACK, ESTABLISHED, defaultAction));
            fsm.addTransition(new Transition(SYN_RCVD, CLOSE, FIN_WAIT_1, defaultAction));

            // ESTABLISHED State
            fsm.addTransition(new Transition(ESTABLISHED, CLOSE, FIN_WAIT_1, defaultAction));
            fsm.addTransition(new Transition(ESTABLISHED, FIN, CLOSE_WAIT, defaultAction));
            fsm.addTransition(new Transition(ESTABLISHED, RDATA, ESTABLISHED, dataAction));
            fsm.addTransition(new Transition(ESTABLISHED, SDATA, ESTABLISHED, dataAction));

            // FIN_WAIT_1 State
            fsm.addTransition(new Transition(FIN_WAIT_1, ACK, FIN_WAIT_2, defaultAction));
            fsm.addTransition(new Transition(FIN_WAIT_1, FIN, CLOSING, defaultAction));

            // FIN_WAIT_2 State
            fsm.addTransition(new Transition(FIN_WAIT_2, FIN, TIME_WAIT, defaultAction));

            // CLOSE_WAIT State
            fsm.addTransition(new Transition(CLOSE_WAIT, CLOSE, LAST_ACK, defaultAction));

            // CLOSING State
            fsm.addTransition(new Transition(CLOSING, ACK, TIME_WAIT, defaultAction));

            // LAST_ACK State
            fsm.addTransition(new Transition(LAST_ACK, ACK, CLOSED, defaultAction));

            // TIME_WAIT State
            fsm.addTransition(new Transition(TIME_WAIT, TIMEOUT, CLOSED, defaultAction));
        } catch (FsmException e) {
            System.err.println("Error adding transition: " + e.toString());
        }
    }

    public void processEvent(String eventStr) {
        TcpEvent event = null;

        switch(eventStr) {
            case "PASSIVE": event = PASSIVE; break;
            case "ACTIVE": event = ACTIVE; break;
            case "SYN": event = SYN; break;
            case "SYNACK": event = SYNACK; break;
            case "ACK": event = ACK; break;
            case "RDATA": event = RDATA; break;
            case "SDATA": event = SDATA; break;
            case "FIN": event = FIN; break;
            case "CLOSE": event = CLOSE; break;
            case "TIMEOUT": event = TIMEOUT; break;
            default:
                System.out.println("Error: unexpected Event: " + eventStr);
                return;
        }

        try {
            fsm.doEvent(event);
        } catch (FsmException e) {
            System.out.println(e.toString());
        }
    }
}