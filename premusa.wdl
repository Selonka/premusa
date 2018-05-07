workflow premusa {
     File input_vcf
     File ref_fasta
     File ref_fasta_fai
     File ref_fasta_dict
     String gatk_docker
     String premusa_docker
     String basename
    call VariantsToTable {
     input:
     input_vcf = input_vcf,
     ref_fasta = ref_fasta,
     ref_fasta_fai = ref_fasta_fai,
     ref_fasta_dict = ref_fasta_dict,
     gatk_docker = gatk_docker,
     table_basename = basename
       
    }
    call PreMuSa { 
     input:
     input_table = VariantsToTable.output_table,
     vcf_basename = basename,
     premusa_docker = premusa_docker
    }
    call IndexSiteVCF {
     input:
     input_vcf = PreMuSa.output_vcf,
     vcf_basename = basename,
     gatk_docker = gatk_docker
    } 
    output {
    PreMuSa.*
    IndexSiteVCF.*
    }
}
task VariantsToTable {
     File input_vcf
     File ref_fasta
     File ref_fasta_fai
     File ref_fasta_dict
     String? gatk_override
     String table_basename
     String gatk_docker
     command {
     set -e
     export GATK_LOCAL_JAR=${default="/root/gatk.jar" gatk_override}
     
     gatk \
     VariantsToTable \
     -R ${ref_fasta} \
     -V ${input_vcf} \
     -F CHROM -F POS -F ID -F REF -F ALT -F TLOD -GF GT -GF AF \
     -O ${table_basename}.table
     }
     runtime {
     docker: gatk_docker        
     }
     output{
	File output_table = "${table_basename}.table"
     }
}
task PreMuSa {
     File input_table
     String vcf_basename
     String premusa_docker
     command {
        java -jar /usr/local/bin/PreMuSa.jar ${input_table} ${vcf_basename}.site.vcf \
     }
     runtime {
      	docker: premusa_docker     
     }
     output {
	File output_vcf = "${vcf_basename}.site.vcf"
     }
}
task IndexSiteVCF {
     File input_vcf
     String gatk_docker
     String vcf_basename
     String? gatk_override
     
     command {
        set -e
        export GATK_LOCAL_JAR=${default="/root/gatk.jar" gatk_override}

        gatk \
        IndexFeatureFile \
        -F ${input_vcf} \
        -O ${vcf_basename}.site.vcf.idx
     }
     runtime {
        docker: gatk_docker
     }
     output {
        File ouput_vcf_index = "${vcf_basename}.site.vcf.idx"
     }
}
