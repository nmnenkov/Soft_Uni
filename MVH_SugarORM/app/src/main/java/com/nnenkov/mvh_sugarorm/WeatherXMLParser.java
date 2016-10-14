package com.nnenkov.mvh_sugarorm;


import com.nnenkov.mvh_sugarorm.model.WeatherXML;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nik on 13.10.16.
 */

public class WeatherXMLParser extends DefaultHandler {





    List<WeatherXML> list=null;

    // string builder acts as a buffer
    StringBuilder builder;

    WeatherXML jobsValues=null;


    // Initialize the arraylist
    // @throws SAXException

    @Override
    public void startDocument() throws SAXException {

        /******* Create ArrayList To Store XmlValuesModel object ******/
        list = new ArrayList<WeatherXML>();
    }


    // Initialize the temp XmlValuesModel object which will hold the parsed info
    // and the string builder that will store the read characters
    // @param uri
    // @param localName ( Parsed Node name will come in localName  )
    // @param qName
    // @param attributes
    // @throws SAXException

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        /****  When New XML Node initiating to parse this function called *****/

        // Create StringBuilder object to store xml node value
        builder=new StringBuilder();

        if(localName.equals("weather")){

            //Log.i("parse","====login=====");
        }
        else if(localName.equals("city")){

            // Log.i("parse","====status=====");
        }
        else if(localName.equals("country")){

            // Log.i("parse","====status=====");
        }
        else if(localName.equals("cityname")){

            // Log.i("parse","====status=====");
        }
        else if(localName.equals("cityname2")){

            // Log.i("parse","====status=====");
        }
        else if(localName.equals("citytime")){

            // Log.i("parse","====status=====");
        }
        else if(localName.equals("step")){

            // Log.i("parse","----Job start----");
            /********** Create Model Object  *********/
            jobsValues = new WeatherXML();
        }
    }



    // Finished reading the login tag, add it to arraylist
    // @param uri
    // @param localName
    // @param qName
    // @throws SAXException

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {


        if(localName.equals("step")){

            /** finished reading a job xml node, add it to the arraylist **/
            list.add( jobsValues );

        }
        else  if(localName.equalsIgnoreCase("status")){


        }
        else  if(localName.equalsIgnoreCase("datetime")){
            jobsValues.setDatetime(builder.toString());
        } if(localName.equalsIgnoreCase("pressure")){
            jobsValues.setPressure(builder.toString());
        } if(localName.equalsIgnoreCase("temperature")){
            jobsValues.setTemperature(builder.toString());
        } if(localName.equalsIgnoreCase("humidity")){
            jobsValues.setHumidity(builder.toString());
        } if(localName.equalsIgnoreCase("cloudcover")){
            jobsValues.setCloudcover(builder.toString());
        } if(localName.equalsIgnoreCase("windspeed")){
            jobsValues.setWindspeed(builder.toString());
        } if(localName.equalsIgnoreCase("windgust")){
            jobsValues.setWindgust(builder.toString());
        } if(localName.equalsIgnoreCase("winddir")){
            jobsValues.setWinddir(builder.toString());
        } if(localName.equalsIgnoreCase("precipitation")){
            jobsValues.setPrecipitation(builder.toString());
        }


        // Log.i("parse",localName.toString()+"========="+builder.toString());
    }


    // Read the value of each xml NODE
    // @param ch
    // @param start
    // @param length
    // @throws SAXException

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        /******  Read the characters and append them to the buffer  ******/
        String tempString=new String(ch, start, length);
        builder.append(tempString);
    }
}