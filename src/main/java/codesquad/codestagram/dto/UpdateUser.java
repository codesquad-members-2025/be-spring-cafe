package codesquad.codestagram.dto;

public class UpdateUser {
    private String currentPassword;
    private String newPassword;
    private String name;
    private String email;

    public UpdateUser(String currentPassword, String newPassword, String name, String email) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.name = name;
        this.email = email;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
