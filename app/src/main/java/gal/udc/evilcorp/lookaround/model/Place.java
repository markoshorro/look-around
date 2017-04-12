package gal.udc.evilcorp.lookaround.model;

/**
 * Created by eloy on 11/04/2017.
 */

public class Place {

    private String id;
    private String name;

    public Place(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
