package com.example.hrdtool;

import com.opencsv.CSVReader;

import java.util.ArrayList;

import static com.example.hrdtool.CSVForm.CITY;
import static com.example.hrdtool.CSVForm.COUNTRY;

public class CSVHelp {

    static class CSVHelpRow {

        private String name;
        private String type;
        private String address;
        private String telephone;
        private String email;
        private String area;

        CSVHelpRow() {
            this.name = null;
            this.type = null;
            this.address = null;
            this.telephone = null;
            this.email = null;
            this.area = null;
        }

        CSVHelpRow(String name, String type, String address,
                   String telephone, String email, String area) {
            this.name = name;
            this.type = type;
            this.address = address;
            this.telephone = telephone;
            this.email = email;
            this.area = area;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAddress() {
            return address;
        }

        /*
        Returns a more complete address with details of the city and country so that google maps
        can find the appropiate place. I assume that all the support is in Dublin, Ireland and
        therefore that information may not be present in the CSV. Without it, Maps can find the
        wrong place and needs the precision to be added. For the actual app, the city might
        not be fixed, but area will be known through the form and we can use that. The country,
        Nicaragua, is known and static, so the same assumption applies.
        */
        public String getCompleteAddress() {
            String fullAddress = this.address;
            if (!fullAddress.toLowerCase().contains(CITY.toLowerCase()))
                fullAddress += ", " + CITY.substring(0, 1).toUpperCase() + CITY.substring(1).toLowerCase();
            if (!fullAddress.toLowerCase().contains(COUNTRY.toLowerCase()))
                fullAddress += ", " + COUNTRY.substring(0, 1).toUpperCase() + COUNTRY.substring(1).toLowerCase();
            return fullAddress;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTelephone() {
            return telephone;
        }

        // Remove all non numeric character inside the telephone number to make it a phone link.
        public String getCleanTelephone() {
            return telephone.replaceAll("[^0-9]", "");
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String toString() {
            String ret = "";
            ret += this.name + ", ";
            ret += this.type + ", ";
            ret += this.address + ", ";
            ret += this.telephone + ", ";
            ret += this.email + ", ";
            ret += this.area + ".";
            return ret;
        }

    }

    static int index = 1;
    static ArrayList<CSVHelpRow> users = new ArrayList<CSVHelpRow>(0);
    static int length = 0;


    static final String[] HELP_HEADERS = {"ID","NAME","TYPE","ADDRESS","TELEPHONE","EMAIL","AREA"};
    static final int HELP_TYPE_COLUMN = 2;
    static final int HELP_AREA_COLUMN = 6;

    static ArrayList<CSVHelpRow> find_user_zone_type(CSVReader reader, String area, String type) {
        boolean headers = check_headers(reader);
        ArrayList<CSVHelpRow> list = new ArrayList<CSVHelpRow>();
        try {
            if(headers)
                reader.skip(1);
            String[] read = reader.readNext();
            while(read != null) {
                boolean add_to_list = true;
                if(!area.equalsIgnoreCase("any"))
                    if(!read[HELP_AREA_COLUMN].toLowerCase().contains(area.toLowerCase()))
                        add_to_list = false;
                if(!type.equalsIgnoreCase("any"))
                    if(!read[HELP_TYPE_COLUMN].toLowerCase().contains(type.toLowerCase()))
                        add_to_list = false;
                if(add_to_list) {
                    CSVHelpRow row = new CSVHelpRow(read[1],read[2],read[3],read[4],read[5],read[6]);
                    list.add(row);
                }
                read = reader.readNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    static boolean check_headers(CSVReader reader) {
        try {
            String[] first_line = reader.peek();
            for(int i=0; i<first_line.length; i++) {
                for(int j=0; j<HELP_HEADERS.length; j++) {
                    if(first_line[i].equalsIgnoreCase(HELP_HEADERS[j]))
                        return true;
                }
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}