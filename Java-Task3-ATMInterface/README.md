# 🏧 ATM Interface Simulation System (Java Console Application)

A robust, fully interactive, object-oriented ATM Interface simulation built using Core Java. This project was developed as part of the **Java Development Internship at Oasis Infobyte (Task 3)**.

The system simulates real-world banking operations including secure user authentication, balance inquiries, funds deposit, account withdrawals, inter-account transfers, and dynamic transaction history tracking.

---

## ✨ Key Features & Highlights

• **Secure Authentication:** User ID and PIN verification system to prevent unauthorized account access.  
• **Object-Oriented Architecture:** Pure OOP design featuring clean separation of concerns across `User`, `Account`, `Transaction`, `ATM`, and `Main` components.  
• **Dynamic Transaction History:** Automatically records timestamps, action types (Deposit, Withdrawal, Transfer), and amounts for every session activity.  
• **Real-Time Balance Updates:** Instant balance reflection and validation logic for overdrawing or negative amount inputs.  
• **Inter-Account Transfers:** Simulates smooth monetary transfers between primary and recipient user accounts.  
• **Input Safety & Robustness:** Handled edge-cases and invalid numeric entries to prevent runtime exceptions.

---

## 🛠️ System Architecture & Class Hierarchy

| Class | Responsibility |
| :--- | :--- |
| `User` | Encapsulates User ID, User PIN, and associated account profile. |
| `Account` | Handles core financial calculations (Balance, Deposit, Withdraw, Transfer). |
| `Transaction` | Stores ledger entries (Timestamp, Operation Type, Amount). |
| `ATM` | Manages CLI menu interactions, user inputs, and session routing. |
| `Main` | Initializes mock entities and boots the application sequence. |

---

## 🔑 Demo Login Credentials

For testing and demonstration purposes, use the following pre-configured credentials:

* **User ID:** `khuram123`
* **User PIN:** `2026`
* **Recipient User ID (for Transfer demo):** `user_target`

---

## 🚀 How to Run locally

1. **Clone the Repository:**
   ```bash
   git clone [https://github.com/KhuramSohail56/OIBSIP.git](https://github.com/KhuramSohail56/OIBSIP.git)
