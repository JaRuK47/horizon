package com.futsal.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private int number;

    private String position;

    private int goals;

    private int games;

    private String photoYellow;
    private String photoWhiteBlack;
    private String photoBlackWhite;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "team_type")
    private int teamType;

    @Column(name = "birth_date")
    private String birthDate;

    private int assists;

    @Column(name = "yellow_cards")
    private int yellowCards;

    @Column(name = "red_cards")
    private int redCards;

    @Column(length = 10000)
    private String content;

    public Player() {}

    public Player(String firstName, String lastName, int number, String position, int teamType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.position = position;
        this.teamType = teamType;
        this.goals = 0;
        this.games = 0;
        this.assists = 0;
        this.yellowCards = 0;
        this.redCards = 0;
        this.birthDate = null;
        this.photoUrl = null;
        this.content = null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public int getGoals() { return goals; }
    public void setGoals(int goals) { this.goals = goals; }

    public int getGames() { return games; }
    public void setGames(int games) { this.games = games; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public int getTeamType() { return teamType; }
    public void setTeamType(int teamType) { this.teamType = teamType; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public int getAssists() { return assists; }
    public void setAssists(int assists) { this.assists = assists; }

    public int getYellowCards() { return yellowCards; }
    public void setYellowCards(int yellowCards) { this.yellowCards = yellowCards; }

    public int getRedCards() { return redCards; }
    public void setRedCards(int redCards) { this.redCards = redCards; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getPhotoYellow() { return photoYellow; }
    public void setPhotoYellow(String photoYellow) { this.photoYellow = photoYellow; }
    public String getPhotoWhiteBlack() { return photoWhiteBlack; }
    public void setPhotoWhiteBlack(String photoWhiteBlack) { this.photoWhiteBlack = photoWhiteBlack; }
    public String getPhotoBlackWhite() { return photoBlackWhite; }
    public void setPhotoBlackWhite(String photoBlackWhite) { this.photoBlackWhite = photoBlackWhite; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getAge() {
        if (birthDate == null || birthDate.isEmpty()) {
            return 0;
        }
        try {
            String[] parts = birthDate.split("\\.");
            if (parts.length == 3) {
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                LocalDate birth = LocalDate.of(year, month, day);
                return Period.between(birth, LocalDate.now()).getYears();
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    public String getPhotoByTheme(String theme) {
        String photo = null;
        if ("yellow-black".equals(theme)) {
            photo = photoYellow;
        } else if ("white-black".equals(theme)) {
            photo = photoWhiteBlack;
        } else if ("black-white".equals(theme)) {
            photo = photoBlackWhite;
        }
        if (photo != null && !photo.isEmpty()) return photo;
        if (photoWhiteBlack != null && !photoWhiteBlack.isEmpty()) return photoWhiteBlack;
        if (photoYellow != null && !photoYellow.isEmpty()) return photoYellow;
        if (photoBlackWhite != null && !photoBlackWhite.isEmpty()) return photoBlackWhite;
        return "/images/Logo.jpg";
    }

    public String getAgeWord() {
        int age = getAge();
        if (age == 0) return "лет";
        int lastDigit = age % 10;
        int lastTwoDigits = age % 100;
        if (lastTwoDigits >= 11 && lastTwoDigits <= 19) return "лет";
        if (lastDigit == 1) return "год";
        if (lastDigit >= 2 && lastDigit <= 4) return "года";
        return "лет";
    }

    public String getShortContent() {
        if (content == null || content.isEmpty()) {
            return "Нажмите для просмотра подробной информации";
        }
        if (content.length() <= 100) {
            return content;
        }
        return content.substring(0, 100) + "...";
    }
}