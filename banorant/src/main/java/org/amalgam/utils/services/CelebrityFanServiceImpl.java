package org.amalgam.utils.services;

import org.amalgam.server.dataAccessLayer.*;
import org.amalgam.utils.Status;
import org.amalgam.utils.models.Availability;
import org.amalgam.utils.models.Booking;
import org.amalgam.utils.models.Session;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class CelebrityFanServiceImpl extends UnicastRemoteObject implements CelebrityFanService {
    private BookingDAL bookingDAL;
    private PaymentDAL paymentDAL;
    private SessionDAL sessionDAL;
    private AvailabilityDAL availabilityDAL;
    public CelebrityFanServiceImpl() throws RemoteException {
        // inject instances of dal objects
        bookingDAL = new BookingDAL();
        paymentDAL = new PaymentDAL();
        sessionDAL = new SessionDAL();
        availabilityDAL = new AvailabilityDAL();
    }


    @Override
    public boolean registerAcceptedPayment(int fanID, int totalAmount, Status status, String paymentDate) throws RemoteException {
        return paymentDAL.registerAcceptedPayment(fanID, totalAmount, status, paymentDate);
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
    public List<Availability> getAvailabilityByUserID(int userId) throws RemoteException {
        return  availabilityDAL.getAvailabilityByUserID(userId);
    }

    @Override
    public boolean createAvailability(Availability availability) throws RemoteException {
        return availabilityDAL.createAvailability(availability);
    }

    @Override
    public boolean updateAvailability(Availability availability) throws RemoteException {
        return availabilityDAL.updateAvailability(availability);
    }

    @Override
    public Availability getAvailabilityByID(int availabilityID) throws RemoteException {
        return availabilityDAL.getAvailabilityByID(availabilityID);
    }

    @Override
    public int getRateByUserID(int userID) throws RemoteException {
        return availabilityDAL.getRateByUserID(userID);
    }

    @Override
    public void updateStartingTime(String updatedStartTime, int availabilityID) throws RemoteException {
        availabilityDAL.updateStartingTime(updatedStartTime, availabilityID);
    }


}
