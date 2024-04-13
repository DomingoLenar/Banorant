package org.amalgam.utils.services;

import org.amalgam.server.dataAccessLayer.*;
import org.amalgam.utils.Status;
import org.amalgam.utils.models.Booking;
import org.amalgam.utils.models.Room;
import org.amalgam.utils.models.Session;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class CelebrityFanServiceImpl extends UnicastRemoteObject implements CelebrityFanService {
    private BookingDAL bookingDAL;
    private PaymentDAL paymentDAL;
    private RateDAL rateDAL;
    private RoomDAL roomDAL;
    private SessionDAL sessionDAL;
    public CelebrityFanServiceImpl() throws RemoteException {
        // inject instances of dal objects
        bookingDAL = new BookingDAL();
        paymentDAL = new PaymentDAL();
        rateDAL = new RateDAL();
        roomDAL = new RoomDAL();
        sessionDAL = new SessionDAL();
    }

    @Override
    public boolean registerAcceptedPayment(int fanID, int totalAmount, Status status, String paymentDate) throws RemoteException {
        return paymentDAL.registerAcceptedPayment(fanID, totalAmount, status, paymentDate);
    }

    @Override
    public double getPlayerRateByPlayerID(int playerID) throws RemoteException {
        return rateDAL.getPlayerRateByPlayerID(playerID);
    }

    @Override
    public List<Session> getAcceptedSession(int userID) throws RemoteException {
        return sessionDAL.getAcceptedSession(userID);
    }

    @Override
    public boolean registerNewSession(int fanID, String date, int duration) throws RemoteException {
        return sessionDAL.registerNewSession(fanID, date, duration);
    }

    @Override
    public Room getRoomByFanAndPlayer(int paymentID) throws RemoteException {
        return roomDAL.getRoomByFanAndPlayer(paymentID);
    }

    @Override
    public boolean registerNewRoom(String room_name, int paymentID, String date) throws RemoteException {
        return roomDAL.registerNewRoom(room_name,paymentID,date);
    }

    @Override
    public List<Booking> fetchBookingByPaymentID(int paymentID) throws RemoteException {
        return bookingDAL.fetchBookingByPaymentID(paymentID);
    }

    @Override
    public int getPaymentIDByUserID(int userID) throws RemoteException {
        return paymentDAL.getPaymentIDByUserID(userID);
    }

    @Override
    public boolean registerNewBooking(int userID, int sessionID, int roomID, int paymentID, String booking_date) throws RemoteException {
        return bookingDAL.registerNewBooking(userID, sessionID, roomID, paymentID, booking_date);
    }

    @Override
    public int getSessionIDByUserID(int userID) throws RemoteException {
       return sessionDAL.getSessionIDByUserID(userID);
    }

    @Override
    public int getRoomIDByPaymentID(int paymentID) throws RemoteException {
        return roomDAL.getRoomIDByPaymentID(paymentID);
    }

    @Override
    public Room getRoomByDate(String date) throws RemoteException {
        return roomDAL.getRoomByDate(date);
    }


}
