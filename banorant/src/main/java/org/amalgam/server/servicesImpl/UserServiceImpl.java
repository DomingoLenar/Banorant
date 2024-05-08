package org.amalgam.server.servicesImpl;

import com.google.gson.*;
import org.amalgam.server.dataAccessLayer.DatabaseUtil;
import org.amalgam.server.dataAccessLayer.UserDAL;
import org.amalgam.utils.models.User;
import org.amalgam.utils.services.UserService;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserServiceImpl extends UnicastRemoteObject implements UserService, Serializable {
    private UserDAL userDAL;
    public UserServiceImpl() throws RemoteException {
        super();
        this.userDAL = new UserDAL();
    }


    @Override
    public boolean createUser(String username, String password) throws RemoteException {
        try(Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?,?)");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateUser(String username, String newPassword) throws RemoteException {
        try(Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE user SET password = ? WHERE username = ?");
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Admin will call this to delete a user
    // TODO:Custom Exception for role checking, not done in this case because this is just a crude prototype to show CRUD requests
    @Override
    public boolean deleteUser(String username) throws RemoteException {
        return userDAL.deleteUser(-1,username);
    }

    @Override
    public int getUserIdByUsername(String username) throws RemoteException {
        return userDAL.getUserIdByUsername(username);
    }

    @Override
    public List<User> getPlayers() throws RemoteException {
        return userDAL.getPlayers();
    }

    //Client will call this if they want to delete their account
    @Override
    public boolean requestUserDeletion(String username) throws RemoteException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String path = "banorant/src/main/resources/for_deletion.json";
        try(
                BufferedReader bufferedReader = new BufferedReader(new FileReader(path))
                ){
            JsonElement rootElement = JsonParser.parseReader(bufferedReader);
            JsonObject rootObject = rootElement.getAsJsonObject();
            JsonArray usersArray = rootObject.getAsJsonArray("users");

            JsonObject forDeletion = new JsonObject();
            forDeletion.addProperty("username",username);

            for(JsonElement element : usersArray){
                JsonObject currentObject = element.getAsJsonObject();
                if(currentObject.get("username").getAsString().equals(username)){
                    return false;
                }
            }

            usersArray.add(forDeletion);

            try(
                    FileWriter writer = new FileWriter(path)
                    ){
                gson.toJson(rootElement, writer);
                return true;
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Admin will call this to view the list of users for deletion
    @Override
    public LinkedList<String> fetchForDeletion() throws RemoteException {
        LinkedList<String> listOfForDeletion = new LinkedList<>();
        String path = "banorant/src/main/resources/for_deletion.json";
        try(
                BufferedReader bufferedReader = new BufferedReader(new FileReader(path))
                ){
            JsonElement rootElement = JsonParser.parseReader(bufferedReader);
            JsonObject rootObject = rootElement.getAsJsonObject();
            JsonArray usersArray = rootObject.getAsJsonArray("users");
            for(JsonElement element : usersArray){
                JsonObject currentObject = element.getAsJsonObject();
                listOfForDeletion.add(currentObject.get("username").getAsString());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return listOfForDeletion;
    }
}
