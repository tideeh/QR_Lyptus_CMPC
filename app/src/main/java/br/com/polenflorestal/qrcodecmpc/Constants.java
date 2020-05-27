package br.com.polenflorestal.qrcodecmpc;


public final class Constants {

    // Nome da empresa que vai usar o APP
    static String EMPRESA_NOME = "CMPC";

    // sharedPreferences nome
    static String SP_NOME = "qrlyptus_cmpc_prefs";
    static String SP_KEY_VERSION_CODE = "version_code";

    // defaults values (quando nao encontra o valor pedido)
    static String DEFAULT_STRING_VALUE = "-1";
    static long DEFAULT_LONG_VALUE = 0;
    static int DEFAULT_INT_VALUE = 0;

    static String QR_DATA = "data";
    static String QR_IS_TOOLBAR_SHOW = "toolbar_show";
    static String QR_TOOLBAR_DRAWABLE_ID = "toolbar_drawable_id";
    static String QR_TOOLBAR_TEXT = "toolbar_text";
    static String QR_TOOLBAR_BACKGROUND_COLOR = "toolbar_background_color";
    static String QR_TOOLBAR_TEXT_COLOR = "toolbar_text_color";
    static String QR_CAMERA_MARGIN_LEFT = "camera_margin_left";
    static String QR_CAMERA_MARGIN_TOP = "camera_margin_top";
    static String QR_CAMERA_MARGIN_RIGHT = "camera_margin_right";
    static String QR_CAMERA_MARGIN_BOTTOM = "camera_margin_bottom";
    static String QR_BACKGROUND_COLOR = "background_color";
    static String QR_CAMERA_BORDER = "CAMERA_BORDER";
    static String QR_CAMERA_BORDER_COLOR = "CAMERA_BORDER_COLOR";
    static String QR_IS_SCAN_BAR = "IS_SCAN_BAR";
    static String QR_IS_BEEP = "IS_BEEP";
    static String QR_BEEP_RESOURCE_ID = "BEEP_RESOURCE_ID";
    static int QR_QR_SCANNER_REQUEST = 1;
}
