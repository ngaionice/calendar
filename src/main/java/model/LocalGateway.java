package model;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalGateway {

    public Map<String, Event> importEventData(String path) {
        try {
            // read the object from file
            InputStream file = new FileInputStream(path); // String path should be "fileName.ser"
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            // method to deserialize object
            Map<String, Event> data = (Map<String, Event>) input.readObject();
            input.close();
            return data;
        } catch (IOException | ClassNotFoundException i) {
            System.out.println("No existing data found.");
            return new HashMap<>(20);
        }
    }

    public List<Map<String, Course>> importCourseData(String coursesPath, String archivedCoursesPath) {
        try {
            // read the object from file
            InputStream file = new FileInputStream(coursesPath); // String path should be "fileName.ser"
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            // method to deserialize object
            Map<String, Course> data = (Map<String, Course>) input.readObject();
            input.close();

            // read the object from file
            InputStream file1 = new FileInputStream(archivedCoursesPath); // String path should be "fileName.ser"
            InputStream buffer1 = new BufferedInputStream(file1);
            ObjectInput input1 = new ObjectInputStream(buffer1);
            // method to deserialize object
            Map<String, Course> data1 = (Map<String, Course>) input1.readObject();
            input1.close();
            return Arrays.asList(data, data1);
        } catch (IOException | ClassNotFoundException i) {
            System.out.println("No existing data found.");
            return Arrays.asList(new HashMap<>(20), new HashMap<>(20));
        }
    }

    /**
     * Serializes the input Manager to the specified file path.
     *
     * @param path     the file path of the to-be serialized Manager
     * @param data     the data to be serialized
     */
    public <V> void exportData(String path, Map<String, V> data) {
        try {
            // save the object to file
            OutputStream file = new FileOutputStream(path);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            // method to serialize object
            output.writeObject(data);
            output.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
