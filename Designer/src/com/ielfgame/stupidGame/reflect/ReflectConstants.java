package com.ielfgame.stupidGame.reflect;

public interface ReflectConstants {
	//reflect------------------------------  
    public static final int FACE_GET_SHIFT = 1 << 0;
    public static final int FACE_SET_SHIFT = 1 << 1;
    public static final int FACE_ALL_SHIFT = 1 << 2;
    
    public static final int XML_GET_SHIFT = 1 << 3;
    public static final int XML_SET_SHIFT = 1 << 4;
    public static final int XML_ALL_SHIFT = 1 << 5;
    
    public static final int DROP_RESID_SHIFT = 1 << 6;
    public static final int COPY_SHIFT = 1 << 7;
    public static final int PASTE_SHIFT = 1 << 8;
    
    public static final int UNDO_SHIFT = 1<<9;
    
    public static final int DEFAULT_SHIFT = FACE_ALL_SHIFT | XML_ALL_SHIFT | PASTE_SHIFT | COPY_SHIFT | UNDO_SHIFT;

    public static final int DEFAULT_FACE_SHIFT = FACE_ALL_SHIFT | PASTE_SHIFT | COPY_SHIFT | UNDO_SHIFT; 
    
    //背后需要运行的方法
    public static final int BACKGROUND_SHIFT = 1 << 10;
}
