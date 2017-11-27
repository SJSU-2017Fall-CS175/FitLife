package nityam.com.myapplication;

/**
 * A class representing all the fields
 * associated with a user in the app
 */

public class User {

    private long mId;
    private String mName;
    private String mGender;
    private float mWeight;

    public User(long id, String name, String gender, float weight) {
        mId = id;
        mName = name;
        mGender = gender;
        mWeight = weight;
    }

    public User() {

    }

    public long getmId() {
        return mId;
    }

    public void setmId(long Id) {
        this.mId = Id;
    }

    public String getmName() {
        return mName;
    }

    public String getmGender() {
        return mGender;
    }

    public float getmWeight() {
        return mWeight;
    }

    public void setmName(String name) {
        mName = name;
    }

    public void setmGender(String gender) {
        mGender = gender;
    }

    public void setmWeight(float weight) {
        mWeight = weight;
    }

    public String toString() {
        return "Username: " + getmName() + "\n" +
                "Gender: " + getmGender() + "\n" +
                "Weight: " + getmWeight();
    }

}
