package com.nijus.alino.bfwcoopmanagement.events;

public class LanguageChange {

    private String langName;

    public LanguageChange(String langName) {
        this.langName = langName;
    }

    public String getLangName() {
        return langName;
    }

    public void setLangName(String langName) {
        this.langName = langName;
    }
}
