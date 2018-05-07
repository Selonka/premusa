package com.company;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mschwarz on 5/7/18.
 */
public class Worker {
    FReader fReader;
    FWriter fWriter;


    static final String seperator = "\t";
    static final String vcfFileFormat = "##fileformat=VCFv4.2";
    static final String vcfInfoHeaderTLOD= "##INFO=<ID=TLOD,Number=A,Type=Float,Description=\"Tumor LOD score\">";
    static final String vcfInfoHeaderAF = "##INFO=<ID=AF,Number=A,Type=Float,Description=\"Allele fractions of alternate alleles in the tumor\">";
    static final String vcfHeader = "#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO";

    public Worker(String pathToTable, String outputPath) throws IOException {
        List<String > tableLineList = readInTable(pathToTable);
        writeList(outputPath,formatList(tableLineList));
    }
    //Read in the table into a String list
    private List<String> readInTable(String pathToTable) throws IOException {
        String line;
        List<String> tableLineList = new LinkedList<>();
        fReader = new FReader();
        fReader.initReader(pathToTable);
        while((line = fReader.readLine()) != null){
            if(!line.startsWith("CHROM")){
                tableLineList.add(line);
            }
        }
        fReader.closeReader();
        return tableLineList;
    }
    //Reformat the lines => Moves TLOD/AF to the INFO-field, add QUAL/Filter with dot, removes fields for TLOD,GT,AF
    private List<String> formatList(List<String> tableLineList){

        String splitArray[];
        StringBuilder lineBuilder;
        String infoString;
        String qualAndFilterString = "."+seperator+"."+seperator;

        List<String> formatedTableLineList = new LinkedList<>();

        for(String line :tableLineList){
            splitArray = line.split(seperator);
            lineBuilder = new StringBuilder();

            try{
                for (int i = 0; i < 5; i++) {
                    lineBuilder.append(splitArray[i] + seperator);
                }
                lineBuilder.append(qualAndFilterString);
                infoString = "TLOD=" + splitArray[5] + ";AF=" + splitArray[7];
                infoString.trim();
                lineBuilder.append(infoString);
                formatedTableLineList.add(lineBuilder.toString());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Tablefile has the wrong format.");
                System.out.println("Tablefile should look like: CHROM\tPOS\tID\tREF\tALT\tTLOD\tSample.GT\tSample.AF\n" +
                        "chr1\t43803849\t.\tT\tC\t5.72\tT/C\t0.028");
                System.exit(-1);
            }
        }
        return formatedTableLineList;
    }
    //Write out the formated list in a vcf conform format
    private void writeList(String outputPath, List<String> formatedtableLineList) throws IOException {

        fWriter = new FWriter(outputPath);
        fWriter.writeLine(vcfFileFormat);
        fWriter.writeLine(vcfInfoHeaderTLOD);
        fWriter.writeLine(vcfInfoHeaderAF);
        fWriter.writeLine(vcfHeader);

        for(String line :formatedtableLineList){
            fWriter.writeLine(line);
        }
        fWriter.closeWriter();
    }
}

