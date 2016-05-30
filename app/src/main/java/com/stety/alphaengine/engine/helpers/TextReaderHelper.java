package com.stety.alphaengine.engine.helpers;

import android.content.res.AssetManager;
import android.content.res.Resources;

import com.stety.alphaengine.engine.android.AlphaEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * EN: Reads in text from file.
 * CS: Čte text ze souboru.
 */
public class TextReaderHelper
{
    public static String readTextFileFromAssets(String name)
    {
        String text = "";
        try
        {
            AssetManager assetsManager = AlphaEngine.context.getAssets();
            InputStream inputStream = assetsManager.open(name);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;

            while ((nextLine = bufferedReader.readLine()) != null)
            {
                text += nextLine;
            }
        } catch (IOException e)
        {
            throw new RuntimeException("Could not open asset: " + name, e);
        } catch (Resources.NotFoundException nfe)
        {
            throw new RuntimeException("Asset not found: " + name, nfe);
        }
        return text;
    }

    public static String readShader(String name)
    {
        return readTextFileFromAssets("shaders/" + name + ".glsl");
    }

    /**
     * EN: Read 3GL (model) file from Assets.
     * CS: Přečte 3GL (model) soubor z Assets.
     *
     * @param name name of 3GL (model) file
     * @return Vertex data.
     */
    public static float[] read3GLFileFromAssets(String name)
    {
        int size = TextReaderHelper.size3GLArray(name);
        float[] vertexData = new float[size];

        try
        {
            AssetManager assetsManager = AlphaEngine.context.getAssets();
            InputStream inputStream = assetsManager.open(name);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;
            String type;
            int i = 0;

            while ((nextLine = bufferedReader.readLine()) != null)
            {
                type = nextLine.substring(0, 2);
                if (!type.equals("//") && !type.equals("vx"))
                {
                    String[] lineSplit = nextLine.split(",");
                    vertexData[i] = Float.parseFloat(lineSplit[0]);
                    i++;
                    vertexData[i] = Float.parseFloat(lineSplit[1]);
                    i++;
                    if (lineSplit.length == 3)
                    {
                        vertexData[i] = Float.parseFloat(lineSplit[2]);
                        i++;
                    }
                    if (lineSplit.length == 4)
                    {
                        vertexData[i] = Float.parseFloat(lineSplit[3]);
                        i++;
                    }
                }
            }
        } catch (IOException e)
        {
            throw new RuntimeException("Could not open asset: " + name, e);
        } catch (Resources.NotFoundException nfe)
        {
            throw new RuntimeException("Asset not found: " + name, nfe);
        }

        return vertexData;
    }

    /**
     * EN: Helper function for determine the size field with vertex data.
     * CS: Pomocná funkce pro zjištění velikosti pole s vertex daty.
     *
     * @param name name of 3GL (model) file
     * @return Size of field.
     */
    private static int size3GLArray(String name)
    {
        int size = 0;
        try
        {
            AssetManager assetsManager = AlphaEngine.context.getAssets();
            InputStream inputStream = assetsManager.open(name);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;
            String type;
            boolean vt = true;

            while ((nextLine = bufferedReader.readLine()) != null && vt)
            {
                type = nextLine.substring(0, 2);
                if (type.equals("vx"))
                {
                    String s = nextLine.substring(2);
                    size = Integer.parseInt(s);
                    vt = false;
                }
            }
        } catch (IOException e)
        {
            throw new RuntimeException("Could not open asset: " + name, e);
        } catch (Resources.NotFoundException nfe)
        {
            throw new RuntimeException("Asset not found: " + name, nfe);
        }

        return size;
    }
}
