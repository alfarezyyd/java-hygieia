DROP DATABASE IF EXISTS java_hygieia;
CREATE DATABASE java_hygieia;
USE java_hygieia;

-- Tabel untuk menyimpan informasi nasionalitas
CREATE TABLE nationalities
(
    nationality_id INT PRIMARY KEY AUTO_INCREMENT,
    nationality    VARCHAR(100) NOT NULL UNIQUE
);

-- Tabel untuk menyimpan informasi klub
CREATE TABLE clubs
(
    club_id   INT PRIMARY KEY AUTO_INCREMENT,
    club_name VARCHAR(255) NOT NULL UNIQUE
);

-- Tabel untuk menyimpan informasi pemain
CREATE TABLE players
(
    player_id      INT PRIMARY KEY AUTO_INCREMENT,
    name           VARCHAR(255) NOT NULL,
    long_name      VARCHAR(255),
    photo_url      VARCHAR(255),
    player_url     VARCHAR(255),
    nationality_id INT,
    age            INT,
    ova            INT,
    pot            INT,
    club_id        INT,
    contract       VARCHAR(255),
    height         VARCHAR(10),
    weight         VARCHAR(10),
    preferred_foot VARCHAR(10),
    best_position  VARCHAR(10),
    joined         VARCHAR(100),
    loan_date_end  VARCHAR(100),
    value          VARCHAR(50),
    wage           VARCHAR(50),
    release_clause VARCHAR(50),
    total_stats    INT,
    base_stats     INT,
    w_f            VARCHAR(10),
    sm             VARCHAR(10),
    a_w            VARCHAR(10),
    d_w            VARCHAR(10),
    ir             VARCHAR(10),
    hits           INT,
    FOREIGN KEY (nationality_id) REFERENCES nationalities (nationality_id),
    FOREIGN KEY (club_id) REFERENCES clubs (club_id)
);

-- Tabel untuk menyimpan statistik pemain
CREATE TABLE statistics
(
    statistic_id     INT PRIMARY KEY AUTO_INCREMENT,
    player_id        INT,
    attacking        INT,
    crossing         INT,
    finishing        INT,
    heading_accuracy INT,
    short_passing    INT,
    volleys          INT,
    skill            INT,
    dribbling        INT,
    curve            INT,
    fk_accuracy      INT,
    long_passing     INT,
    ball_control     INT,
    movement         INT,
    acceleration     INT,
    sprint_speed     INT,
    agility          INT,
    reactions        INT,
    balance          INT,
    power            INT,
    shot_power       INT,
    jumping          INT,
    stamina          INT,
    strength         INT,
    long_shots       INT,
    mentality        INT,
    aggression       INT,
    interceptions    INT,
    positioning      INT,
    vision           INT,
    penalties        INT,
    composure        INT,
    defending        INT,
    marking          INT,
    standing_tackle  INT,
    sliding_tackle   INT,
    goalkeeping      INT,
    gk_diving        INT,
    gk_handling      INT,
    gk_kicking       INT,
    gk_positioning   INT,
    gk_reflexes      INT,
    FOREIGN KEY (player_id) REFERENCES players (player_id)
);

SELECT * FROM nationalities;
SELECT * FROM clubs;
SELECT * FROM players;
SELECT * FROM statistics;