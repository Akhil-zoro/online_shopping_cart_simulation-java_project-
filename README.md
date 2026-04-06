🛒 E-Commerce Product Listing and Cart System

📌 Project Overview

This is a Java + MySQL console-based e-commerce system that simulates real-world platforms like Amazon.

It includes:

- Authentication
- Product browsing
- Cart with reservation
- Favorites
- Address management
- Checkout & billing

---

🚀 Key Features

👤 Authentication

🔹 Register & Login (Auth.java)

if (!email.matches("^[A-Za-z0-9+_.-]+@gmail\\.com$")) {
    System.out.println("❌ Invalid email!");
    return;
}

✔ Validates email
✔ Stores user in DB
✔ Maintains session

---

🛍️ Product Display

🔹 Smart Stock Display (ProductByCategory.java)

if (available <= 0) {
    status = "❌ Out of Stock";
} else if (available <= 5) {
    status = "⚠ Only few left";
} else {
    status = "✔ In Stock";
}

✔ Hides exact stock
✔ Shows availability like Amazon

---

🔍 Search Feature

String sql = "SELECT * FROM product WHERE LOWER(name) LIKE LOWER(?)";
ps.setString(1, "%" + keyword + "%");

✔ Case-insensitive search

---

🛒 Cart System

🔹 Add to Cart

String insertSql = 
"INSERT INTO cart(product_id, quantity, user_id, reserved_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

✔ Tracks user cart
✔ Stores reservation time

---

⏳ Reservation System

AND c.reserved_at > NOW() - INTERVAL ? MINUTE

✔ Prevents stock overuse
✔ Temporary hold system

---

❤️ Favorites

INSERT INTO favorites(user_id, product_id)

✔ Add/remove favorites
✔ Move to cart

---

📍 Address Management

🔹 Validation

if (!phone.matches("\\d{10}")) {
    System.out.println("❌ Phone must be 10 digits!");
}

if (!pincode.matches("\\d{6}")) {
    System.out.println("❌ Pincode must be 6 digits!");
}

✔ Clean user data
✔ Multiple addresses supported

---

🚚 Checkout System

🔹 Address Selection

int addressId = AddressManager.selectAddress();
String userPin = AddressManager.getPincode(addressId);

✔ Valid address required
✔ Supports adding new address

---

🔹 Billing Calculation

double discount = (subtotal >= 50000) ? subtotal * 0.10 : 0;
double gst = (subtotal - discount) * 0.05;
double finalAmount = subtotal - discount + gst;

✔ Discount logic
✔ GST applied

---

📦 Order Processing

con.setAutoCommit(false);
con.commit();

✔ Transaction safe
✔ Prevents data loss

---

👤 Account Section

Includes:

- Order History
- Favorites
- Address Management
- Change Password
- Logout/Login

---

🔐 Admin Features

- Add Product
- Update Product
- Delete Product
- Reset System

---

🗂️ Project Structure

MainMenu.java
-Auth.java
-UserSession.java
-CategoryMenu.java
-ProductByCategory.java
-AddToCart.java
-ViewCart.java
Billing.java
OrderHistory.java
Favorites.java
AddressManager.java
AdminReset.java
DBConnection.java
ReservationConfig.java

---

🛠️ Technologies Used

- Java (Core)
- MySQL
- JDBC

---

⚙️ How to Run

Compile

javac -cp ".;mysql-connector-j-9.6.0.jar" *.java

Run

java -cp ".;mysql-connector-j-9.6.0.jar" MainMenu

---

🗄️ Database Tables

- users
- product
- cart
- address
- favorites
- order_history

---

🔥 Special Highlights

- ✔ Real-time stock reservation
- ✔ Amazon-style stock display
- ✔ Input validation (email, phone, pincode)
- ✔ Address-based delivery check
- ✔ Transaction-safe checkout

---

🎯 Conclusion

This project successfully simulates a real-world e-commerce system by integrating backend logic, database operations, and user interaction, providing a strong foundation for scalable application development.

---

🙌 Thank You
