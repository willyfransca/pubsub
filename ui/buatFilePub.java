/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author asus
 */
public class buatFilePub {

    public static int awalSimulasi = 0;
//    public static int akhirSimulasi = 274883;
    public static int akhirSimulasi = 8490908;
//    public static String nodePublish = "28,95,56,85,94";
//    public static String nodePublish = "59,24,17,86,61";
//    public static String nodePublish = "31,51,87,89,8";
    
    //duplicate
    public static String nodePublish = "31,59,87,40,4";
    public static String nodeInterest = "luna,via,nella,maya,ana";
    public static Map<Integer, String> temp = new HashMap<Integer, String>();

    public static int luna, via, nella, maya, ana;

    //untuk mengecek setiap node publisher sudah create pesan belum
    //min 1 hri sekali
    public static ArrayList<Integer> tempFrom = new ArrayList<Integer>();

    public static void main(String[] args) {
        luna = via = nella = maya = ana = 1;
        //baca node publisher nya dulu
        List<Integer> pub = bacaPublish(nodePublish);

//mengabuat node publisher yang random
//        List<Integer> pub = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            int a = getRandomNumberInRange(0, 96);
//            while (a >= 66 && a < 81) {
//                a = getRandomNumberInRange(0, 96);
//            }
//            pub.add(a);
//        }
        List<String> interest = bacaPub(nodeInterest);

        //pas awal simulasi membuat tiap node ada interest nya dulu  
        temp(pub, interest);

        //Inisialisasi Objek dan Mendefinisikan Path Lokasi File
        File file = new File("G:\\ContohFileRealityDuplicateRandom.txt");

        //Membuat Statement Try-Resource-Statement
        int waktu = 604800;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            for (int i = awalSimulasi; i < akhirSimulasi; i = i + getRandomNumberInRange(50000, 86400)) {

                //looping untuk membuat berapa banyak pesan pada detik tertentu
                for (int j = 0; j < getRandomNumberInRange(5, 8); j++) {
                    int from = getRandomElement(pub);

                    //cek node yang belum buat pesan
                    //asumsi setiap publisher min buat 1 pesan per hari
                    while (tempFrom.contains(from) && tempFrom.size() < 5) {
                        from = getRandomElement(pub);
                    }
                    tempFrom.add(from);

                    int to = getRandomNumberInRange(0, 96);

                    //mengecek node Dest yang di random bukan node publisher
                    while (pub.contains(to)) {
                        to = getRandomNumberInRange(0, 96);
                    }
                    int loop;
                    if (temp.get(from).equalsIgnoreCase("luna")) {
                        loop = luna;
                        //Menulis data String
                        bw.write(i + "\tC\t" + temp.get(from) + loop + "\t\t" + from + "\t" + to + "\t" + "512000" + "\t" + temp.get(from));

                        //Membuat Baris Baru
                        bw.newLine();
                        luna++;
                    } else if (temp.get(from).equalsIgnoreCase("via")) {
                        loop = via;
                        //Menulis data String
                        bw.write(i + "\tC\t" + temp.get(from) + loop + "\t\t" + from + "\t" + to + "\t" + "512000" + "\t" + temp.get(from));

                        //Membuat Baris Baru
                        bw.newLine();
                        via++;

                    } else if (temp.get(from).equalsIgnoreCase("nella")) {
                        loop = nella;
                        //Menulis data String
                        bw.write(i + "\tC\t" + temp.get(from) + loop + "\t\t" + from + "\t" + to + "\t" + "512000" + "\t" + temp.get(from));

                        //Membuat Baris Baru
                        bw.newLine();
                        nella++;

                    } else if (temp.get(from).equalsIgnoreCase("maya")) {
                        loop = maya;
                        //Menulis data String
                        bw.write(i + "\tC\t" + temp.get(from) + loop + "\t\t" + from + "\t" + to + "\t" + "512000" + "\t" + temp.get(from));

                        //Membuat Baris Baru
                        bw.newLine();
                        maya++;

                    } else if (temp.get(from).equalsIgnoreCase("ana")) {
                        loop = ana;
                        //Menulis data String
                        bw.write(i + "\tC\t" + temp.get(from) + loop + "\t\t" + from + "\t" + to + "\t" + "512000" + "\t" + temp.get(from));

                        //Membuat Baris Baru
                        bw.newLine();
                        ana++;
                    }
                }

                //mengubah interest nya pada saat ganti hari
                if (i >= waktu) {
                    waktu += 604800;
//                     System.out.println(interest);
                    temp.clear();
                    Collections.shuffle(interest);
                    temp(pub, interest);
                }
                tempFrom.clear();
            }
        } catch (FileNotFoundException ex) {
            //Menampilkan pesan jika file tidak ditemukan
            System.out.println("File " + file.getName() + " Tidak Ditemukan");
        } catch (IOException ex) {
            //Menampilkan pesan jika terjadi error atau file tidak dapat dibaca
            System.out.println("File " + file.getName() + " Tidak Dapat DIbaca");
        }
    }

    //membuat bilangan random antara min - max
    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    //membaca node publish untuk array
    private static ArrayList<Integer> bacaPublish(String text) {
        String[] pecah = text.split(",");
        ArrayList jadi = new ArrayList();
        for (String string : pecah) {
            jadi.add(Integer.parseInt(string));
        }
//        System.out.println(jadi);
        return jadi;
    }

    //membaca interest yang di inputkan
    private static ArrayList<String> bacaPub(String text) {
        String[] pecah = text.split(",");
        ArrayList jadi = new ArrayList();
        for (String string : pecah) {
            jadi.add(string);
        }
//        System.out.println(jadi);
        return jadi;
    }

    //mengambil random untuk node dalam array
    private static int getRandomElement(List<Integer> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    //mengambil random untuk Interest dalam array
    public static String getRandomElementInterest(List<String> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    //membuat random lagi node yang publish
    private static String nodePublish() {
        String node = "";
        for (int i = 0; i < 10; i++) {
            node = node + getRandomNumberInRange(0, 40) + ",";
        }
        node.substring(0, node.length() - 1);
        return node;
    }

    //mengabungkan antaran suatu node dengan interest nya
    private static void temp(List<Integer> pub, List<String> Interest) {
        for (Integer node : pub) {
            String inter = getRandomElementInterest(Interest);
            //untuk mengecek interest nya sudah ada belum
            while (temp.containsValue(inter) && temp.size() < 5) {
                inter = getRandomElementInterest(Interest);
            }
            temp.put(node, inter);

        }
        System.out.println(temp);
    }

}
