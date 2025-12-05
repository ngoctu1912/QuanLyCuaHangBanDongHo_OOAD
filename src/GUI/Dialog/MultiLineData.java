package GUI.Dialog;

public class MultiLineData {
    private String Line1;
    private String Line2;
    private String Line3;
    
    public MultiLineData(){
    }
    public MultiLineData(String line1, String line2 , String line3){
        Line1 = line1;
        Line2 = line2;
        Line3 = line3;
    }
    public String getLine1() {
        return Line1;
    }
    public void setLine1(String line1) {
        Line1 = line1;
    }
    public String getLine2() {
        return Line2;
    }
    public void setLine2(String line2) {
        Line2 = line2;
    }
    public String getLine3() {
        return Line3;
    }
    public void setLine3(String line3) {
        Line3 = line3;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((Line1 == null) ? 0 : Line1.hashCode());
        result = prime * result + ((Line2 == null) ? 0 : Line2.hashCode());
        result = prime * result + ((Line3 == null) ? 0 : Line3.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MultiLineData other = (MultiLineData) obj;
        if (Line1 == null) {
            if (other.Line1 != null)
                return false;
        } else if (!Line1.equals(other.Line1))
            return false;
        if (Line2 == null) {
            if (other.Line2 != null)
                return false;
        } else if (!Line2.equals(other.Line2))
            return false;
        if (Line3 == null) {
            if (other.Line3 != null)
                return false;
        } else if (!Line3.equals(other.Line3))
            return false;
        return true;
    }
    
    

    
    
}
