/*    */
package net.development.mitw.utils.holograms.touch;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */ public enum TouchAction
        /*    */ {
    /* 32 */   RIGHT_CLICK,
    /* 33 */   LEFT_CLICK,
    /* 34 */   UNKNOWN;

    /*    */
    /*    */
    TouchAction() {
    }

    /* 37 */
    public static TouchAction fromUseAction(Object useAction) {
        if (useAction == null) {
            return UNKNOWN;
            /*    */
        }
        /* 39 */
        int i = ((Enum) useAction).ordinal();
        /* 40 */
        switch (i) {
            /*    */
            case 0:
                /* 42 */
                return RIGHT_CLICK;
            /*    */
            case 1:
                /* 44 */
                return LEFT_CLICK;
            /*    */
        }
        /*    */
        /*    */
        /* 48 */
        return UNKNOWN;
        /*    */
    }
    /*    */
}


/* Location:              C:\Users\msi\Downloads\HologramAPI_v1.6.2.jar!\de\inventivegames\hologram\touch\TouchAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */