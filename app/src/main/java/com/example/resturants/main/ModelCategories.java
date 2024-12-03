package com.example.resturants.main;

import java.io.Serializable;



public class ModelCategories implements Serializable {
    //La sérialisation est le processus de conversion d'un objet Java en un flux d'octets (bytes),
    // qui peut être stocké ou transmis (par exemple, dans un fichier ou à travers un réseau).

    int iIcon;
    String strName;

    public ModelCategories(int iIcon, String strName) {
        this.iIcon = iIcon;
        this.strName = strName;
    }

    public int getiIcon() {
        return iIcon;
    }

    public void setiIcon(int iIcon) {
        this.iIcon = iIcon;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }
}
