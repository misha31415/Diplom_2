package praktikum.models;
import lombok.Data;

@Data
public class UserResponse {
    private boolean success;
    private UserData user;
    private String accessToken;
    private String refreshToken;
    private String message;

    @Data
    public static class UserData {
        private String email;
        private String name;
    }
}
