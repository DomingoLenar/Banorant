package org.amalgam.utils.services;

import org.amalgam.utils.Status;
import org.amalgam.utils.models.Availability;
import org.amalgam.utils.models.Booking;
import org.amalgam.utils.models.Session;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

// todo: create methods to be used by clients
public interface CelebrityFanService extends Remote {
    boolean getUserCredentials(int userID) throws RemoteException;
    boolean registerAcceptedPayment(int fanID, int totalAmount, Status status, String paymentDate) throws RemoteException;
    List<Session> getAcceptedSession(int userID) throws RemoteException;
    boolean registerNewSession(int fanID, String date, int duration) throws RemoteException;
    List<Booking> fetchBookingByPaymentID(int paymentID) throws RemoteException;
    int getPaymentIDByUserID(int userID) throws RemoteException;
    boolean registerNewBooking(int userID, int sessionID, int availabilityID, int paymentID, String booking_date) throws RemoteException;
    public int getSessionIDByUserID (int userID) throws RemoteException;

    public List<Availability> getAvailabilityByUserID(int userId) throws RemoteException;
}
