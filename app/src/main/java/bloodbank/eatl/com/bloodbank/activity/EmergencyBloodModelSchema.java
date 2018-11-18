package bloodbank.eatl.com.bloodbank.activity;

/**
 * Created by Adil on 2/21/2018.
 */

public class EmergencyBloodModelSchema {
    private String Name,Location,Phone,Emergency_BloodGroup;

    public EmergencyBloodModelSchema(String name, String location, String phone, String emergency_BloodGroup) {
        Name = name;
        Location = location;
        Phone = phone;
        Emergency_BloodGroup = emergency_BloodGroup;
    }
    public EmergencyBloodModelSchema(){

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmergency_BloodGroup() {
        return Emergency_BloodGroup;
    }

    public void setEmergency_BloodGroup(String emergency_BloodGroup) {
        Emergency_BloodGroup = emergency_BloodGroup;
    }
}
