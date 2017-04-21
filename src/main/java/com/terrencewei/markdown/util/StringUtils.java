package com.terrencewei.markdown.util;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by terrencewei on 2017/04/21.
 */
public class StringUtils {

    private static int[]    sHexNybbleToDecimal  = createHexNybbleToDecimal();
    private static int[]    sOctalDigitToDecimal = createOctalDigitToDecimal();
    private static char[]   sEscapedCharToChar   = createEscapedCharToChar();
    private static char[]   sDecimalToHexNybble  = createDecimalToHexNybble();
    private static String[] sCharToEscapedString = createCharToEscapedString();



    public StringUtils() {
    }



    public static int numOccurrences(String pString, char pDelimiter) {
        int ret = 0;
        int pos = 0;

        int ix;
        for (int len = pString.length(); pos < len; pos = ix + 1) {
            ix = pString.indexOf(pDelimiter, pos);
            if (ix < 0) {
                break;
            }

            ++ret;
        }

        return ret;
    }



    public static int numOccurrences(String pString, String pDelimiter) {
        int ret = 0;
        int pos = 0;

        int ix;
        for (int len = pString.length(); pos < len; pos = ix + 1) {
            ix = pString.indexOf(pDelimiter, pos);
            if (ix < 0) {
                break;
            }

            ++ret;
        }

        return ret;
    }



    public static int numOccurrences(String pString, char pDelimiter, char pEscape) {
        if (pString.indexOf(pEscape) < 0) {
            return numOccurrences(pString, pDelimiter);
        } else {
            int numDelimiters = 0;
            int pos = 0;
            int len = pString.length();
            StringUtils.SplitterState state = StringUtils.SplitterState.NORMAL;

            while (pos < len) {
                char ch = pString.charAt(pos++);
                switch (state.ordinal()) {
                case 1:
                    if (ch == pEscape) {
                        state = StringUtils.SplitterState.ESCAPE;
                    } else if (ch == pDelimiter) {
                        ++numDelimiters;
                    }
                    break;
                case 2:
                    state = StringUtils.SplitterState.NORMAL;
                }
            }

            return numDelimiters;
        }
    }



    public static int splitStringAtCharacter(String pString, char pDelimiter, String[] pStrings, int pStart) {
        int numStrings = 0;
        int outIndex = pStart;
        int pos = 0;

        int ix;
        for (int len = pString.length(); pos < len && outIndex < pStrings.length; pos = ix + 1) {
            ix = pString.indexOf(pDelimiter, pos);
            if (ix < 0) {
                ix = len;
            }

            pStrings[outIndex] = pString.substring(pos, ix);
            ++numStrings;
            ++outIndex;
        }

        return numStrings;
    }



    public static int splitStringAtCharacter(String pString, char pDelimiter, char pEscape, String[] pStrings,
            int pStart) {
        if (pString.indexOf(pEscape) < 0) {
            return splitStringAtCharacter(pString, pDelimiter, pStrings, pStart);
        } else {
            int numStrings = 0;
            int outIndex = pStart;
            int pos = 0;
            int len = pString.length();
            StringUtils.SplitterState state = StringUtils.SplitterState.NORMAL;
            StringBuilder sb = new StringBuilder();
            StringBuilder escapeSequence = new StringBuilder();

            while (pos < len && outIndex < pStrings.length) {
                char ch = pString.charAt(pos++);
                switch (state.ordinal()) {
                case 1:
                    if (ch == pEscape) {
                        escapeSequence.append(ch);
                        state = StringUtils.SplitterState.ESCAPE;
                    } else if (ch == pDelimiter) {
                        pStrings[outIndex++] = sb.toString();
                        ++numStrings;
                        sb.setLength(0);
                    } else {
                        sb.append(ch);
                    }
                    break;
                case 2:
                    if (ch == pDelimiter) {
                        sb.append(ch);
                    } else {
                        sb.append(escapeSequence);
                        sb.append(ch);
                    }

                    escapeSequence.setLength(0);
                    state = StringUtils.SplitterState.NORMAL;
                }
            }

            if (escapeSequence.length() > 0) {
                sb.append(escapeSequence);
            }

            if (sb.length() > 0 && outIndex < pStrings.length) {
                pStrings[outIndex++] = sb.toString();
                ++numStrings;
            }

            return numStrings;
        }
    }



    public static int splitStringAtString(String pString, String pDelimiter, String[] pStrings, int pStart) {
        int numStrings = 0;
        int outIndex = pStart;
        int pos = 0;
        int len = pString.length();

        int ix;
        for (int delimLen = pDelimiter.length(); pos < len && outIndex < pStrings.length; pos = ix + delimLen) {
            ix = pString.indexOf(pDelimiter, pos);
            if (ix < 0) {
                ix = len;
            }

            pStrings[outIndex] = pString.substring(pos, ix);
            ++numStrings;
            ++outIndex;
        }

        return numStrings;
    }



    public static String[] splitStringAtCharacter(String pString, char pDelimiter) {
        String[] ret = new String[numOccurrences(pString, pDelimiter) + 1];
        int n = splitStringAtCharacter(pString, pDelimiter, ret, 0);
        if (n >= ret.length) {
            return ret;
        } else {
            String[] newRet = new String[n];

            for (int i = 0; i < n; ++i) {
                newRet[i] = ret[i];
            }

            return newRet;
        }
    }



    public static String[] splitStringAtCharacter(String pString, char pDelimiter, char pEscape) {
        String[] ret = new String[numOccurrences(pString, pDelimiter, pEscape) + 1];
        int n = splitStringAtCharacter(pString, pDelimiter, pEscape, ret, 0);
        if (n >= ret.length) {
            return ret;
        } else {
            String[] newRet = new String[n];

            for (int i = 0; i < n; ++i) {
                newRet[i] = ret[i];
            }

            return newRet;
        }
    }



    public static String[] splitStringAtString(String pString, String pDelimiter) {
        String[] ret = new String[numOccurrences(pString, pDelimiter) + 1];
        int n = splitStringAtString(pString, pDelimiter, ret, 0);
        if (n >= ret.length) {
            return ret;
        } else {
            String[] newRet = new String[n];

            for (int i = 0; i < n; ++i) {
                newRet[i] = ret[i];
            }

            return newRet;
        }
    }



    public static int splitStringAtCharacterWithQuoting(String pString, char pDelimiter, String[] pStrings,
            int pStart) {
        int numStrings = 0;
        int outIndex = pStart;
        int pos = 0;

        int ix;
        for (int len = pString.length(); pos < len && outIndex < pStrings.length; pos = ix + 1) {
            ix = pString.indexOf(pDelimiter, pos);
            String split = null;
            if (ix < 0) {
                ix = len;
                split = pString.substring(pos);
            } else {
                for (split = pString.substring(pos, ix); ix >= 0 && ix + 1 < len
                        && pString.charAt(ix + 1) == pDelimiter; split = split + pDelimiter
                                + pString.substring(pos, ix)) {
                    pos = ix + 2;
                    ix = pString.indexOf(pDelimiter, pos);
                    if (ix < 0) {
                        ix = len;
                    }
                }
            }

            pStrings[outIndex] = split;
            ++numStrings;
            ++outIndex;
        }

        return numStrings;
    }



    public static String[] splitStringAtCharacterWithQuoting(String pString, char pDelimiter) {
        String[] ret = new String[numOccurrences(pString, pDelimiter) + 1];
        int n = splitStringAtCharacterWithQuoting(pString, pDelimiter, ret, 0);
        if (n >= ret.length) {
            return ret;
        } else {
            String[] newRet = new String[n];

            for (int i = 0; i < n; ++i) {
                newRet[i] = ret[i];
            }

            return newRet;
        }
    }



    public static String replace(String pSrc, char pFrom, String pTo) {
        StringBuilder result = null;
        int start = 0;

        int ind;
        while ((ind = pSrc.indexOf(pFrom, start)) != -1) {
            String tmp = pSrc.substring(start, ind);
            start = ind + 1;
            if (result == null) {
                result = new StringBuilder(pTo.length());
            }

            result.append(tmp);
            result.append(pTo);
        }

        if (result == null) {
            return pSrc;
        } else {
            result = result.append(pSrc.substring(start));
            return result.toString();
        }
    }



    public static String replace(String pSrc, String pFrom, String pTo) {
        StringBuilder result = null;
        int start = 0;
        if (pFrom.equals("")) {
            return pSrc;
        } else {
            int ind;
            while ((ind = pSrc.indexOf(pFrom, start)) != -1) {
                String tmp = pSrc.substring(start, ind);
                start = ind + pFrom.length();
                if (result == null) {
                    result = new StringBuilder(pTo.length());
                }

                result.append(tmp);
                result.append(pTo);
            }

            if (result == null) {
                return pSrc;
            } else {
                result = result.append(pSrc.substring(start));
                return result.toString();
            }
        }
    }



    public static String toUpperCase(String pSrc) {
        int len = pSrc.length();

        for (int i = 0; i < len; ++i) {
            char c = pSrc.charAt(i);
            char z = Character.toUpperCase(c);
            if (c != z) {
                char[] buf = new char[len];

                int j;
                for (j = 0; j < i; ++j) {
                    buf[j] = pSrc.charAt(j);
                }

                for (buf[j++] = z; j < len; ++j) {
                    buf[j] = Character.toUpperCase(pSrc.charAt(j));
                }

                return new String(buf, 0, len);
            }
        }

        return pSrc;
    }



    public static String toLowerCase(String pSrc) {
        int len = pSrc.length();

        for (int i = 0; i < len; ++i) {
            char c = pSrc.charAt(i);
            char z = Character.toLowerCase(c);
            if (c != z) {
                char[] buf = new char[len];

                int j;
                for (j = 0; j < i; ++j) {
                    buf[j] = pSrc.charAt(j);
                }

                for (buf[j++] = z; j < len; ++j) {
                    buf[j] = Character.toLowerCase(pSrc.charAt(j));
                }

                String rtn = new String(buf, 0, len);
                return rtn;
            }
        }

        return pSrc;
    }



    public static boolean arrayEquals(String[] a, String[] b) {
        if (a != null && b != null) {
            if (a.length != b.length) {
                return false;
            } else {
                for (int n = 0; n < a.length; ++n) {
                    if (!a[n].equals(b[n])) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return a == null && b == null;
        }
    }



    public static boolean arrayEqualsIgnoreCase(String[] a, String[] b) {
        if (a != null && b != null) {
            if (a.length != b.length) {
                return false;
            } else {
                for (int n = 0; n < a.length; ++n) {
                    if (!a[n].equalsIgnoreCase(b[n])) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return a == null && b == null;
        }
    }



    public static String removeCharacters(String iString, String iFilter) {
        int length = iString.length();
        StringBuilder buffer = new StringBuilder(length);

        for (int i = 0; i < length; ++i) {
            char c = iString.charAt(i);
            if (iFilter.indexOf(c) == -1) {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }



    public static String removeWhiteSpace(String iString) {
        int length = iString.length();
        StringBuilder buffer = new StringBuilder(length);

        for (int i = 0; i < length; ++i) {
            char c = iString.charAt(i);
            if (!Character.isWhitespace(c)) {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }



    public static String removeCRLF(String iString) {
        char cReturn = 13;
        char cLine = 10;
        int length = iString.length();
        StringBuilder buffer = new StringBuilder(length);

        for (int i = 0; i < length; ++i) {
            char c = iString.charAt(i);
            if (c != cReturn && c != cLine) {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }



    public static String alphaNumOnly(String iString) {
        int length = iString.length();
        StringBuilder buffer = new StringBuilder(length);

        for (int i = 0; i < length; ++i) {
            char c = iString.charAt(i);
            if (c >= 65 && c <= 122) {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }



    public static String alphaNumericOnly(String pStr) {
        int length = pStr.length();
        StringBuilder buffer = new StringBuilder(length);

        for (int i = 0; i < length; ++i) {
            char c = pStr.charAt(i);
            if (c >= 48 && c <= 57 || c >= 65 && c <= 90 || c >= 97 && c <= 122) {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }



    public static boolean isAlphaNumOnly(String iString) {
        int length = iString.length();

        for (int i = 0; i < length; ++i) {
            char c = iString.charAt(i);
            if ((c < 97 || c > 122) && (c < 65 || c > 90) && (c < 48 || c > 57) && c != 95 && c != 45 && c != 32) {
                return false;
            }
        }

        return true;
    }



    public static String replaceSubstrings(String pString, String[] pReplacements) {
        int length = pString.length();
        StringBuilder buffer = new StringBuilder(length);

        for (int i = 0; i < length; ++i) {
            int c;
            for (c = 0; c < pReplacements.length; c += 2) {
                if (pString.regionMatches(i, pReplacements[c], 0, pReplacements[c].length())) {
                    buffer.append(pReplacements[c + 1]);
                    i += pReplacements[c].length() - 1;
                    break;
                }
            }

            if (c >= pReplacements.length) {
                buffer.append(pString.charAt(i));
            }
        }

        return buffer.toString();
    }



    public static String replaceCharacters(String pString, char[] pReplacements) {
        int length = pString.length();
        StringBuilder buffer = new StringBuilder(length);

        for (int index = 0; index < length; ++index) {
            char character = pString.charAt(index);

            int pair;
            for (pair = 0; pair < pReplacements.length; pair += 2) {
                if (character == pReplacements[pair]) {
                    buffer.append(pReplacements[pair + 1]);
                    break;
                }
            }

            if (pair >= pReplacements.length) {
                buffer.append(character);
            }
        }

        return buffer.toString();
    }



    public static Vector tokenizer(String pSrc, String pToken) {
        if (pSrc == null) {
            return null;
        } else {
            Vector v = null;

            for (StringTokenizer st = new StringTokenizer(pSrc, pToken); st.hasMoreTokens(); v
                    .addElement(st.nextToken().trim())) {
                if (v == null) {
                    v = new Vector();
                }
            }

            return v;
        }
    }



    public static String removeExtension(String pSrc) {
        int index;
        return (index = pSrc.lastIndexOf(46)) < 0 ? pSrc : pSrc.substring(0, index);
    }



    public static String makeList(Enumeration pElements, String pSeparator) {
        StringBuilder buf = makeList(pElements, pSeparator, new StringBuilder());
        return buf.toString();
    }



    public static StringBuffer makeList(Enumeration pElements, String pSeparator, StringBuffer pResult) {
        if (pElements != null) {
            while (pElements.hasMoreElements()) {
                Object next = pElements.nextElement();
                pResult.append(next.toString());
                if (pSeparator != null && pElements.hasMoreElements()) {
                    pResult.append(pSeparator);
                }
            }
        }

        return pResult;
    }



    public static StringBuilder makeList(Enumeration pElements, String pSeparator, StringBuilder pResult) {
        if (pElements != null) {
            while (pElements.hasMoreElements()) {
                Object next = pElements.nextElement();
                pResult.append(next.toString());
                if (pSeparator != null && pElements.hasMoreElements()) {
                    pResult.append(pSeparator);
                }
            }
        }

        return pResult;
    }



    public static String makeList(Enumeration pElements, String pFormat, String pSeparator) {
        StringBuilder buf = makeList(pElements, pFormat, pSeparator, new StringBuilder());
        return buf.toString();
    }



    public static StringBuffer makeList(Enumeration pElements, String pFormat, String pSeparator,
            StringBuffer pResult) {
        if (pElements != null) {
            MessageFormat fmt = new MessageFormat(pFormat);
            Object[] args = new Object[1];

            while (pElements.hasMoreElements()) {
                args[0] = pElements.nextElement();
                pResult.append(fmt.format(args));
                if (pSeparator != null && pElements.hasMoreElements()) {
                    pResult.append(pSeparator);
                }
            }
        }

        return pResult;
    }



    public static StringBuilder makeList(Enumeration pElements, String pFormat, String pSeparator,
            StringBuilder pResult) {
        if (pElements != null) {
            MessageFormat fmt = new MessageFormat(pFormat);
            Object[] args = new Object[1];

            while (pElements.hasMoreElements()) {
                args[0] = pElements.nextElement();
                pResult.append(fmt.format(args));
                if (pSeparator != null && pElements.hasMoreElements()) {
                    pResult.append(pSeparator);
                }
            }
        }

        return pResult;
    }



    public static String makeList(Object[] pElements, String pSeparator) {
        StringBuilder buf = makeList(pElements, pSeparator, new StringBuilder());
        return buf.toString();
    }



    public static StringBuffer makeList(Object[] pElements, String pSeparator, StringBuffer pResult) {
        if (pElements != null) {
            int n = 0;

            for (int limit = pElements.length; n < limit; ++n) {
                Object next = pElements[n];
                pResult.append(next.toString());
                if (pSeparator != null && n < limit - 1) {
                    pResult.append(pSeparator);
                }
            }
        }

        return pResult;
    }



    public static StringBuilder makeList(Object[] pElements, String pSeparator, StringBuilder pResult) {
        if (pElements != null) {
            int n = 0;

            for (int limit = pElements.length; n < limit; ++n) {
                Object next = pElements[n];
                pResult.append(next == null ? String.valueOf(next) : next.toString());
                if (pSeparator != null && n < limit - 1) {
                    pResult.append(pSeparator);
                }
            }
        }

        return pResult;
    }



    public static String makeList(Object[] pElements, String pFormat, String pSeparator) {
        StringBuilder buf = makeList(pElements, pFormat, pSeparator, new StringBuilder());
        return buf.toString();
    }



    public static StringBuffer makeList(Object[] pElements, String pFormat, String pSeparator, StringBuffer pResult) {
        if (pElements != null) {
            MessageFormat fmt = new MessageFormat(pFormat);
            Object[] args = new Object[1];
            int n = 0;

            for (int limit = pElements.length; n < limit; ++n) {
                args[0] = pElements[n];
                pResult.append(fmt.format(args));
                if (pSeparator != null && n < limit - 1) {
                    pResult.append(pSeparator);
                }
            }
        }

        return pResult;
    }



    public static StringBuilder makeList(Object[] pElements, String pFormat, String pSeparator, StringBuilder pResult) {
        if (pElements != null) {
            MessageFormat fmt = new MessageFormat(pFormat);
            Object[] args = new Object[1];
            int n = 0;

            for (int limit = pElements.length; n < limit; ++n) {
                args[0] = pElements[n];
                pResult.append(fmt.format(args));
                if (pSeparator != null && n < limit - 1) {
                    pResult.append(pSeparator);
                }
            }
        }

        return pResult;
    }



    public static String normalizeWhitespace(String s) {
        StringBuilder sb = new StringBuilder();
        int len = s.length();
        boolean lastWasSpace = false;

        for (int i = 0; i < len; ++i) {
            char c = s.charAt(i);
            switch (c) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
                if (!lastWasSpace) {
                    sb.append(' ');
                    lastWasSpace = true;
                }
                break;
            default:
                sb.append(c);
                lastWasSpace = false;
            }
        }

        return sb.toString();
    }



    static int[] createHexNybbleToDecimal() {
        int[] ret = new int[256];

        for (int i = 0; i < ret.length; ++i) {
            ret[i] = -1;
        }

        ret[48] = 0;
        ret[49] = 1;
        ret[50] = 2;
        ret[51] = 3;
        ret[52] = 4;
        ret[53] = 5;
        ret[54] = 6;
        ret[55] = 7;
        ret[56] = 8;
        ret[57] = 9;
        ret[97] = 10;
        ret[98] = 11;
        ret[99] = 12;
        ret[100] = 13;
        ret[101] = 14;
        ret[102] = 15;
        ret[65] = 10;
        ret[66] = 11;
        ret[67] = 12;
        ret[68] = 13;
        ret[69] = 14;
        ret[70] = 15;
        return ret;
    }



    static int[] createOctalDigitToDecimal() {
        int[] ret = new int[256];

        for (int i = 0; i < ret.length; ++i) {
            ret[i] = -1;
        }

        ret[48] = 0;
        ret[49] = 1;
        ret[50] = 2;
        ret[51] = 3;
        ret[52] = 4;
        ret[53] = 5;
        ret[54] = 6;
        ret[55] = 7;
        return ret;
    }



    static char[] createEscapedCharToChar() {
        char[] ret = new char[256];

        for (int i = 0; i < ret.length; ++i) {
            ret[i] = 0;
        }

        ret[110] = 10;
        ret[116] = 9;
        ret[98] = 8;
        ret[114] = 13;
        ret[102] = 12;
        ret[92] = 92;
        ret[39] = 39;
        ret[34] = 34;
        return ret;
    }



    static char[] createDecimalToHexNybble() {
        char[] ret = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        return ret;
    }



    public static String literalizeForRegex(String pString) {
        if (pString == null) {
            return null;
        } else {
            StringBuilder strbuf = new StringBuilder(pString.length());
            int len = pString.length();

            for (int i = 0; i < len; ++i) {
                char chCur = pString.charAt(i);
                if (chCur >= 65 && chCur <= 90) {
                    strbuf.append(chCur);
                } else if (chCur >= 48 && chCur <= 57) {
                    strbuf.append(chCur);
                } else if (chCur >= 97 && chCur <= 122) {
                    strbuf.append(chCur);
                } else {
                    switch (chCur) {
                    case '\u0007':
                        strbuf.append("\\a");
                        break;
                    case '\b':
                    case '\u000b':
                    case '\u000e':
                    case '\u000f':
                    case '\u0010':
                    case '\u0011':
                    case '\u0012':
                    case '\u0013':
                    case '\u0014':
                    case '\u0015':
                    case '\u0016':
                    case '\u0017':
                    case '\u0018':
                    case '\u0019':
                    case '\u001a':
                    case '\u001c':
                    case '\u001d':
                    case '\u001e':
                    case '\u001f':
                    case '!':
                    case '#':
                    case '%':
                    case '&':
                    case ',':
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                    case '@':
                    case 'A':
                    case 'B':
                    case 'C':
                    case 'D':
                    case 'E':
                    case 'F':
                    case 'G':
                    case 'H':
                    case 'I':
                    case 'J':
                    case 'K':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'O':
                    case 'P':
                    case 'Q':
                    case 'R':
                    case 'S':
                    case 'T':
                    case 'U':
                    case 'V':
                    case 'W':
                    case 'X':
                    case 'Y':
                    case 'Z':
                    case '`':
                    case 'a':
                    case 'b':
                    case 'c':
                    case 'd':
                    case 'e':
                    case 'f':
                    case 'g':
                    case 'h':
                    case 'i':
                    case 'j':
                    case 'k':
                    case 'l':
                    case 'm':
                    case 'n':
                    case 'o':
                    case 'p':
                    case 'q':
                    case 'r':
                    case 's':
                    case 't':
                    case 'u':
                    case 'v':
                    case 'w':
                    case 'x':
                    case 'y':
                    case 'z':
                    default:
                        strbuf.append("\\u");
                        strbuf.append(sDecimalToHexNybble[chCur >> 12 & 15]);
                        strbuf.append(sDecimalToHexNybble[chCur >> 8 & 15]);
                        strbuf.append(sDecimalToHexNybble[chCur >> 4 & 15]);
                        strbuf.append(sDecimalToHexNybble[chCur & 15]);
                        break;
                    case '\t':
                        strbuf.append("\\t");
                        break;
                    case '\n':
                        strbuf.append("\\n");
                        break;
                    case '\f':
                        strbuf.append("\\f");
                        break;
                    case '\r':
                        strbuf.append("\\r");
                        break;
                    case '\u001b':
                        strbuf.append("\\e");
                        break;
                    case ' ':
                    case '"':
                    case '\'':
                    case '-':
                    case '/':
                    case ':':
                    case ';':
                    case '<':
                    case '=':
                    case '>':
                    case '_':
                    case '~':
                        strbuf.append(chCur);
                        break;
                    case '$':
                        strbuf.append("\\$");
                        break;
                    case '(':
                        strbuf.append("\\(");
                        break;
                    case ')':
                        strbuf.append("\\)");
                        break;
                    case '*':
                        strbuf.append("\\*");
                        break;
                    case '+':
                        strbuf.append("\\+");
                        break;
                    case '.':
                        strbuf.append("\\.");
                        break;
                    case '?':
                        strbuf.append("\\?");
                        break;
                    case '[':
                        strbuf.append("\\[");
                        break;
                    case '\\':
                        strbuf.append("\\\\");
                        break;
                    case ']':
                        strbuf.append("\\]");
                        break;
                    case '^':
                        strbuf.append("\\^");
                        break;
                    case '{':
                        strbuf.append("\\{");
                        break;
                    case '|':
                        strbuf.append("\\|");
                        break;
                    case '}':
                        strbuf.append("\\}");
                    }
                }
            }

            return strbuf.toString();
        }
    }



    static String[] createCharToEscapedString() {
        String[] ret = new String[256];

        for (int i = 0; i < 256; ++i) {
            char ch = (char) i;
            if (ch == 10) {
                ret[i] = "\\n";
            } else if (ch == 9) {
                ret[i] = "\\t";
            } else if (ch == 8) {
                ret[i] = "\\b";
            } else if (ch == 13) {
                ret[i] = "\\r";
            } else if (ch == 12) {
                ret[i] = "\\f";
            } else if (ch == 92) {
                ret[i] = "\\\\";
            } else if (ch == 39) {
                ret[i] = "\\'";
            } else if (ch == 34) {
                ret[i] = "\\\"";
            } else {
                StringBuilder buf;
                if ((ch < 0 || ch > 31) && ch != 127) {
                    buf = new StringBuilder();
                    buf.append(ch);
                    ret[i] = buf.toString();
                } else {
                    buf = new StringBuilder();
                    buf.append('\\');
                    buf.append(sDecimalToHexNybble[ch >> 6 & 7]);
                    buf.append(sDecimalToHexNybble[ch >> 3 & 7]);
                    buf.append(sDecimalToHexNybble[ch & 7]);
                    ret[i] = buf.toString();
                }
            }
        }

        return ret;
    }



    public static String decodeFromJavaString(String pJavaString, boolean pDecodeUnicode) {
        if (pJavaString.length() < 2) {
            return "";
        } else if (pJavaString.indexOf(92) < 0) {
            return pJavaString.substring(1, pJavaString.length() - 1);
        } else {
            char[] src = new char[pJavaString.length() - 2];
            char[] dest = new char[src.length];
            int srcptr = 0;
            int destptr = 0;
            pJavaString.getChars(1, pJavaString.length() - 1, src, 0);
            int state = 0;
            int charval = 0;

            while (true) {
                while (srcptr < src.length) {
                    char ch = src[srcptr++];
                    switch (state) {
                    case 0:
                        if (ch == 92) {
                            state = 1;
                        } else {
                            dest[destptr++] = ch;
                        }
                        break;
                    case 1:
                        if (ch >= 0 && ch <= 255) {
                            if (sEscapedCharToChar[ch] != 0) {
                                dest[destptr++] = sEscapedCharToChar[ch];
                                state = 0;
                                break;
                            }

                            if (ch >= 48 && ch <= 55) {
                                charval = sOctalDigitToDecimal[ch];
                                state = 2;
                                break;
                            }

                            if (ch == 117 && pDecodeUnicode) {
                                state = 4;
                                break;
                            }

                            dest[destptr++] = ch;
                            state = 0;
                            break;
                        }

                        dest[destptr++] = ch;
                        state = 0;
                        break;
                    case 2:
                        if (sOctalDigitToDecimal[ch] >= 0) {
                            charval = charval << 3 | sOctalDigitToDecimal[ch];
                            state = 3;
                        } else {
                            dest[destptr++] = (char) charval;
                            --srcptr;
                            state = 0;
                        }
                        break;
                    case 3:
                        if (sOctalDigitToDecimal[ch] >= 0) {
                            charval = charval << 3 | sOctalDigitToDecimal[ch];
                        } else {
                            --srcptr;
                        }

                        dest[destptr++] = (char) charval;
                        state = 0;
                        break;
                    case 4:
                        if (sHexNybbleToDecimal[ch] >= 0) {
                            charval = sHexNybbleToDecimal[ch];
                            state = 5;
                        } else {
                            --srcptr;
                            state = 0;
                        }
                        break;
                    case 5:
                        if (sHexNybbleToDecimal[ch] >= 0) {
                            charval = charval << 4 | sHexNybbleToDecimal[ch];
                            state = 6;
                        } else {
                            dest[destptr++] = (char) charval;
                            --srcptr;
                            state = 0;
                        }
                        break;
                    case 6:
                        if (sHexNybbleToDecimal[ch] >= 0) {
                            charval = charval << 4 | sHexNybbleToDecimal[ch];
                            state = 7;
                        } else {
                            dest[destptr++] = (char) charval;
                            --srcptr;
                            state = 0;
                        }
                        break;
                    case 7:
                        if (sHexNybbleToDecimal[ch] >= 0) {
                            charval = charval << 4 | sHexNybbleToDecimal[ch];
                            boolean var10 = false;
                        } else {
                            --srcptr;
                        }

                        dest[destptr++] = (char) charval;
                        state = 0;
                    }
                }

                return new String(dest, 0, destptr);
            }
        }
    }



    public static String encodeToJavaString(String pString) {
        StringBuilder buf = new StringBuilder();
        buf.append('"');
        int len = pString.length();

        for (int i = 0; i < len; ++i) {
            char ch = pString.charAt(i);
            if (ch >= 256) {
                buf.append('\\');
                buf.append('u');
                buf.append(sDecimalToHexNybble[ch >> 12 & 15]);
                buf.append(sDecimalToHexNybble[ch >> 8 & 15]);
                buf.append(sDecimalToHexNybble[ch >> 4 & 15]);
                buf.append(sDecimalToHexNybble[ch & 15]);
            } else {
                buf.append(sCharToEscapedString[ch]);
            }
        }

        buf.append('"');
        return buf.toString();
    }



    public static boolean isEmpty(String pStr) {
        return pStr == null || pStr.length() == 0;
    }



    public static boolean isNotEmpty(String pStr) {
        return !isEmpty(pStr);
    }



    public static boolean isBlank(String pStr) {
        return pStr == null || pStr.length() == 0 || pStr.trim().length() == 0;
    }



    public static boolean isNotBlank(String pStr) {
        return !isBlank(pStr);
    }



    public static String trimToNull(String pString) {
        String strResult = pString;
        if (pString != null) {
            strResult = pString.trim();
            if (strResult.length() == 0) {
                strResult = null;
            }
        }

        return strResult;
    }



    public static String extractString(String pStr) {
        return extractString(pStr, "");
    }



    public static String extractString(String pStr, String pDefault) {
        return pStr == null ? pDefault : pStr;
    }



    public static String extractStringIfEmpty(String pStr, String pDefault) {
        return isEmpty(pStr) ? pDefault : pStr;
    }



    public static String extractStringIfBlank(String pStr, String pDefault) {
        return isBlank(pStr) ? pDefault : pStr;
    }



    public static String expandEntries(String pValue, Map pMap, String pOpen, String pClose) {
        int start;
        int end;
        String valueStr;
        if (pValue.indexOf(pOpen) >= 0) {
            for (; (start = pValue.indexOf(pOpen)) >= 0; pValue = pValue.substring(0, start) + valueStr
                    + pValue.substring(end + pClose.length())) {
                end = pValue.indexOf(pClose);
                if (end < 0) {
                    throw new IllegalArgumentException(pValue);
                }

                String expressionStr = pValue.substring(start + pOpen.length(), end);
                valueStr = (String) pMap.get(expressionStr);
                if (valueStr == null) {
                    valueStr = "";
                }
            }
        }

        return pValue;
    }



    public static String expandEntries(String pValue, Map pMap) {
        return expandEntries(pValue, pMap, "{", "}");
    }



    public static String joinStrings(String[] pStrings, char pSeparator) {
        if (pStrings != null && pStrings.length != 0) {
            StringBuffer strbufResult = new StringBuffer();

            for (int i = 0; i < pStrings.length; ++i) {
                if (i > 0) {
                    strbufResult.append(pSeparator);
                }

                strbufResult.append(pStrings[i]);
            }

            return strbufResult.toString();
        } else {
            return "";
        }
    }



    public static String joinStringsWithQuoting(String[] pStrings, char pSeparator) {
        if (pStrings != null && pStrings.length != 0) {
            String strSingle = Character.toString(pSeparator);
            String strDouble = strSingle + strSingle;
            StringBuilder sb = new StringBuilder();
            boolean bFirstTime = true;
            String[] arr$ = pStrings;
            int len$ = pStrings.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                String strCur = arr$[i$];
                if (!bFirstTime) {
                    sb.append(strSingle);
                } else {
                    bFirstTime = false;
                }

                sb.append(strCur.replace(strSingle, strDouble));
            }

            return sb.toString();
        } else {
            return "";
        }
    }



    public static String joinStringsWithNonRepeatingSeparator(String[] pStrings, char pSeparator,
            boolean pLeadingSeparator, boolean pTrailingSeparator) {
        StringBuilder result = new StringBuilder();
        if (pLeadingSeparator) {
            result.append(pSeparator);
        }

        if (pStrings != null && pStrings.length != 0) {
            for (int ii = 0; ii < pStrings.length; ++ii) {
                String strCur = pStrings[ii];
                if (!isBlank(strCur)) {
                    if (result.length() != 0 && result.charAt(result.length() - 1) != pSeparator) {
                        result.append(pSeparator);
                    }

                    if (strCur.charAt(0) == pSeparator) {
                        result.append(strCur.substring(1));
                    } else {
                        result.append(strCur);
                    }
                }
            }
        }

        if (pTrailingSeparator && (result.length() == 0 || result.charAt(result.length() - 1) != pSeparator)) {
            result.append(pSeparator);
        }

        return result.toString();
    }



    public static String escapeHtmlString(String pStr) {
        return escapeHtmlString(pStr, true);
    }



    public static String escapeHtmlString(String pStr, boolean pEscapeAmp) {
        String str = pStr;
        if (pStr == null) {
            return null;
        } else {
            StringBuilder strbuf = null;
            int len = pStr.length();

            for (int i = 0; i < len; ++i) {
                char chCur = str.charAt(i);
                String strReplace;
                switch (chCur) {
                case '"':
                    strReplace = "&quot;";
                    break;
                case '&':
                    strReplace = pEscapeAmp ? "&amp;" : null;
                    break;
                case '\'':
                    strReplace = "&#39;";
                    break;
                case '<':
                    strReplace = "&lt;";
                    break;
                case '>':
                    strReplace = "&gt;";
                    break;
                case '\\':
                    strReplace = "&#92;";
                    break;
                default:
                    strReplace = null;
                }

                if (strReplace != null) {
                    if (strbuf == null) {
                        strbuf = new StringBuilder(len + 3);
                        strbuf.insert(0, pStr, 0, i);
                    }

                    strbuf.append(strReplace);
                } else if (strbuf != null) {
                    strbuf.append(chCur);
                }
            }

            if (strbuf == null) {
                return pStr;
            } else {
                return strbuf.toString();
            }
        }
    }



    public static String convertStringToPropertiesFileFormat(String theString, boolean escapeSpace,
            boolean escapeUnicode) {
        int len = theString.length();
        StringBuilder outBuffer = new StringBuilder();

        for (int x = 0; x < len; ++x) {
            char aChar = theString.charAt(x);
            if (aChar > 61 && aChar < 127) {
                if (aChar == 92) {
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                } else {
                    outBuffer.append(aChar);
                }
            } else {
                switch (aChar) {
                case '\t':
                    outBuffer.append('\\');
                    outBuffer.append('t');
                    continue;
                case '\n':
                    outBuffer.append('\\');
                    outBuffer.append('n');
                    continue;
                case '\f':
                    outBuffer.append('\\');
                    outBuffer.append('f');
                    continue;
                case '\r':
                    outBuffer.append('\\');
                    outBuffer.append('r');
                    continue;
                case ' ':
                    if (x == 0 || escapeSpace) {
                        outBuffer.append('\\');
                    }

                    outBuffer.append(' ');
                    continue;
                case '!':
                case '#':
                case ':':
                case '=':
                    outBuffer.append('\\');
                    outBuffer.append(aChar);
                    continue;
                }

                if ((aChar < 32 || aChar > 126) & escapeUnicode) {
                    outBuffer.append('\\');
                    outBuffer.append('u');
                    outBuffer.append(sDecimalToHexNybble[aChar >> 12 & 15]);
                    outBuffer.append(sDecimalToHexNybble[aChar >> 8 & 15]);
                    outBuffer.append(sDecimalToHexNybble[aChar >> 4 & 15]);
                    outBuffer.append(sDecimalToHexNybble[aChar & 15]);
                } else {
                    outBuffer.append(aChar);
                }
            }
        }

        return outBuffer.toString();
    }



    public static String convertStringFromPropertiesFileFormat(String in) {
        StringBuilder buffer = new StringBuilder();
        int end = in.length();
        int off = 0;

        while (true) {
            while (true) {
                while (off < end) {
                    char aChar = in.charAt(off++);
                    if (aChar == 92) {
                        aChar = in.charAt(off++);
                        if (aChar == 117) {
                            int value = 0;

                            for (int i = 0; i < 4; ++i) {
                                aChar = in.charAt(off++);
                                switch (aChar) {
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    value = (value << 4) + aChar - 48;
                                    break;
                                case ':':
                                case ';':
                                case '<':
                                case '=':
                                case '>':
                                case '?':
                                case '@':
                                case 'G':
                                case 'H':
                                case 'I':
                                case 'J':
                                case 'K':
                                case 'L':
                                case 'M':
                                case 'N':
                                case 'O':
                                case 'P':
                                case 'Q':
                                case 'R':
                                case 'S':
                                case 'T':
                                case 'U':
                                case 'V':
                                case 'W':
                                case 'X':
                                case 'Y':
                                case 'Z':
                                case '[':
                                case '\\':
                                case ']':
                                case '^':
                                case '_':
                                case '`':
                                default:
                                    throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                                case 'A':
                                case 'B':
                                case 'C':
                                case 'D':
                                case 'E':
                                case 'F':
                                    value = (value << 4) + 10 + aChar - 65;
                                    break;
                                case 'a':
                                case 'b':
                                case 'c':
                                case 'd':
                                case 'e':
                                case 'f':
                                    value = (value << 4) + 10 + aChar - 97;
                                }
                            }

                            buffer.append((char) value);
                        } else {
                            if (aChar == 116) {
                                aChar = 9;
                            } else if (aChar == 114) {
                                aChar = 13;
                            } else if (aChar == 110) {
                                aChar = 10;
                            } else if (aChar == 102) {
                                aChar = 12;
                            }

                            buffer.append(aChar);
                        }
                    } else {
                        buffer.append(aChar);
                    }
                }

                return buffer.toString();
            }
        }
    }



    public static String[] join(String[] pFirst, String[] pSecond) {
        int len1 = pFirst == null ? 0 : pFirst.length;
        int len2 = pSecond == null ? 0 : pSecond.length;
        String[] joined = new String[len1 + len2];
        if (len1 > 0) {
            System.arraycopy(pFirst, 0, joined, 0, len1);
        }

        if (len2 > 0) {
            System.arraycopy(pSecond, 0, joined, len1, len2);
        }

        return joined;
    }

    static enum SplitterState {
        NORMAL, ESCAPE;

        private SplitterState() {
        }
    }
}
