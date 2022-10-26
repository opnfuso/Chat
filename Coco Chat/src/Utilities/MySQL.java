package Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Danny
 */
public class MySQL {

    public Connection conexion = null;
    Statement comando = null;
    ResultSet registro;

    public Connection MySQLConnect() {

        try {
            //Driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Nombre del servidor. localhost:3306 es la ruta y el puerto de la conexión MySQL
            //panamahitek_text es el nombre que le dimos a la base de datos
            String servidor = "jdbc:mysql://localhost/coco_chat?serverTimezone=UTC";
            //El root es el nombre de usuario por default. No hay contraseña
            String usuario = "root";
            String pass = "";
            //Se inicia la conexión
            conexion = DriverManager.getConnection(servidor, usuario, pass);

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error en la conexión a la base de datos: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion = null;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error en la conexión a la base de datos: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion = null;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex, "Error en la conexión a la base de datos: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion = null;
        } finally {
//            JOptionPane.showMessageDialog(null, "Conexión Exitosa");
            return conexion;
        }
    }

    public void uploadMessage(String username, String receptor, String message) throws SQLException {
        System.out.println("\nUsuario: " + username + "\nReceptor: " + receptor);
        MySQLConnect();
        PreparedStatement preparedStatement = conexion
                .prepareStatement("SELECT idUser FROM user WHERE name = ?");
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        int idUser = rs.getInt("idUser");

        preparedStatement = conexion
                .prepareStatement("SELECT idUser FROM user WHERE name = ?");
        preparedStatement.setString(1, receptor);
        rs = preparedStatement.executeQuery();
        rs.next();
        int idUser2 = rs.getInt("idUser");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        preparedStatement = conexion
                .prepareStatement("insert into messagesfriends (message, timeStamp, idEmiter, idReceptor) VALUES (?, ?, ?, ?)");
        preparedStatement.setString(1, message);
        preparedStatement.setTimestamp(2, timestamp);
        preparedStatement.setInt(3, idUser);
        preparedStatement.setInt(4, idUser2);
        preparedStatement.executeUpdate();
        System.out.println("Mensaje enviado\n");
    }

    public void uploadGroupMessage(String username, String group, String message) throws SQLException {
        System.out.println("\nUsuario: " + username + "\nGrupo: " + group);
        MySQLConnect();
        PreparedStatement preparedStatement = conexion
                .prepareStatement("SELECT idUser FROM user WHERE name = ?");
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        int idUser = rs.getInt("idUser");

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        preparedStatement = conexion
                .prepareStatement("insert into messagesgroups (nombre, timeStamp, mensaje, emitter) VALUES (?, ?, ?, ?)");
        preparedStatement.setString(1, group);
        preparedStatement.setTimestamp(2, timestamp);
        preparedStatement.setString(3, message);
        preparedStatement.setString(4, username);
        preparedStatement.executeUpdate();
        System.out.println("Mensaje enviado\n");
    }

    public void switchStatus(String username, boolean status) throws SQLException {
        int idUser = getId(username);

        PreparedStatement preparedStatement = conexion
                .prepareStatement("SELECT * FROM user WHERE name = ?");
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();

        boolean online = rs.getBoolean("conected");
        online = !online;

        preparedStatement = conexion
                .prepareStatement("UPDATE user SET conected = ? WHERE idUser = ?");
        preparedStatement.setBoolean(1, online);
        preparedStatement.setInt(2, idUser);
        preparedStatement.executeUpdate();
    }

    public String[] getList(int situation, String user) throws SQLException {
        MySQLConnect();

        ArrayList resultList = new ArrayList();
        Statement st = conexion.createStatement();
        String statement = "";
        String element = "name";
        String intId = "";
        switch (situation) {
            case Codes.CONNECTED_LIST:
                statement = "SELECT name FROM user WHERE conected= 1";
                break;
            case Codes.DISCONNECTED_LIST:
                statement = "SELECT name FROM user WHERE conected= 0";
                break;
            case Codes.FRIENDS_LIST:
                Statement id = conexion.createStatement();
                Statement friendView = conexion.createStatement();
                ResultSet getId = id.executeQuery("SELECT idUser FROM user WHERE name= '" + user + "'");
                String code = "create or replace VIEW  amigos as SELECT user.idUser, user.name, friend.nickname, friend.idUser1 FROM user, friend WHERE user.idUser = friend.idUser2";
                friendView.executeUpdate(code);
                while (getId.next()) {
                    intId = getId.getString("idUser");
                }
                statement = "SELECT name FROM amigos WHERE idUser1= '" + intId + "'";
                friendView.closeOnCompletion();
                id.closeOnCompletion();
                break;
            case Codes.GROUP_LIST:
                statement = "SELECT *FROM groups";
                element = "nameGroup";
                break;
        }

        ResultSet result = st.executeQuery(statement);
        while (result.next()) {
            resultList.add(result.getString(element));
        }
        return (String[]) resultList.toArray(new String[resultList.size()]);
    }

    public String[] getGroupMessages(String name) throws SQLException {
        MySQLConnect();

        ArrayList result = new ArrayList();

        Statement st = conexion.createStatement();
        ResultSet messages = st.executeQuery("SELECT emitter, mensaje FROM messagesgroups WHERE nombre = '" + name + "'");

        while (messages.next()) {
            result.add(messages.getString("emitter") + ": " + messages.getString("mensaje"));
        }

        return (String[]) result.toArray(new String[result.size()]);
    }

    public String[] getMessages(String emitter, String receptor) throws SQLException {
        int emitterId = getId(emitter);
        int receptorId = getId(receptor);

        ArrayList result = new ArrayList();
        PreparedStatement preparedStatement = conexion
                .prepareStatement("SELECT * FROM messagesfriends WHERE (idEmiter = ? AND idReceptor = ?) OR (idEmiter = ? AND idReceptor = ?) ORDER BY timeStamp ASC");
        preparedStatement.setInt(1, emitterId);
        preparedStatement.setInt(2, receptorId);
        preparedStatement.setInt(3, receptorId);
        preparedStatement.setInt(4, emitterId);
        ResultSet messages = preparedStatement.executeQuery();

        while (messages.next()) {
            result.add(getName(messages.getInt("idEmiter")) + ": " + messages.getString("message"));
        }

        return (String[]) result.toArray(new String[result.size()]);
    }

    public int getId(String name) throws SQLException {
        MySQLConnect();
        PreparedStatement preparedStatement = conexion
                .prepareStatement("SELECT idUser FROM user WHERE name = ?");
        preparedStatement.setString(1, name);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        return rs.getInt("idUser");
    }

    public String getName(int id) throws SQLException {
        MySQLConnect();
        PreparedStatement preparedStatement = conexion
                .prepareStatement("SELECT name FROM user WHERE idUser = ?");
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        //System.out.println("En getName: " + rs.getString("name"));
        return rs.getString("name");
    }

    public void crearCuenta(String username, String password, String email) throws SQLException {
        MySQLConnect();
        PreparedStatement preparedStatement = conexion
                .prepareStatement("SELECT COUNT(*) AS rowcount FROM user");

        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        int count = rs.getInt("rowcount");
        count = count + 1;
        preparedStatement = conexion
                .prepareStatement("insert into user VALUES (?, ?, ?, ?, ?)");
        preparedStatement.setInt(1, count);
        preparedStatement.setString(2, username);
        preparedStatement.setString(3, email);
        preparedStatement.setString(4, password);
        preparedStatement.setInt(5, 1);
        preparedStatement.executeUpdate();
        System.out.println(" Usuario Registrado \n");

    }

    public boolean logIn(String username, String password) throws SQLException {
        MySQLConnect();
        PreparedStatement preparedStatement = conexion
                .prepareStatement("SELECT password FROM user WHERE name=?");
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();
        if (!rs.next()) {
            return false;
        }

        String contra = rs.getString("password");
        if (contra.equals(password)) {
            System.out.println(" Contraseña correcta");
            return true;
        } else {
            System.out.println(" Contraseña incorrecta");
            return false;
        }

    }
}
