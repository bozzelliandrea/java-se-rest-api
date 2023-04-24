package server.auth;

import database.model.Serializable;

public final class AuthRequest {

    public static class Register extends Login {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isValid() {
            return super.isValid() && email != null;
        }

        @Override
        public String serialize() {
            return super.serialize();
        }
    }

    public static class Login implements Serializable {
        protected String username;
        protected String password;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isValid() {
            return username != null && password != null;
        }

        @Override
        public String serialize() {
            return null;
        }
    }
}
