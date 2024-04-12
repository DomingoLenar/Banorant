package org.amalgam.server.dataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentDAL {

    public boolean registerAcceptedPayment() { // payment processing is in client side but should be done by PSP third party
        return false;
    }
}
