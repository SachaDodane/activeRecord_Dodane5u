import java.sql.*;

public class Film {
    private String titre;
    private int id;
    private int id_real;

    public Film(String titre, Personne real) {
        this.titre = titre;
        this.id = -1;
        this.id_real = real.getId();
    }

    private Film(String titre, int id, int id_real) {
        this.id = id;
        this.id_real = id_real;
        this.titre = titre;
    }

    public Film findById(int id) {
        Film res = null;
        String query = "SELECT * FROM Film WHERE id=?";
        try (Connection connect = DBConnection.getConnection();
             PreparedStatement st = connect.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, id);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                res = new Film(resultSet.getString("titre"), resultSet.getInt("id"), resultSet.getInt("id_real"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}
