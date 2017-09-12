package com.andc.slidingmenu.Utility;

/**
 * Created by y.jafari on 6/6/2015.
 */
public class NationalCode {
    public static int COD_SIZE = 10;
    public static boolean checkNationalCode(String code) {
        if(code.length() != 10)
            return false ;
        if ( validateNum(code) == false)
            return false;
        if (code.equals("0000000000") || code.equals("1111111111") || code.equals("2222222222") || code.equals("3333333333") ||
            code.equals("4444444444") || code.equals("5555555555") || code.equals("6666666666") || code.equals("7777777777") ||
            code.equals("8888888888") || code.equals("9999999999"))
            return false;
        int[] X = tokenizeCodeToArray(code);
        int N = calculateN(X);
        int R =  (N % 11);
        int resultC;
        switch (R) {
            case 0:
                resultC = 0;
                break;
            case 1:
                resultC = 1;
                break;
            default:
                resultC = 11 - R;
                break;
        }
        if (resultC == X[COD_SIZE - 1])
        {
            return true;
        }
        else
        {
            return false;
        }

    }
    private static int[] tokenizeCodeToArray(String code) {
        char[] charList = code.toCharArray();
        int[] result = new int[10];
        for (int i = 0; i < charList.length; i++) {
            result[i] = Integer.parseInt(String.valueOf(charList[i]));
        }
        return result;
    }
    private static int calculateN(int[] l) {
        int n = 0;
        for (int ii = 0; ii < COD_SIZE - 1; ii++)
        {
            n += l[ii] * (COD_SIZE - ii);
        }
        return n;
    }
    private static boolean validateNum(String code)
    {
        char[] charList = code.toCharArray();
        for (int jj = 0; jj < charList.length; jj++)
        {

            switch (charList[jj])
            {
                case '0':

                    break;
                case '1':

                    break;
                case '2':

                    break;
                case '3':

                    break;
                case '4':

                    break;
                case '5':

                    break;
                case '6':

                    break;
                case '7':

                    break;
                case '8':

                    break;
                case '9':

                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
