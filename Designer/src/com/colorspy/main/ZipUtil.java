package com.colorspy.main;

/*
* 文件名:     ZipUtil.java
* 版权：        xxxxxxxx.com. Copyright 1999-2010, All rights reserved
* 描述：       是压缩工具类,此类根据com.jcraft.jzlib地三方提供的核心类进行.压缩和解压缩。
* 修改人：     
* 修改时间：   2010-09-13
* 跟踪单号：    
* 修改单号：    
* 修改内容：    新增
可以到google是去下载jzlib4me20100516.rar 也就是jzlib4me的google项目为第三方支持包.
这个ZipUtil.java的zlib支持J2ME.也就是将zlib的压缩和解压缩的两个方法可以放到J2ME项目中.但也需要jzlib4me20100516.rar包.
*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZInputStream;
import com.jcraft.jzlib.ZOutputStream;

/**
* 压缩工具包
*/
public class ZipUtil
{

    public static byte[] deflate(byte[] bContent)
    {
//        MapServerLog.beforeMethod();
        try
        {
            String sys_compress = "ZIP";
            
            byte[] temp = null;
            
            if("LZIP".equals(sys_compress))
            {
                temp = ZipUtil.zLib(bContent);
            }
            else
            if("GZIP".equals(sys_compress))
            {
                temp = ZipUtil.gZip(bContent);
            }
            else
            if("ZIP".equals(sys_compress))
            {
                temp = ZipUtil.zip(bContent);
            }
           
            MapServerLog.afterMethod();
            
            return temp;
        }
        catch (IOException e)
        {
            MapServerLog.exceptionMethod(e);
            e.printStackTrace();
        }
        
        MapServerLog.afterMethod();
        return null;
    }

    public static byte[] inflate(byte[] bContent)
    {
        MapServerLog.beforeMethod();
        try
        {
            String sys_compress = "ZIP";
            
            byte[] temp = null;
            
            if("LZIP".equals(sys_compress))
            {
                temp = ZipUtil.unZLib(bContent);
            }
            else
            if("GZIP".equals(sys_compress))
            {
                temp = ZipUtil.unGZip(bContent);
            }
            else
            if("ZIP".equals(sys_compress))
            {
                temp = ZipUtil.unZip(bContent);
            }
           
            MapServerLog.afterMethod();
            
            return temp;
        }
        catch (IOException e)
        {
            MapServerLog.exceptionMethod(e);
            e.printStackTrace();
        }
        
        MapServerLog.afterMethod();
        return null;
    }

    // 输入数据的最大长度
    private static final int MAXLENGTH = 102400;

    // 设置缓存大小
    private static final int BUFFERSIZE = 1024;

    // 压缩选择方式：
    //
    // /** Try o get the best possible compression */
    // public static final int COMPRESSION_MAX = JZlib.Z_BEST_COMPRESSION;
    //
    // /** Favor speed over compression ratio */
    // public static final int COMPRESSION_MIN = JZlib.Z_BEST_SPEED;
    //
    // /** No compression */
    // public static final int COMPRESSION_NONE = JZlib.Z_NO_COMPRESSION;
    //
    // /** Default compression */
    // public static final int COMPRESSION_DEFAULT =
    // JZlib.Z_DEFAULT_COMPRESSION;

    /**
     * ZLib压缩数据
     * 
     * @param object
     * @return
     * @throws IOException
     */
    public static byte[] zLib(byte[] bContent) throws IOException
    {

        byte[] data = null;
        try
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ZOutputStream zOut = new ZOutputStream(out,
                    JZlib.Z_BEST_COMPRESSION); // 压缩级别,缺省为1级
            DataOutputStream objOut = new DataOutputStream(zOut);
            objOut.write(bContent);
            objOut.flush();
            zOut.close();
            data = out.toByteArray();
            out.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw e;
        }
        return data;
    }

    /**
     * ZLib解压数据
     * 
     * @param object
     * @return
     * @throws IOException
     */
    public static byte[] unZLib(byte[] bContent) throws IOException
    {

        byte[] data = new byte[MAXLENGTH];
        try
        {
            ByteArrayInputStream in = new ByteArrayInputStream(bContent);
            ZInputStream zIn = new ZInputStream(in);
            DataInputStream objIn = new DataInputStream(zIn);

            int len = 0;
            int count = 0;
            while ((count = objIn.read(data, len, len + BUFFERSIZE)) != -1)
            {
                len = len + count;
            }

            byte[] trueData = new byte[len];
            System.arraycopy(data, 0, trueData, 0, len);

            objIn.close();
            zIn.close();
            in.close();

            return trueData;

        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * GZip压缩数据
     * 
     * @param object
     * @return
     * @throws IOException
     */
    public static byte[] gZip(byte[] bContent) throws IOException
    {

        byte[] data = null;
        try
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            GZIPOutputStream gOut = new GZIPOutputStream(out, bContent.length); // 压缩级别,缺省为1级
            DataOutputStream objOut = new DataOutputStream(gOut);
            objOut.write(bContent);
            objOut.flush();
            gOut.close();
            data = out.toByteArray();
            out.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw e;
        }
        return data;
    }

    /**
     * GZip解压数据
     * 
     * @param object
     * @return
     * @throws IOException
     */
    public static byte[] unGZip(byte[] bContent) throws IOException
    {

        byte[] data = new byte[MAXLENGTH];
        try
        {
            ByteArrayInputStream in = new ByteArrayInputStream(bContent);
            GZIPInputStream pIn = new GZIPInputStream(in);
            DataInputStream objIn = new DataInputStream(pIn);

            int len = 0;
            int count = 0;
            while ((count = objIn.read(data, len, len + BUFFERSIZE)) != -1)
            {
                len = len + count;
            }

            byte[] trueData = new byte[len];
            System.arraycopy(data, 0, trueData, 0, len);

            objIn.close();
            pIn.close();
            in.close();

            return trueData;

        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    /***
     * 压缩Zip
     * 
     * @param data
     * @return
     * @throws IOException
     */
    public static byte[] zip(byte[] bContent) throws IOException
    {

        byte[] b = null;
        try
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(bos);
            ZipEntry entry = new ZipEntry("zip");
            entry.setSize(bContent.length);
            zip.putNextEntry(entry);
            zip.write(bContent);
            zip.closeEntry();
            zip.close();
            b = bos.toByteArray();
            bos.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return b;
    }

    /***
     * 解压Zip
     * 
     * @param data
     * @return
     * @throws IOException
     */
    public static byte[] unZip(byte[] bContent) throws IOException
    {
        byte[] b = null;
        try
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(bContent);
            ZipInputStream zip = new ZipInputStream(bis);
            while (zip.getNextEntry() != null)
            {
                byte[] buf = new byte[1024];
                int num = -1;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((num = zip.read(buf, 0, buf.length)) != -1)
                {
                    baos.write(buf, 0, num);
                }
                b = baos.toByteArray();
                baos.flush();
                baos.close();
            }
            zip.close();
            bis.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return b;
    }

    public static void main(String[] args)
    {
        String newContent = "";

        try
        {
            String content = "{'C'='NewSeed', D={'Seed'=12347777}}";
            System.out.println(content);
            byte[] origin = content.getBytes();
            System.out.println("原始长度 length is : " + origin.length);

            // ZLib 压缩
            byte[] zLibCnt = zLib(origin);
            System.out.println("zLib压缩后长度 : " + zLibCnt.length);

            byte[] unzLibCnt = unZLib(zLibCnt);
            System.out.println("zLib解压后长度 : " + unzLibCnt.length);

            newContent = new String(unzLibCnt);
            System.out.println(newContent);
            
            // GZip 压缩
            byte[] gZipCnt = gZip(origin);
            System.out.println("GZip压缩后长度 : " + gZipCnt.length);

            byte[] ungZipCnt = unGZip(gZipCnt);
            System.out.println("GZip解压后长度 : " + ungZipCnt.length);

            newContent = new String(ungZipCnt);
            System.out.println(newContent);

            // Zip 压缩
            byte[] zipCnt = zip(origin);
            System.out.println("Zip压缩后长度 : " + zipCnt.length);

            byte[] unZipCnt = unZip(zipCnt);
            System.out.println("Zip解压后长度 : " + unZipCnt.length);

            newContent = new String(unZipCnt);
            System.out.println(newContent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}

