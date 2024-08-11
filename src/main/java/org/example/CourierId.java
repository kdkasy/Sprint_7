package org.example;

public class CourierId {
    private String id;

    public CourierId() {
    }

    public CourierId(String id) {
        this.id = id;
    }

    public static CourierId from (int courierId) {
        return new CourierId(String.valueOf(courierId));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
