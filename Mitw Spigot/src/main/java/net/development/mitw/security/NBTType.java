package net.development.mitw.security;

public enum NBTType
{
    String("String", 0, (byte)8), 
    Integer("Integer", 1, (byte)3), 
    Double("Double", 2, (byte)6), 
    Float("Float", 3, (byte)5), 
    Byte("Byte", 4, (byte)1), 
    ByteArray("ByteArray", 5, (byte)7), 
    Compound("Compound", 6, (byte)10), 
    IntArray("IntArray", 7, (byte)11), 
    Long("Long", 8, (byte)4), 
    Short("Short", 9, (byte)2), 
    End("End", 10, (byte)0), 
    List("List", 11, (byte)9);
    
    private final byte type;
    
    private NBTType(final String s, final int n, final byte type) {
        this.type = type;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public static NBTType getTypeById(final int id) {
        for (int i = 0; i < values().length; ++i) {
            if (id == values()[i].getType()) {
                return values()[i];
            }
        }
        return null;
    }
}
