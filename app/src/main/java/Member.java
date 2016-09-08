import com.example.jakubkalinowski.contractfoxandroid.Address;

/**
 * Created by jakubkalinowski on 9/4/16.
 */
public class Member {

    /*
     TO DO:
     Adapt to the FIREBASE syntax
      */

    private String firstName;
    private String lastName;
    private String telNumber;
    private String email;
    private Boolean contractorOption;
    private String profilePicture;

    Address address = new Address();

    public Member(){}

    public Member(String firstName, String lastName, String telNumber, String email,
                  Boolean contractorOption, String profilePicture, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telNumber = telNumber;
        this.email = email;
        this.contractorOption = contractorOption;
        this.profilePicture = profilePicture;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getContractorOption() {
        return contractorOption;
    }

    public void setContractorOption(Boolean contractorOption) {
        this.contractorOption = contractorOption;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
