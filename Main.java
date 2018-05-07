package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// PreMuSa Creates site only vcf for Mutect2 to call difference in 2 samples from a table. See: https://software.broadinstitute.org/gatk/blog?id=11315
        System.out.println("##PreMuSa");
        System.out.println("##PreMuSa Creates site only vcf for Mutect2 to call difference in 2 samples from a table.");
        System.out.println("##Inputs: table.file outputpath");
        System.out.println("##By: M.Schwarz, Kiel 05/2018");
        String inputTable = args[0];
        String outputPath = args[1];
        Worker worker = new Worker(inputTable,outputPath);
    }
}
