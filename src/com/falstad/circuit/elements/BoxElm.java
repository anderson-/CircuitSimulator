package com.falstad.circuit.elements;

import com.falstad.circuit.EditInfo;
import com.falstad.circuit.GraphicElm;
import java.awt.*;
import java.util.StringTokenizer;
import java.util.Vector;

public class BoxElm extends GraphicElm {

    public BoxElm(int xx, int yy) {
        super(xx, yy);
        x2 = xx + 16;
        y2 = yy + 16;
        setBbox(x, y, x2, y2);
    }

    public BoxElm(int xa, int ya, int xb, int yb, int f,
            StringTokenizer st) {
        super(xa, ya, xb, yb, f);
        x2 = xb;
        y2 = yb;
        /*	if ( st.hasMoreTokens() )
         x = new Integer(st.nextToken()).intValue();
         if ( st.hasMoreTokens() )
         y = new Integer(st.nextToken()).intValue();
         if ( st.hasMoreTokens() )
         x2 = new Integer(st.nextToken()).intValue();
         if ( st.hasMoreTokens() )
         y2 = new Integer(st.nextToken()).intValue();*/
        setBbox(x, y, x2, y2);
    }

    public String dump() {
        return super.dump();
    }

    public int getDumpType() {
        return 'b';
    }

    public void drag(int xx, int yy) {
        x = xx;
        y = yy;
    }

    public void draw(Graphics g) {
        //g.setColor(needsHighlight() ? selectColor : lightGrayColor);
        g.setColor(needsHighlight() ? selectColor : Color.GRAY);
        setBbox(x, y, x2, y2);
        if (x < x2 && y < y2) {
            g.fillRect(x, y, x2 - x, y2 - y);
        } else if (x > x2 && y < y2) {
            g.fillRect(x2, y, x - x2, y2 - y);
        } else if (x < x2 && y > y2) {
            g.fillRect(x, y2, x2 - x, y - y2);
        } else {
            g.fillRect(x2, y2, x - x2, y - y2);
        }
    }

    public EditInfo getEditInfo(int n) {
        return null;
    }

    public void setEditValue(int n, EditInfo ei) {
    }

    public void getInfo(String arr[]) {
    }

    @Override
    public int getShortcut() {
        return 0;
    }
}
