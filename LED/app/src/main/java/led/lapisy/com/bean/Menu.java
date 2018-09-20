package led.lapisy.com.bean;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2018/4/12
 * Desc  :
 */

public class Menu {
    public String name;
    public int icon;
    public boolean isSelected;

    public Menu() {

    }

    public Menu(String pName, int pIcon) {
        this.name = pName;
        this.icon = pIcon;
    }
}
