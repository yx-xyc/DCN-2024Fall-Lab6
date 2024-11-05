import Fsm.*;
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
    }

    private void initializeActions() {
        defaultAction = new TcpAction("default");
        dataAction = new TcpAction("data");
    }

    private void initializeFSM() {
        fsm = new FSM("Tcp Protocol", CLOSED);
    }

    private void addTransitions() {
        try {
            // Initial transitions
            fsm.addTransition(new Transition(CLOSED, PASSIVE, LISTEN, defaultAction));
            fsm.addTransition(new Transition(CLOSED, ACTIVE, SYN_SENT, defaultAction));

            // Three-way handshake
            fsm.addTransition(new Transition(LISTEN, SYN, SYN_RCVD, defaultAction));
            fsm.addTransition(new Transition(SYN_SENT, SYNACK, ESTABLISHED, defaultAction));
            fsm.addTransition(new Transition(SYN_RCVD, ACK, ESTABLISHED, defaultAction));

            // Data transfer in ESTABLISHED state
            fsm.addTransition(new Transition(ESTABLISHED, RDATA, ESTABLISHED, dataAction));
            fsm.addTransition(new Transition(ESTABLISHED, SDATA, ESTABLISHED, dataAction));

            // Connection termination
            fsm.addTransition(new Transition(ESTABLISHED, CLOSE, FIN_WAIT_1, defaultAction));
            fsm.addTransition(new Transition(ESTABLISHED, FIN, CLOSE_WAIT, defaultAction));
            fsm.addTransition(new Transition(FIN_WAIT_1, ACK, FIN_WAIT_2, defaultAction));
            fsm.addTransition(new Transition(FIN_WAIT_1, FIN, CLOSING, defaultAction));
            fsm.addTransition(new Transition(CLOSING, ACK, TIME_WAIT, defaultAction));
            fsm.addTransition(new Transition(FIN_WAIT_2, FIN, TIME_WAIT, defaultAction));
            fsm.addTransition(new Transition(CLOSE_WAIT, CLOSE, LAST_ACK, defaultAction));
            fsm.addTransition(new Transition(LAST_ACK, ACK, CLOSED, defaultAction));
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