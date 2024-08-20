package com.wxz.netty.endecoder;

/**
 * 通用工具类
 *
 * @author wxz
 * @date 11:59 2024/8/19
 */
public class CommonTools
{
    public static final int FIXEDLENGTHFRAME_LENGTH = 256; // 定长解码器 FixedLengthFrameDecoder 对应的消息长度

    /**
     * 生成指定长度的字符串
     *
     * @param str
     * @param assignLength
     * @return java.lang.String
     * @author wxz
     * @date 12:05 2024/8/19
     */
    public static String formatString(String str, int assignLength)
    {
        int intStrLen = 0;

        if (str != null)
        {
            intStrLen = str.length();
        }

        if (intStrLen >= assignLength)
        {
            return str;
        }
        else
        {
            StringBuilder strSpace = new StringBuilder();
            for (int i = 0, num = assignLength - intStrLen; i < num; i++)
            {
                strSpace.append(" ");
            }
            return str + strSpace;
        }
    }
}
