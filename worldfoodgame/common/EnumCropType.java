package common;

public enum EnumCropType
{
  WHEAT
  {
    public String toString() {return "Wheat";}
  },
  
  
  RICE  
  {
    public String toString() {return "Rice";}
  },
  
  CORN  
  {
    public String toString() {return "Corn";}
  },
  
  SOY  
  {
    public String toString() {return "Soy";}
  },
  
  OTHER_CROPS
  {
    public String toString() {return "Other Crops";}
  };
  
  public static final int SIZE = values().length;
}
