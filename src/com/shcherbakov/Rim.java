package com.shcherbakov;

class Rim {
    protected static String ConvertArabianToRoman(int result)
    {
        String rs = "";
        int unus = 0;
        if (result == 100) rs = "C";
        else if (result > 89 && result < 100) rs = "XC";
        else if (result == 50) rs = "L";
        else if (result > 39 && result < 50) rs = "XL";
        else if (result > 50 && result < 90)
        {
            rs = "L";
            int decem = (result-50)/10;
            unus = result - 50 - (decem*10);
            for (int i=0; i<decem; i++)
            {
                rs = rs + "X";
            }
        }
        else if (result > 9 && result < 40)
        {
            rs = "X";
            int decem = (result-10)/10;
            unus = result - 10 - (decem*10);
            for (int i=0; i<decem; i++)
            {
                rs = rs + "X";
            }
        }
        else if (result < 10) unus = result;

        switch(unus)
        {
            case 1:
                rs = rs + "I";
                break;
            case 2:
                rs = rs + "II";
                break;
            case 3:
                rs = rs + "III";
                break;
            case 4:
                rs = rs + "IV";
                break;
            case 5:
                rs = rs + "V";
                break;
            case 6:
                rs = rs + "VI";
                break;
            case 7:
                rs = rs + "VII";
                break;
            case 8:
                rs = rs + "VIII";
                break;
            case 9:
                rs = rs + "IX";
                break;
        }
        return rs;
    }


}
