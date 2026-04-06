# 🛒 E-Commerce Product Listing & Cart System

A full-stack **E-Commerce Web Application** that allows users to browse products, add items to cart, and manage purchases, while providing admin controls for product management.

---

## 🚀 Features

### 👤 User Authentication
- User Registration & Login
- Input validation (Email must contain `@gmail.com`)
- Secure session handling
- Logout functionality with proper redirection

### 🛍️ Product Catalog
- View all products with categories
- Product details page
- Search and filter functionality

### 🛒 Shopping Cart
- Add / Remove products
- Update product quantity
- View total price dynamically

### 🛠️ Admin Panel
- Admin login
- Add / Update / Delete products
- Manage product listings

---

## 🏗️ Tech Stack

| Layer        | Technology |
|-------------|------------|
| Frontend     | HTML, CSS, JavaScript |
| Backend      | Java |
| Database     | MySQL |
| Tools        | Git, GitHub |

---

## 📂 Project Structure
Ecommerce-System/
│
├── frontend/
│ ├── index.html
│ ├── login.html
│ ├── register.html
│ ├── cart.html
│ └── styles/
│ └── style.css
│
├── backend/
│ ├── controllers/
│ │ └── UserController.java
│ │ └── ProductController.java
│ │
│ ├── models/
│ │ └── User.java
│ │ └── Product.java
│ │
│ ├── services/
│ │ └── UserService.java
│ │ └── ProductService.java
│ │
│ └── database/
│ └── DBConnection.java
│
├── database/
│ └── schema.sql
│
├── README.md
└── .gitignore
