package org.amalgam.utils.services;

import org.amalgam.utils.Status;
import org.amalgam.utils.models.Booking;
import org.amalgam.utils.models.Room;
import org.amalgam.utils.models.Session;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CelebrityFanService extends Remote {
    boolean getUserCredentials(int userID) throws RemoteException;
    boolean registerAcceptedPayment(int fanID, int totalAmount, Status status, String paymentDate) throws RemoteException;
    double getPlayerRateByPlayerID(int playerID) throws RemoteException;
    List<Session> getAcceptedSession(int userID) throws RemoteException;
    boolean registerNewSession(int fanID, String date, int duration) throws RemoteException;
    Room getRoomByFanAndPlayer(int paymentID) throws RemoteException;
    boolean registerNewRoom(String room_name, int paymentID, String date) throws RemoteException;
    List<Booking> fetchBookingByPaymentID(int paymentID) throws RemoteException;
    int getPaymentIDByUserID(int userID) throws RemoteException;
    boolean registerNewBooking(int userID, int sessionID, int roomID, int paymentID, String booking_date) throws RemoteException;
    public int getSessionIDByUserID (int userID) throws RemoteException;

    public int getRoomIDByPaymentID (int paymentID) throws RemoteException;
    Room getRoomByDate(String date) throws RemoteException;
}
