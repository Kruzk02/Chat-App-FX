package com.chatapp.DAO;

import com.chatapp.Database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    private final Logger logger = LoggerFactory.getLogger(MessageDAO.class);

    public int saveMessage(Message message){

        String sql = "INSERT INTO messages(sender,receiver,content) VALUES(?,?,?)";
        try(Connection connection = Database.getDBConnection();
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            statement.setString(1,message.getSender());
            statement.setString(2,message.getReceiver());
            statement.setString(3,message.getContent());
            statement.executeUpdate();

            try(ResultSet resultSet = statement.getGeneratedKeys()){
                if(resultSet.next()) return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("ERROR AT SAVE SEND: "+e.getMessage());
        }
        return 0;
    }
    public List<Message> getMessages(String sender,String receiver){
        List<Message> messages = new ArrayList<>();
        String sql = "Select * from messages WHERE sender =? AND receiver =?";

        try(Connection connection = Database.getDBConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,sender);
            statement.setString(2,receiver);

            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    Message message = new Message();
                    message.setId(resultSet.getInt("id"));
                    message.setSender(resultSet.getString("sender"));
                    message.setReceiver(resultSet.getString("receiver"));
                    message.setContent(resultSet.getString("content"));

                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR AT GET MESSAGE: "+e.getMessage());
        }
        return messages;
    }
}
