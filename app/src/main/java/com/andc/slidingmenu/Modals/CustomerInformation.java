package com.andc.slidingmenu.Modals;

/**
 * Created by win on 4/6/2015.
 */
public class CustomerInformation {
    public int branchCode;
    public long billIdentification;
    public String fabrikNumber;
    public String firstName;
    public String lastName;

    public CustomerInformation(){}

    public CustomerInformation(int subscriptionNumber, long billIdentification, String fabrikNumber, String firstName, String lastName) {
        this.branchCode = subscriptionNumber;
        this.billIdentification = billIdentification;
        this.fabrikNumber = fabrikNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
