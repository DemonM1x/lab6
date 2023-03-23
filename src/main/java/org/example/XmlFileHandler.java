package org.example;

import com.sun.source.tree.Tree;
import org.example.Collection.City;
import org.example.Exceptions.FileLoadingException;
import org.example.Exceptions.NoAccessToFileException;
import org.example.interfaces.Loadable;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.example.Collection.Cities;

public class XmlFileHandler implements Loadable {
    private TreeSet<City> cities;
    private ZonedDateTime initializationTime;

    private boolean checkPermission(File file){
        if (!file.canRead()){
            System.out.println("File cannot be read from. You should have this permission.");
            return false;
        }
        if (!file.canWrite()){
            System.out.println("File cannot be written to. You should have this permission.");
            return false;
        }
        return true;
    }

    @Override
    public void load(File xmlfile) throws NoAccessToFileException, FileLoadingException {


        try {
            var fileCreated = xmlfile.createNewFile();
            if (!checkPermission(xmlfile)) {
                throw new NoAccessToFileException(xmlfile);
            }
            initializationTime = ZonedDateTime.now();
            if (!xmlfile.exists()) {
                System.out.println("0 groups were downloaded");
                cities = new TreeSet<>();
                return;
            }

            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            var bufferedReader = new FileReader(xmlfile);

            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(bufferedReader);

            JAXBContext context = JAXBContext.newInstance(Cities.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            cities = unmarshaller.unmarshal(xmlEventReader, Cities.class).getValue().getCities();


            System.out.println("loaded " + " cities: " + cities.size());
        } catch (Exception jaxbException) {
            cities = new TreeSet<>();
        }
    }

    @Override
    public boolean save(TreeSet<City> city, File file) throws Exception {
        try {
            if (city.size() == 0){
                new OutputStreamWriter(new FileOutputStream(file),"UTF-8").close();
                return true;
            }
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
            var citiesXml = new Cities();
            citiesXml.setCities(city);
            JAXBContext jaxbContext = JAXBContext.newInstance(Cities.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(citiesXml, outputStreamWriter);
            outputStreamWriter.close();

        }
        catch (JAXBException | IOException jaxbException) {
            System.err.print(jaxbException.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public ZonedDateTime getInitializationTime() {
        return initializationTime;
    }

    @Override
    public TreeSet<City> get() {
        return cities;
    }
}
