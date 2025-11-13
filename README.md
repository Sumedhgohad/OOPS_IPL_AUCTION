# 🏏 IPL Auction Management System

An Object-Oriented Programming project implementing an IPL Auction Management System with MySQL database integration and AI chatbot support.

## 🎯 Features

- **Player Management**: View and manage cricket player profiles
- **Team Management**: Track teams, budgets, and squad composition
- **Live Auction**: Real-time bidding system with database persistence
- **Dashboard**: Overview of auction statistics and status
- **AI Chatbot**: Gemini-powered chatbot for auction queries
- **Database Integration**: Full JDBC implementation with MySQL

## 🔧 OOP Concepts Demonstrated

1. **Inheritance** - `Player` extends `Person` class
2. **Multiple Inheritance** - `Player` implements `Valuable` and `Auctionable` interfaces
3. **Polymorphism** - Method overriding and overloading
4. **Encapsulation** - Private fields with public getters/setters
5. **Abstraction** - Abstract `Person` class with abstract methods

## 📋 Prerequisites

- Java JDK 8 or higher
- MySQL Server (version 5.7+)
- MySQL Connector/J (included in `lib/` folder)
- Gemini API Key (for chatbot feature)

## 🚀 Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/Sumedhgohad/OOPS_IPL_AUCTION.git
cd OOPS_IPL_AUCTION
```

### 2. Database Setup

Run the following SQL commands in MySQL Workbench or command line:

```sql
CREATE DATABASE IF NOT EXISTS ipl_auction;
USE ipl_auction;

-- Run the schema file
source db/schema.sql;
```

Or manually run all commands from `db/schema.sql`

### 3. Create Configuration Files

Copy the example files and add your credentials:

```powershell
# Copy run script
Copy-Item run.ps1.example run.ps1

# Copy setup script
Copy-Item setup-auction-fix.ps1.example setup-auction-fix.ps1
```

Then edit `run.ps1` and replace:
- `YOUR_DATABASE_PASSWORD` with your MySQL root password
- `YOUR_GEMINI_API_KEY` with your Gemini API key

### 4. Compile the Project

```powershell
.\setup-auction-fix.ps1
```

Or manually:

```powershell
javac -d out -cp "lib\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0.jar" src\com\ipl\auction\model\*.java src\com\ipl\auction\db\*.java src\com\ipl\auction\dao\*.java src\com\ipl\auction\data\*.java src\com\ipl\auction\ui\*.java src\com\ipl\auction\chat\*.java src\com\ipl\auction\*.java
```

### 5. Run the Application

```powershell
.\run.ps1
```

Or manually:

```powershell
java -cp "out;lib\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0.jar" com.ipl.auction.AuctionApp
```

## 📁 Project Structure

```
OOPS_IPL_AUCTION/
├── src/
│   └── com/ipl/auction/
│       ├── model/          # Domain models (Player, Team, Person, etc.)
│       ├── dao/            # Database Access Objects
│       ├── db/             # Database connection management
│       ├── ui/             # Swing UI components
│       ├── chat/           # Gemini chatbot service
│       ├── data/           # Data loaders
│       └── AuctionApp.java # Main application entry point
├── db/
│   ├── schema.sql          # Database schema
│   └── update_schema.sql   # Schema updates
├── lib/
│   └── mysql-connector-j-9.5.0/ # MySQL JDBC driver
├── run.ps1.example         # Run script template
├── setup-auction-fix.ps1.example # Setup script template
└── README.md
```

## 🎮 How to Use

1. **Dashboard**: View auction overview and statistics
2. **Players Page**: Browse available players
3. **Teams Page**: View team budgets and squads
4. **Auction Page**: 
   - Select a player
   - Select a team
   - Enter bid amount (must be ≥ base price)
   - Click "Place Bid"
5. **Chatbot**: Ask questions about the auction

## 🗄️ Database Schema

### Tables

- **teams**: Team information and budgets
- **players**: Player profiles and auction status
- **team_players**: Junction table linking teams to purchased players

## 📚 Documentation

- `OOP_CONCEPTS_EXPLANATION.md` - Detailed OOP implementation guide
- `QUICK_REFERENCE.md` - Quick reference for OOP concepts
- `CLASS_DIAGRAM.md` - Visual class hierarchy
- `AUCTION_FIX_README.md` - Database update instructions

## 🔐 Security Note

**Never commit sensitive information!**

The following files are gitignored:
- `run.ps1` - Contains database credentials
- `setup-auction-fix.ps1` - Contains database credentials
- `*.env` files
- `out/` folder

Always use the `.example` files as templates.

## 🐛 Troubleshooting

### Database Connection Error
- Verify MySQL is running
- Check database credentials in `run.ps1`
- Ensure database exists: `CREATE DATABASE ipl_auction;`

### Compilation Errors
- Verify Java version: `java -version`
- Check classpath includes MySQL connector

### Auction Not Persisting
- Run `db/update_schema.sql` to create `team_players` table
- Check database connection in console output

## 👥 Contributors

- Anish
- Sumedh Gohad

## 📄 License

This project is created for educational purposes as part of an OOP course assignment.

## 🙏 Acknowledgments

- MySQL for database support
- Google Gemini API for chatbot functionality
- Java Swing for UI framework
