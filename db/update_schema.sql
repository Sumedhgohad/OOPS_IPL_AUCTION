-- Update script to add team_players table
USE ipl_auction;

CREATE TABLE IF NOT EXISTS team_players (
  team_id INT NOT NULL,
  player_id INT NOT NULL,
  purchase_price DOUBLE NOT NULL,
  PRIMARY KEY (team_id, player_id),
  FOREIGN KEY (team_id) REFERENCES teams(id),
  FOREIGN KEY (player_id) REFERENCES players(id)
);

-- Reset players to AVAILABLE status (optional - for testing)
-- UPDATE players SET status = 'AVAILABLE';

-- Clear existing team_players data (optional - for testing)
-- DELETE FROM team_players;

-- Reset team budgets (optional - for testing)
-- UPDATE teams SET budget = 90000000 WHERE id = 1;
-- UPDATE teams SET budget = 85000000 WHERE id = 2;
-- UPDATE teams SET budget = 87000000 WHERE id = 3;
