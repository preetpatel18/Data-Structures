package singly;

public class TrainCar {
    public TrainCar next;
    public String name;

    public TrainCar getNext() {
        return next;
    }

    public String getName() {
        return name;
    }

    public void setNext(TrainCar nxt) {
        next = nxt;
    }

    public void setName(String nme) {
        name = nme;
    }
}
