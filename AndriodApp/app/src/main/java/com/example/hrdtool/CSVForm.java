package com.example.hrdtool;

public class CSVForm {

    static class CSVFormRow {
        private String data_type;

        private String incident_date;

        private String attention_date;

        private String gender;

        private String age;

        private String details;


        CSVFormRow() {
            this.data_type = null;
            this.incident_date = null;
            this.attention_date = null;
            this.gender = null;
            this.age = null;
            this.details = null;
        }

        CSVFormRow(String data_type, String incident_date, String attention_date, String gender,
                   String age, String details) {
            this.data_type = data_type;
            this.incident_date = incident_date;
            this.attention_date = attention_date;
            this.gender = gender;
            this.age = age;
            this.details = details;
        }

        public String getData() {
            return data_type;
        }

        public void setData(String data_type) {
            this.data_type = data_type;
        }

        public String getIncident() {
            return incident_date;
        }

        public void setIncident(String incident_date) {
            this.incident_date = incident_date;
        }

        public String getAttention() {
            return attention_date;
        }

        public void setAttention(String attention_date) {
            this.attention_date = attention_date;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String toString() {
            String ret = "";
            ret += this.data_type + ", ";
            ret += this.incident_date + ", ";
            ret += this.attention_date + ", ";
            ret += this.gender + ", ";
            ret += this.age + ", ";
            ret += this.details + ".";
            return ret;
        }
    }

    static final String CITY = "DUBLIN";
    static final String COUNTRY = "IRELAND";

    static final String[] TYPES = {"Choose type of support needed", "Any", "Legal", "Medical", "Psychological"};
    static final String[] AREA = {"What area are you located in?", "Any", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10",
            "D11", "D12", "D13", "D14", "D15", "D16", "D17", "D18", "D19", "D20"};
    static final String[] SUPPORT = {"Choose type of support needed",
            "Any", "Legal", "Medical", "Psychological"};
    static final String[] GENDER = {"What is your gender?", "Male", "Female", "Other"};
    static final String[] AGE = {"Select Age Range", "0-9", "10-19", "20-29", "30-39", "40-49", "50+"};
    }
