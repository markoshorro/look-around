package gal.udc.evilcorp.lookaround.model;

/**
 * Created by eloy on 16/04/2017.
 */

public class Event {

    private String id;

    private String name;

    private String description;

    private String place;


    public Event(String id, String name, String description, String place) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.place = place;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return "Nombre: '" + name + "\n" +
                "Local: '" + place;
    }
}
