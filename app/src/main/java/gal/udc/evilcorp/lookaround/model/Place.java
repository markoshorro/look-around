package gal.udc.evilcorp.lookaround.model;

/**
 * Created by eloy on 11/04/2017.
 */

public class Place {

    private String id;
    private String name;
    private double lat;
    private double lng;

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

    public double getLat() { return lat; }

    public double getLng() { return lng; }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLat(double lat) { this.lat = lat; }

    public void setLng(double lng) { this.lng = lng; }

    @Override
    public String toString() {
        return "Place{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
