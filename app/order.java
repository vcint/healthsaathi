import java.util.Date;
public class order {
    public class Order {
        private String imageUrl;
        private String userId;
        private String dateOfBirth;


        // Required empty constructor (Firebase Realtime Database needs this)
        public Order() {
            // Default constructor required for calls to DataSnapshot.getValue(Order.class)
        }

        // Constructor with parameters
        public Order(String userId, String dateOfBirth, String imageUrl) {
            this.userId = userId;

            this.dateOfBirth = dateOfBirth;
            this.imageUrl = imageUrl;
        }

        // Getters and setters for all fields
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }


        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
