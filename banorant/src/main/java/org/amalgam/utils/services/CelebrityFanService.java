package org.amalgam.utils.services;

import org.amalgam.utils.Status;
import org.amalgam.utils.models.Booking;
import org.amalgam.utils.models.Room;
import org.amalgam.utils.models.Session;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface CelebrityFanService extends Remote {
    boolean registerAcceptedPayment(int fanID, int totalAmount, Status status, String paymentDate) throws RemoteException;
    double getPlayerRateByPlayerID(int playerID) throws RemoteException;
    List<Session> getAcceptedSession(int userID) throws RemoteException;
    boolean registerNewSession(int fanID, String date, int duration, boolean isCelebrity) throws RemoteException;
    Room getRoomByFanAndPlayer(int paymentID) throws RemoteException;
    boolean registerNewRoom(String room_name, int paymentID) throws RemoteException;
    List<Booking> fetchBookingByPaymentID(int paymentID) throws RemoteException;
}
