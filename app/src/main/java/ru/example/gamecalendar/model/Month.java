package ru.example.gamecalendar.model;

import androidx.annotation.NonNull;

public enum Month {
    january("january", 1, "Январь"),
    february("february", 2, "Февраль"),
    march("march", 3, "Март"),
    april("april", 4, "Апрель"),
    may("may", 5, "Май"),
    june("june", 6, "Июнь"),
    july("july", 7, "Июль"),
    august("august", 8, "Август"),
    september("september", 9, "Сентябрь"),
    october("october", 10, "Октябрь"),
    november("november", 11, "Ноябрь"),
    december("december", 12, "Декабрь");

    private String name;
    private int id;
    private String translate;

    Month(String name, int id, String translate) {
        this.name = name;
        this.id = id;
        this.translate = translate;
    }

    public int getId() {
        return id;
    }

    public String getTranslate() {
        return translate;
    }

    public static Month parse(String str){
        switch (str) {
            case "Январь":
                return Month.january;
            case "Февраль":
                return Month.february;
            case "Март":
                return Month.march;
            case "Апрель":
                return Month.april;
            case "Май":
                return Month.may;
            case "Июнь":
                return Month.june;
            case "Июль":
                return Month.july;
            case "Август":
                return Month.august;
            case "Сентябрь":
                return Month.september;
            case "Октябрь":
                return Month.october;
            case "Ноябрь":
                return Month.november;
            case "Декабрь":
                return Month.december;
            default:
                return null;
        }
    }

    public static Month equalsName(String str){
        return Month.valueOf(str);
    }

    @NonNull
    @Override
    public String toString() {
        return this.name();
    }
}
