import java.sql.*;
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
            String query = "SELECT id, nom, prenom FROM Personne";

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
        String query = "SELECT id, nom, prenom FROM Personne where id = ?";
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

    public static ArrayList<Personne> findByName(String name){
        Personne personne = null;
        ArrayList<Personne> res = new ArrayList<>();
        String query = "SELECT * FROM Personne where nom = ?";
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

    public String getPrenom() {
        return prenom;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public static void createTable(){
        String query = "CREATE TABLE `Personne` (\n" +
                "  `id` int(11) PRIMARY KEY NOT NULL,\n" +
                "  `nom` varchar(40) NOT NULL,\n" +
                "  `prenom` varchar(40) NOT NULL\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        String query2 = "ALTER TABLE `Personne`\n" +
                "  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;";
        try (Connection connect = DBConnection.getConnection();
             Statement st = connect.createStatement()){
                st.executeUpdate(query);
                st.executeUpdate(query2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteTable(){
        String query = "DROP TABLE IF EXISTS personne;";
        try (Connection connect = DBConnection.getConnection();
             Statement st = connect.createStatement()){
            st.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(){
        String query = "";
        if (this.id == -1){
            query = "INSERT INTO `Personne` (`nom`, `prenom`) VALUES (?, ?)";
        } else{
            query = "UPDATE Personne SET nom = ?, prenom = ? where id = ?";
        }

        try (Connection connect = DBConnection.getConnection();
             PreparedStatement st = connect.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            st.setString(1, this.getNom());
            st.setString(2, this.getPrenom());
            if (this.id != -1){
                st.setInt(3, this.id);
            }
            st.executeUpdate(query);
            ResultSet clefs = st.getGeneratedKeys();
            clefs.last();
            int id = clefs.getInt("id");
            this.setId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(){
        String query = "DELETE FROM Personne WHERE id=?";
        try (Connection connect = DBConnection.getConnection();
             PreparedStatement st = connect.prepareStatement(query)){
            st.setInt(1, this.getId());
            st.executeUpdate(query);
            this.setId(-1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
