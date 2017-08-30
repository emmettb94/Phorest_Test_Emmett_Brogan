package com.example.emmett.phorest_test_emmett_brogan;

/**
 * Created by Emmett Brogan on 29/08/2017.
 */

public class ClientModel
{
    private String clientId;
    private String firstName;
    private String lastName;
    private String email;
    private int mobile;
    private int landLine;

//    public ClientModel(String clientId, String firstName, String lastName, int mobile, int landLine, String email)
//    {
//        this.clientId = clientId;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.mobile = mobile;
//        this.landLine = landLine;
//        this.email = email;
//    }

    public String getClientId() {
        return clientId;
    }


    public void setClientId(String clientId) {
        this.clientId = clientId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public int getLandLine() {
        return landLine;
    }

    public void setLandLine(int landLine) {
        this.landLine = landLine;
    }

}


