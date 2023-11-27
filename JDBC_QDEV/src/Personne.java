import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Personne {
    private int id;
    private String nom;
    private String prenom;

    public Personne(String nom, String prenom){
        this.nom = nom;
        this.prenom = prenom;
        this.id = -1;
    }

    public void setId(int id){
        this.id = id;
    }

    public static ArrayList<Personne> findAll() {
        ArrayList<Personne> res = new ArrayList<>();
        try {
            Connection connect = DBConnection.getConnection();
            String query = "SELECT id, nom, prenom FROM personne";

            try (PreparedStatement preparedStatement = connect.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");

                    Personne personne = new Personne(nom, prenom);
                    personne.setId(id);
                    res.add(personne);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Personne findById(int id){
        Personne personne = null;
        String query = "SELECT id, nom, prenom FROM personne where id = ?";
        try (Connection connect = DBConnection.getConnection();
             PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                personne = new Personne(nom, prenom);
                personne.setId(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (personne!=null){
            return personne;
        }else{
            return null;
        }

    }

    public static List<Personne> findByName(String name){
        Personne personne = null;
        List<Personne> res = new ArrayList<>();
        String query = "SELECT * FROM personne where nom = ?";
        try (Connection connect = DBConnection.getConnection();
            PreparedStatement ps = connect.prepareStatement(query)){
            ps.setString(1, name);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                personne = new Personne(nom, prenom);
                personne.setId(id);
                res.add(personne);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}
