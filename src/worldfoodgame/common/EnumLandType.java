package worldfoodgame.common;

public enum EnumLandType
{
    WHEAT
    {
      public boolean isCultivated() {return true;};
      public EnumCropType getCropType() {return EnumCropType.WHEAT;};
    },
    
    RICE  
    {
      public boolean isCultivated() {return true;};
      public EnumCropType getCropType() {return EnumCropType.RICE;};
    },
    
    CORN  
    {
      public boolean isCultivated() {return true;};
      public EnumCropType getCropType() {return EnumCropType.CORN;};
    },
    
    SOY  
    {
      public boolean isCultivated() {return true;};
      public EnumCropType getCropType() {return EnumCropType.SOY;};
    },
    
    OTHER_CROPS
    {
      public boolean isCultivated() {return true;};
      public EnumCropType getCropType() {return EnumCropType.OTHER_CROPS;};
    }, 
    
    UNCULTIVATED_ARABLE  
    {
      public boolean isCultivated() {return false;};
      public EnumCropType getCropType() {return null;};
    },
    
    NONARABLE
    {
      public boolean isCultivated() {return false;};
      public EnumCropType getCropType() {return null;};
    }; 
    

    
    public static final int SIZE = values().length;
    public abstract boolean isCultivated();
    public abstract EnumCropType getCropType();
    
}
