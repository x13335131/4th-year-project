package com.example.louis.prototype;

/**
 * Created by louis on 17/01/2018.
 */

public class Med {

        String medID;
        String medName;
        int medDosage;
        String medDate;
        String userID;

        public Med(){//used while reading the values

        }

        public Med(String medID, String medName, int medDosage, String medDate, String userID) {
            this.medID = medID;
            this.medName = medName;
            this.medDosage = medDosage;
            this.medDate = medDate;
            this.userID = userID;
        }

        public String getMedID() {
            return medID;
        }
        public String getMedName() {
            return medName;
        }
        public int getMedDosage() {
        return medDosage;
    }
        public String getMedDate() {
            return medDate;
        }
        public String getUserID() {
            return userID;
        }


}
