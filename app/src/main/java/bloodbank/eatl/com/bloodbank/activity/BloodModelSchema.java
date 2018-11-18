package bloodbank.eatl.com.bloodbank.activity;

/**
 * Created by Adil on 2/5/2018.
 */

public class BloodModelSchema {
    private String Area,Available,Blood_Group,Mobile,Name,Email;

    public BloodModelSchema(String area, String available, String blood_Group, String mobile, String name, String email) {
        Area = area;
        Available = available;
        Blood_Group = blood_Group;
        Mobile = mobile;
        Name = name;
        Email = email;
    }
    public BloodModelSchema(){

    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getAvailable() {
        return Available;
    }

    public void setAvailable(String available) {
        Available = available;
    }

    public String getBlood_Group() {
        return Blood_Group;
    }

    public void setBlood_Group(String blood_Group) {
        Blood_Group = blood_Group;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
