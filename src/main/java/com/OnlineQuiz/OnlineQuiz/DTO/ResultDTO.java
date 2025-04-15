package com.OnlineQuiz.OnlineQuiz.DTO;

public class ResultDTO {
    private int total;
    private int score;
    private double percentage;

    public ResultDTO(int total, int score, double percentage) {
        this.total = total;
        this.score = score;
        this.percentage = percentage;
    }

    // Getters and Setters

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

}
