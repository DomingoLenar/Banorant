package org.amalgam.server.dataAccessLayer;

import org.amalgam.utils.models.Session;

import java.time.LocalDate;
import java.util.List;

public class SessionDAL {

    public boolean registerNewSession(int fanID, int playerID, LocalDate date, int duration){ // insert new session if payment is accepted -> fan
        return false;
    }
    public List<Session> getAcceptedSession(){ // list of session where payment status is accepted -> celebrity
        return null;
    }
}
