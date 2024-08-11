package org.example;


public class OrderTrack {
    private int track;

    public OrderTrack() {
    }

    public OrderTrack(int track) {
        this.track = track;
    }

    public static OrderTrack from (int track){
        return new OrderTrack(track);
    }
    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }
}
