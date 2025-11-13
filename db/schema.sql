-- MySQL schema for IPL Auction
CREATE DATABASE IF NOT EXISTS ipl_auction;
USE ipl_auction;

CREATE TABLE IF NOT EXISTS teams (
  id INT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  budget DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS players (
  id INT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  country VARCHAR(60) NOT NULL,
  role ENUM('BATSMAN','BOWLER','ALL_ROUNDER','WICKETKEEPER') NOT NULL,
  base_price DOUBLE NOT NULL,
  status ENUM('AVAILABLE','SOLD','UNSOLD') DEFAULT 'AVAILABLE'
);

CREATE TABLE IF NOT EXISTS team_players (
  team_id INT NOT NULL,
  player_id INT NOT NULL,
  purchase_price DOUBLE NOT NULL,
  PRIMARY KEY (team_id, player_id),
  FOREIGN KEY (team_id) REFERENCES teams(id),
  FOREIGN KEY (player_id) REFERENCES players(id)
);

-- Sample data (optional)
INSERT INTO teams (id, name, budget) VALUES
  (1, 'Mumbai Indians', 90000000),
  (2, 'Chennai Super Kings', 85000000),
  (3, 'Royal Challengers Bangalore', 87000000)
ON DUPLICATE KEY UPDATE name=VALUES(name), budget=VALUES(budget);

INSERT INTO players (id, name, country, role, base_price, status) VALUES
  (1, 'Virat Kohli', 'India', 'BATSMAN', 20000000, 'AVAILABLE'),
  (2, 'Rohit Sharma', 'India', 'BATSMAN', 18000000, 'AVAILABLE'),
  (3, 'Jasprit Bumrah', 'India', 'BOWLER', 15000000, 'AVAILABLE'),
  (4, 'Hardik Pandya', 'India', 'ALL_ROUNDER', 16000000, 'AVAILABLE'),
  (5, 'MS Dhoni', 'India', 'WICKETKEEPER', 10000000, 'AVAILABLE'),
  (6, 'Pat Cummins', 'Australia', 'BOWLER', 12000000, 'AVAILABLE'),
  (7, 'Rashid Khan', 'Afghanistan', 'BOWLER', 14000000, 'AVAILABLE'),
  (8, 'Ben Stokes', 'England', 'ALL_ROUNDER', 13000000, 'AVAILABLE')
ON DUPLICATE KEY UPDATE name=VALUES(name), country=VALUES(country), role=VALUES(role), base_price=VALUES(base_price), status=VALUES(status);


