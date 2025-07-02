package org.example;

import java.awt.Color;
import java.util.StringTokenizer;

/**
 * 
 */

/**
 * 
 */
@SuppressWarnings("ALL")
public class LogCatParser implements ILogParser
{
    final String TOKEN_KERNEL= "<>[]";
    final String TOKEN_SPACE = " ";
    final String TOKEN_SLASH = "/";
    final String TOKEN       = "/()";
    final String TOKEN_PID   = "/() ";
    final String TOKEN_MESSAGE = "'";
    
    public Color getColor(LogInfo logInfo)
    {
        switch (logInfo.m_strLogLV) {
            case "FATAL":
            case "F":
                return new Color(LogColor.COLOR_FATAL);
            case "ERROR":
            case "E":
            case "3":
                return new Color(LogColor.COLOR_ERROR);
            case "WARN":
            case "W":
            case "4":
                return new Color(LogColor.COLOR_WARN);
            case "INFO":
            case "I":
            case "6":
                return new Color(LogColor.COLOR_INFO);
            case "DEBUG":
            case "D":
            case "7":
                return new Color(LogColor.COLOR_DEBUG);
            case "0":
                return new Color(LogColor.COLOR_0);
            case "1":
                return new Color(LogColor.COLOR_1);
            case "2":
                return new Color(LogColor.COLOR_2);
            case "5":
                return new Color(LogColor.COLOR_5);
            default:
                return Color.BLACK;

        }
    }

    public int getLogLV(LogInfo logInfo)
    {
        switch (logInfo.m_strLogLV) {
            case "FATAL":
            case "F":
                return LogInfo.LOG_LV_FATAL;
            case "ERROR":
            case "E":
                return LogInfo.LOG_LV_ERROR;
            case "WARN":
            case "W":
                return LogInfo.LOG_LV_WARN;
            case "INFO":
            case "I":
                return LogInfo.LOG_LV_INFO;
            case "DEBUG":
            case "D":
                return LogInfo.LOG_LV_DEBUG;
            default:
                return LogInfo.LOG_LV_VERBOSE;
        }
    }
    
//04-17 09:01:18.910 D/LightsService(  139): BKL : 106
    public boolean isNormal(String strText)
    {
        if(strText.length() < 22) return false;

        String strLevel = strText.substring(19, 21);
        if(strLevel.equals("D/")
                || strLevel.equals("V/")
                || strLevel.equals("I/")
                || strLevel.equals("W/")
                || strLevel.equals("E/")
                || strLevel.equals("F/")
                )
            return true;
        return false;
    }

//04-20 12:06:02.125   146   179 D BatteryService: update start    
    public boolean isThreadTime(String strText)
    {
        if(strText.length() < 34) return false;

        String strLevel = strText.substring(31, 33);
        if(strLevel.equals("D ")
                || strLevel.equals("V ")
                || strLevel.equals("I ")
                || strLevel.equals("W ")
                || strLevel.equals("E ")
                || strLevel.equals("F ")
                )
            return true;
        return false;
    }
    
//    <4>[19553.494855] [DEBUG] USB_SEL(1) HIGH set USB mode 
    public boolean isKernel(String strText)
    {
        if(strText.length() < 18) return false;

        String strLevel = strText.substring(1, 2);
        if(strLevel.equals("0")
                || strLevel.equals("1")
                || strLevel.equals("2")
                || strLevel.equals("3")
                || strLevel.equals("4")
                || strLevel.equals("5")
                || strLevel.equals("6")
                || strLevel.equals("7")
                )
            return true;
        return false;
    }
    
    public LogInfo getNormal(String strText)
    {
        LogInfo logInfo = new LogInfo();
        
        StringTokenizer stk = new StringTokenizer(strText, TOKEN_PID, false);
        if(stk.hasMoreElements())
            logInfo.m_strDate = stk.nextToken();
        if(stk.hasMoreElements())
            logInfo.m_strTime = stk.nextToken();
        if(stk.hasMoreElements())
            logInfo.m_strLogLV = stk.nextToken().trim();
        if(stk.hasMoreElements())
            logInfo.m_strTag = stk.nextToken();
        if(stk.hasMoreElements())
            logInfo.m_strPid = stk.nextToken().trim();
        if(stk.hasMoreElements())
        {
            logInfo.m_strMessage = stk.nextToken(TOKEN_MESSAGE);
            while(stk.hasMoreElements())
            {
                logInfo.m_strMessage += stk.nextToken(TOKEN_MESSAGE);
            }
            logInfo.m_strMessage = logInfo.m_strMessage.replaceFirst("\\): ", "");
        }
        logInfo.m_TextColor = getColor(logInfo);
        return logInfo;
    }

    public LogInfo getThreadTime(String strText)
    {
        LogInfo logInfo = new LogInfo();
        
        StringTokenizer stk = new StringTokenizer(strText, TOKEN_SPACE, false);
        if(stk.hasMoreElements())
            logInfo.m_strDate = stk.nextToken();
        if(stk.hasMoreElements())
            logInfo.m_strTime = stk.nextToken();
        if(stk.hasMoreElements())
            logInfo.m_strPid = stk.nextToken().trim();
        if(stk.hasMoreElements())
            logInfo.m_strThread = stk.nextToken().trim();
        if(stk.hasMoreElements())
            logInfo.m_strLogLV = stk.nextToken().trim();
        if(stk.hasMoreElements())
            logInfo.m_strTag = stk.nextToken();
        if(stk.hasMoreElements())
        {
            logInfo.m_strMessage = stk.nextToken(TOKEN_MESSAGE);
            while(stk.hasMoreElements())
            {
                logInfo.m_strMessage += stk.nextToken(TOKEN_MESSAGE);
            }
            logInfo.m_strMessage = logInfo.m_strMessage.replaceFirst("\\): ", "");
        }
        logInfo.m_TextColor = getColor(logInfo);
        return logInfo;
    }

    public LogInfo getKernel(String strText)
    {
        LogInfo logInfo = new LogInfo();
        
        StringTokenizer stk = new StringTokenizer(strText, TOKEN_KERNEL, false);
        if(stk.hasMoreElements())
            logInfo.m_strLogLV = stk.nextToken();
        if(stk.hasMoreElements())
            logInfo.m_strTime = stk.nextToken();
        if(stk.hasMoreElements())
        {
            logInfo.m_strMessage = stk.nextToken(TOKEN_KERNEL);
            while(stk.hasMoreElements())
            {
                logInfo.m_strMessage += " " + stk.nextToken(TOKEN_SPACE);
            }
            logInfo.m_strMessage = logInfo.m_strMessage.replaceFirst("  ", "");
        }
        logInfo.m_TextColor = getColor(logInfo);
        return logInfo;
    }

    public LogInfo parseLog(String strText)
    {
        if(isNormal(strText))
            return getNormal(strText);
        else if(isThreadTime(strText))
            return getThreadTime(strText);
        else if(isKernel(strText))
            return getKernel(strText);
        else
        {
            LogInfo logInfo = new LogInfo();
            logInfo.m_strMessage = strText;
            return logInfo;
        }
    }
}
