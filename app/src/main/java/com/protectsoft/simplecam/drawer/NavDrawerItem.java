package com.protectsoft.simplecam.drawer;

/**
 */
public class NavDrawerItem {

    private String title;
    private String subtitle;
    private int icon;
    private int chooseicon = 0;


    public NavDrawerItem(String title){
        this.title = title;
    }

    public NavDrawerItem(String title,int icon) {
        this.subtitle = title;
        this.icon = icon;
    }

    public NavDrawerItem(String title,String subtitle,int icon) {
        this.title = title;
        this.subtitle = subtitle;
        this.icon = icon;
    }

    public NavDrawerItem(String title,String subtitle,int icon,int chooseIcon) {
        this.title = title;
        this.subtitle = subtitle;
        this.icon = icon;
        this.chooseicon = chooseIcon;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getChooseicon(){return chooseicon;}

    public void setChooseicon(int ico){chooseicon = ico;}

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSubtitle() {
        if(subtitle == null) {
            return "";
        }
        return subtitle;
    }

}
