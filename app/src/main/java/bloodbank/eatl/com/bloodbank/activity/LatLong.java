package bloodbank.eatl.com.bloodbank.activity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ASUS on 27-Aug-17.
 */

@JsonIgnoreProperties (ignoreUnknown = true)
public class LatLong {

    @JsonProperty ("latitude")
    double latitude;

    @JsonProperty ("longitude")
    double longitude;

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
}
