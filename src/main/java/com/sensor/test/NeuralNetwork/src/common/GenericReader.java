//package com.sensor.test.NeuralNetwork.src.common;
//
//import model.Tuple;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class GenericReader {
//
//
//    /**
//     * Initialize the files from csv.
//     *
//     * @param filePath
//     * @param fromCSV
//     * @param <T>
//     * @return
//     */
//    public static <T> List<T> init(String filePath, Loadable<T> fromCSV) {
//        List<T> dataFromCSV = new ArrayList<>();
//        InputStream is = null;
//        BufferedReader reader = null;
//
//        try {
//
//            is = common.GenericReader.class.getResourceAsStream(filePath);
//            reader = new BufferedReader(new InputStreamReader(is));
//
//            String line = reader.readLine();
//            line = reader.readLine();
//
//            while (line != null) {
//                String[] attributes = line.split(",");
//
//                T obj = fromCSV.create(attributes);
//
//                dataFromCSV.add(obj);
//
//                line = reader.readLine();
//            }
//
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        } finally {
//            try {
//                is.close();
//                reader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return dataFromCSV;
//
//
//    }
//
//
//    public static Tuple createTuple(String[] metadata) {
////        System.out.println(metadata.length);
////        System.out.println(Arrays.toString(metadata));
//        String[] tuples = Arrays.copyOfRange(metadata, 1, metadata.length);
//        double[] tple = new double[tuples.length];
//        for (int i = 0; i < tuples.length; i++) {
//            tple[i] = Double.parseDouble(tuples[i]);
//        }
////        System.out.println(Arrays.toString(tple));
//        return new Tuple(tple, Integer.parseInt(metadata[0] + ""), 10);
//    }
//
//    public static Tuple createTuple2(String[] metadata) {
//        double[] tuples = new double[metadata.length - 1];
//        for (int i = 0; i < metadata.length - 1; i++) {
//            tuples[i] = Double.parseDouble(metadata[i].trim());
//        }
//        return new Tuple(tuples, Integer.parseInt(metadata[metadata.length - 1].trim()), 4);
//    }
//
//}
